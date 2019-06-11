package test.org.fugerit.java.query.export.tool;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.junit.Test;

public class TestCSV extends TestBase {

	@Test
	public void test() {
		try {
			initDatabase();
			logger.info( "test start" );
			FileOutputStream fos = new FileOutputStream( new File( "target/test_export.csv" ) );
			String query = " SELECT * FROM  test_export "; 
			QueryExportConfig config = QueryExportConfig.newConfigCSV( fos , getConnection(), query );
			QueryExportFacade.export( config );
			logger.info( "test end" );
		} catch (Exception e) {
			String message = "Error "+e.getMessage();
			logger.error( message, e );
			fail( message );
		}
		
	}

}
