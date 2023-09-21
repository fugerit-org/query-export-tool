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
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportHandler;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

public abstract class QueryExportHandlerXLSBase extends QueryExportHandler {

    public static void resizeSheet( Sheet s ) {
        Row row = s.getRow( 0 );
        Iterator<Cell> cells = row.cellIterator();
        while ( cells.hasNext() ) {
                Cell c = cells.next();
                s.autoSizeColumn( c.getColumnIndex() );
        }
    }

	public static void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        resizeSheet( sheet );
	    }
	}
	
	protected QueryExportHandlerXLSBase(String format) {
		super(format);
	}

	public static final String ARG_XLS_TEMPLATE = "xls-template";
	
	public static final String ARG_XLS_RESIZE = "xls-resize";
	
	public static final String ARG_XLS_RESIZE_DEFAULT = "false";
	
	private void addRow( Iterator<MetaField> currentRecord, Sheet sheet, int index ) {
		int col = 0;
		Row row = sheet.createRow( index );
		while ( currentRecord.hasNext() ) {
			MetaField field = currentRecord.next();
			String value = field.getStringValue();
			Cell cell = row.createCell( col );
			cell.setCellValue( value );
			col++;
		}
	}
	
	public abstract Workbook newWorkbook();
	
	public abstract Workbook newWorkbook( InputStream is ) throws IOException;
	
	private Workbook newWorkbookHelper( String xlsTemplate ) throws IOException {
		Workbook workbook = null;
		if ( xlsTemplate == null ) {
			workbook = this.newWorkbook();
		} else {
			try (FileInputStream fis = new FileInputStream( new File( xlsTemplate ) )) {
				workbook = this.newWorkbook( fis );
			}
		}
		return workbook;
	}
	
	public int export( QueryExportConfig config, MetaResult meta ) {
		return SafeFunction.get( () -> {
			int res = 0;
			Sheet sheet = null;
			String xlsTemplate = config.getParams().getProperty( ARG_XLS_TEMPLATE );
			try ( Workbook workbook = this.newWorkbookHelper(xlsTemplate) ) {
				if ( xlsTemplate == null ) {
					sheet = workbook.createSheet();
					if ( meta.hasHeader() ) {
						addRow( meta.headerIterator() , sheet, 0 );
					}
				} else {
					sheet = workbook.getSheetAt( 0 );
				}
				int index = 1;
				Iterator<MetaRecord> itRec = meta.recordIterator();
				while ( itRec.hasNext() ) {
					MetaRecord currentRecord = itRec.next();
					addRow( currentRecord.fieldIterator() , sheet, index );
					index++;
				}
				if ( BooleanUtils.isTrue( config.getParams().getProperty( ARG_XLS_RESIZE ) ) ) {
					resizeSheet( sheet );
				}
				workbook.write( config.getOutput() );
			}
			return res;
		} );
	}

}
