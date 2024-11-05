package org.fugerit.java.query.export.catalog;

import org.fugerit.java.core.cfg.xml.BasicIdConfigType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class QueryConfig extends BasicIdConfigType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 40082112689288L;

	@Getter @Setter private String sql;
	
	@Getter @Setter private String queryFile;
	
	@Getter @Setter private String outputFile;
	
	@Getter @Setter private String outputFormat;
	
	@Getter @Setter private String csvSeparator;
	
	@Getter @Setter private String xlsResize;
	
	@Getter @Setter private String xlsTemplate;
	
	@Getter @Setter private String createPath;

	@Getter @Setter private String tryColumnType;
	
}
