package org.fugerit.java.query.export.facade;

import org.fugerit.java.core.util.collection.KeyObject;
import org.fugerit.java.query.export.meta.MetaResult;

public abstract class QueryExportHandler implements KeyObject<String> {

	public static final int EXIT_OK = 0;
	
	private String format;

	public String getFormat() {
		return format;
	}

	protected QueryExportHandler(String format) {
		super();
		this.format = format;
	}
	
	public abstract int export( QueryExportConfig config, MetaResult meta );

	@Override
	public String getKey() {
		return this.getFormat();
	}

}
