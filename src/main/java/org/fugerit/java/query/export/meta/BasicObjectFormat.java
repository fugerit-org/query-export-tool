package org.fugerit.java.query.export.meta;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicObjectFormat {

	public String format( Object current ) {
		String value = null;
		if ( current != null ) {
			value = String.valueOf( current );
		}
		return value;
	}

	public static BasicObjectFormat withDateFormat( final String dateFormat, BasicObjectFormat objectFormat ) {
		if ( dateFormat != null ) {
			return new BasicObjectFormat() {
				@Override
				public String format(Object current) {
					if ( current instanceof Date) {
						SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
						return sdf.format( (Date)current );
					}
					return super.format(current);
				}
			};
		} else {
			return objectFormat;
		}
	}

}
