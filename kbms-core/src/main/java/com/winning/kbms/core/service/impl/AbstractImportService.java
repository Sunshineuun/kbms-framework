package com.winning.kbms.core.service.impl;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.csvreader.CsvReader;
import com.winning.domain.kbms.core.ModifyDomain;
import com.winning.kbms.core.domain.ImportItem;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.DictionaryService;
import com.winning.kbms.core.service.ImportService;
import com.winning.kbms.core.utils.ContextUtils;
import com.winning.utils.DateUtils;
import com.winning.utils.RandomUIDUtils;
import com.winning.utils.excel.ExcelUtils;

/**
 * Execl导入的基本实现
 * 
 * @author gang.liu
 * @date 2013-4-2
 */
public abstract class AbstractImportService<T extends ModifyDomain> extends AbstractManagementService<T> implements
		ImportService<T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractImportService.class);

	@Autowired
	private DictionaryService dictionaryService;

	public void importExcel(InputStream is, Map<String, Object> params) {
		List<Map<String, Object>> records = readImportRecordsFromExcel(is, params);
		// 验证数据
		validateImportRecords(records, params);
		// 正式导入
		importRecords(warpImportRecords(records, params));
	}

	public void importCsv(InputStream is, Map<String, Object> params) {
		List<Map<String, Object>> records = readImportRecordsFromCsv(is, params);
		// 验证数据
		validateImportRecords(records, params);
		// 正式导入
		importRecords(warpImportRecords(records, params));
	}

	protected List<Map<String, Object>> readImportRecordsFromExcel(InputStream is, Map<String, Object> params) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(0);
			int rowNum = sheet.getLastRowNum();

			List<ImportItem> importItems = this.getImportDataTemplete();
			for (int i = 1; i <= rowNum; i++) { // 第一行是表头
				Map<String, Object> record = this.readRecord(sheet, i, importItems);
				if (!record.isEmpty()) {
					result.add(record);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	protected List<Map<String, Object>> readImportRecordsFromCsv(InputStream is, Map<String, Object> params) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		CsvReader reader = null;
		try {
			List<ImportItem> importItems = this.getImportDataTemplete();
			reader = new CsvReader(is, Charset.forName("UTF-8"));

			Map<String, Object> record = null;
			String[] values = null;
			while (reader.readRecord()) {
				// 把一行数据分割成多个字段
				record = new HashMap<String, Object>();
				values = reader.getValues();
				for (int j = 0; j < importItems.size(); j++) {
					Object colData = values[j];
					switch (importItems.get(j).getDataType()) {
					case STRING:
						break;
					case NUMERIC:
						colData = Integer.parseInt((String) colData);
						break;
					case DATE:
						try {
							colData = DateUtils.parse((String) colData, importItems.get(j).getDateFormat());
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							throw new ServiceException("字段" + importItems.get(j).getName() + "类型不正确！");
						}
						break;
					}
					record.put(importItems.get(j).getName(), colData);
				}

				if (!record.isEmpty())
					result.add(record);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(is);

			if (reader != null)
				reader.close();
		}
		return result;
	}

	private Map<String, Object> readRecord(Sheet sheet, int i, List<ImportItem> importItems) {
		Map<String, Object> record = new HashMap<String, Object>();
		for (int j = 0; j < importItems.size(); j++) {
			Object colData = null;
			switch (importItems.get(j).getDataType()) {
			case STRING:
				colData = StringUtils.trim((String) ExcelUtils.getContent(sheet, i, j, ExcelUtils.TYPE_STRING));
				break;
			case NUMERIC:
				colData = ExcelUtils.getContent(sheet, i, j, ExcelUtils.TYPE_NUMERIC);
				break;
			case DATE:
				try {
					colData = DateUtils.parse(
							StringUtils.trim((String) ExcelUtils.getContent(sheet, i, j, ExcelUtils.TYPE_DATE)),
							importItems.get(j).getDateFormat());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException("字段" + importItems.get(j).getName() + "类型不正确！");
				}
				break;
			default:
				colData = (String) ExcelUtils.getContent(sheet, i, 0, ExcelUtils.TYPE_STRING);
			}
			record.put(importItems.get(j).getName(), colData);
		}
		return record;
	}

	protected void validateImportRecords(List<Map<String, Object>> records, Map<String, Object> params) {
		for (ImportItem importItem : this.getImportDataTemplete()) {

			Map<String, String> dictMap = null;
			if (importItem.getDictionary() != null) {
				switch (importItem.getDictionary().getDictType()) {
				case DICT_TYPE_CODE:
					dictMap = dictionaryService.getDictionaryByTypeCode(importItem.getDictionary().getDictCode());
					break;
				case DICT_QUERY_ID:
					dictMap = dictionaryService.getDictionaryOnQueryId(importItem.getDictionary().getDictCode());
					break;
				default:
					dictMap = dictionaryService.getDictionaryByTypeCode(importItem.getDictionary().getDictCode());
					break;
				}
			}

			for (Map<String, Object> map : records) {
				// 校验字段是否不能为空
				if (!validateImportData(importItem, map.get(importItem.getName()))) {
					throw new ValidationException("字段" + importItem.getName() + "不能为空！");
				}

				if (dictMap != null) {
					String key = getKey(dictMap, (String) map.get(importItem.getName()));
					if (importItem.getRequired() && key == null) {
						throw new ValidationException("字段" + importItem.getName() + "中值："
								+ map.get(importItem.getName()) + ",不存在！");
					}
					map.put(importItem.getName(), key);
				}
			}
		}
	}

	protected abstract List<ImportItem> getImportDataTemplete();

	protected String getKey(Map<String, String> map, String value) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (StringUtils.equals(entry.getValue(), value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	protected boolean validateImportData(ImportItem importItem, Object colData) {
		switch (importItem.getDataType()) {
		case STRING:
			if (importItem.getRequired() && StringUtils.isEmpty((String) colData))
				return false;
			break;
		case NUMERIC:
			break;
		case DATE:
			if (importItem.getRequired() && colData == null)
				return false;
			break;
		default:
			if (importItem.getRequired() && StringUtils.isEmpty((String) colData))
				return false;
		}
		return true;
	}

	protected List<T> warpImportRecords(List<Map<String, Object>> records, Map<String, Object> params) {
		try {
			Date date = new Date();
			String username = ContextUtils.getCurrUserName();
			Class<T> clazz = this.getDomainClass();
			T obj = null;
			List<T> list = new ArrayList<T>(records.size());
			for (Map<String, Object> record : records) {
				obj = clazz.newInstance();
				BeanUtils.populate(obj, record);

				obj.setId(generateId());
				obj.setUpdateBy(username);
				obj.setUpdateTime(date);

				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	protected String generateId() {
		return RandomUIDUtils.getUUID();
	}

	@Transactional
	protected void importRecords(List<T> records) {
		this.add(records);
	}
}
