package test.org.fugerit.java.query.export.tool;

import java.io.File;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.fugerit.java.query.export.tool.QueryExportToolMain;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestTool extends TestBase {

	@Test
	public void testRegisterHandlerKo() {
		SafeFunction.apply( () -> {
			boolean ok = QueryExportFacade.registerHandler( "not.exists.HandlerType" );
			Assert.assertFalse( ok );
		} );			
	}
	
	@Test
	public void testKo1() {
		SafeFunction.apply( () -> {
			log.info( "test start" );
			String[] args = {
				"--"+QueryExportToolMain.ARG_DB_CONFIG, "src/test/resources/sample/db-config.properties",
				"--"+QueryExportToolMain.ARG_QUERY_FILE, "src/test/resources/sample/sample-query.sql"
			};
			QueryExportToolMain.main( args );
			log.info( "test end" );
		} );			
	}
	
	@Test
	public void testKo2() {
		SafeFunction.apply( () -> {
			log.info( "test start" );
			String[] args = {
				"--"+QueryExportToolMain.ARG_DB_CONFIG, "src/test/resources/sample/db-config.properties",
			};
			QueryExportToolMain.main( args );
			log.info( "test end" );
		} );			
	}
	
	@Test
	public void testOk() {
		SafeFunction.apply( () -> {
			log.info( "test start" );
			String[] args = {
				"--"+QueryExportToolMain.ARG_DB_CONFIG, "src/test/resources/sample/db-config.properties",
				"--"+QueryExportToolMain.ARG_OUTPUT_FORMAT, QueryExportFacade.FORMAT_HTML,
				"--"+QueryExportToolMain.ARG_OUTPUT_FILE, "target/test-tool.html",
				"--"+QueryExportToolMain.ARG_QUERY_FILE, "src/test/resources/sample/sample-query.sql"
			};
			QueryExportToolMain.main( args );
			log.info( "test end" );
		} );			
	}
	
	@Test
	public void testOk2() {
		SafeFunction.apply( () -> {
			log.info( "test start" );
			File testCreatePath = new File( "target/subdir/test-tool.html" );
			testCreatePath.delete();
			testCreatePath.getParentFile().delete();
			String[] args = {
				"--"+QueryExportToolMain.ARG_DB_CONFIG, "src/test/resources/sample/db-config.properties",
				"--"+QueryExportToolMain.ARG_OUTPUT_FORMAT, QueryExportFacade.FORMAT_HTML,
				"--"+QueryExportToolMain.ARG_CREATE_PATH, BooleanUtils.BOOLEAN_TRUE,
				"--"+QueryExportToolMain.ARG_OUTPUT_FILE, testCreatePath.getCanonicalPath(),
				"--"+QueryExportToolMain.ARG_QUERY_FILE, "src/test/resources/sample/sample-query.sql"
			};
			QueryExportToolMain.main( args );
			log.info( "test end" );
		} );			
	}
	
	@Test
	public void testOk3() {
		SafeFunction.apply( () -> {
			log.info( "test start" );
			String[] args = {
				"--"+QueryExportToolMain.ARG_DB_CONFIG, "src/test/resources/sample/db-config.properties",
				"--"+QueryExportToolMain.ARG_OUTPUT_FORMAT, QueryExportFacade.FORMAT_HTML,
				"--"+QueryExportToolMain.ARG_CREATE_PATH, BooleanUtils.BOOLEAN_TRUE,
				"--"+QueryExportToolMain.ARG_OUTPUT_FILE, "target/test-tool-1.html",
				"--"+QueryExportToolMain.ARG_QUERY_FILE, "src/test/resources/sample/sample-query.sql"
			};
			QueryExportToolMain.main( args );
			log.info( "test end" );
		} );			
	}

}
