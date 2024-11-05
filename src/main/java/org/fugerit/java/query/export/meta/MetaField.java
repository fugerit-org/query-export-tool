package org.fugerit.java.query.export.meta;

import java.sql.Timestamp;

public interface MetaField {

	public static final int TYPE_STRING = 1;

	public static final int TYPE_NUMBER = 2;

	public static final int TYPE_DATE = 3;

	String getStringValue();

	default int getType() {
		return TYPE_STRING;
	}

	default Long getNumberValue() {
		return null;
	}

	default Timestamp getTimestampValue() {
		return null;
	}

}
