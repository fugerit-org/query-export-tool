package org.fugerit.java.query.export.facade.format;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.query.export.facade.QueryExportFacade;

public class QueryExportHandlerXLSX extends QueryExportHandlerXLSBase {

	public QueryExportHandlerXLSX() {
		super( QueryExportFacade.FORMAT_XLSX );
	}

	@Override
	public Workbook newWorkbook() {
		return new XSSFWorkbook();
	}

	@Override
	public Workbook newWorkbook(InputStream is) throws IOException {
		return new XSSFWorkbook( is );
	}

}
