/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ReadFileUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.winning.execptions.ReadExcelException;
import com.winning.utils.DateUtils;

public class ExcelUtils
{
    public static final String TYPE_STRING = "string";
    public static final String TYPE_NUMERIC = "numeric";
    public static final String TYPE_DATE = "date";

    public static boolean isXLS (File file)
    {
        String name = file.getName ().substring (file.getName ().lastIndexOf (".") + 1);
        if (name.equalsIgnoreCase ("xls"))
        {
            return true;
        }

        return false;
    }

    public static int getColnum (HSSFSheet sheet) throws ReadExcelException
    {
        try
        {
            HSSFRow row = sheet.getRow (0);
            return row.getLastCellNum ();
        }
        catch (Exception e)
        {
            throw new ReadExcelException (e);
        }
    }

    public static void setContent (Sheet sheet, int rownum, int colnum, int cType, String value)
    {
        Row row = null;
        Cell cell = null;

        row = sheet.getRow (rownum);
        if (row == null)
        {
            row = sheet.createRow (rownum);
        }

        cell = row.getCell ((short) colnum);
        if (cell == null)
        {
            cell = row.createCell ((short) colnum);
        }

        cell.setCellType (cType);
        switch (cType)
        {
        case Cell.CELL_TYPE_NUMERIC:
            cell.setCellValue (Double.parseDouble (value));
            break;
        case Cell.CELL_TYPE_STRING:
            cell.setCellValue (new HSSFRichTextString (value));
            break;
        case Cell.CELL_TYPE_FORMULA:
            cell.setCellFormula (value);
            break;
        }
    }

    public static String getFormula (HSSFSheet sheet, int rownum, int colnum) throws ReadExcelException
    {
        HSSFRow row = null;
        HSSFCell cell = null;

        try
        {
            row = sheet.getRow (rownum);
            if (row != null)
            {
                cell = row.getCell (colnum);

                if (cell != null)
                {
                    if (cell.getCellType () == HSSFCell.CELL_TYPE_FORMULA)
                    {
                        return cell.getCellFormula ();
                    }
                }
            }

            return "";
        }
        catch (Exception e)
        {
            throw new ReadExcelException (e);
        }
    }

    public static HSSFCell getCell (HSSFSheet sheet, int rownum, int colnum) throws ReadExcelException
    {
        HSSFRow row = null;
        HSSFCell cell = null;

        try
        {
            row = sheet.getRow (rownum);
            if (row != null)
            {
                cell = row.getCell (colnum);
                return cell;
            }

            return null;
        }
        catch (Exception e)
        {
            throw new ReadExcelException (e);
        }

    }

    public static String getContent (HSSFSheet sheet, int rownum, int colnum) throws ReadExcelException
    {
        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFRichTextString text = null;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance ();
        nf.setGroupingUsed (false);

        try
        {
            row = sheet.getRow (rownum);
            if (row != null)
            {
                cell = row.getCell (colnum);

                if (cell != null)
                {
                    int cType = cell.getCellType ();
                    switch (cType)
                    {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted (cell))
                        {
                            return DateUtils.formatDateTime (DateUtil.getJavaDate (cell.getNumericCellValue ()));
                        }
                        else
                        {
                            return nf.format (cell.getNumericCellValue ());
                        }
                    case HSSFCell.CELL_TYPE_STRING:
                        text = cell.getRichStringCellValue ();
                        return text.getString ().trim ();
                    case HSSFCell.CELL_TYPE_FORMULA:
                        sheet.setDisplayFormulas (true);
                        return nf.format (cell.getNumericCellValue ());
                    case HSSFCell.CELL_TYPE_BLANK:
                        return "";
                    }
                }
            }

            return null;
        }
        catch (Exception e)
        {
            throw new ReadExcelException (e);
        }
    }

    public static Object getContent (Sheet sheet, int rownum, int colnum, String type) throws ReadExcelException
    {
        Row row = null;
        Cell cell = null;
        RichTextString text = null;
        try
        {
            row = sheet.getRow (rownum);
            if (row != null)
            {
                cell = row.getCell (colnum);

                if (cell != null)
                {
                    int cType = cell.getCellType ();
                    switch (cType)
                    {
                    case Cell.CELL_TYPE_NUMERIC:
                        java.text.NumberFormat nf = java.text.NumberFormat.getInstance ();
                        nf.setGroupingUsed (false);
                        if (type.equals (ExcelUtils.TYPE_DATE))
                        {
                            return cell.getDateCellValue ();
                        }
                        else
                        {
                            return nf.format (cell.getNumericCellValue ());
                        }
                    case Cell.CELL_TYPE_STRING:
                        text = cell.getRichStringCellValue ();
                        return text.getString ().trim ();
                    case HSSFCell.CELL_TYPE_FORMULA:
                    	/**
                    	 * 此处为公式处理。
                    	 * 可能出现非纯数值的状况，当为非纯数值的状况下会抛出异常，
                    	 * 在内部自行处理，改为文本转换。
                    	 */
                    	String value = null;
                        java.text.NumberFormat nf2 = java.text.NumberFormat.getInstance ();
                        nf2.setGroupingUsed (false);
                        sheet.setDisplayFormulas (true);
                        try{
                        	value = nf2.format (cell.getNumericCellValue ());
                        }catch(Exception e){
                        	value = String.valueOf(cell.getRichStringCellValue());
                        }
                        return value;
                        // return Double.toString(cell.getNumericCellValue());
                    case Cell.CELL_TYPE_BLANK:
                        if (type.equals (ExcelUtils.TYPE_NUMERIC))
                        {
                            return "0";
                        }
                        return "";
                    }
                }
                else
                {
                    if (type.equals (ExcelUtils.TYPE_NUMERIC))
                    {
                        return "0";
                    }
                    else
                    {
                        return "";
                    }
                }
            }

            return "";
        }
        catch (Exception e)
        {
            throw new ReadExcelException (e);
        }
    }

    public static List <String> getTitle (HSSFSheet sheet, int startrow, int startcol) throws ReadExcelException
    {
        List <String> titleList = new ArrayList <String> ();
        int colnum = getColnum (sheet);

        for (int i = startcol; i < colnum; i++)
        {
            titleList.add (ExcelUtils.getContent (sheet, startrow, i));
        }

        return titleList;
    }

}


/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */