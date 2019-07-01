package org.fugerit.java.query.export.facade.format;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.fugerit.java.query.export.facade.QueryExportFacade;

public class QueryExportHandlerXLS extends QueryExportHandlerXLSBase {

	public QueryExportHandlerXLS() {
		super( QueryExportFacade.FORMAT_XLS );
	}

	@Override
	public Workbook newWorkbook() {
		return new HSSFWorkbook();
	}

	@Override
	public Workbook newWorkbook(InputStream is) throws IOException {
		return new HSSFWorkbook( is );
	}

	
	
}
