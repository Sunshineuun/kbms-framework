package com.winning.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

import com.winning.test.Table.Field;
import com.winning.utils.CamelCaseUtils;

public class MapperBuilder
{
    private static String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@192.168.1.252:1521:orcl";
    private static String USERNAME = "kbms_f";
    private static String PASSWORD = "kbms";
    private static String tableName = "KBMS_LOG_WS_CALL";

    static
    {
        try
        {
            Class.forName (DRIVER_CLASS_NAME);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace ();
        }
    }

    public static void main (String[] args) throws Exception
    {
        Table table = getTableInfo (tableName);
        printMapper (table);
    }

    public static void printMapper (Table table)
    {
        String name = CamelCaseUtils.toCapitalizeCamelCase (tableName.substring (tableName.indexOf ("_") + 1));
        
        StringBuffer sb = new StringBuffer();
        sb.append ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append ("\n");
        sb.append ("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >").append ("\n");
        sb.append ("<mapper namespace=\"" + name + "Mapper\">").append ("\n");
        
        sb.append ("<resultMap id=\"" + name + "Map\" type=\"com.winning.kbms.clinical.rule.domain." + name + "\">").append ("\n");
        Field field = null;
        for (int i = 0; i < table.getFields ().size (); i++)
        {
            field = table.getFields ().get (i);
            if("ID".equals (field.getName ())){
                sb.append ("<id column=\"ID\" property=\"id\" jdbcType=\"VARCHAR\"/>").append ("\n");
            }else if(("UPDATE_TIME".equals (field.getName ()))){
                sb.append ("<result column=\"UPDATE_TIME\" property=\"updateTime\" jdbcType=\"TIMESTAMP\"/>").append ("\n");
            }else{
                sb.append ("<result column=\"" + field.getName () + "\" property=\"" + CamelCaseUtils.toCamelCase (field.getName ()) + "\" jdbcType=\"VARCHAR\"/>").append ("\n");
            }
        }
        sb.append ("</resultMap>").append ("\n");
        
        sb.append ("<insert id=\"add" + name + "\" parameterType=\"com.winning.kbms.clinical.rule.domain." + name + "\">").append ("\n");
        sb.append ("insert into ").append (tableName).append (" (");
        for (int i = 0; i < table.getFields ().size (); i++)
        {
            field = table.getFields ().get (i);
            sb.append (field.getName ());
            if (i != table.getFields ().size () - 1)
                sb.append (",");
        }
        sb.append (")\n").append (" values (");
        
        for (int i = 0; i < table.getFields ().size (); i++)
        {
            field = table.getFields ().get (i);
            if("UPDATE_TIME".equals (field.getName ()))
                sb.append ("#{").append (CamelCaseUtils.toCamelCase(field.getName ())).append (",jdbcType=TIMESTAMP}");
            else
                sb.append ("#{").append (CamelCaseUtils.toCamelCase(field.getName ())).append (",jdbcType=VARCHAR}");
            if (i != table.getFields ().size () - 1)
                sb.append (", ");
        }
        sb.append (")\n").append ("</insert>\n");
        
        
        sb.append ("<update id=\"update" + name + "\" parameterType=\"com.winning.kbms.clinical.rule.domain." + name + "\">").append ("\n");
        sb.append ("update ").append (tableName).append (" set ");
        for (int i = 0; i < table.getFields ().size (); i++){
            field = table.getFields ().get (i);
            if(!"ID".equals (field.getName ())){
                if("UPDATE_TIME".equals (field.getName ())){
                    sb.append (field.getName ()).append ("=").append ("#{").append (CamelCaseUtils.toCamelCase(field.getName ())).append (",jdbcType=TIMESTAMP}");
                }else{
                    sb.append (field.getName ()).append ("=").append ("#{").append (CamelCaseUtils.toCamelCase(field.getName ())).append (",jdbcType=VARCHAR}");
                }
                if (i != table.getFields ().size () - 1)
                    sb.append (", ");
            }
        }
        sb.append (" where id=#{id,jdbcType=VARCHAR}").append ("\n");
        sb.append ("</update>").append ("\n");
        
        sb.append ("<delete id=\"remove" + name + "\" parameterType=\"com.winning.kbms.clinical.rule.domain." + name + "\">").append ("\n");
        sb.append ("delete from " + tableName + " where id=#{id,jdbcType=VARCHAR}").append ("\n");
        sb.append ("</delete>").append ("\n");
        sb.append ("</mapper>");
        
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
