package org.fugerit.java.query.export.meta;

public class BasicObjectFormat {

	public final static BasicObjectFormat DEF = new BasicObjectFormat();
	
	public String format( Object current ) throws Exception {
		String value = null;
		if ( current != null ) {
			value = String.valueOf( current );
		}
		return value;
	}
	
}
