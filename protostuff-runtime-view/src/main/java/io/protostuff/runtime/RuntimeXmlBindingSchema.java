/**
 * 
 */
package io.protostuff.runtime;

import java.util.List;

import io.protostuff.runtime.RuntimeEnv.Instantiator;
import io.protostuff.runtime.RuntimeView.BaseSchema;

/**
 * @author i78v86e
 *
 *         erstellt 19.05.2017
 *
 */
public abstract class RuntimeXmlBindingSchema<T> extends BaseSchema<T> implements FieldMap<T> {
	public final FieldMap<T> fieldMap;

	/**
	 * 
	 */
	protected RuntimeXmlBindingSchema(Class<? super T> typeClass, Instantiator<T> instantiator, FieldMap<T> fieldMap) {
		super(typeClass, instantiator);
		this.fieldMap = fieldMap;
	}

	@Override
	public Field<T> getFieldByNumber(int n) {
		return fieldMap.getFieldByNumber(n);
	}

	@Override
	public Field<T> getFieldByName(String fieldName) {
		return fieldMap.getFieldByName(fieldName);
	}

	@Override
	public int getFieldCount() {
		return fieldMap.getFieldCount();
	}

	@Override
	public List<Field<T>> getFields() {
		return fieldMap.getFields();
	}

}
