package com.winning.kbms.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 使用CVS模式解决XLSX文件，可以有效解决用户模式内存溢出的问题
 * 该模式是POI官方推荐的读取大数据的模式，在用户模式下，数据量较大、Sheet较多、或者是有很多无用的空行的情况
 * ，容易出现内存溢出,用户模式读取Excel的典型代码如下： FileInputStream file=new
 * FileInputStream("c:\\test.xlsx"); Workbook wb=new XSSFWorkbook(file);
 * 
 * 
 * 
 */
public class XLSXCovertCSVReader {

    /**
     * The type of the data value is indicated by an attribute on the cell. The
     * value is usually in a "v" element within the cell.
     */
    enum xssfDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
    }

    /**
     * 使用xssf_sax_API处理Excel,请参考：
     * http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
     * <p/>
     * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
     * http://www.ecma-international.org/publications/standards/Ecma-376.htm
     * <p/>
     * A web-friendly version is http://openiso.org/Ecma/376/Part4
     */
    class MyXSSFSheetHandler extends DefaultHandler {

        /* Table with styles */
        private StylesTable stylesTable;

        /* Table with unique strings */
        private ReadOnlySharedStringsTable sharedStringsTable;

        /* Destination for data */
        private final PrintStream output;

        /* Number of columns to read starting with leftmost */
        private final int minColumnCount;

        /* Set when V start element is seen */
        private boolean vIsOpen;

        /*
         * Set when cell start element is seen; used when cell close element is
         * seen.
         */
        private xssfDataType nextDataType;

        /* 用于格式化数字单元格值 */
        private short formatIndex;

        /* 用于格式化文本描述 */
        private String formatString;

        /* 数据格式 */
        private final DataFormatter formatter;

        /* 当前列 */
        private int thisColumn = -1;

        /* 最后一列 */
        private int lastColumnNumber = -1;

        /* 收集字符 */
        private StringBuffer value;

        /* 单行结果集合 */
        private String[] record;

        /* 结果集 */
        private List<String[]> rows = new ArrayList<String[]>();


        /**
         * Accepts objects needed while parsing.
         * 
         * @author qiushengming
         * @param styles 共享的字符串表
         * @param strings
         * @param cols 最小列数
         * @param target 输出流
         */
        public MyXSSFSheetHandler(StylesTable styles,
            ReadOnlySharedStringsTable strings, int cols, PrintStream target) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.minColumnCount = cols;
            this.output = target;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
            record = new String[this.minColumnCount];
        }


        /*
         * (non-Javadoc)
         * 
         * @see
         * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        @Override
        public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {

            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                /* 清除缓存 */
                value.setLength(0);
            }
            else if ("c".equals(name)) {// c => cell
                /* 获取引用单元格，单元格坐标,例如A1,A2 */
                String r = attributes.getValue("r");

                /* 为获取A1,AF1,前的字符，会截取第一个数字之前的字符，已便算出当前是第几列 */
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));

                /* 设置默认值 */
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                /* 获取标签中t属性的值 */
                String cellType = attributes.getValue("t");
                /* 获取标签中s属性的值 */
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType))
                    nextDataType = xssfDataType.BOOL;
                else if ("e".equals(cellType))
                    nextDataType = xssfDataType.ERROR;
                else if ("inlineStr".equals(cellType))
                    nextDataType = xssfDataType.INLINESTR;
                else if ("s".equals(cellType))
                    nextDataType = xssfDataType.SSTINDEX;
                else if ("str".equals(cellType))
                    nextDataType = xssfDataType.FORMULA;
                else if (cellStyleStr != null) {
                    /* 通过cellStyleStr，可以确定当前单元格的样式，对于此处需求没什么作用 */
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null)
                        this.formatString = BuiltinFormats
                                .getBuiltinFormat(this.formatIndex);
                }
            }

        }


        @Override
        public void endElement(String uri, String localName, String name)
            throws SAXException {

            String thisStr = null;

            if ("v".equals(name)) {// v => contents of a cell
                /* 通过nextDataType区分类型不同类型用不同取值方式 */
                switch (nextDataType) {
                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;
                    case ERROR:
                        thisStr = "\"ERROR:" + value.toString() + '"';
                        break;
                    case FORMULA:
                        // A formula could result in a string value,
                        // so always add double-quote characters.
                        thisStr = '"' + value.toString() + '"';
                        break;

                    case INLINESTR:
                        XSSFRichTextString rtsi =
                                new XSSFRichTextString(value.toString());
                        thisStr = rtsi.toString();
                        break;
                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(
                                    sharedStringsTable.getEntryAt(idx));
                            thisStr = rtss.toString();
                        }
                        catch (NumberFormatException ex) {
                            output.println("Failed to parse SST index '"
                                    + sstIndex + "': " + ex.toString());
                        }
                        break;
                    case NUMBER:
                        String n = value.toString();
                        // 判断是否是日期格式
                        if (HSSFDateUtil.isADateFormat(this.formatIndex, n)) {
                            Double d = Double.parseDouble(n);
                            Date date = HSSFDateUtil.getJavaDate(d);
                            thisStr = formateDateToString(date);
                        }
                        else if (this.formatString != null) {
                            thisStr = formatter.formatRawCellContents(
                                    Double.parseDouble(n), this.formatIndex,
                                    this.formatString);
                        }
                        else {
                            thisStr = n;
                        }
                        break;
                    default:
                        thisStr =
                                "(TODO: Unexpected type: " + nextDataType + ")";
                        break;
                }

                if (thisStr != null) {
                    thisStr = thisStr.trim();
                }

                record[thisColumn] = thisStr;

                if (thisColumn > -1)
                    lastColumnNumber = thisColumn;

            }
            else if ("row".equals(name)) {
                /* 当遇到row标签的时候说明该行遇到结尾了，将该行数据增加到list集合中，并清空 */
                if (minColumns > 0) {
                    if (lastColumnNumber == -1) {
                        lastColumnNumber = 0;
                    }
                    for (String s : record) {
                        if (s != null) {
                            rows.add(record.clone());
                            for (int i = 0; i < record.length; i++) {
                                record[i] = null;
                            }
                            break;
                        }
                    }
                }
                lastColumnNumber = -1;
            }

        }


        public List<String[]> getRows() {
            return rows;
        }


        public void setRows(List<String[]> rows) {
            this.rows = rows;
        }


        /**
         * Captures characters only if a suitable element is open. Originally
         * was just "v"; extended for inlineStr also.
         */
        public void characters(char[] ch, int start, int length)
            throws SAXException {
            if (vIsOpen)
                value.append(ch, start, length);
        }


        /**
         * 将列名，例如AA,A,B,C转换对应的数值
         * 
         * @param name AA,AM列索引的传入
         * @return Index 列索引转换为数值索引
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }


        /**
         * 
         * 2017年4月26日 qiushengming
         * 
         * @param date 日期对象
         * @return String 转换为yyyy-MM-dd HH:mm:ss格式
         *         <p>
         *         </p>
         */
        private String formateDateToString(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期
            return sdf.format(date);

        }

    }

    private static Logger logger = LoggerFactory.getLogger(XLSXCovertCSVReader.class);

    private OPCPackage xlsxPackage;

    private int minColumns;
    
    private String sheetName;

    private PrintStream output;


    /**
     * 
     * @date 2017年4月20日 下午2:35:44
     * @authon qiushengming
     * @param pkg The XLSX package to process
     * @param output The PrintStream to output the CSV to
     * @param sheetName sheet页校验用
     * @param minColumns The minimum number of columns to output, or -1 for no
     *        minimum
     */
    public XLSXCovertCSVReader(OPCPackage pkg, PrintStream output,
        int minColumns, String sheetName) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
        this.sheetName = sheetName;
    }


    /**
     * 
     * @date 2017年4月20日 下午2:33:49
     * @author qiushengming
     * @param styles
     * @param strings
     * @param sheetInputStream
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException List<String[]>
     *         <p>
     *         </p>
     */
    public List<String[]> processSheet(StylesTable styles,
        ReadOnlySharedStringsTable strings, InputStream sheetIn)
        throws IOException, ParserConfigurationException, SAXException {

        InputSource sheetSource = new InputSource(sheetIn);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings,
                this.minColumns, this.output);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);

        return handler.getRows();
    }


    /**
     * 
     * @date 2017年4月20日 下午2:32:03
     * @author qiushengming
     * @return 返回list集合；
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     *         <p>
     *         Excel读取器
     *         </p>
     */
    public List<String[]> process()
        throws IOException, OpenXML4JException, ParserConfigurationException,
        SAXException {

        ReadOnlySharedStringsTable strings =
                new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);

        /* 结果存放集合 */
        List<String[]> list = new ArrayList<String[]>();
        StylesTable styles = xssfReader.getStylesTable();

        /* sheet页迭代器 */
        XSSFReader.SheetIterator iter =
                (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        /* 只读取第一个sheet页 */
        while (iter.hasNext()) {
            InputStream stream = iter.next();

            /* 获取当前迭代器的sheet名称 */
            String sheetNameTemp = iter.getSheetName();
            
            if(sheetNameTemp.equals(sheetName)){
                /* 存储读取结果 */
                list.addAll(processSheet(styles, strings, stream));
            }
            /* 关闭流 */
            stream.close();
        }
        return list;
    }


    /**
     * 
     * @date 2017年4月20日 下午2:34:43
     * @author qiushengming
     * @param path
     * @param sheetName
     * @param minColumns
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException List<String[]>
     *         <p>
     *         通过路径的形式读取Excel
     *         </p>
     */
    public static List<String[]> readerExcel(String path, int minColumns, 
        String sheetName)
        throws IOException, OpenXML4JException, ParserConfigurationException,
        SAXException {
        OPCPackage p = OPCPackage.open(path, PackageAccess.READ);
        XLSXCovertCSVReader xlsx2csv =
                new XLSXCovertCSVReader(p, System.out, minColumns, sheetName);
        List<String[]> list = xlsx2csv.process();
        p.close();
        return list;
    }


    /**
     * 
     * @date 2017年4月20日 下午2:34:33
     * @author qiushengming
     * @param ipn
     * @param sheetName
     * @param minColumns
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException List<String[]>
     *         <p>
     *         通过流的形式读取Excel
     *         </p>
     */
    public static List<String[]> readerExcel(InputStream ipn, int minColumns, 
        String sheetName)
        throws IOException, OpenXML4JException, ParserConfigurationException,
        SAXException {
        List<String[]> result = new ArrayList<String[]>();

        OPCPackage p = OPCPackage.open(ipn);
        XLSXCovertCSVReader xlsx2csv =
                new XLSXCovertCSVReader(p, System.out, minColumns,sheetName);
        result = xlsx2csv.process();
        return result;
    }

    public static void main(String[] args) throws Exception {
        List<String[]> result =
                XLSXCovertCSVReader.readerExcel("d:\\药品标准目录.xlsx", 50,"药品目录");
        System.out.println(result);
    }
}
