package org.fugerit.java.query.export.data;

import org.fugerit.java.core.util.collection.KeyObject;

public class FieldMetaData implements KeyObject<String> {

	private int fieldIndex;
	
	private String label;
	
	private String name;
	
	private int sqlType;

	public int getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex(int fieldIndex) {
		this.fieldIndex = fieldIndex;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getKey() {
		return this.getName();
	}

	
}
