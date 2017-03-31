package com.winning.kbms.core.service.impl;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.winning.kbms.core.domain.ExportDefine;
import com.winning.kbms.core.domain.ExportTitle;
import com.winning.kbms.core.domain.GroupHeader;
import com.winning.kbms.core.service.DataToExeclService;
import com.winning.kbms.core.utils.DictionaryUtils;
import com.winning.utils.DateUtils;
import com.winning.utils.excel.ExcelUtils;

/**
 * 将数据转换成execl
 * 
 * @author gang.liu
 * @date 2013-1-16
 */
@Service("dataToExeclService")
public class DataToExeclServiceImpl implements DataToExeclService {

    public SXSSFWorkbook createSXSSFWorkbook(int rowaccess) {
        return new SXSSFWorkbook(rowaccess);
    }

    public SXSSFWorkbook createSXSSFWorkbook() {
        return new SXSSFWorkbook();
    }

    public HSSFWorkbook createHSSFWorkbook() {
        return new HSSFWorkbook();
    }

    public Sheet createSheet(Workbook wb) {
        return wb.createSheet();
    }

    private int writeTitles(ExportTitle exportTitle, HSSFSheet s) {
        int dataStartRowNum = 1;

        if (exportTitle.getGroupHeaders() == null
                || exportTitle.getGroupHeaders().size() == 0) { // 一级表头
            dataStartRowNum = 1;
            for (int i = 0; i < exportTitle.getExportDefines().size(); i++) {
                // 写表头
                ExportDefine exportDefine = exportTitle.getExportDefines().get(
                        i);
                setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                        exportDefine.getTitle());
            }
        } else if (exportTitle.getComplexGroupHeaders() == null
                || exportTitle.getComplexGroupHeaders().size() == 0) { // 二级表头
            dataStartRowNum = 2;
            int flag = 0;
            for (int i = 0; i < exportTitle.getExportDefines().size(); i++) {
                // 写表头
                ExportDefine exportDefine = exportTitle.getExportDefines().get(
                        i);

                GroupHeader groupHeader = getGroupHeader(
                        exportTitle.getGroupHeaders(), exportDefine.getName());
                if (groupHeader == null) {
                    if (flag == 0) {
                        s.addMergedRegion(new CellRangeAddress(0, 1, i, i));
                        setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    } else {
                        flag--;
                        setTitleContent(s, 1, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    }
                } else {
                    flag = groupHeader.getNumberOfColumns() - 1;
                    s.addMergedRegion(new CellRangeAddress(0, 0, i, i
                            + groupHeader.getNumberOfColumns() - 1));
                    setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                            groupHeader.getTitleText());

                    setTitleContent(s, 1, i, HSSFCell.CELL_TYPE_STRING,
                            exportDefine.getTitle());
                }
            }
        } else { // 三级表头
            dataStartRowNum = 3;
            int flag2 = 0;
            int flag3 = 0;
            for (int i = 0; i < exportTitle.getExportDefines().size(); i++) {
                // 写表头
                ExportDefine exportDefine = exportTitle.getExportDefines().get(
                        i);

                GroupHeader groupHeader = getGroupHeader(
                        exportTitle.getGroupHeaders(), exportDefine.getName());
                GroupHeader complexGroupHeader = getGroupHeader(
                        exportTitle.getComplexGroupHeaders(),
                        exportDefine.getName());
                if (groupHeader == null && complexGroupHeader == null) {
                    if (flag2 == 0 && flag3 == 0) {
                        s.addMergedRegion(new CellRangeAddress(0, 2, i, i));
                        setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    } else if (flag2 != 0 && flag3 == 0) {
                        flag2--;
                        setTitleContent(s, 2, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    } else if (flag2 == 0 && flag3 != 0) {
                        flag3--;
                        setTitleContent(s, 1, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    } else if (flag2 != 0 && flag3 != 0) {
                        flag2--;
                        flag3--;
                        setTitleContent(s, 2, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    }
                } else if (groupHeader != null && complexGroupHeader == null) {
                    if (flag3 == 0) {
                        flag2 = groupHeader.getNumberOfColumns() - 1;
                        s.addMergedRegion(new CellRangeAddress(0, 1, i, i
                                + groupHeader.getNumberOfColumns() - 1));
                        setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                                groupHeader.getTitleText());

                        setTitleContent(s, 2, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    } else {
                        flag3--;
                        flag2 = groupHeader.getNumberOfColumns() - 1;
                        s.addMergedRegion(new CellRangeAddress(1, 1, i, i
                                + groupHeader.getNumberOfColumns() - 1));
                        setTitleContent(s, 1, i, HSSFCell.CELL_TYPE_STRING,
                                groupHeader.getTitleText());

                        setTitleContent(s, 2, i, HSSFCell.CELL_TYPE_STRING,
                                exportDefine.getTitle());
                    }
                } else if (groupHeader == null && complexGroupHeader != null) {
                    flag3 = complexGroupHeader.getNumberOfColumns() - 1;
                    s.addMergedRegion(new CellRangeAddress(0, 0, i, i
                            + complexGroupHeader.getNumberOfColumns() - 1));
                    s.addMergedRegion(new CellRangeAddress(1, 2, i, i));
                    setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                            complexGroupHeader.getTitleText());

                    setTitleContent(s, 1, i, HSSFCell.CELL_TYPE_STRING,
                            exportDefine.getTitle());
                } else if (groupHeader != null && complexGroupHeader != null) {
                    flag2 = groupHeader.getNumberOfColumns() - 1;
                    flag3 = complexGroupHeader.getNumberOfColumns() - 1;
                    s.addMergedRegion(new CellRangeAddress(0, 0, i, i
                            + groupHeader.getNumberOfColumns() - 1));
                    s.addMergedRegion(new CellRangeAddress(0, 0, i, i
                            + complexGroupHeader.getNumberOfColumns() - 1));
                    setTitleContent(s, 0, i, HSSFCell.CELL_TYPE_STRING,
                            complexGroupHeader.getTitleText());
                    setTitleContent(s, 1, i, HSSFCell.CELL_TYPE_STRING,
                            groupHeader.getTitleText());

                    setTitleContent(s, 2, i, HSSFCell.CELL_TYPE_STRING,
                            exportDefine.getTitle());
                }
            }
        }
        return dataStartRowNum;
    }

    public static void setTitleContent(Sheet sheet, int rownum, int colnum,
            int cType, String value) {
        Row row = null;
        Cell cell = null;

        row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }

        cell = row.getCell((short) colnum);
        if (cell == null) {
            cell = row.createCell((short) colnum);
        }

        CellStyle cellstyle = sheet.getWorkbook().createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        cell.setCellStyle(cellstyle);
        cell.setCellType(cType);
        switch (cType) {
        case Cell.CELL_TYPE_NUMERIC:
            cell.setCellValue(Double.parseDouble(value));
            break;
        case Cell.CELL_TYPE_STRING:
            cell.setCellValue(new HSSFRichTextString(value));
            break;
        case Cell.CELL_TYPE_FORMULA:
            cell.setCellFormula(value);
            break;
        }
    }

