package org.fugerit.java.query.export.facade.format;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.fugerit.java.query.export.facade.QueryExportHandler;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

import com.opencsv.CSVWriter;

public class QueryExportHandlerCSV extends QueryExportHandler {

	@Override
	public int export(QueryExportConfig config, MetaResult meta) throws Exception {
		int res = EXIT_OK;
		int columnCount = meta.getColumnCount();
		char separator = config.getSeparator();
		BufferedWriter writer =  new BufferedWriter( new OutputStreamWriter( config.getOutput() , Charset.forName( "UTF-8" ) ) );
		CSVWriter csvwriter = new CSVWriter( writer,
				separator,
				'"',
				CSVWriter.NO_ESCAPE_CHARACTER,
				CSVWriter.DEFAULT_LINE_END);
		String[] line = new String[ columnCount ];
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
		return res;
	}

	public QueryExportHandlerCSV() {
		super( QueryExportFacade.FORMAT_CSV );
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

}
