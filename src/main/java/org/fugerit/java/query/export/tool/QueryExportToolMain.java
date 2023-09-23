package org.fugerit.java.query.export.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.core.db.connect.ConnectionFactory;
import org.fugerit.java.core.db.connect.ConnectionFactoryImpl;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExportToolMain {

	private QueryExportToolMain() {}
	
	public static final String ARG_DB_CONFIG = "db-config";
	
	public static final String ARG_QUERY_FILE = "query-file";
	
	public static final String ARG_OUTPUT_FILE = "output-file";
	
	public static final String ARG_OUTPUT_FORMAT = "output-format";

	public static final String ARG_CSV_SEPARATOR = "csv-separator";
	
	public static final String ARG_QUERY_SQL = "query-sql";
	
	public static final String ARG_CREATE_PATH = "create-path";
	public static final String ARG_CREATE_PATH_DEFAULT = BooleanUtils.BOOLEAN_FALSE;
	
	protected static final Logger logger = LoggerFactory.getLogger(QueryExportToolMain.class);
	
	private static final String PRINT_PARAM = "param : {} = {}";
	
	public static void worker(Properties params) {
		try {
			logger.info("params: {}", params);
			String dbConfig = params.getProperty(ARG_DB_CONFIG);
			String queryFile = params.getProperty(ARG_QUERY_FILE);
			String querySql = params.getProperty(ARG_QUERY_SQL);
			String outputFile = params.getProperty(ARG_OUTPUT_FILE);
			String outputFormat = params.getProperty(ARG_OUTPUT_FORMAT, QueryExportFacade.FORMAT_DEFAULT );
			String createPath = params.getProperty(ARG_CREATE_PATH, ARG_CREATE_PATH_DEFAULT );
			logger.info(PRINT_PARAM, ARG_DB_CONFIG , dbConfig);
			logger.info(PRINT_PARAM, ARG_QUERY_FILE , queryFile);
			logger.info(PRINT_PARAM, ARG_OUTPUT_FILE , outputFile);
			logger.info(PRINT_PARAM, ARG_OUTPUT_FORMAT , outputFormat);
			String sql = querySql;
			if ( StringUtils.isNotEmpty( queryFile ) ) {
				sql = FileIO.readString( queryFile );
			}
			if ( sql == null || outputFile == null) {
				throw new ConfigRuntimeException("Missing required arguments " + ARG_OUTPUT_FILE + ", " + ARG_QUERY_FILE + " or " + ARG_QUERY_SQL);
			} else {
				Properties props = PropsIO.loadFromFile(dbConfig);
				ConnectionFactory cf = ConnectionFactoryImpl.newInstance(props);
				File output = new File( outputFile );
				if ( BooleanUtils.isTrue( createPath ) && !output.getParentFile().exists() ) {
					logger.info( "create-path {} -> {}", output.getParentFile().getCanonicalPath(), output.getParentFile().mkdirs() );
				}
				try ( Connection conn = cf.getConnection(); 
						FileOutputStream fos = new FileOutputStream( output ) ) {
					String csvSeparator = params.getProperty( ARG_CSV_SEPARATOR, "," );
					QueryExportConfig exportConfig = QueryExportConfig.newConfigCSV( fos, conn, sql, csvSeparator.charAt( 0 ) );
					exportConfig.setFormat( outputFormat );
					exportConfig.setParams( params );
					QueryExportFacade.export( exportConfig );
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		Properties params = ArgUtils.getArgs(args);
		worker(params);
	}
	
}