    private GroupHeader getGroupHeader(List<GroupHeader> groupHeaders,
            String startColumnName) {
        for (GroupHeader groupHeader : groupHeaders) {
            if (groupHeader.getStartColumnName().equals(startColumnName)) {
                return groupHeader;
            }
        }
        return null;
    }

    public HSSFSheet writeData(ExportTitle exportTitle,
            List<Map<String, Object>> records, HSSFSheet s) {
        Map<String, Map<String, String>> dicts = new HashMap<String, Map<String, String>>();

        int indexRow = writeTitles(exportTitle, s);
        for (Map<String, Object> map : records) { // 遍历数据
            int indexColumn = 0;
            for (ExportDefine exportDefine : exportTitle.getExportDefines()) {
                if (StringUtils.isNotEmpty(exportDefine.getDictionaryCode())) {
                    initDictionaryValue(dicts, exportDefine);

                    ExcelUtils.setContent(s, indexRow, indexColumn,
                            HSSFCell.CELL_TYPE_STRING,
                            getDictValue(dicts, exportDefine, map));
                } else {
                    if (map.get(exportDefine.getKey()) instanceof String) {
                        ExcelUtils.setContent(s, indexRow, indexColumn,
                                HSSFCell.CELL_TYPE_STRING,
                                (String) map.get(exportDefine.getKey()));
                    } else if (map.get(exportDefine.getKey()) instanceof Number) {
                        ExcelUtils
                                .setContent(
                                        s,
                                        indexRow,
                                        indexColumn,
                                        HSSFCell.CELL_TYPE_NUMERIC,
                                        ((Number) map.get(exportDefine.getKey()))
                                                .toString());
                    } else if (map.get(exportDefine.getKey()) instanceof Date) {
                        ExcelUtils.setContent(s, indexRow, indexColumn,
                                HSSFCell.CELL_TYPE_STRING, DateUtils
                                        .formatDate(((Date) map
                                                .get(exportDefine.getKey()))));
                    } else if (map.get(exportDefine.getKey()) instanceof Clob) {
                        ExcelUtils.setContent(s, indexRow, indexColumn,
                                HSSFCell.CELL_TYPE_STRING,
                                getClobString((Clob) map.get(exportDefine
                                        .getKey())));
                    }
                }
                indexColumn++;
            }

            indexRow++;
        }
        return s;
    }

