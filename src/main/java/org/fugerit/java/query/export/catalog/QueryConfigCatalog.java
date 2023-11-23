package org.fugerit.java.query.export.catalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cfg.xml.CustomListCatalogConfig;
import org.fugerit.java.core.cfg.xml.GenericListCatalogConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.helper.HelperIOException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.collection.ListMapStringKey;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryConfigCatalog extends CustomListCatalogConfig<QueryConfig, ListMapStringKey<QueryConfig>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4378688201418189400L;

	public QueryConfigCatalog() {
		super( "query-catalog" , "query" );
		this.getGeneralProps().setProperty( ATT_TYPE , QueryConfig.class.getName() );
	}
	
	public static QueryConfigCatalog loadQueryConfigCatalogSafe( String path ) {
		return SafeFunction.get( () -> {
			QueryConfigCatalog catalog = new QueryConfigCatalog();
			try ( InputStream is = StreamHelper.resolveStream( path ) ) {
				GenericListCatalogConfig.load( is ,catalog );
			}
			return catalog;
		} );
	}
	
	public void handle( Connection conn, String catalogId, String queryId ) throws IOException {
		log.info( "handle catalogId : {}, queryId : {}", catalogId, queryId );
		ListMapStringKey<QueryConfig> catalog = this.getListMap( catalogId );
		if ( catalog == null ) {
			throw new IOException( "Catalog not found : "+catalogId );
		}
		QueryConfig queryConfig = catalog.get( queryId );
		if ( queryConfig == null ) {
			throw new IOException( "Query not found : "+queryId+" in catalog : "+catalogId );
		}
		handle(conn, queryConfig);
	}
	
	public static void handle( Connection conn, QueryConfig queryConfig ) throws IOException {
		HelperIOException.apply( () -> {
			QueryExportConfig config = null;
			String outputFile = queryConfig.getOutputFile();
			File file = new File( outputFile );
			String format = queryConfig.getOutputFormat();
			String query = queryConfig.getSql();
			if ( StringUtils.isNotEmpty( queryConfig.getQueryFile() ) ) {
				query = FileIO.readString( queryConfig.getQueryFile() );
			}
			Properties params = new Properties();
			if ( StringUtils.isNotEmpty( queryConfig.getXlsResize() ) ) {
				params.setProperty( QueryExportFacade.ARG_XLS_RESIZE , queryConfig.getXlsResize() );
			}
			if ( StringUtils.isNotEmpty( queryConfig.getXlsTemplate() ) ) {
				params.setProperty( QueryExportFacade.ARG_XLS_TEMPLATE , queryConfig.getXlsTemplate() );
			}
			boolean createPath = BooleanUtils.isTrue( queryConfig.getCreatePath() );
			if ( createPath ) {
				log.info( "create path {} : {}", file, file.getParentFile().mkdirs() );
			}
			try ( FileOutputStream fos = new FileOutputStream( file ) ) {
				log.info( "queryConfig : {}, outputFile : {}", queryConfig, file.getCanonicalPath() );
				if ( QueryExportFacade.FORMAT_CSV.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigCSV( fos , conn, query );
					if ( StringUtils.isNotEmpty( queryConfig.getCsvSeparator() ) ) {
						config.setSeparator( queryConfig.getCsvSeparator().charAt( 0 ) );
					}
				} else if ( QueryExportFacade.FORMAT_HTML.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigHTML( fos , conn, query );
				} else if ( QueryExportFacade.FORMAT_XLSX.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigXLSX( fos , conn, query );

				} else if ( QueryExportFacade.FORMAT_XLS.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigXLS( fos , conn, query );
				} else {
					throw new ConfigRuntimeException( "Unsuppoorted format : "+format );
				}
				config.getParams().putAll( params );
				QueryExportFacade.export(config);
			}
		} );
	}
	
}
