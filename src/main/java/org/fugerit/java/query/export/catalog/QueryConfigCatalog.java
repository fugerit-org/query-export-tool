package org.fugerit.java.query.export.catalog;

import org.fugerit.java.core.cfg.xml.CustomListCatalogConfig;
import org.fugerit.java.core.util.collection.ListMapStringKey;

public class QueryConfigCatalog extends CustomListCatalogConfig<QueryConfig, ListMapStringKey<QueryConfig>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4378688201418189400L;

	public QueryConfigCatalog() {
		super( "query-catalog" , "query" );
		this.getGeneralProps().setProperty( ATT_TYPE , QueryConfig.class.getName() );
	}
	
}
