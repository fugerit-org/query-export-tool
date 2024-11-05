package test.org.fugerit.java.query.export.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.junit.Assert;
import org.junit.Test;

public class TestExport extends TestBase {

	private boolean testWorkerSingle( String format, Properties params ) {
		return this.testWorkerSingle( format, params, false, "" );
	}

	private boolean testWorkerSingle( String format, Properties params, boolean tryColumnType, String fileNameAppend ) {
		return SafeFunction.get( () -> {
			logger.info( "test start" );
			File outputFile = new File( "target/test_export_"+fileNameAppend+params.size()+"."+format );
			try ( FileOutputStream fos = new FileOutputStream( outputFile ) ) {
				String query = " SELECT * FROM  test_export "; 
				QueryExportConfig config = null;
				if ( QueryExportFacade.FORMAT_CSV.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigCSV( fos , getConnection(), query );
				} else if ( QueryExportFacade.FORMAT_HTML.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigHTML( fos , getConnection(), query );
				} else if ( QueryExportFacade.FORMAT_XLSX.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigXLSX( fos , getConnection(), query );
				} else if ( QueryExportFacade.FORMAT_XLS.equalsIgnoreCase( format ) ) {
					config = QueryExportConfig.newConfigXLS( fos , getConnection(), query );
				} else {
					config = new QueryExportConfig(format, ',', fos, getConnection(), query);		// fail
				}
				config.setTryColumnType( tryColumnType );
				config.getParams().putAll( params );
				QueryExportFacade.export( config );
				logger.info( "test end" );
			}
			return outputFile.exists();
		} );
	}

	@Test
	public void testXlsxTemplate() {
		Properties params = new Properties();
		params.setProperty( QueryExportFacade.ARG_XLS_TEMPLATE , "src/test/resources/template/test_template.xlsx" );
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_XLSX, params );
		Assert.assertTrue( ok );
	}

	@Test
	public void testXlsTemplate() {
		Properties params = new Properties();
		params.setProperty( QueryExportFacade.ARG_XLS_TEMPLATE , "src/test/resources/template/test_template.xls" );
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_XLS, params );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testXlsx() {
		Properties params = new Properties();
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_XLSX, params );
		Assert.assertTrue( ok );
	}

	@Test
	public void testXlsxTryType() {
		Properties params = new Properties();
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_XLSX, params, true, "try_column_type_" );
		Assert.assertTrue( ok );
	}

	@Test
	public void testXls() {
		Properties params = new Properties();
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_XLS, params );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testCsv() {
		Properties params = new Properties();
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_CSV, params );
		Assert.assertTrue( ok );
	}

	@Test
	public void testHtml() {
		Properties params = new Properties();
		boolean ok = this.testWorkerSingle( QueryExportFacade.FORMAT_HTML, params );
		Assert.assertTrue( ok );
	}

	@Test
	public void testFail() {
		Properties params = new Properties();
		Assert.assertThrows( ConfigRuntimeException.class , () -> this.testWorkerSingle( "fail", params ) );
	}
	
}
