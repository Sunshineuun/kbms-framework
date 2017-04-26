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
public abstract class AbstractImportService<T extends ModifyDomain> extends AbstractManagementService<T>
    implements ImportService<T> {
    private static final Logger logger =
            LoggerFactory.getLogger(AbstractImportService.class);

    /* 表格中最小列数 */
    private Integer minColumns = 0;

    @Autowired
    private DictionaryService dictionaryService;


    public void importExcel(InputStream is, Map<String, Object> params) {
        List<Map<String, Object>> records =
                readImportRecordsFromExcel(is, params);
        // 验证数据
        validateImportRecords(records, params);
        // 正式导入
        importRecords(warpImportRecords(records, params));
    }

    public void importCsv(InputStream is, Map<String, Object> params) {
        List<Map<String, Object>> records =
                readImportRecordsFromCsv(is, params);
        // 验证数据
        validateImportRecords(records, params);
        // 正式导入
        importRecords(warpImportRecords(records, params));
    }


    protected List<Map<String, Object>> readImportRecordsFromExcel(
        InputStream is, Map<String, Object> params) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<ImportItem> importItems = this.getImportDataTemplete();
        
        List<String[]> csvData = new ArrayList<String[]>();
        try {
            csvData = XLSXCovertCSVReader.readerExcel(is, getMinColumns());
        }
        catch (Exception e) {
            logger.equals(e);
        }
        
        /*校验表头*/
        validateImportTitle(csvData.get(0),importItems);

        // Workbook workbook = null;
        // workbook = WorkbookFactory.create(is);
        // Sheet sheet = workbook.getSheetAt(0);
        // int rowNum = sheet.getLastRowNum();
        for (int i=1; i<csvData.size(); i++) { // 第一行是表头
            Map<String, Object> record = this.readRecord(csvData.get(i), importItems);
            if (!record.isEmpty()) {
                result.add(record);
            }
            else {
                continue;
            }
        }
        return result;
    }


    protected List<Map<String, Object>> readImportRecordsFromCsv(InputStream is,
        Map<String, Object> params) {
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
                                colData = DateUtils.parse((String) colData,
                                        importItems.get(j).getDateFormat());
                            }
                            catch (Exception e) {
                                logger.error(e.getMessage(), e);
                                throw new ServiceException("字段"
                                        + importItems.get(j).getName()
                                        + "类型不正确！");
                            }
                            break;
                    }
                    record.put(importItems.get(j).getName(), colData);
                }

                if (!record.isEmpty())
                    result.add(record);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IOUtils.closeQuietly(is);

            if (reader != null)
                reader.close();
        }
        return result;
    }


    /**
     * 
     * @date 2017年4月20日 下午2:55:15
     * @author qiushengming
     * @param sheet
     * @param i
     * @param importItems
     * @return Map<String,Object>
     *         <p>
     *         基于Excel读取数据
     *         </p>
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Map<String, Object> readRecord(Sheet sheet, int i,
        List<ImportItem> importItems) {
        Map<String, Object> record = new HashMap<String, Object>();
        for (int j = 0; j < importItems.size(); j++) {
            Object colData = null;
            switch (importItems.get(j).getDataType()) {
                case STRING:
                    colData = StringUtils.trim((String) ExcelUtils
                            .getContent(sheet, i, j, ExcelUtils.TYPE_STRING));
                    break;
                case NUMERIC:
                    colData = ExcelUtils.getContent(sheet, i, j,
                            ExcelUtils.TYPE_NUMERIC);
                    break;
                case DATE:
                    try {
                        colData = DateUtils.parse(
                                StringUtils.trim((String) ExcelUtils.getContent(
                                        sheet, i, j, ExcelUtils.TYPE_DATE)),
                                importItems.get(j).getDateFormat());
                    }
                    catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        throw new ServiceException(
                                "字段" + importItems.get(j).getName() + "类型不正确！");
                    }
                    break;
                default:
                    colData = (String) ExcelUtils.getContent(sheet, i, 0,
                            ExcelUtils.TYPE_STRING);
            }
            record.put(importItems.get(j).getName(), colData);
        }
        return record;
    }


    private Map<String, Object> readRecord(String[] data,
        List<ImportItem> importItems) {
        Map<String, Object> record = new HashMap<String, Object>();

        for (int i = 0; i < importItems.size(); i++) {
            record.put(importItems.get(i).getName(), replaceSymbol(data[i]));
        }

        return record;
    }


    /**
     * @date 2017年4月24日 下午3:30:50
     * @author qiushengming
     * @param string
     * @return
     * <p>替换文本中无效字符
     * </br>1.目前替换英文的双引号。</p>
     */
    private Object replaceSymbol(String arg) {
        
        /*为空直接返回*/
        if(StringUtils.isEmpty(arg)){
            return arg;
        }
        
        /*替换英文的单引号*/
        arg = arg.replaceAll("\"", "");
        
        return arg;
    }

    /**
     * @date 2017年4月25日 下午3:17:50
     * @author qiushengming
     * @param records
     * <p>校验表头是否与定义导入模板相同</p>
     */
    protected void validateImportTitle(String[] title, List<ImportItem> importItems) {
        for(int i=0; i<importItems.size(); i++){
            String titlTemp = title[i].replace("\"", "");
            String importItemName = importItems.get(i).getAnnotation();
            if(!StringUtils.equals(importItemName, titlTemp)){
                /* 抛出异常 */
                String errorInfo = "模板中" + importItemName
                        + "与导入表格中" 
                        + titlTemp
                        + "不匹配";
                throw new ValidationException(errorInfo);
            }
        }
    }

    protected void validateImportRecords(List<Map<String, Object>> records,
        Map<String, Object> params) {
        for (ImportItem importItem : this.getImportDataTemplete()) {

            Map<String, String> dictMap = null;
            if (importItem.getDictionary() != null) {
                switch (importItem.getDictionary().getDictType()) {
                    case DICT_TYPE_CODE:
                        dictMap = dictionaryService.getDictionaryByTypeCode(
                                importItem.getDictionary().getDictCode());
                        break;
                    case DICT_QUERY_ID:
                        dictMap = dictionaryService.getDictionaryOnQueryId(
                                importItem.getDictionary().getDictCode());
                        break;
                    default:
                        dictMap = dictionaryService.getDictionaryByTypeCode(
                                importItem.getDictionary().getDictCode());
                        break;
                }
            }

            /* 索引，当出现异常的时候能告知异常数据所在行 */
            int recordsIndex = 1;

            for (Map<String, Object> map : records) {

                /* 校验字段是否不能为空 */
                if (!validateImportData(importItem,
                        map.get(importItem.getName()))) {

                    /* 拼接错误信息 */
                    StringBuilder errorInfo = new StringBuilder();
                    errorInfo.append(importItem.getAnnotation());
                    errorInfo.append("字段为必填项，请维护数据后重新导入。在");
                    errorInfo.append(recordsIndex);
                    errorInfo.append("行。");

                    /* 抛出异常 */
                    throw new ValidationException(errorInfo.toString());
                }

                if (dictMap != null) {
                    String key = getKey(dictMap,
                            (String) map.get(importItem.getName()));
                    if (importItem.getRequired() && key == null) {
                        throw new ValidationException("字段"
                                + importItem.getName() + "中值："
                                + map.get(importItem.getName()) + ",不存在！");
                    }
                    map.put(importItem.getName(), key);
                }

                recordsIndex++;
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


    protected boolean validateImportData(ImportItem importItem,
        Object colData) {
        switch (importItem.getDataType()) {
            case STRING:
                if (importItem.getRequired()
                        && StringUtils.isEmpty((String) colData))
                    return false;
                break;
            case NUMERIC:
                break;
            case DATE:
                if (importItem.getRequired() && colData == null)
                    return false;
                break;
            default:
                if (importItem.getRequired()
                        && StringUtils.isEmpty((String) colData))
                    return false;
        }
        return true;
    }


    protected List<T> warpImportRecords(List<Map<String, Object>> records,
        Map<String, Object> params) {
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
            records.clear();
            return list;
        }
        catch (Exception e) {
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
        records.clear();
    }


    /**
     * 
     * @date 2017年4月20日 下午2:50:48
     * @author qiushengming
     * @return Integer
     *         <p>
     *         表格中的最小列数，尽量比表格的列数多，不然会出现数组越界的问题
     *         </p>
     */
    protected Integer getMinColumns() {
        return minColumns;
    }
}
