package org.fugerit.java.query.export.facade.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportHandler;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

public abstract class QueryExportHandlerXLSBase extends QueryExportHandler {

	public QueryExportHandlerXLSBase(String format) {
		super(format);
	}

	public static final String ARG_XLS_TEMPLATE = "xls-template";
	
	private void addRow( Iterator<MetaField> record, Sheet sheet, int index ) throws Exception {
		int col = 0;
		Row row = sheet.createRow( index );
		while ( record.hasNext() ) {
			MetaField field = record.next();
			String value = field.getStringValue();
			Cell cell = row.createCell( col );
			cell.setCellValue( value );
			col++;
		}
	}
	
	public abstract Workbook newWorkbook();
	
	public abstract Workbook newWorkbook( InputStream is ) throws IOException;
	
	public int export( QueryExportConfig config, MetaResult meta ) throws Exception {
		int res = 0;
		Workbook workbook = null;
		Sheet sheet = null;
		String xlsTemplate = config.getParams().getProperty( ARG_XLS_TEMPLATE );
		if ( xlsTemplate == null ) {
			workbook = this.newWorkbook();
			sheet = workbook.createSheet();
			if ( meta.hasHeader() ) {
				addRow( meta.headerIterator() , sheet, 0 );
			}
		} else {
			FileInputStream fis = new FileInputStream( new File( xlsTemplate ) );
			workbook = this.newWorkbook( fis );
			fis.close();
			sheet = workbook.getSheetAt( 0 );
		}
		int index = 1;
		Iterator<MetaRecord> itRec = meta.recordIterator();
		while ( itRec.hasNext() ) {
			MetaRecord record = itRec.next();
			addRow( record.fieldIterator() , sheet, index );
			index++;
		}
		workbook.write( config.getOutput() );
		workbook.close();
		return res;
	}

}
