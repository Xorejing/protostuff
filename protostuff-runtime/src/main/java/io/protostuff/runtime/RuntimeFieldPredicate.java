/**
 * 
 */
package io.protostuff.runtime;

/**
 * @author marug
 *
 */
public abstract class RuntimeFieldPredicate<M> implements RuntimePredicate<java.lang.reflect.Field, M> {

	int fieldMappingIndex = 0;

	@Override
	public boolean skipDeprecated(java.lang.reflect.Field field) {
        if (field.getAnnotation(Deprecated.class) != null) {
            // this field should be ignored by ProtoStuff.
            // preserve its field number for backward-forward compat
        	++fieldMappingIndex;
        	return true;
        }
		return false;
	}

	@Override
	public int getFieldMapping(java.lang.reflect.Field field) {
        // Fields gets assigned mapping tags according to their
        // definition order
		return ++fieldMappingIndex;
	}

	@Override
	public Field<M> createField(java.lang.reflect.Field field, int fieldMapping, IdStrategy strategy) {
		return RuntimeFieldFactory.getFieldFactory(field.getType(), strategy).create(fieldMapping,
		        field.getName(), field, strategy);
	}

}
