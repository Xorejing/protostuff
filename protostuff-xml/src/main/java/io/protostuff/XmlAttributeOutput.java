/**
 * 
 */
package io.protostuff;

import java.io.IOException;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author marug
 *
 * erstellt 30.05.2017
 *
 */
public class XmlAttributeOutput extends XmlOutputBase {

	/**
	 * @param writer
	 */
	public XmlAttributeOutput(XMLStreamWriter writer) {
		super(writer);
	}

	/**
	 * @param writer
	 * @param schema
	 */
	public XmlAttributeOutput(XMLStreamWriter writer, Schema<?> schema) {
		super(writer, schema);
	}

	/**
	 * @see io.protostuff.XmlOutputBase#write(javax.xml.stream.XMLStreamWriter, java.lang.String, java.lang.String)
	 */
	@Override
	void write(XMLStreamWriter writer, String name, String value) throws IOException {
		XmlOutputBase.writeAttribute(writer, name, value);
	}

	/**
	 * @see io.protostuff.XmlOutputBase#writeB64Encoded(javax.xml.stream.XMLStreamWriter, java.lang.String, char[])
	 */
	@Override
	void writeB64Encoded(XMLStreamWriter writer, String name, char[] value) throws IOException {
		XmlOutputBase.writeB64EncodedAttribute(writer, name, value);
	}

	@Override
	public StatefulXmlOutput getAttributeOutput() {
		return this;
	}

	@Override
	public StatefulXmlOutput getValueOutput() {
		throw new RuntimeException("Wrong output access");
	}

}
