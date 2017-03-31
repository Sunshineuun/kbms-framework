package com.winning.kbms.core.domain;

import java.io.Serializable;

/**
 * 
 * @author gang.liu
 * @date 2013-5-20
 */
public class GroupHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String startColumnName;
    private int numberOfColumns;
    private String titleText;

    public String getStartColumnName() {
        return startColumnName;
    }

    public void setStartColumnName(String startColumnName) {
        this.startColumnName = startColumnName;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
}
