package org.fugerit.java.query.export.meta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.db.dao.RSExtractor;

public abstract class BasicMetaRSE implements RSExtractor<MetaRecord> {

	private ResultSetMetaData rsmd;
	
	private BasicObjectFormat format;

	public void init( ResultSetMetaData rsmd ) {
		this.init( rsmd , BasicObjectFormat.DEF );
	}
	
	public void init( ResultSetMetaData rsmd, BasicObjectFormat format ) {
		this.rsmd = rsmd;
		this.format = format;
		if ( format == null ) {
			this.format = BasicObjectFormat.DEF;	
		}
	}
	
	public void destroy( ) {
		this.rsmd = null;
	}
	
	protected ResultSetMetaData getRsmd() {
		return rsmd;
	}
	
	public BasicObjectFormat getFormat() {
		return format;
	}

	public static BasicMetaRSE newInstanceAllToString( ResultSetMetaData rsmd, BasicObjectFormat format ) {
		BasicMetaRSEAllToString rse = new BasicMetaRSEAllToString();
		rse.init( rsmd, format );
		return rse;
	}
	
	public static BasicMetaRSE newInstanceAllToString( ResultSetMetaData rsmd ) {
		return newInstanceAllToString( rsmd, BasicObjectFormat.DEF );
	}

}

class BasicMetaRSEAllToString extends BasicMetaRSE {
	
	@Override
	public MetaRecord extractNext(ResultSet rs) throws SQLException {
		List<MetaField> fields = new ArrayList<>();
		for ( int k=0; k<this.getRsmd().getColumnCount(); k++ ) {
			Object current = rs.getObject( k+1 );
			try {
				String value = this.getFormat().format( current );
				MetaField field = new BasicMetaField( value );
				fields.add( field );
			} catch (Exception e) {
				throw new SQLException( "Format error "+e.getMessage() , e );
			}
		}
		return new BasicMetaRecord( fields );
	}
	
}