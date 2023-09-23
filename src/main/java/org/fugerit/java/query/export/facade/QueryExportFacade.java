package org.fugerit.java.query.export.facade;

import java.sql.ResultSet;
import java.sql.Statement;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.collection.ListMapStringKey;
import org.fugerit.java.query.export.meta.BasicMetaRSE;
import org.fugerit.java.query.export.meta.BasicMetaResult;
import org.fugerit.java.query.export.meta.MetaResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryExportFacade {

	private QueryExportFacade() {}
	
	public static final String FORMAT_XLS = "xls";
	public static final String FORMAT_XLSX = "xlsx";
	
	public static final String FORMAT_CSV = "csv";
	public static final String FORMAT_DEFAULT = FORMAT_CSV;
	public static final char CSV_SEPARATOR_DEF = ',';
	
	public static final String FORMAT_HTML = "html";

	public static final String ARG_XLS_TEMPLATE = "xls-template";
		
	public static final String ARG_XLS_RESIZE = "xls-resize";
	
	public static final String ARG_XLS_RESIZE_DEFAULT = "false";
	
	public static boolean registerHandler( String type ) {
		boolean ok = false;
		try {
			QueryExportHandler handler = (QueryExportHandler)ClassHelper.newInstance( type );
			setGet( null, handler );
			ok = true;
		} catch (Exception | NoClassDefFoundError e) {
			String message = "Failed to register handler : "+type+" "+e;
			log.warn("{} [set log level to debug for full stack trace]", message );
			log.debug( message, e );
		}
		return ok;
	}
	
	private static final ListMapStringKey<QueryExportHandler> HANDLERS = new ListMapStringKey<>();
	static {
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerCSV" );
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerXLS" );
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerXLSX" );
		registerHandler( "org.fugerit.java.query.export.facade.format.QueryExportHandlerHTML" );
	}
	
	private static synchronized QueryExportHandler setGet( String format, QueryExportHandler handler ) {
		QueryExportHandler res = null;
		if ( format != null ) {
			res = HANDLERS.get( format );
		} else {
			HANDLERS.add( handler );
		}
		return res;
	}
	
	public static int export( QueryExportConfig config ) {
		return SafeFunction.get( () -> {
			int res = 0;
			try (Statement stm = config.getConn().createStatement();
					ResultSet rs = stm.executeQuery( config.getQuery() ) ) {
				log.info( "sql : {}", config.getQuery() );
				MetaResult meta = new BasicMetaResult( BasicMetaRSE.newInstanceAllToString( rs.getMetaData(), config.getObjectFormat() ) , rs );
				export( config, meta );
				int count = meta.close();
				log.info( "record count {}", count );
			}
			return res;
		} );
	}
	
	public static int export( QueryExportConfig config, MetaResult meta ) {
		return SafeFunction.get( () -> {
			int res = 0;
			String format = config.getFormat().toLowerCase();
			QueryExportHandler handler = setGet( format, null );
			if ( handler != null ) {
				res = handler.export(config, meta);
			} else {
				throw new ConfigRuntimeException( "Unsupported export format "+format );
			}
			return res;			
		} );
	}
	
}

