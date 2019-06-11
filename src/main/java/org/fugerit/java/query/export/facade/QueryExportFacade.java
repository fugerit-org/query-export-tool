package org.fugerit.java.query.export.facade;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

public class QueryExportFacade {

	public static final String FORMAT_CSV = "csv";
	public static final String FORMAT_DEFAULT = FORMAT_CSV;
	public static final char CSV_SEPARATOR_DEF = ',';
	
	protected static final Logger logger = LoggerFactory.getLogger(QueryExportFacade.class);
	
	public static int export( QueryExportConfig config ) throws Exception {
		int res = 0;
		Statement stm = config.getConn().createStatement();
		logger.info( "sql : "+config.getQuery() );
		ResultSet rs = stm.executeQuery( config.getQuery() );
		int count = 0;
		ResultSetMetaData rsmd = rs.getMetaData();
		if ( FORMAT_CSV.equalsIgnoreCase( config.getFormat() ) ) {
			char separator = config.getSeparator();
			BufferedWriter writer =  new BufferedWriter( new OutputStreamWriter( config.getOutput() , Charset.forName( "UTF-8" ) ) );
			CSVWriter csvwriter = new CSVWriter( writer,
					separator,
					'"',
					CSVWriter.NO_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);
			String[] line = new String[ rsmd.getColumnCount() ];
			for ( int k=0; k<rsmd.getColumnCount(); k++ ) {
				line[k] = rsmd.getColumnLabel( k+1 );
			}
			csvwriter.writeNext( line );
			while ( rs.next() ) {
				for ( int k=0; k<rsmd.getColumnCount(); k++ ) {
					line[k] = rs.getString( k+1 );
				} 
				csvwriter.writeNext( line );
				count++;
			}
			csvwriter.flush();
			csvwriter.close();
		}
		rs.close();
		stm.close();
		logger.info( "record count "+count );
		return res;
	}
	
}
