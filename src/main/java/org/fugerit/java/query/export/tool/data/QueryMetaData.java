package org.fugerit.java.query.export.tool.data;

import org.fugerit.java.core.util.collection.ListMapStringKey;

public class QueryMetaData {

	public QueryMetaData() {
		this.fieldMetaData = new ListMapStringKey<FieldMetaData>();
	}
	
	private ListMapStringKey<FieldMetaData> fieldMetaData;

	public ListMapStringKey<FieldMetaData> getFieldMetaData() {
		return fieldMetaData;
	}
	
}
