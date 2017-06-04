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
public class XmlValueOutput extends XmlOutputBase {

	/**
	 * @param writer
	 */
	public XmlValueOutput(XMLStreamWriter writer) {
		super(writer);
	}

	/**
	 * @param writer
	 * @param schema
	 */
	public XmlValueOutput(XMLStreamWriter writer, Schema<?> schema) {
		super(writer, schema);
	}

	/**
	 * @see io.protostuff.XmlOutputBase#write(javax.xml.stream.XMLStreamWriter, java.lang.String, java.lang.String)
	 */
	@Override
	void write(XMLStreamWriter writer, String name, String value) throws IOException {
		XmlOutputBase.writeValue(writer, name, value);
	}

	/**
	 * @see io.protostuff.XmlOutputBase#writeB64Encoded(javax.xml.stream.XMLStreamWriter, java.lang.String, char[])
	 */
	@Override
	void writeB64Encoded(XMLStreamWriter writer, String name, char[] value) throws IOException {
		XmlOutputBase.writeB64EncodedValue(writer, name, value);
	}

	@Override
	public StatefulXmlOutput getAttributeOutput() {
		throw new RuntimeException("Wrong output access");
	}

	@Override
	public StatefulXmlOutput getValueOutput() {
		return this;
	}

}
