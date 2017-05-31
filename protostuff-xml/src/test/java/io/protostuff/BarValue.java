/**
 * 
 */
package io.protostuff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author i78v86e
 *
 * erstellt 30.05.2017
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BarValue {

	private FooValue foo;
	
	/**
	 * 
	 */
	public BarValue() {
		super();
	}

	/**
	 * @return the foo
	 */
	public FooValue getFoo() {
		return foo;
	}

	/**
	 * @param foo the foo to set
	 */
	public void setFoo(FooValue foo) {
		this.foo = foo;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("BarValue: [ ");
		sb.append(foo).append(" ]");
		return sb.toString();
	}
}
