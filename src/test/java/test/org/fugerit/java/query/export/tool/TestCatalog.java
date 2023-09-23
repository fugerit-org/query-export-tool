package test.org.fugerit.java.query.export.tool;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.query.export.catalog.QueryConfig;
import org.fugerit.java.query.export.catalog.QueryConfigCatalog;
import org.junit.Assert;
import org.junit.Test;

public class TestCatalog extends TestBase {
	
	private boolean testWorkerSingle( QueryConfig queryConfig ) {
		return SafeFunction.get( () -> {
			logger.info( "test start" );
			File outputFile = new File( queryConfig.getOutputFile() );
			try ( Connection conn = getConnection() ) {
				QueryConfigCatalog.handle( conn, queryConfig);
			}
			return outputFile.exists();			
		} );
	}

	@Test
	public void testCatalog() throws IOException, SQLException {
		QueryConfigCatalog catalog = QueryConfigCatalog.loadQueryConfigCatalogSafe( "cl://sample/query-catalog-sample.xml" );
		catalog.getListMap( "sample-catalog" ).stream().forEach( c -> Assert.assertTrue( this.testWorkerSingle(c) ) );
		catalog.getListMap( "fail-catalog" ).stream().forEach(  
				c -> Assert.assertThrows( ConfigRuntimeException.class, () -> this.testWorkerSingle(c) ) );
		// test specific
		try ( Connection conn = getConnection() ) {
			catalog.handle( conn , "main-catalog", "Q010");
			Assert.assertThrows( IOException.class , () -> catalog.handle(conn, "main-catalog", "not-exists" ) );
			Assert.assertThrows( IOException.class , () -> catalog.handle(conn, "not-exists", "not-exists" ) );
		}
	}

	
}
