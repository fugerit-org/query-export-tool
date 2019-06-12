package org.fugerit.java.query.export.meta;

public class BasicMetaField implements MetaField {

	public BasicMetaField(String stringValue) {
		super();
		this.stringValue = stringValue;
	}

	private String stringValue;

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
