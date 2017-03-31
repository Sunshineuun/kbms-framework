package com.winning.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import com.winning.test.Table.Field;
import com.winning.utils.CamelCaseUtils;

public class GridBuilder
{
    private static String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@192.168.1.252:1521:orcl";
    private static String USERNAME = "kbms_f";
    private static String PASSWORD = "kbms";
    private static String tableName = "kbms_log_ws_call";

    static
    {
        try
        {
            Class.forName(DRIVER_CLASS_NAME);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) throws Exception
    {
        Table table = getTableInfo (tableName);
        printGrid(table);
    }

    public static void printGrid (Table table)
    {
        StringBuffer sb = new StringBuffer ();
        sb.append ("Ext.widget(\"uxgridview\", {").append ("\n");
        sb.append ("startLoad : true,").append ("\n");
        sb.append ("url : \"\",").append ("\n");
        sb.append ("editUrl : \"\",").append ("\n");
        sb.append ("border : true,").append ("\n");
        sb.append ("inViewportShow : false,").append ("\n");
        sb.append ("columns : [new Ext.grid.RowNumberer(), ").append ("\n");
        Field field = null;
        for (int i = 0; i < table.getFields ().size (); i++)
        {
            field = table.getFields ().get (i);
            sb.append ("{").append ("\n");
            sb.append (MessageFormat.format ("header : \"{0}\",", field.getComments ())).append ("\n");
            sb.append (MessageFormat.format ("editname : \"{0}\",",
                                             CamelCaseUtils.toCamelCase (field.getName ()))).append ("\n");
            sb.append (MessageFormat.format ("dataIndex : \"{0}\",", field.getName ())).append ("\n");
            sb.append ("searchoptions : {").append ("\n");
            sb.append ("sopt : [\"eq\", \"cn\"]").append ("\n");
            sb.append ("}").append ("\n");
            if (i == table.getFields ().size () - 1)
            {
                sb.append ("}").append ("\n");
            }
            else
            {
                sb.append ("},").append ("\n");
            }
        }
        
        sb.append ("],").append ("\n");
        sb.append ("jsonReader : {").append ("\n");
        sb.append ("root : 'result.result',").append ("\n");
        sb.append ("id : \"ID\",").append ("\n");
        sb.append ("idName : \"id\",").append ("\n");
        sb.append ("totalProperty : \"result.totalCount\",").append ("\n");
        sb.append ("successProperty : 'success'").append ("\n");
        sb.append ("},").append ("\n");
        sb.append ("navGrid : {").append ("\n");
        sb.append ("add : false,").append ("\n");
        sb.append ("edit : false,").append ("\n");
        sb.append ("del : false,").append ("\n");
        sb.append ("search : true,").append ("\n");
        sb.append ("}\n");
        sb.append ("});");
        
        System.out.println (sb.toString ());
    }

    private static Connection getConnection () throws SQLException
    {
        return DriverManager.getConnection (URL, USERNAME, PASSWORD);
    }

    private static Table getTableInfo (String tableName) throws Exception
    {
        if (StringUtils.isEmpty (tableName))
            throw new Exception ("表名称不能为空！");

        Table table = getTable (tableName);
        setFields (table);
        return table;
    }

    private static Table getTable (String tableName) throws Exception
    {
        if (StringUtils.isEmpty (tableName))
            throw new Exception ("表名称不能为空！");

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Table table = new Table ();
        try
        {
            conn = getConnection ();
            statement = conn.createStatement ();
            resultSet = statement.executeQuery ("select * from user_tab_comments t where t.TABLE_NAME='"
                                                + tableName.toUpperCase () + "'");
            while (resultSet.next ())
            {
                table.setTableName (tableName);
                table.setTableComments (resultSet.getString ("COMMENTS"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
        finally
        {
            if (conn != null)
                conn.close ();
            if (statement != null)
                statement.close ();
            if (resultSet != null)
                resultSet.close ();
        }

        return table;
    }

    private static void setFields (Table table) throws Exception
    {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            conn = getConnection ();
            statement = conn.createStatement ();

            DatabaseMetaData dbmd = conn.getMetaData ();
            resultSet = dbmd.getColumns (conn.getCatalog (), USERNAME.toUpperCase (), table.getTableName ()
                                                                                           .toUpperCase (), null);

            while (resultSet.next ())
            {
                Field field = table.new Field ();
                field.setName (resultSet.getString ("COLUMN_NAME"));
                field.setTypeName (resultSet.getString ("TYPE_NAME"));
                field.setDecimalDigits (resultSet.getInt ("DECIMAL_DIGITS"));
                field.setLength (resultSet.getInt ("COLUMN_SIZE"));
                field.setNull ("YES".equals (resultSet.getString ("IS_NULLABLE")) ? true : false);
                field.setDataType (resultSet.getInt ("DATA_TYPE"));
                table.addField (field);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
        finally
        {
            if (conn != null)
                conn.close ();
            if (statement != null)
                statement.close ();
            if (resultSet != null)
                resultSet.close ();
        }

        setFieldComments (table);
    }

    private static void setFieldComments (Table table) throws Exception
    {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            conn = getConnection ();
            statement = conn.createStatement ();

            resultSet = statement.executeQuery ("select * from user_col_comments t where t.TABLE_NAME='"
                                                + table.getTableName ().toUpperCase () + "'");
            while (resultSet.next ())
            {
                Field field = table.getField (resultSet.getString ("COLUMN_NAME"));
                String comments = resultSet.getString ("COMMENTS");
                if (comments != null)
                    comments = comments.replace ("\n", "").replace ("\t", "");
                if (field != null)
                    field.setComments (comments);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
        finally
        {
            if (conn != null)
                conn.close ();
            if (statement != null)
                statement.close ();
            if (resultSet != null)
                resultSet.close ();
        }
    }
}
