package com.winning.mybatis.builder;

public class Statement {
	private String id;
	private String databaseId;
	private String parameterType;
	private String resultMap;
	private String resultType;
	private String lang;
	private String resultSetType;
	private String sqlCommandType;
	private boolean flushCache;
	private boolean useCache;
	private boolean resultOrdered;

	public boolean isFlushCache() {
		return flushCache;
	}

	public void setFlushCache(boolean flushCache) {
		this.flushCache = flushCache;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public boolean isResultOrdered() {
		return resultOrdered;
	}

	public void setResultOrdered(boolean resultOrdered) {
		this.resultOrdered = resultOrdered;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getResultMap() {
		return resultMap;
	}

	public void setResultMap(String resultMap) {
		this.resultMap = resultMap;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getResultSetType() {
		return resultSetType;
	}

	public void setResultSetType(String resultSetType) {
		this.resultSetType = resultSetType;
	}

	public String getSqlCommandType() {
		return sqlCommandType;
	}

	public void setSqlCommandType(String sqlCommandType) {
		this.sqlCommandType = sqlCommandType;
	}

}
