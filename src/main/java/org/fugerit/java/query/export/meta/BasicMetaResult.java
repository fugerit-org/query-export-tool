package org.fugerit.java.query.export.meta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fugerit.java.core.cfg.CloseHelper;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.util.IteratorHelper;

public class BasicMetaResult implements MetaResult {
	
	public BasicMetaResult( BasicMetaRSE rse, ResultSet rs) {
		super();
		this.rse = rse;
		this.rs = rs;
		this.count = new SimpleValue<>( 0 );
	}

	private BasicMetaRSE rse;
	
	private ResultSet rs;
	
	private SimpleValue<Integer> count;
	
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
		return IteratorHelper.createSimpleIteratorSafe( 
				rs::next , 
				() -> {
					count.setValue( count.getValue()+1 );
					return rse.extractNext(rs);
				} );
	}

	@Override
	public int close() {
		Exception ex = CloseHelper.closeAll( this.rs, this.rse );
		this.rs = null;
		this.rse = null;
		SafeFunction.applyIfNotNull( ex , () -> { throw new ConfigRuntimeException( ex ); } );
		return this.count.getValue();
	}
	
}
