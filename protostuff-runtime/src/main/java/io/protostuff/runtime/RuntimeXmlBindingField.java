/**
 * 
 */
package io.protostuff.runtime;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

/**
 * Runtime Field Extension to hold JAXB infos about fields
 * 
 * @author i78v86e
 *
 *         erstellt 19.05.2017
 *
 */
public class RuntimeXmlBindingField<T> extends Field<T> {

	private boolean xmlAttribute;
	private boolean xmlTransient;
	private String xmlName;

	/**
	 * @param type
	 * @param number
	 * @param name
	 * @param repeated
	 * @param tag
	 */
	public RuntimeXmlBindingField(FieldType type, int number, String name, boolean repeated, Tag tag,
			List<Annotation> jaxbAnnotations) {
		super(type, number, name, repeated, tag);
		// TODO check annotations
	}

	/**
	 * @param type
	 * @param number
	 * @param name
	 * @param tag
	 */
	public RuntimeXmlBindingField(FieldType type, int number, String name, Tag tag) {
		super(type, number, name, tag);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see io.protostuff.runtime.Field#writeTo(io.protostuff.Output, java.lang.Object)
	 */
	@Override
	protected void writeTo(Output output, T message) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see io.protostuff.runtime.Field#mergeFrom(io.protostuff.Input, java.lang.Object)
	 */
	@Override
	protected void mergeFrom(Input input, T message) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see io.protostuff.runtime.Field#transfer(io.protostuff.Pipe, io.protostuff.Input, io.protostuff.Output, boolean)
	 */
	@Override
	protected void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the xmlAttribute
	 */
	public boolean isXmlAttribute() {
		return xmlAttribute;
	}

	/**
	 * @return the xmlTransient
	 */
	public boolean isXmlTransient() {
		return xmlTransient;
	}

	/**
	 * @return the xmlName
	 */
	public String getXmlName() {
		return xmlName;
	}

}
