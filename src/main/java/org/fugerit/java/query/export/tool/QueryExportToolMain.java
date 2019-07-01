package org.fugerit.java.query.export.tool;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Properties;

import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.core.db.connect.ConnectionFactory;
import org.fugerit.java.core.db.connect.ConnectionFactoryImpl;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.util.PropsIO;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExportToolMain {

	public static final String ARG_DB_CONFIG = "db-config";
	
	public static final String ARG_QUERY_FILE = "query-file";
	
	public static final String ARG_OUTPUT_FILE = "output-file";
	
	public static final String ARG_OUTPUT_FORMAT = "output-format";

	public static final String ARG_CSV_SEPARATOR = "csv-separator";
	
	protected static final Logger logger = LoggerFactory.getLogger(QueryExportToolMain.class);
	
	public static void main(String[] args) {
		Properties params = ArgUtils.getArgs(args);
		try {
			logger.info("params: " + params);
			String dbConfig = params.getProperty(ARG_DB_CONFIG);
			String queryFile = params.getProperty(ARG_QUERY_FILE);
			String outputFile = params.getProperty(ARG_OUTPUT_FILE);
			String outputFormat = params.getProperty(ARG_OUTPUT_FORMAT, QueryExportFacade.FORMAT_DEFAULT );
			logger.info("param : " + ARG_DB_CONFIG + "=" + dbConfig);
			logger.info("param : " + ARG_QUERY_FILE + "=" + queryFile);
			logger.info("param : " + ARG_OUTPUT_FILE + "=" + outputFile);
			logger.info("param : " + ARG_OUTPUT_FORMAT + "=" + outputFormat);
			if (queryFile == null || outputFile == null) {
				throw new Exception("Missing required arguments " + ARG_OUTPUT_FILE + ", " + ARG_QUERY_FILE);
			} else {
				Properties props = PropsIO.loadFromFile(dbConfig);
				ConnectionFactory cf = ConnectionFactoryImpl.newInstance(props);
				Connection conn = cf.getConnection();
				FileOutputStream fos = new FileOutputStream( outputFile );
				try {
					String sql = FileIO.readString( queryFile );
					String csvSeparator = params.getProperty( ARG_CSV_SEPARATOR, "," );
					QueryExportConfig exportConfig = QueryExportConfig.newConfigCSV( fos, conn, sql, csvSeparator.charAt( 0 ) );
					exportConfig.setFormat( outputFormat );
					exportConfig.setParams( params );
					QueryExportFacade.export( exportConfig );
				} catch (Exception e) {
					throw e;
				} finally {
					if (conn != null) {
						conn.close();
					}
					if ( fos != null ) {
						fos.close();	
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
