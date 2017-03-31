package com.winning.mybatis.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;

public final class ClassMap {
	private String tableName;
	private String resultMapId;
	private String selectSql;
	private String insertSql;
	private String updateSql;
	private String deleteSql;
	private Class<?> type;
	private List<Property> properties = new ArrayList<Property>(5);
	private List<Property> idProperties = new ArrayList<Property>(1);

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public ClassMap(Class<?> type, String tableName, String resultMapId, String selectSql) {
		this.tableName = tableName;
		this.resultMapId = resultMapId;
		this.selectSql = selectSql;
		this.type = type;
	}

	public Property getPropertyByName(String name) {
		for (Property property : properties) {
			if (StringUtils.equals(property.getName(), name))
				return property;
		}

		for (Property property : idProperties) {
			if (StringUtils.equals(property.getName(), name))
				return property;
		}
		return null;
	}

	public String getInsertSql() {
		if (insertSql == null)
			insertSql = generateInsertSql();
		return insertSql;
	}

	public String getUpdateSql() {
		if (updateSql == null)
			updateSql = generateUpdateSql();
		return updateSql;
	}

	public String getDeleteSql() {
		if (deleteSql == null)
			deleteSql = generateDeleteSql();
		return deleteSql;
	}

	private String generateInsertSql() {
		List<Property> properties = this.getAllAddProperties();
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append(this.getTableName()).append(" (");
		Property property;
		for (int i = 0; i < properties.size(); i++) {
			property = properties.get(i);
			sb.append(property.getColumn());
			if (i < properties.size() - 1)
				sb.append(",");
		}
		sb.append(") values (");
		for (int i = 0; i < properties.size(); i++) {
			property = properties.get(i);
			sb.append("#{").append(property.getName()).append(",jdbcType=").append(property.getJdbcTypeName())
					.append("}");
			if (i < properties.size() - 1)
				sb.append(",");
		}
		sb.append(")");
		return sb.toString();
	}

	private String generateUpdateSql() {
		List<Property> properties = this.getAllUpdateProperties();
		StringBuilder sb = new StringBuilder();
		sb.append("update ").append(this.getTableName()).append(" set ");
		Property property;
		for (int i = 0; i < properties.size(); i++) {
			property = properties.get(i);

			sb.append(property.getColumn()).append("=").append("#{").append(property.getName()).append(",jdbcType=")
					.append(property.getJdbcTypeName()).append("}");
			if (i < properties.size() - 1)
				sb.append(",");
		}
		sb.append(" where ");
		for (int i = 0; i < this.getIdProperties().size(); i++) {
			property = this.getIdProperties().get(i);
			sb.append(property.getColumn()).append("=").append("#{").append(property.getName()).append(",jdbcType=")
					.append(property.getJdbcTypeName()).append("}");
			if (i < this.getIdProperties().size() - 1)
				sb.append(" and ");
		}
		return sb.toString();
	}

	private String generateDeleteSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ").append(this.getTableName());
		return sb.toString();
	}

	public List<Property> getAllAddProperties() {
		List<Property> list = new ArrayList<ClassMap.Property>();
		list.addAll(idProperties);
		for (Property property : properties) {
			if (property.isAdd)
				list.add(property);
		}
		return list;
	}

	public List<Property> getAllUpdateProperties() {
		List<Property> list = new ArrayList<ClassMap.Property>();
		for (Property property : properties) {
			if (property.isUpdate)
				list.add(property);
		}
		return list;
	}

	public String[] getColumns() {
		String[] columns = new String[properties.size() + idProperties.size()];
		for (int i = 0; i < idProperties.size(); i++) {
			columns[i] = idProperties.get(i).getColumn();
		}
		for (int i = 0; i < properties.size(); i++) {
			columns[i] = properties.get(i).getColumn();
		}
		return columns;
	}

	public String[] getPropertyNames() {
		String[] propertyNames = new String[properties.size() + idProperties.size()];
		for (int i = 0; i < idProperties.size(); i++) {
			propertyNames[i] = idProperties.get(i).getName();
		}
		for (int i = 0; i < properties.size(); i++) {
			propertyNames[i] = properties.get(i).getName();
		}
		return propertyNames;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getResultMapId() {
		return resultMapId;
	}

	public void setResultMapId(String resultMapId) {
		this.resultMapId = resultMapId;
	}

	public String getSelectSql() {
		if (StringUtils.isEmpty(selectSql)) {
			selectSql = "select * from " + tableName;
		}
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public List<Property> getIdProperties() {
		return idProperties;
	}

	public void addProperty(Property property) {
		properties.add(property);
	}

	public void addIdProperty(Property property) {
		idProperties.add(property);
	}

	public List<Property> getProperties() {
		return properties;
	}

	public class Property {
		private boolean isAdd;
		private boolean isUpdate;
		private String column;
		private String name;
		private Class<?> type;

		public Property(String name, Class<?> type, String column, boolean isAdd, boolean isUpdate) {
			this.name = name;
			this.type = type;
			this.column = column;
			this.isAdd = isAdd;
			this.isUpdate = isUpdate;
		}

		public Class<?> getType() {
			return type;
		}

		public void setType(Class<?> type) {
			this.type = type;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isAdd() {
			return isAdd;
		}

		public void setAdd(boolean isAdd) {
			this.isAdd = isAdd;
		}

		public boolean isUpdate() {
			return isUpdate;
		}

		public void setUpdate(boolean isUpdate) {
			this.isUpdate = isUpdate;
		}

		public JdbcType getJdbcType() {
			if (type == String.class)
				return JdbcType.VARCHAR;
			else if (type == boolean.class || type == Boolean.class)
				return JdbcType.BOOLEAN;
			else if (type == Date.class)
				return JdbcType.TIMESTAMP;
			return JdbcType.VARCHAR;
		}

		public String getJdbcTypeName() {
			switch (getJdbcType()) {
			case VARCHAR:
				return "VARCHAR";
			case BOOLEAN:
				return "BOOLEAN";
			case TIMESTAMP:
				return "TIMESTAMP";
			default :
			    return "VARCHAR";
			}
		}
	}
}
