package org.fugerit.java.query.export.catalog;

import org.fugerit.java.core.cfg.xml.BasicIdConfigType;

public class QueryConfig extends BasicIdConfigType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 40082112689288L;

	private String sql;
	
	private String outputFile;
	
	private String outputFormat;
	
	private String csvSeparator;
	
	private String xlsResize;
	
	private String createPath;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getCsvSeparator() {
		return csvSeparator;
	}

	public void setCsvSeparator(String csvSeparator) {
		this.csvSeparator = csvSeparator;
	}

	public String getXlsResize() {
		return xlsResize;
	}

	public void setXlsResize(String xlsResize) {
		this.xlsResize = xlsResize;
	}

	public String getCreatePath() {
		return createPath;
	}

	public void setCreatePath(String createPath) {
		this.createPath = createPath;
	}
	
}
