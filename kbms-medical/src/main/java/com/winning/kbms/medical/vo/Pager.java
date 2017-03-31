package com.winning.kbms.medical.vo;

import java.util.List;

import com.winning.kbms.medical.entity.LinstrFileInfo;

public class Pager {
	private int startPage;
	private int startIndex;
	private int maxRows;
	private int endIndex;
	private int totalRows;
	private List<LinstrFileInfo> results;
	
	
	
	public Pager(){
		super();
	}
	
	public Pager(int startPage,int maxRows){
		this.startPage = startPage;
		this.maxRows = maxRows;
		this.startIndex = (startPage-1)*maxRows;
		this.endIndex = startPage*maxRows-1;
	}
	
	
	
	public List<LinstrFileInfo> getResults() {
		return results;
	}

	public void setResults(List<LinstrFileInfo> results) {
		this.results = results;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPage() {
		return this.totalRows/maxRows+1;
	}

	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getMaxRows() {
		return maxRows;
	}
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}
