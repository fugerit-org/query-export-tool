package org.fugerit.java.query.export.meta;

import java.util.Iterator;

public interface MetaResult {
	
	public Iterator<MetaRecord> recordIterator();

	public int getColumnCount();
	
	public boolean hasHeader();
	
	public Iterator<MetaField> headerIterator();
	
	public int close();
	
}
