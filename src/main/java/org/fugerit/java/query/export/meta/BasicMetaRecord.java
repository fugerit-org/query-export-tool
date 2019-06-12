package org.fugerit.java.query.export.meta;

import java.util.Collection;
import java.util.Iterator;

public class BasicMetaRecord implements MetaRecord {

	private Collection<MetaField> fields;
	
	@Override
	public Iterator<MetaField> fieldIterator() {
		return this.fields.iterator();
	}

	public BasicMetaRecord(Collection<MetaField> fields) {
		super();
		this.fields = fields;
	}

}
