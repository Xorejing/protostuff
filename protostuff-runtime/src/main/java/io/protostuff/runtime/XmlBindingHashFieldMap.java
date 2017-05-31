/**
 * 
 */
package io.protostuff.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.protostuff.runtime.HashFieldMap.FieldComparator;

/**
 * @author i78v86e
 *
 */
public final class XmlBindingHashFieldMap<T> implements FieldMap<T> {

    private static final FieldComparator FIELD_COMPARATOR = new FieldComparator();
    private final List<Field<T>> fields;
    private final Map<Integer, DynamicBindingField<T>> fieldsByNumber;
    private final Map<String, DynamicBindingField<T>> fieldsByName;
    private final Map<String, DynamicBindingField<T>> fieldsByXmlName;

	
	/**
	 * 
	 * @param fields
	 * @param xmlValueFieldNumber
	 */
	public XmlBindingHashFieldMap(Collection<Field<T>> fields, String xmlValueFieldName, int xmlValueFieldNumber) 
	{
        fieldsByName = new HashMap<String, DynamicBindingField<T>>();
        fieldsByXmlName = new HashMap<String, DynamicBindingField<T>>();
        fieldsByNumber = new HashMap<Integer, DynamicBindingField<T>>();
        for (Field<T> bare : fields)
        {
        	@SuppressWarnings("unchecked")
			DynamicBindingField<T> f = DynamicBindingField.class.cast(bare);
            if (fieldsByName.containsKey(f.name))
            {
                Field<T> prev = fieldsByName.get(f.name);
                throw new IllegalStateException(prev + " and " + f + " cannot have the same name.");
            }
            if (fieldsByXmlName.containsKey(f.dynamicField.name))
            {
                Field<T> prev = fieldsByXmlName.get(f.dynamicField.name);
                throw new IllegalStateException(prev + " and " + f + " cannot have the same name.");
            }
            if (fieldsByNumber.containsKey(f.number))
            {
                Field<T> prev = fieldsByNumber.get(f.number);
                throw new IllegalStateException(prev + " and " + f + " cannot have the same number.");
            }
            this.fieldsByNumber.put(f.number, f);
            this.fieldsByName.put(f.name, f);
            this.fieldsByXmlName.put(f.dynamicField.name, f);
            if(xmlValueFieldNumber == f.number) 
            {
            	// add xmlValue name mapping
            	fieldsByName.put(xmlValueFieldName, f);
            	fieldsByXmlName.put(xmlValueFieldName, f);
            }
            
        }

        List<Field<T>> fieldList = new ArrayList<Field<T>>(fields.size());
        fieldList.addAll(fields);
        Collections.sort(fieldList, FIELD_COMPARATOR);
        this.fields = Collections.unmodifiableList(fieldList);
	}

	/**
	 * @see io.protostuff.runtime.FieldMap#getFieldByNumber(int)
	 */
	@Override
	public Field<T> getFieldByNumber(int n) 
	{
        return fieldsByNumber.get(n);
	}

	/**
	 * @see io.protostuff.runtime.FieldMap#getFieldByName(java.lang.String)
	 */
	@Override
	public Field<T> getFieldByName(String fieldName) 
	{
		final Field<T> alias = fieldsByXmlName.get(fieldName);
		return null != alias ? alias : fieldsByName.get(fieldName);
	}

	/**
	 * @see io.protostuff.runtime.FieldMap#getFieldCount()
	 */
	@Override
	public int getFieldCount() 
	{
		return fields.size();
	}

	/**
	 * @see io.protostuff.runtime.FieldMap#getFields()
	 */
	@Override
	public List<Field<T>> getFields() 
	{
		return fields;
	}

}
