package com.winning.kbms.core.commons;

import java.beans.PropertyEditorSupport;

public final class BooleanEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ("true".equals(text) || "on".equals(text) || "1".equals(text)) {
			setValue(true);
		} else {
			setValue(false);
		}
	}

	@Override
	public String getAsText() {
		if (getValue() == null)
			return null;

		return getValue().toString();
	}
}
