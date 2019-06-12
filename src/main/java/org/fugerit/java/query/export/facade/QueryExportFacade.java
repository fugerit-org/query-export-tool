package org.fugerit.java.query.export.facade;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Iterator;

import org.fugerit.java.query.export.meta.BasicMetaRSE;
import org.fugerit.java.query.export.meta.BasicMetaResult;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;
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
		MetaResult meta = new BasicMetaResult( BasicMetaRSE.newInstanceAllToString( rs.getMetaData() ) , rs );
		export( config, meta );
		int count = meta.close();
		stm.close();
		logger.info( "record count "+count );
		return res;
	}
	
	private static void writeRecordCSV( String[] line, Iterator<MetaField> itField, CSVWriter csvwriter ) throws Exception {
		int k = 0;
		while ( itField.hasNext() ) {
			MetaField field = itField.next();
			line[k] = field.getStringValue();
			k++;
		}
		csvwriter.writeNext( line );
	}
	
	public static int export( QueryExportConfig config, MetaResult meta ) throws Exception {
		int res = 0;
		ResultSetMetaData rsmd = meta.getMetaData();
		if ( FORMAT_CSV.equalsIgnoreCase( config.getFormat() ) ) {
			char separator = config.getSeparator();
			BufferedWriter writer =  new BufferedWriter( new OutputStreamWriter( config.getOutput() , Charset.forName( "UTF-8" ) ) );
			CSVWriter csvwriter = new CSVWriter( writer,
					separator,
					'"',
					CSVWriter.NO_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);
			String[] line = new String[ rsmd.getColumnCount() ];
			if ( meta.hasHeader() ) {
				writeRecordCSV(line, meta.headerIterator(), csvwriter);	
			}
			Iterator<MetaRecord> itRec = meta.recordIterator();
			while ( itRec.hasNext() ) {
				MetaRecord record = itRec.next();
				Iterator<MetaField> itField = record.fieldIterator();
				writeRecordCSV(line, itField, csvwriter);
			}
			csvwriter.flush();
			csvwriter.close();
		}
		return res;
	}
	
}
