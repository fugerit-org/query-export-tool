package org.fugerit.java.query.export.meta;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

public interface MetaResult {
	
	public Iterator<MetaRecord> recordIterator() throws Exception;

	public ResultSetMetaData getMetaData() throws SQLException;
	
	public boolean hasHeader() throws Exception;
	
	public Iterator<MetaField> headerIterator() throws Exception;
	
	public int close() throws Exception;
	
}
