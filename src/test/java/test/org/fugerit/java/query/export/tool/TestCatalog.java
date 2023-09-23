package test.org.fugerit.java.query.export.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cfg.xml.GenericListCatalogConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.query.export.catalog.QueryConfig;
import org.fugerit.java.query.export.catalog.QueryConfigCatalog;
import org.fugerit.java.query.export.facade.QueryExportConfig;
import org.fugerit.java.query.export.facade.QueryExportFacade;
import org.junit.Test;

public class TestCatalog extends TestBase {
	
	private boolean testWorkerSingle( QueryConfig queryConfig ) {
		return SafeFunction.get( () -> {
			logger.info( "test start" );
			File outputFile = new File( queryConfig.getOutputFile() );
			try ( FileOutputStream fos = new FileOutputStream( outputFile ) ) {
				String format = queryConfig.getOutputFormat();
				String query = queryConfig.getSql();
				Properties params = new Properties();
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
					throw new ConfigRuntimeException( "Unsuppoorted format : "+format );
				}
				config.getParams().putAll( params );
				logger.info( "test end" );
			}
			return outputFile.exists();
		} );
	}

	@Test
	public void testCatalog() throws IOException {
		QueryConfigCatalog catalog = new QueryConfigCatalog();
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/query-catalog-sample.xml" ) ) {
			GenericListCatalogConfig.load(is, catalog);
			List<QueryConfig> queryList = catalog.getListMap( "sample-catalog" );
			for ( QueryConfig queryConfig : queryList ) {
				this.testWorkerSingle(queryConfig);
			}
		}
	}

	
}
