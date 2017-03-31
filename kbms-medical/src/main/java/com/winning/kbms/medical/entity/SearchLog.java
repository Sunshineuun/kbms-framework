package com.winning.kbms.medical.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "SEARCHLOG", schema = "dbo")
public class SearchLog extends BaseDomain {
	
	private String searchStr;//搜索内容
	private String searchResult;//搜索结果
	
	@Column(name = "SEARCHSTR")
	public String getSearchStr() {
		return searchStr;
	}
	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	@Column(name = "SEARCHRESULT")
	public String getSearchResult() {
		return searchResult;
	}
	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}
	

	
	
	
	
}
