/**
 * 
 */
package io.protostuff.runtime;

import java.util.HashSet;

/**
 * @author marug
 *
 */
public class RuntimeFieldExclusions<M> implements RuntimePredicate.Factory<java.lang.reflect.Field, M> 
{

	@Override
	public RuntimePredicate<java.lang.reflect.Field, M> create(final String[] exclusions) 
	{

		final HashSet<String> exclusionSet = new HashSet<String>();
		for (String exclusion : exclusions) {
			exclusionSet.add(exclusion);
		}

		return new RuntimeFieldPredicate<M>() {

			@Override
			public boolean apply(java.lang.reflect.Field field) {
				return !exclusionSet.contains(field.getName());
			}

			@Override
			public boolean apply(java.lang.reflect.Field field, M message) {
				return apply(field);
			}


			@Override
			public String getFieldName(java.lang.reflect.Field field) {
				return field.getName();
			}

		};

	}

}
