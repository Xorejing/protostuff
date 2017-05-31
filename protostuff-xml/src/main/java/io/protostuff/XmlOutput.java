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

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import io.protostuff.StringSerializer.STRING;

/**
 * An output used for writing data with xml format.
 * 
 * @author David Yu
 * @created May 24, 2010
 */
public final class XmlOutput implements StatefulOutput
{

    static final char[] EMPTY = new char[0];
    
    private final XmlAttributeOutput xmlAttributeOutput;
    private final XmlFieldOutput xmlFieldOutput;
    private final XmlValueOutput xmlValueOutput;
    
    private final XMLStreamWriter writer;
    private Schema<?> schema;

    public XmlOutput(XMLStreamWriter writer)
    {
        this(writer, null);
    }

    public XmlOutput(XMLStreamWriter writer, Schema<?> schema)
    {
        this.writer = writer;
        this.schema = schema;
        this.xmlAttributeOutput = new XmlAttributeOutput(writer, schema);
        this.xmlFieldOutput = new XmlFieldOutput(writer, schema);
        this.xmlValueOutput = new XmlValueOutput(writer, schema);
    }

	public XmlOutput use(Schema<?> schema)
    {
        this.schema = schema;
        xmlAttributeOutput.use(schema);
        xmlFieldOutput.use(schema);
        xmlValueOutput.use(schema);
        return this;
    }

    @Override
    public void updateLast(Schema<?> schema, Schema<?> lastSchema)
    {
        if (lastSchema != null && lastSchema == this.schema)
        {
            this.schema = schema;
            xmlAttributeOutput.updateLast(schema, lastSchema);
            xmlFieldOutput.updateLast(schema, lastSchema);
            xmlValueOutput.updateLast(schema, lastSchema);
        }
    }


    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
    	xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Long.toString(value));
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Float.toString(value));
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Double.toString(value));
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), String.valueOf(value));
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), Integer.toString(value));
    }

    @Override
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        xmlFieldOutput.write(writer, schema.getFieldName(fieldNumber), value);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
    	xmlFieldOutput.writeB64Encoded(writer, schema.getFieldName(fieldNumber),
                value.length == 0 ? EMPTY : B64Code.cencode(value));
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        if (utf8String)
        {
            writeString(fieldNumber, STRING.deser(value, offset, length), repeated);
        }
        else
        {
        	xmlFieldOutput.writeB64Encoded(writer, schema.getFieldName(fieldNumber),
                    length == 0 ? EMPTY : B64Code.cencode(value, offset, length));
        }
    }

    @Override
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        final Schema<?> lastSchema = this.schema;
        this.use(schema);

        final XMLStreamWriter writer = this.writer;
        try
        {
            writer.writeStartElement(lastSchema.getFieldName(fieldNumber));

            schema.writeTo(this, value);

            writer.writeEndElement();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }

        // restore state
        this.updateLast(lastSchema, schema);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);
    }
    
    public XmlAttributeOutput getXmlAttributeOutput() 
    {
    	return xmlAttributeOutput;
    }

    public XmlValueOutput getXmlValueOutput() 
    {
    	return xmlValueOutput;
    }

}
