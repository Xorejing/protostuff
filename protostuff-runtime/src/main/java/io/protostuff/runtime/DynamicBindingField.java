/**
 * 
 */
package io.protostuff.runtime;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.StatefulInput;
import io.protostuff.StatefulXmlOutput;
import io.protostuff.Tag;

/**
 * dynamic field mapping for stateful xml output 
 * 
 * @author marug
 * 
 */
public class DynamicBindingField<T> extends Field<T> 
{

	public final Field<T> delegate;
	public final Field<T> dynamicField;
	
	private final boolean fieldIsAttribute;
	private final boolean fieldIsValue;

	public DynamicBindingField(final Field<T> field, final Field<T> aliasField, final boolean isXmlAttribute,
			final boolean isXmlValue) 
	{
		super(field.type, field.number, field.name, field.repeated, createTag(field.groupFilter));
		delegate = field;
		dynamicField = aliasField;
		fieldIsAttribute = isXmlAttribute;
		fieldIsValue = isXmlValue;
	}

	/**
	 * @see io.protostuff.runtime.Field#writeTo(io.protostuff.Output,
	 * java.lang.Object)
	 */
	@Override
	protected void writeTo(Output output, T message) throws IOException 
	{
		if(output instanceof StatefulXmlOutput) 
		{
			StatefulXmlOutput statefuloutput = (StatefulXmlOutput) output;
			if(fieldIsAttribute) 
			{
				dynamicField.writeTo(statefuloutput.getAttributeOutput(), message);
			} else if(fieldIsValue)
			{
				dynamicField.writeTo(statefuloutput.getValueOutput(), message);
			} else 
			{
				dynamicField.writeTo(output, message);
			}
		} else 
		{
			delegate.writeTo(output, message);
		}
	}

	/**
	 * @see io.protostuff.runtime.Field#mergeFrom(io.protostuff.Input,
	 * java.lang.Object)
	 */
	@Override
	protected void mergeFrom(Input input, T message) throws IOException 
	{
		if(input instanceof StatefulInput) 
		{
			dynamicField.mergeFrom(input, message);
		} else 
		{
			delegate.mergeFrom(input, message);
		}
	}

	/**
	 * @see io.protostuff.runtime.Field#transfer(io.protostuff.Pipe,
	 * io.protostuff.Input, io.protostuff.Output, boolean)
	 */
	@Override
	protected void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException 
	{
		delegate.transfer(pipe, input, output, repeated);
	}

	private static Tag createTag(final int groupFilter) 
	{
		return new Tag() 
		{

			@Override
			public Class<? extends Annotation> annotationType() 
			{
				return Tag.class;
			}

			@Override
			public int value() 
			{
				return 0;
			}

			@Override
			public String alias() 
			{
				return "";
			}

			@Override
			public int groupFilter() 
			{
				return groupFilter;
			}
		};
	}
	
	/**
	 * retrieve JAXB name alias if present
	 * 
	 * @param field
	 * 
	 * @return element name or attribute name or field name
	 */
	public static String retrieveXmlAlias(final java.lang.reflect.Field field) 
	{
		final String xmlName;
		final XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);
		final XmlElement element = field.getAnnotation(XmlElement.class);
		if (null != attribute && !"##default".equals(attribute.name())) 
		{
			xmlName = attribute.name();
		} else if (null != element && !"##default".equals(element.name())) 
		{
			xmlName = element.name();
		} else 
		{
			xmlName = field.getName();
		}
		return xmlName;
	}
	
	/**
	 * 
	 * @param field
	 * 
	 * @return true if Annotation {@link XmlTransient} is present
	 */
	public static boolean fieldIsXmlTransient(final java.lang.reflect.Field field) 
	{
		return null != field.getAnnotation(XmlTransient.class);
	}
	
	/**
	 * 
	 * @param field
	 * 
	 * @return true if Annotation {@link XmlValue} is present
	 */
	public static boolean fieldIsXmlValue(final java.lang.reflect.Field field) 
	{
		return null != field.getAnnotation(XmlValue.class);
	}
	
	/**
	 * 
	 * @param field
	 * 
	 * @return true if Annotation {@link XmlAttribute} is present
	 */
	public static boolean fieldIsXmlAttribute(final java.lang.reflect.Field field) 
	{
		return null != field.getAnnotation(XmlAttribute.class);
	}
	
}
