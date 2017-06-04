/**
 * 
 */
package io.protostuff.runtime;

import io.protostuff.Exclude;
import io.protostuff.Tag;

/**
 * @author marug
 *
 */
public class RuntimeFieldsTagged<M> implements RuntimePredicate.Factory<java.lang.reflect.Field, M> {

	@Override
	public RuntimePredicate<java.lang.reflect.Field, M> create(String[] args) {

		if (null == args || args.length != 2) {
			throw new RuntimeException("RuntimeFieldsTagged RuntimePredicate needs two parameters.");
		}

		final String typeClassName = args[0];
		final boolean hasFields = Boolean.valueOf(args[1]);

		return new RuntimeFieldPredicate<M>() {

			Boolean annotated = null;

			@Override
			public boolean apply(java.lang.reflect.Field field) {
				return null == field.getAnnotation(Exclude.class);
			}

			@Override
			public boolean apply(java.lang.reflect.Field field, M message) {
				return apply(field);
			}

			@Override
			public String getFieldName(java.lang.reflect.Field field) {
				if (annotated(field)) {
					final Tag tag = field.getAnnotation(Tag.class);
					return tag.alias().isEmpty() ? field.getName() : tag.alias() ;
				}
				return field.getName();
			}

			@Override
			public int getFieldMapping(java.lang.reflect.Field field) {
				if (annotated(field)) {
					// Fields gets assigned mapping tags according to their
					// annotation
					final int fieldMapping = field.getAnnotation(Tag.class).value();
					if (fieldMapping < RuntimeSchema.MIN_TAG_VALUE || fieldMapping > RuntimeSchema.MAX_TAG_VALUE) {
						throw new IllegalArgumentException(
						        RuntimeSchema.ERROR_TAG_VALUE + ": " + fieldMapping + " on " + typeClassName);
					}
					return fieldMapping;
				}
				return ++fieldMappingIndex;
			}

			private boolean annotated(java.lang.reflect.Field field) {
				final Tag tag = field.getAnnotation(Tag.class);
				if (null == tag && null != annotated && annotated) {
					final String message = String.format("%s#%s is not annotated with @Tag", typeClassName,
					        field.getName());
					throw new RuntimeException(message);
				}
				if (null != annotated && !annotated && hasFields && null != tag) {
					throw new RuntimeException("When using annotation-based mapping, "
					        + "all fields must be annotated with @" + Tag.class.getSimpleName());
				}
				annotated = (null != annotated && annotated) || null != tag;
				return annotated;
			}

		};
	}

}
