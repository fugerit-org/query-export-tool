package org.fugerit.java.query.export.facade;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import org.fugerit.java.query.export.meta.BasicObjectFormat;

public class QueryExportConfig {

	public static final boolean DEFAULT_EXPORT_HEADER = true;
	
	public static QueryExportConfig newConfigHTML( OutputStream output, Connection conn, String query ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_HTML , QueryExportFacade.CSV_SEPARATOR_DEF, output, conn, query, DEFAULT_EXPORT_HEADER );
	}

	public static QueryExportConfig newConfigCSV( OutputStream output, Connection conn, String query ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_CSV , QueryExportFacade.CSV_SEPARATOR_DEF, output, conn, query, DEFAULT_EXPORT_HEADER );
	}
	
	public static QueryExportConfig newConfigXLS( OutputStream output, Connection conn, String query ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_XLS , QueryExportFacade.CSV_SEPARATOR_DEF, output, conn, query, DEFAULT_EXPORT_HEADER );
	}
	
	public static QueryExportConfig newConfigXLSX( OutputStream output, Connection conn, String query ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_XLSX , QueryExportFacade.CSV_SEPARATOR_DEF, output, conn, query, DEFAULT_EXPORT_HEADER );
	}
	
	public static QueryExportConfig newConfigCSV( OutputStream output, char separator ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_CSV , separator, output, null, null, DEFAULT_EXPORT_HEADER );
	}
	
	public static QueryExportConfig newConfigCSV( OutputStream output, Connection conn, String query, char separator ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_CSV , separator, output, conn, query, DEFAULT_EXPORT_HEADER );
	}
	
	public QueryExportConfig(String format, char separator, OutputStream output, Connection conn, String query) {
		this( format, separator, output, conn, query, true );
	}
	
	public QueryExportConfig(String format, char separator, OutputStream output, Connection conn, String query, boolean exportHeader) {
		super();
		this.format = format;
		this.separator = separator;
		this.output = output;
		this.conn = conn;
		this.query = query;
		this.exportHeader = exportHeader;
		this.params = new Properties();
		this.tryColumnType = false;
	}

	private String format;
		
	private char separator;

	private OutputStream output;
	
	private Connection conn;
	
	private String query;
	
	private boolean exportHeader;

	private BasicObjectFormat objectFormat;
	
	private Properties params;
	
	public Properties getParams() {
		return params;
	}

	public void setParams(Properties params) {
		this.params = params;
	}

	public String getFormat() {
		return format;
	}

	public char getSeparator() {
		return separator;
	}

	public Connection getConn() {
		return conn;
	}

	public String getQuery() {
		return query;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	public OutputStream getOutput() {
		return output;
	}
	public void setOutput(OutputStream output) {
		this.output = output;
	}

	public boolean isExportHeader() {
		return exportHeader;
	}

	public void setExportHeader(boolean exportHeader) {
		this.exportHeader = exportHeader;
	}

	public BasicObjectFormat getObjectFormat() {
		return objectFormat;
	}

	public void setObjectFormat(BasicObjectFormat objectFormat) {
		this.objectFormat = objectFormat;
	}

	/*
	 * if set to true, by default :
	 * string - is unchanged
	 * number - dependent on outout format, for excel : #,###
	 * date - dependent on outout format, for excel : m/d/yy h:mm
	 */
	@Getter @Setter
	private boolean tryColumnType;
	
}
