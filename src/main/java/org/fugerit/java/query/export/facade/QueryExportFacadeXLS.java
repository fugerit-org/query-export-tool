package org.fugerit.java.query.export.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.query.export.meta.MetaField;
import org.fugerit.java.query.export.meta.MetaRecord;
import org.fugerit.java.query.export.meta.MetaResult;

public class QueryExportFacadeXLS {

	private QueryExportFacadeXLS() {}
	
	public static final String ARG_XLS_TEMPLATE = "xls-template";
	
	private static void addRow( Iterator<MetaField> currentRecord, Sheet sheet, int index ) {
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
	
	private static Workbook getWorkbook(String xlsTemplate, QueryExportConfig config ) throws IOException {
		Workbook workbook = null;
		if ( xlsTemplate == null ) {
			if ( QueryExportFacade.FORMAT_XLS.equalsIgnoreCase( config.getFormat() ) ) {
				workbook = new HSSFWorkbook();
			} else {
				workbook = new XSSFWorkbook();
			}
		} else {
			try ( FileInputStream fis = new FileInputStream( new File( xlsTemplate ) ) ) {
				if ( QueryExportFacade.FORMAT_XLS.equalsIgnoreCase( config.getFormat() ) ) {
					workbook = new HSSFWorkbook( fis );
				} else {
					workbook = new XSSFWorkbook( fis );
				}
			}
		}
		return workbook;
	}
	
	public static int export( QueryExportConfig config, MetaResult meta ) {
		return SafeFunction.get( () -> {
			int res = 0;
			Sheet sheet = null;
			String xlsTemplate = config.getParams().getProperty( ARG_XLS_TEMPLATE );
			try ( Workbook workbook = getWorkbook(xlsTemplate, config ) )  {
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
				workbook.write( config.getOutput() );
			}
			return res;
		});
	}
	
}