    private String getDictValue(Map<String, Map<String, String>> dicts,
            ExportDefine exportDefine, Map<String, Object> data) {
        String value = dicts.get(exportDefine.getDictionaryCode()).get(
                data.get(exportDefine.getKey()) + "");
        return value == null ? (String) data.get(exportDefine.getKey()) : value;
    }

    public SXSSFSheet writeTitles(List<ExportDefine> exportDefines,
            SXSSFWorkbook wb) {
        SXSSFSheet sheet = (SXSSFSheet) this.createSheet(wb);
        for (int i = 0; i < exportDefines.size(); i++) {
            // 写表头
            ExportDefine exportDefine = exportDefines.get(i);
            ExcelUtils.setContent(sheet, 0, i, HSSFCell.CELL_TYPE_STRING,
                    exportDefine.getTitle());
        }
        return sheet;
    }

    public SXSSFSheet writeData(List<ExportDefine> exportDefines,
            List<Map<String, Object>> records, SXSSFSheet s, int startRowNum,
            SXSSFWorkbook wb) throws IOException {
        Map<String, Map<String, String>> dicts = new HashMap<String, Map<String, String>>();

        int indexRow = startRowNum;
        for (Map<String, Object> map : records) { // 遍历数据
            int indexColumn = 0;
            for (ExportDefine exportDefine : exportDefines) {
                if (StringUtils.isNotEmpty(exportDefine.getDictionaryCode())) {

                    initDictionaryValue(dicts, exportDefine);

                    ExcelUtils.setContent(
                            s,
                            indexRow,
                            indexColumn,
                            HSSFCell.CELL_TYPE_STRING,
                            dicts.get(exportDefine.getDictionaryCode()).get(
                                    map.get(exportDefine.getKey()) + ""));
                } else {
                    if (map.get(exportDefine.getKey()) instanceof String) {
                        ExcelUtils.setContent(s, indexRow, indexColumn,
                                HSSFCell.CELL_TYPE_STRING,
                                (String) map.get(exportDefine.getKey()));
                    } else if (map.get(exportDefine.getKey()) instanceof Number) {
                        ExcelUtils
                                .setContent(
                                        s,
                                        indexRow,
                                        indexColumn,
                                        HSSFCell.CELL_TYPE_NUMERIC,
                                        ((Number) map.get(exportDefine.getKey()))
                                                .toString());
                    } else if (map.get(exportDefine.getKey()) instanceof Date) {
                        ExcelUtils.setContent(s, indexRow, indexColumn,
                                HSSFCell.CELL_TYPE_STRING, DateUtils
                                        .formatDate(((Date) map
                                                .get(exportDefine.getKey()))));
                    } else if (map.get(exportDefine.getKey()) instanceof Clob) {
                        ExcelUtils.setContent(s, indexRow, indexColumn,
                                HSSFCell.CELL_TYPE_STRING,
                                getClobString((Clob) map.get(exportDefine
                                        .getKey())));
                    }
                }
                indexColumn++;
            }

            // 每当行数达到设置的值就刷新数据到硬盘,以清理内存
            if (indexRow % wb.getRandomAccessWindowSize() == 0) {
                s.flushRows();
            }

            indexRow++;
        }
        return s;
    }

    private void initDictionaryValue(Map<String, Map<String, String>> dicts,
            ExportDefine exportDefine) {
        if (!dicts.containsKey(exportDefine.getDictionaryCode())) { // 判断内存中是否存在数据，如果没有再到数据库中获取
            switch (exportDefine.getDictionaryType()) {
            case DICT_TYPE_CODE:
                dicts.put(exportDefine.getDictionaryCode(), DictionaryUtils
                        .getDictionaryByTypeCode(exportDefine
                                .getDictionaryCode()));
                break;
            case DICT_QUERY_ID:
                dicts.put(exportDefine.getDictionaryCode(), DictionaryUtils
                        .getDictionaryOnQueryId(exportDefine
                                .getDictionaryCode()));
                break;
            default:
                break;
            }
        }
    }

    private String getClobString(Clob clob) {
        if (clob == null)
            return null;

        try {
            return clob.getSubString((long) 1, (int) clob.length());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
