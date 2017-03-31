package com.winning.kbms.core.domain;

import com.winning.annotations.enums.DictType;
import com.winning.domain.BaseDomain;
import com.winning.kbms.core.enums.DataType;

/**
 * 
 * @author gang.liu
 * @date 2013-4-2
 */
public class ImportItem extends BaseDomain {
	private static final long serialVersionUID = 1L;
	private String name;
	private DataType dataType = DataType.STRING;
	private Dictionary dictionary;
	private boolean required = true;
	private String dateFormat;

	public ImportItem() {
	}

	public ImportItem(String name) {
		this(name, DataType.STRING);
	}

	public ImportItem(String name, DataType type) {
		this.name = name;
		this.dataType = type;
	}

	public ImportItem(String name, Dictionary dictionary) {
		this.name = name;
		this.dictionary = dictionary;
	}

	public ImportItem(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public class Dictionary {
		private DictType dictType; // 1: 字典 2：其他信息，如机构，交易码等
		private String dictCode;

		public Dictionary(DictType dictType, String dictCode) {
			this.dictCode = dictCode;
			this.dictType = dictType;
		}

		public DictType getDictType() {
			return dictType;
		}

		public void setDictType(DictType dictType) {
			this.dictType = dictType;
		}

		public String getDictCode() {
			return dictCode;
		}

		public void setDictCode(String dictCode) {
			this.dictCode = dictCode;
		}
	}
}
