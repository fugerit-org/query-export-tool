package org.fugerit.java.query.export.facade.format;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.fugerit.java.query.export.facade.QueryExportHandler;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;

public class QueryExportHandlerCSV extends QueryExportHandler {

	@Override
	public int export(QueryExportConfig config, MetaResult meta) {
		return SafeFunction.get( () -> {
			int res = EXIT_OK;
			int columnCount = meta.getColumnCount();
			char separator = config.getSeparator();
			BufferedWriter writer =  new BufferedWriter( new OutputStreamWriter( config.getOutput() , StandardCharsets.UTF_8 ) );
			try ( CSVWriter csvwriter = new CSVWriter( writer,
					separator,
					'"',
					ICSVWriter.NO_ESCAPE_CHARACTER,
					ICSVWriter.DEFAULT_LINE_END) ) {
				String[] line = new String[ columnCount ];
				if ( meta.hasHeader() ) {
					writeRecordCSV(line, meta.headerIterator(), csvwriter);	
				}
				Iterator<MetaRecord> itRec = meta.recordIterator();
				while ( itRec.hasNext() ) {
					MetaRecord currentRecord = itRec.next();
					Iterator<MetaField> itField = currentRecord.fieldIterator();
					writeRecordCSV(line, itField, csvwriter);
				}
				csvwriter.flush();
			}
			return res;
		} );
	}

	public QueryExportHandlerCSV() {
		super( QueryExportFacade.FORMAT_CSV );
	}

	private static void writeRecordCSV( String[] line, Iterator<MetaField> itField, CSVWriter csvwriter ) {
		int k = 0;
		while ( itField.hasNext() ) {
			MetaField field = itField.next();
			line[k] = field.getStringValue();
			k++;
		}
		csvwriter.writeNext( line );
	}

}
