/**
 * 
 */
package io.protostuff.runtime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author marug
 *
 */
public class RuntimeXmlBinding<M> implements RuntimePredicate.Factory<java.lang.reflect.Field, M> {


	@Override
	public RuntimePredicate<java.lang.reflect.Field, M> create(String[] args) {
		
		return new RuntimeFieldPredicate<M>() {
			
			@Override
			public boolean apply(java.lang.reflect.Field field) {
				return null != field.getAnnotation(XmlTransient.class);
			}

			@Override
			public boolean apply(java.lang.reflect.Field field, Object message) {
				return apply(field);
			}

			@Override
			public String getFieldName(java.lang.reflect.Field field) {
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

			@Override
			public Field<M> createField(final java.lang.reflect.Field rField, final int fieldMapping, 
					final IdStrategy strategy) {
	            final Field<M> field = RuntimeFieldFactory.getFieldFactory(
	            		rField.getType(), strategy).create(fieldMapping, rField.getName(), rField,
	                    strategy);
				
            	final Field<M> aliasField = RuntimeFieldFactory.getFieldFactory(
            			rField.getType(), strategy).create(fieldMapping,
            					DynamicBindingField.retrieveXmlAlias(rField), rField, strategy);
            
            	final DynamicBindingField<M> xmlBinding = new DynamicBindingField<M>(field, aliasField,
            			DynamicBindingField.fieldIsXmlAttribute(rField), DynamicBindingField.fieldIsXmlValue(rField));

            	return xmlBinding;
			}

			
		};
	}

	
}
