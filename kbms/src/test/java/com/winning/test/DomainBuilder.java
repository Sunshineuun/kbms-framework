package com.winning.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.winning.test.Table.Field;
import com.winning.utils.CamelCaseUtils;

public class DomainBuilder
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
			Class.forName(DRIVER_CLASS_NAME);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception
	{
		Table table = getTableInfo(tableName);

		System.out.println(getEntityString(table));
	}

	public static String getEntityString(Table table)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("package com.winning.kbms.clinical.rule.domain;").append("\n\n");
//				.append("import javax.persistence.Column;").append("\n").append("import javax.persistence.Entity;")
//				.append("\n").append("import javax.persistence.Id;").append("\n")
//				.append("import javax.persistence.Table;").append("\n\n");

		sb.append("import com.winning.kbms.core.domain.BaseDomain;").append("\n\n");

		if (checkHasDate(table.getFields()))
			sb.append("import java.util.Date;").append("\n\n");

//		sb.append("@Entity").append("\n");
//		sb.append("@Table(name = \"").append(table.getTableName()).append("\")").append("\n");

		sb.append("public class ").append(getClassName(table.getTableName())).append(" extends BaseDomain").append("\n")
				.append("{").append("\n");

		sb.append("\t").append("private static final long serialVersionUID = 1L;").append("\n\n");

		sb.append(getFieldsString(table.getFields()));
		sb.append(getFieldsMethodString(table.getFields()));

		sb.append("}");
		return sb.toString();
	}

	private static boolean checkHasDate(List<Field> fields)
	{
		for (Field field : fields)
		{
			if (field.getDataType() == Types.DATE || field.getDataType() == Types.TIMESTAMP)
			{
				return true;
			}
		}
		return false;
	}

	private static String getFieldsMethodString(List<Field> fields)
	{
		StringBuilder sb = new StringBuilder();

		for (Field field : fields)
		{
//			if ("id".equalsIgnoreCase(field.getName()))
//			{
//				sb.append("\t").append("@Id").append("\n");
//				sb.append("\t").append("@Column(name = \"").append(field.getName()).append("\"").append(", length = ")
//						.append(36).append(", nullable = ").append(false).append(")").append("\n");
//			}
//			else
//			{
//				// sb.append("\t").append("@Column(name = \"").append(field.getName()).append("\"").append(", length = ")
//				// .append(field.getLength()).append(", nullable = ").append(field.isNull()).append(")")
//				// .append("\n");
//				sb.append("\t").append("@Column(name = \"").append(field.getName()).append("\"").append(")")
//						.append("\n");
//			}

			sb.append("\t").append("public ").append(getFieldType(field)).append(" get")
					.append(CamelCaseUtils.toCapitalizeCamelCase(field.getName())).append("()").append("\n\t")
					.append("{").append("\n");
			sb.append("\t\t").append("return ").append(CamelCaseUtils.toCamelCase(field.getName())).append(";")
					.append("\n\t").append("}").append("\n\n");

			sb.append("\t").append("public void set").append(CamelCaseUtils.toCapitalizeCamelCase(field.getName()))
					.append("(").append(getFieldType(field)).append(" ")
					.append(CamelCaseUtils.toCamelCase(field.getName())).append(")").append("\n\t").append("{")
					.append("\n");
			sb.append("\t\t").append("this.").append(CamelCaseUtils.toCamelCase(field.getName())).append(" = ")
					.append(CamelCaseUtils.toCamelCase(field.getName())).append(";").append("\n\t").append("}")
					.append("\n\n");

		}

		return sb.toString();
	}

	private static String getFieldsString(List<Field> fields)
	{
		StringBuilder sb = new StringBuilder();

		for (Field field : fields)
		{
			if (field.getComments() != null)
			{
				sb.append("\t").append("// ").append(field.getComments()).append("\n");
			}

			sb.append("\t").append("private ").append(getFieldType(field)).append(" ")
					.append(CamelCaseUtils.toCamelCase(field.getName())).append(";").append("\n\n");
		}

		return sb.toString();
	}

	public static String getFieldType(Field field)
	{
		switch (field.getDataType())
		{
		case Types.VARCHAR:
			return "String";
		case Types.CHAR:
			return "String";
		case Types.DOUBLE:
			return "Double";
		case Types.FLOAT:
			return "Double";
		case Types.DECIMAL:
			return "Double";
		case Types.DATE:
			return "Date";
		case Types.TIMESTAMP:
			return "Date";
		}
		return "null";
	}

	public static String getClassName(String tableName)
	{
		int i = tableName.indexOf("_");
		if (i > 0)
		{
			String name = tableName.substring(i + 1);
			return CamelCaseUtils.toCapitalizeCamelCase(name);
		}
		else
		{
			return CamelCaseUtils.toCapitalizeCamelCase(tableName);
		}

	}

	private static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	private static Table getTableInfo(String tableName) throws Exception
	{
		if (StringUtils.isEmpty(tableName))
			throw new Exception("表名称不能为空！");

		Table table = getTable(tableName);
		setFields(table);
		return table;
	}

	private static Table getTable(String tableName) throws Exception
	{
		if (StringUtils.isEmpty(tableName))
			throw new Exception("表名称不能为空！");

		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Table table = new Table();
		try
		{
			conn = getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery("select * from user_tab_comments t where t.TABLE_NAME='"
					+ tableName.toUpperCase() + "'");
			while (resultSet.next())
			{
				table.setTableName(tableName);
				table.setTableComments(resultSet.getString("COMMENTS"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
				conn.close();
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
		}

		return table;
	}

	private static void setFields(Table table) throws Exception
	{
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try
		{
			conn = getConnection();
			statement = conn.createStatement();

			DatabaseMetaData dbmd = conn.getMetaData();
			resultSet = dbmd.getColumns(conn.getCatalog(), USERNAME.toUpperCase (), table.getTableName().toUpperCase(), null);

			// ResultSetMetaData rsm = resultSet.getMetaData();
			// int col = rsm.getColumnCount();
			// for (int i = 0; i < col; i++)
			// {
			// System.out.println(rsm.getColumnName(i + 1));
			// }

			while (resultSet.next())
			{
				Field field = table.new Field();
				field.setName(resultSet.getString("COLUMN_NAME"));
				field.setTypeName(resultSet.getString("TYPE_NAME"));
				field.setDecimalDigits(resultSet.getInt("DECIMAL_DIGITS"));
				field.setLength(resultSet.getInt("COLUMN_SIZE"));
				field.setNull("YES".equals(resultSet.getString("IS_NULLABLE")) ? true : false);
				field.setDataType(resultSet.getInt("DATA_TYPE"));
				table.addField(field);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
				conn.close();
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
		}

		setFieldComments(table);
	}

	private static void setFieldComments(Table table) throws Exception
	{
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try
		{
			conn = getConnection();
			statement = conn.createStatement();

			resultSet = statement.executeQuery("select * from user_col_comments t where t.TABLE_NAME='"
					+ table.getTableName().toUpperCase() + "'");
			while (resultSet.next())
			{
				Field field = table.getField(resultSet.getString("COLUMN_NAME"));
				String comments = resultSet.getString("COMMENTS");
				if (comments != null)
					comments = comments.replace("\n", "").replace("\t", "");
				if(field != null)
				    field.setComments(comments);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
				conn.close();
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
		}
	}
}

class Table
{
	private String tableName;
	private String tableComments;
	private List<Field> fields = new ArrayList<Table.Field>();

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getTableComments()
	{
		return tableComments;
	}

	public void setTableComments(String tableComments)
	{
		this.tableComments = tableComments;
	}

	public List<Field> getFields()
	{
		return fields;
	}

	public Field getField(String fieldName)
	{
		for (int i = 0; i < fields.size(); i++)
		{
			if (StringUtils.equals(fields.get(i).getName(), fieldName))
			{
				return fields.get(i);
			}
		}
		return null;
	}

	public void addField(Field field)
	{
		fields.add(field);
	}

	class Field
	{
		private String name;
		private String comments;
		private int length;
		private String typeName;
		private int decimalDigits;
		private boolean isNull;
		private int dataType;

		public int getDataType()
		{
			return dataType;
		}

		public void setDataType(int dataType)
		{
			this.dataType = dataType;
		}

		public boolean isNull()
		{
			return isNull;
		}

		public void setNull(boolean isNull)
		{
			this.isNull = isNull;
		}

		public String getTypeName()
		{
			return typeName;
		}

		public void setTypeName(String typeName)
		{
			this.typeName = typeName;
		}

		public int getDecimalDigits()
		{
			return decimalDigits;
		}

		public void setDecimalDigits(int decimalDigits)
		{
			this.decimalDigits = decimalDigits;
		}

		public int getLength()
		{
			return length;
		}

		public void setLength(int length)
		{
			this.length = length;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getComments()
		{
			return comments;
		}

		public void setComments(String comments)
		{
			this.comments = comments;
		}

	}
}
