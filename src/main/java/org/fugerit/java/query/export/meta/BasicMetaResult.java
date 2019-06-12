package org.fugerit.java.query.export.meta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicMetaResult implements MetaResult {

	public BasicMetaResult( BasicMetaRSE rse, ResultSet rs) {
		super();
		this.rse = rse;
		this.rs = rs;
	}

	private BasicMetaRSE rse;
	
	private ResultSet rs;
	
	private int count;
	
	@Override
	public boolean hasHeader() throws Exception {
		return true;
	}

	@Override
	public Iterator<MetaField> headerIterator() throws Exception {
		List<MetaField> list = new ArrayList<MetaField>();
		ResultSetMetaData rsmd = this.rs.getMetaData();
		for ( int k=0; k<rsmd.getColumnCount(); k++ ) {
			list.add( new BasicMetaField( rsmd.getColumnLabel( k+1 ) ) );
		}
		return list.iterator();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.rs.getMetaData();
	}

	@Override
	public Iterator<MetaRecord> recordIterator() throws Exception {
		return new Iterator<MetaRecord>() {
			@Override
			public boolean hasNext() {
				boolean next = false;
				try {
					next = rs.next();
				} catch (SQLException e) {
					throw new RuntimeException( e );
				}
				return next;
			}
			@Override
			public MetaRecord next() {
				MetaRecord record = null;
				try {
					record = rse.extractNext( rs );
					count++;
				} catch (SQLException e) {
					throw new RuntimeException( e );
				}
				return record;
			}
		};
	}

	@Override
	public int close() throws Exception {
		this.rs.close();
		this.rs = null;
		this.rse.destroy();
		this.rse = null;
		return this.count;
	}
	
}
