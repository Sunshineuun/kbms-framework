package com.winning.kbms.core.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author gang.liu
 * @date 2013-5-20
 */
public class ExportTitle implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<ExportDefine> exportDefines;
    private List<GroupHeader> groupHeaders;
    private List<GroupHeader> complexGroupHeaders;

    public ExportTitle() {
        super();
    }

    public ExportTitle(List<ExportDefine> exportDefines) {
        this(exportDefines, null, null);
    }

    public ExportTitle(List<ExportDefine> exportDefines, List<GroupHeader> groupHeaders) {
        this(exportDefines, groupHeaders, null);
    }

    public ExportTitle(List<ExportDefine> exportDefines, List<GroupHeader> groupHeaders,
            List<GroupHeader> complexGroupHeaders) {
        super();
        this.exportDefines = exportDefines;
        this.groupHeaders = groupHeaders;
        this.complexGroupHeaders = complexGroupHeaders;
    }

    public List<ExportDefine> getExportDefines() {
        return exportDefines;
    }

    public void setExportDefines(List<ExportDefine> exportDefines) {
        this.exportDefines = exportDefines;
    }

    public List<GroupHeader> getGroupHeaders() {
        return groupHeaders;
    }

    public void setGroupHeaders(List<GroupHeader> groupHeaders) {
        this.groupHeaders = groupHeaders;
    }

    public List<GroupHeader> getComplexGroupHeaders() {
        return complexGroupHeaders;
    }

    public void setComplexGroupHeaders(List<GroupHeader> complexGroupHeaders) {
        this.complexGroupHeaders = complexGroupHeaders;
    }
}
