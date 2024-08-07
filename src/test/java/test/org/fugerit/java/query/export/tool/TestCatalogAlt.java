package test.org.fugerit.java.query.export.tool;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.collection.ListMapStringKey;
import org.fugerit.java.query.export.catalog.QueryConfig;
import org.fugerit.java.query.export.catalog.QueryConfigCatalog;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class TestCatalogAlt extends TestBase {

	@Test
	public void testCatalogAlt() throws IOException, SQLException {
		QueryConfigCatalog catalog = QueryConfigCatalog.loadQueryConfigCatalogSafe("cl://sample/query-catalog-sample-alt.xml");
		ListMapStringKey<QueryConfig> listQuery = catalog.getListMap( "sample-catalog-alt" );
		QueryConfig queryConfig = listQuery.get( "Q001ALT" );
		logger.info( "query config : {}, sql : {}", queryConfig, queryConfig.getSql() );
		Assert.assertEquals( "SELECT * FROM  test_export", queryConfig.getSql() );
		QueryConfig queryConfig2 = listQuery.get( "Q002ALT" );
		logger.info( "query config 2 : {}, sql : {}", queryConfig2, queryConfig2.getSql() );
		Assert.assertEquals( "SELECT * FROM  test_export2", queryConfig2.getSql() );
	}

}
