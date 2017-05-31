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

/**
 * @author i78v86e
 *
 */
public class XmlBindingArrayFieldMap<T> implements FieldMap<T> {

    private final List<Field<T>> fields;
    private final DynamicBindingField<T>[] fieldsByNumber;
    private final Map<String, DynamicBindingField<T>> fieldsByName;
    private final Map<String, DynamicBindingField<T>> fieldsByXmlName;

    /**
     * Konstruktor
     * 
     * @param fields
     * 
     * @param lastFieldNumber
     */
    @SuppressWarnings("unchecked")
	public XmlBindingArrayFieldMap(Collection<Field<T>> fields, int lastFieldNumber, String xmlValueFieldName, int xmlValueFieldNumber) 
    {
        fieldsByName = new HashMap<String, DynamicBindingField<T>>();
        fieldsByXmlName = new HashMap<String, DynamicBindingField<T>>();
        fieldsByNumber = (DynamicBindingField<T>[]) new DynamicBindingField<?>[lastFieldNumber + 1];
        for (Field<T> bare : fields)
        {
        	DynamicBindingField<T> f = DynamicBindingField.class.cast(bare);
        	DynamicBindingField<T> last = this.fieldsByName.put(f.name, f);
            if (last != null)
            {
                throw new IllegalStateException(last + " and " + f
                        + " cannot have the same name.");
            }
        	DynamicBindingField<T> xmlLast = this.fieldsByXmlName.put(f.dynamicField.name, f);
            if (xmlLast != null)
            {
                throw new IllegalStateException(xmlLast + " and " + f
                        + " cannot have the same name.");
            }
            if (fieldsByNumber[f.number] != null)
            {
                throw new IllegalStateException(fieldsByNumber[f.number]
                        + " and " + f + " cannot have the same number.");
            }

            fieldsByNumber[f.number] = f;
            if(xmlValueFieldNumber == f.number) 
            {
            	// add xmlValue name mapping
            	fieldsByName.put(xmlValueFieldName, f);
            	fieldsByXmlName.put(xmlValueFieldName, f);
            }
        }

        List<Field<T>> fieldList = new ArrayList<Field<T>>(fields.size());
        for (DynamicBindingField<T> field : fieldsByNumber)
        {
            if (field != null)
                fieldList.add(field);
        }
        this.fields = Collections.unmodifiableList(fieldList);    	
    }


	/**
	 * @see io.protostuff.runtime.FieldMap#getFieldByNumber(int)
	 */
	@Override
	public Field<T> getFieldByNumber(int n) 
	{
        return n < fieldsByNumber.length ? fieldsByNumber[n] : null;
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
