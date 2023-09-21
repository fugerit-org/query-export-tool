package org.fugerit.java.query.export.meta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fugerit.java.core.cfg.CloseHelper;
import org.fugerit.java.core.function.SafeFunction;

public class BasicMetaResult implements MetaResult {

	public static MetaRecord createMetaRecord( String[] line ) {
		List<MetaField> list = new ArrayList<>( line.length );
		for ( int k=0; k<line.length; k++ ) {
			list.add( new BasicMetaField( line[k] ) );
		}
		return new BasicMetaRecord( list );
	}
	
	public BasicMetaResult( BasicMetaRSE rse, ResultSet rs) {
		super();
		this.rse = rse;
		this.rs = rs;
	}

	private BasicMetaRSE rse;
	
	private ResultSet rs;
	
	private int count;
	
	@Override
	public boolean hasHeader() {
		return true;
	}

	@Override
	public Iterator<MetaField> headerIterator() {
		return SafeFunction.get( () -> {
			List<MetaField> list = new ArrayList<>();
			ResultSetMetaData rsmd = this.rs.getMetaData();
			for ( int k=0; k<rsmd.getColumnCount(); k++ ) {
				list.add( new BasicMetaField( rsmd.getColumnLabel( k+1 ) ) );
			}
			return list.iterator();
		} );
	}

	@Override
	public int getColumnCount() {
		return SafeFunction.get( () -> this.rs.getMetaData().getColumnCount() );
	}

	@Override
	public Iterator<MetaRecord> recordIterator() {
		return new Iterator<MetaRecord>() {
			@Override
			public boolean hasNext() {
				return SafeFunction.get( () -> rs.next() );
			}
			@Override
			public MetaRecord next() {
				return SafeFunction.get( () -> rse.extractNext( rs ) );
			}
			@Override
			public void remove() {
				// no need to do anything here
			}
		};
	}

	@Override
	public int close() {
		CloseHelper.closeRuntimeEx( this.rs );
		this.rs = null;
		this.rse.destroy();
		this.rse = null;
		return this.count;
	}
	
}
