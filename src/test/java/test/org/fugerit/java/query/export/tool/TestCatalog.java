package test.org.fugerit.java.query.export.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cfg.xml.GenericListCatalogConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.query.export.catalog.QueryConfig;
import org.fugerit.java.query.export.catalog.QueryConfigCatalog;
import org.junit.Assert;
import org.junit.Test;

public class TestCatalog extends TestBase {
	
	private boolean testWorkerSingle( QueryConfig queryConfig ) {
		return SafeFunction.get( () -> {
			logger.info( "test start" );
			File outputFile = new File( queryConfig.getOutputFile() );
			QueryConfigCatalog.handle(getConnection(), queryConfig);
			return outputFile.exists();			
		} );
	}

	@Test
	public void testCatalog() throws IOException {
		QueryConfigCatalog catalog = new QueryConfigCatalog();
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/query-catalog-sample.xml" ) ) {
			GenericListCatalogConfig.load(is, catalog);
			catalog.getListMap( "sample-catalog" ).stream().forEach( c -> Assert.assertTrue( this.testWorkerSingle(c) ) );
			catalog.getListMap( "fail-catalog" ).stream().forEach(  
					c -> Assert.assertThrows( ConfigRuntimeException.class, () -> this.testWorkerSingle(c) )
			);
		}
	}

	
}
