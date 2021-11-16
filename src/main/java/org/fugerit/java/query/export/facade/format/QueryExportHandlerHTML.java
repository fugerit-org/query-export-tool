package org.fugerit.java.query.export.facade.format;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.fugerit.java.query.export.facade.QueryExportHandler;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

public class QueryExportHandlerHTML extends QueryExportHandler {

	@Override
	public int export(QueryExportConfig config, MetaResult meta) throws Exception {
		int res = EXIT_OK;
		try ( PrintWriter writer =  new PrintWriter( new OutputStreamWriter( config.getOutput() , Charset.forName( "UTF-8" ) ) ) ) {
			writer.println( "<table>" );
			if ( meta.hasHeader() ) {
				writeRecordHTML( meta.headerIterator(), writer, "th");	
			}
			Iterator<MetaRecord> itRec = meta.recordIterator();
			while ( itRec.hasNext() ) {
				MetaRecord record = itRec.next();
				Iterator<MetaField> itField = record.fieldIterator();
				writeRecordHTML( itField, writer, "td");
			}
			writer.println( "</table>" );
		}
		return res;
	}

	public QueryExportHandlerHTML() {
		super( QueryExportFacade.FORMAT_HTML );
	}

	private static void writeRecordHTML( Iterator<MetaField> itField, PrintWriter writer, String cellType ) throws Exception {
		writer.println( "<tr>" );
		while ( itField.hasNext() ) {
			MetaField field = itField.next();
			writer.println( "<"+cellType+">"+field.getStringValue()+"</"+cellType+">" );
		}
		writer.println( "</tr>" );
	}

}
