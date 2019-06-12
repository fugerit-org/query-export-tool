package org.fugerit.java.query.export.meta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.db.dao.RSExtractor;

public abstract class BasicMetaRSE implements RSExtractor<MetaRecord> {

	private ResultSetMetaData rsmd;
	
	@Override
	public abstract MetaRecord extractNext(ResultSet rs) throws SQLException;

	public void init( ResultSetMetaData rsmd ) throws Exception {
		this.rsmd = rsmd;
	}
	
	public void destroy( ) throws Exception {
		this.rsmd = null;
	}
	
	protected ResultSetMetaData getRsmd() {
		return rsmd;
	}

	public static BasicMetaRSE newInstanceAllToString( ResultSetMetaData rsmd ) throws Exception {
		BasicMetaRSEAllToString rse = new BasicMetaRSEAllToString();
		rse.init( rsmd );
		return rse;
		
	}
	
}

class BasicMetaRSEAllToString extends BasicMetaRSE {
	
	@Override
	public MetaRecord extractNext(ResultSet rs) throws SQLException {
		List<MetaField> fields = new ArrayList<MetaField>();
		for ( int k=0; k<this.getRsmd().getColumnCount(); k++ ) {
			Object current = rs.getObject( k+1 );
			String value = null;
			if ( current != null ) {
				value = String.valueOf( current );
			}
			MetaField field = new BasicMetaField( value );
			fields.add( field );
		}
		return new BasicMetaRecord( fields );
	}
	
}