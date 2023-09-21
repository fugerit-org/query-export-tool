package org.fugerit.java.query.export.meta;

public class BasicObjectFormat {

	private BasicObjectFormat() {}
	
	public static final BasicObjectFormat DEF = new BasicObjectFormat();
	
	public String format( Object current ) {
		String value = null;
		if ( current != null ) {
			value = String.valueOf( current );
		}
		return value;
	}
	
}
