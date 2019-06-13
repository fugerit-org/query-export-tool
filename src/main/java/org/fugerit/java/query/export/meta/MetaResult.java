package org.fugerit.java.query.export.meta;

import java.util.Iterator;

public interface MetaResult {
	
	public Iterator<MetaRecord> recordIterator() throws Exception;

	public int getColumnCount() throws Exception;
	
	public boolean hasHeader() throws Exception;
	
	public Iterator<MetaField> headerIterator() throws Exception;
	
	public int close() throws Exception;
	
}
