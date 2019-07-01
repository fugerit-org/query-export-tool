package org.fugerit.java.query.export.facade;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

public class QueryExportFacadeXLS {

	public static final String ARG_XLS_TEMPLATE = "xls-template";
	
	private static void addRow( Iterator<MetaField> record, Sheet sheet, int index ) throws Exception {
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
	
	public static int export( QueryExportConfig config, MetaResult meta ) throws Exception {
		int res = 0;
		Workbook workbook = null;
		Sheet sheet = null;
		String xlsTemplate = config.getParams().getProperty( ARG_XLS_TEMPLATE );
		if ( xlsTemplate == null ) {
			if ( QueryExportFacade.FORMAT_XLS.equalsIgnoreCase( config.getFormat() ) ) {
				workbook = new HSSFWorkbook();
			} else {
				workbook = new XSSFWorkbook();
			}
			sheet = workbook.createSheet();
			if ( meta.hasHeader() ) {
				addRow( meta.headerIterator() , sheet, 0 );
			}
		} else {
			FileInputStream fis = new FileInputStream( new File( xlsTemplate ) );
			if ( QueryExportFacade.FORMAT_XLS.equalsIgnoreCase( config.getFormat() ) ) {
				workbook = new HSSFWorkbook( fis );
			} else {
				workbook = new XSSFWorkbook( fis );
			}
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
