package org.fugerit.java.query.export.facade.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
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
	
	protected QueryExportHandlerXLSBase(String format) {
		super(format);
	}
	
	private void addRow( Workbook workbook, Iterator<MetaField> currentRecord, Sheet sheet, int index, QueryExportConfig config ) {
		int col = 0;
		Row row = sheet.createRow( index );
		while ( currentRecord.hasNext() ) {
			MetaField field = currentRecord.next();
			Cell cell = row.createCell( col );
			if ( config.isTryColumnType() && field.getType() != MetaField.TYPE_STRING ) {
				if ( field.getType() == MetaField.TYPE_DATE ) {
					DataFormat format = workbook.createDataFormat();
					CellStyle style = workbook.createCellStyle();
					style.setDataFormat( format.getFormat( "m/d/yy h:mm" ) );
					cell.setCellValue(  field.getTimestampValue() );
					cell.setCellStyle(style);
				} else if ( field.getType() == MetaField.TYPE_NUMBER ) {
					DataFormat format = workbook.createDataFormat();
					CellStyle style = workbook.createCellStyle();
					style.setDataFormat( format.getFormat( "#,###" ) );
					cell.setCellValue( field.getNumberValue() );
					cell.setCellStyle(style);
				}
			} else {
				cell.setCellValue( field.getStringValue() );
			}
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
			String xlsTemplate = config.getParams().getProperty( QueryExportFacade.ARG_XLS_TEMPLATE );
			try ( Workbook workbook = this.newWorkbookHelper(xlsTemplate) ) {
				if ( xlsTemplate == null ) {
					sheet = workbook.createSheet();
					if ( meta.hasHeader() ) {
						addRow( workbook, meta.headerIterator() , sheet, 0, config );
					}
				} else {
					sheet = workbook.getSheetAt( 0 );
				}
				int index = 1;
				Iterator<MetaRecord> itRec = meta.recordIterator();
				while ( itRec.hasNext() ) {
					MetaRecord currentRecord = itRec.next();
					addRow( workbook, currentRecord.fieldIterator() , sheet, index, config );
					index++;
				}
				if ( BooleanUtils.isTrue( config.getParams().getProperty( QueryExportFacade.ARG_XLS_RESIZE ) ) ) {
					resizeSheet( sheet );
				}
				workbook.write( config.getOutput() );
			}
			return res;
		} );
	}

}
