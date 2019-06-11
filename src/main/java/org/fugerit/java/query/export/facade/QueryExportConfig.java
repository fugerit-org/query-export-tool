package org.fugerit.java.query.export.facade;

import java.io.OutputStream;
import java.sql.Connection;

public class QueryExportConfig {

	public static QueryExportConfig newConfigCSV( OutputStream output, Connection conn, String query ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_CSV , QueryExportFacade.CSV_SEPARATOR_DEF, output, conn, query );
	}
	
	public static QueryExportConfig newConfigCSV( OutputStream output, Connection conn, String query, char separator ) {
		return new QueryExportConfig( QueryExportFacade.FORMAT_CSV , separator, output, conn, query );
	}
	
	public QueryExportConfig(String format, char separator, OutputStream output, Connection conn, String query) {
		super();
		this.format = format;
		this.separator = separator;
		this.output = output;
		this.conn = conn;
		this.query = query;
	}

	private String format;
		
	private char separator;

	private OutputStream output;
	
	private Connection conn;
	
	private String query;

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
	
}
