package com.winning.kbms.core.service;

import java.io.InputStream;
import java.util.Map;

public interface ImportService<T> {
	public void importExcel(InputStream is, Map<String, Object> params);

	public void importCsv(InputStream is, Map<String, Object> params);
}
