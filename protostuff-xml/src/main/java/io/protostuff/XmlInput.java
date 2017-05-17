//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package io.protostuff;

import static javax.xml.stream.XMLStreamConstants.ATTRIBUTE;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * An input used for reading data with xml format.
 * 
 * @author David Yu
 * @created May 24, 2010
 */
public final class XmlInput implements Input {
	private static final byte[] EMPTY = new byte[0];

	private final XMLStreamReader parser;
	private boolean emptyMessage = false;
	private int attributeCount = 0;
	private int attributeIndex = -1;

	public XmlInput(XMLStreamReader parser) {
		this.parser = parser;
	}

	private int next() throws IOException {
		try {
			return parser.next();
		} catch (XMLStreamException e) {
			throw new XmlInputException(e);
		}
	}

	int nextTag() throws IOException {
		try {
			if (END_ELEMENT == parser.getEventType()) {
				return parser.nextTag();
			}
			attributeCount = parser.getAttributeCount();
			if (0 == attributeCount) {
				return parser.nextTag();
			}

			// element has attributes
			attributeIndex = attributeIndex + 1;
			if (attributeCount == attributeIndex) {
				attributeIndex = -1;
				return parser.nextTag();
			}
			// stay put while parsing the attributes
			return parser.getEventType();

		} catch (XMLStreamException e) {
			throw new XmlInputException(e);
		}
	}

	private byte[] getB64Decoded() throws IOException {
		try {
			for (int next = parser.next();; next = parser.next()) {
				switch (next) {
				case CHARACTERS:
					byte[] decoded = B64Code.cdecode(parser.getTextCharacters(), parser.getTextStart(),
							parser.getTextLength());

					while (END_ELEMENT != parser.next()) {
						// move to next element
					}
					nextTag();
					return decoded;

				case END_ELEMENT:
					// empty bytestring
					// move to next element
					nextTag();
					return EMPTY;

				default:
					continue;
				}
			}
		} catch (XMLStreamException e) {
			throw new XmlInputException(e);
		}
	}

	private String getText() throws IOException {
		try {
			final String text = -1 == attributeIndex ? parser.getElementText()
					: parser.getAttributeValue(attributeIndex);
			// move to next element
			nextTag();
			return text;
		} catch (XMLStreamException e) {
			throw new XmlInputException(e);
		}
	}

	@Override
	public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException {
		final String name = -1 == attributeIndex ? parser.getLocalName() : parser.getAttributeLocalName(attributeIndex);
		while (true) {
			switch (next()) {
			case ATTRIBUTE:
				// we can skip this unknown attribute field.
				nextTag();
				return;
			case END_ELEMENT:
				if (name.equals(parser.getLocalName())) {
					// we can skip this unknown scalar field.
					nextTag();
					return;
				}
				throw new XmlInputException("Unknown field: " + name + " on message " + schema.messageFullName());
			case END_DOCUMENT:
				// malformed xml.
				throw new XmlInputException("Malformed xml on message " + schema.messageFullName());
			case START_ELEMENT:
				// message field
				// we do not know how deep this message is
				throw new XmlInputException("Unknown field: " + name + " on message " + schema.messageFullName());
			default:
				continue;
			}
		}
	}

	@Override
	public <T> int readFieldNumber(final Schema<T> schema) throws IOException {
		if (emptyMessage) {
			emptyMessage = false;
			return 0;
		}

		if (parser.getEventType() == END_ELEMENT) {
			return 0;
		}

		final String name = -1 == attributeIndex ? parser.getLocalName() : parser.getAttributeLocalName(attributeIndex);
		final int num = schema.getFieldNumber(name);

		if (num == 0) {
			while (true) {
				switch (next()) {
				case ATTRIBUTE:
					// we can skip this unknown attribute field.
					nextTag();
					return readFieldNumber(schema);
				case END_ELEMENT:
					if (name.equals(parser.getLocalName())) {
						// we can skip this unknown scalar field.
						nextTag();
						return readFieldNumber(schema);
					}
					throw new XmlInputException("Unknown field: " + name + " on message " + schema.messageFullName());
				case END_DOCUMENT:
					// malformed xml.
					throw new XmlInputException("Malformed xml on message " + schema.messageFullName());
				case START_ELEMENT:
					// message field
					// we do not know how deep this message is
					throw new XmlInputException("Unknown field: " + name + " on message " + schema.messageFullName());
				default:
					continue;
				}
			}
		}

		return num;
	}

	@Override
	public int readInt32() throws IOException {
		return Integer.parseInt(getText());
	}

	@Override
	public int readUInt32() throws IOException {
		return Integer.parseInt(getText());
	}

	@Override
	public int readSInt32() throws IOException {
		return Integer.parseInt(getText());
	}

	@Override
	public int readFixed32() throws IOException {
		return Integer.parseInt(getText());
	}

	@Override
	public int readSFixed32() throws IOException {
		return Integer.parseInt(getText());
	}

	@Override
	public long readInt64() throws IOException {
		return Long.parseLong(getText());
	}

	@Override
	public long readUInt64() throws IOException {
		return Long.parseLong(getText());
	}

	@Override
	public long readSInt64() throws IOException {
		return Long.parseLong(getText());
	}

	@Override
	public long readFixed64() throws IOException {
		return Long.parseLong(getText());
	}

	@Override
	public long readSFixed64() throws IOException {
		return Long.parseLong(getText());
	}

	@Override
	public float readFloat() throws IOException {
		return Float.parseFloat(getText());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.parseDouble(getText());
	}

	@Override
	public boolean readBool() throws IOException {
		return Boolean.parseBoolean(getText());
	}

	@Override
	public int readEnum() throws IOException {
		return Integer.parseInt(getText());
	}

	@Override
	public String readString() throws IOException {
		return getText();
	}

	@Override
	public ByteString readBytes() throws IOException {
		return ByteString.wrap(readByteArray());
	}

	@Override
	public byte[] readByteArray() throws IOException {
		return getB64Decoded();
	}

	@Override
	public <T> T mergeObject(T value, final Schema<T> schema) throws IOException {
		emptyMessage = nextTag() == END_ELEMENT;

		if (value == null) {
			value = schema.newMessage();
		}

		schema.mergeFrom(this, value);
		if (!schema.isInitialized(value)) {
			throw new UninitializedMessageException(value, schema);
		}

		// onto the next
		nextTag();
		return value;
	}

	@Override
	public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber, boolean repeated)
			throws IOException {
		if (utf8String)
			output.writeString(fieldNumber, readString(), repeated);
		else
			output.writeByteArray(fieldNumber, readByteArray(), repeated);
	}

	/**
	 * Reads a byte array/ByteBuffer value.
	 */
	@Override
	public ByteBuffer readByteBuffer() throws IOException {
		return ByteBuffer.wrap(readByteArray());
	}

}
