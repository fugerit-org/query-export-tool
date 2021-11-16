package test.org.fugerit.java.query.export.tool;

import static org.junit.Assert.fail;

import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.fugerit.java.query.export.tool.QueryExportToolMain;
import org.junit.Test;

public class TestTool extends TestBase {

	@Test
	public void test() {
		try {
			initDatabase();
			logger.info( "test start" );
			String[] args = {
				"--"+QueryExportToolMain.ARG_DB_CONFIG, "src/test/resources/sample/db-config.properties",
				"--"+QueryExportToolMain.ARG_OUTPUT_FORMAT, QueryExportFacade.FORMAT_HTML,
				"--"+QueryExportToolMain.ARG_OUTPUT_FILE, "target/test-tool.html",
				"--"+QueryExportToolMain.ARG_QUERY_FILE, "src/test/resources/sample/sample-query.sql"
			};
			QueryExportToolMain.main( args );
			logger.info( "test end" );
		} catch (Exception e) {
			String message = "Error "+e.getMessage();
			logger.error( message, e );
			fail( message );
		}
		
	}

}
