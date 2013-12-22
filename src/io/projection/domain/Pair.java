package io.projection.domain;

import java.lang.reflect.Field;

/**
 * Defines the relation between a field and the instance where it depends. It's
 * used by the Mapping business logic to define what instance and what field
 * must be mapped from other Pair.
 * 
 * @author Jorge Araújo
 * 
 */
public class Pair {
	Object object;
	Field field;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}
