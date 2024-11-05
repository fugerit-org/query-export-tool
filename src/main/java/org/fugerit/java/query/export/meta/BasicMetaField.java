package org.fugerit.java.query.export.meta;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicMetaField implements MetaField {

	public BasicMetaField(String stringValue) {
		this( TYPE_STRING, stringValue, null, null );
	}

	public BasicMetaField(String stringValue, Long longValue) {
		this( TYPE_NUMBER, stringValue, longValue, null );
	}

	public BasicMetaField(String stringValue, Timestamp timestampValue) {
		this( TYPE_DATE, stringValue, null, timestampValue );
	}

	@Getter
	private int type;

	@Getter
	private String stringValue;

	@Getter
	private Long numberValue;

	@Getter
	private Timestamp timestampValue;

}
