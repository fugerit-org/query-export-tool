package org.fugerit.java.query.export.facade;

import java.sql.ResultSet;
import java.sql.Statement;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.collection.ListMapStringKey;
import org.fugerit.java.query.export.meta.BasicMetaRSE;
import org.fugerit.java.query.export.meta.BasicMetaResult;
import org.fugerit.java.query.export.meta.MetaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExportFacade {

	public static final String FORMAT_XLS = "xls";
	public static final String FORMAT_XLSX = "xlsx";
	
	public static final String FORMAT_CSV = "csv";
	public static final String FORMAT_DEFAULT = FORMAT_CSV;
	public static final char CSV_SEPARATOR_DEF = ',';
	
	public static final String FORMAT_HTML = "html";
	
	protected static final Logger logger = LoggerFactory.getLogger(QueryExportFacade.class);
	
	private static void registerHandler( String type, ListMapStringKey<QueryExportHandler> handlers ) {
		try {
			QueryExportHandler handler = (QueryExportHandler)ClassHelper.newInstance( type );
			handlers.add( handler );
		} catch (Throwable e) {
			String message = "Failed to register handler : "+type+" "+e;
			logger.warn( message+" [set log level to debug for full stack trace]" );
			logger.debug( message, e );
		}
	}
	
	private static ListMapStringKey<QueryExportHandler> HANDLERS = new ListMapStringKey<QueryExportHandler>();
	static {
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerCSV" , HANDLERS );
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerXLS" , HANDLERS );
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerXLSX" , HANDLERS );
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerHTML" , HANDLERS );
	}
	
	public static int export( QueryExportConfig config ) throws Exception {
		int res = 0;
		Statement stm = config.getConn().createStatement();
		logger.info( "sql : "+config.getQuery() );
		ResultSet rs = stm.executeQuery( config.getQuery() );
		MetaResult meta = new BasicMetaResult( BasicMetaRSE.newInstanceAllToString( rs.getMetaData(), config.getObjectFormat() ) , rs );
		export( config, meta );
		int count = meta.close();
		stm.close();
		logger.info( "record count "+count );
		return res;
	}
	
	public static int export( QueryExportConfig config, MetaResult meta ) throws Exception {
		int res = 0;
		String format = config.getFormat().toLowerCase();
		QueryExportHandler handler = HANDLERS.get( format );
		if ( handler != null ) {
			res = handler.export(config, meta);
		} else {
			throw new Exception( "Unsupported export format "+format );
		}
		return res;
	}
	
}

