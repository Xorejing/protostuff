/**
 * 
 */
package io.protostuff.runtime;

/**
 * @author i78v86e
 *
 *         erstellt 19.05.2017
 *
 */
public class XmlBindingPredicate implements Predicate {

	/**
	 * 
	 */
	public XmlBindingPredicate() {
		super();
	}

	/**
	 * @see io.protostuff.runtime.Predicate#apply(io.protostuff.runtime.Field)
	 */
	@Override
	public boolean apply(Field<?> f) {
		if (!(f instanceof RuntimeXmlBindingField)) {
			return false;
		}
		RuntimeXmlBindingField<?> field = RuntimeXmlBindingField.class.cast(f);
		return !field.isXmlTransient();
	}

	/**
	 * @see io.protostuff.runtime.Predicate#apply(io.protostuff.runtime.Field, java.lang.Object)
	 */
	@Override
	public boolean apply(Field<?> f, Object message) {
		return apply(f);
	}

}
