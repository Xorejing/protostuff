/**
 * 
 */
package io.protostuff.runtime;

/**
 * 
 *
 */
public interface RuntimePredicate<T, M> 
{

	   /**
     * Returns true if the field is included.
     */
    public boolean apply(T field);

    /**
     * Returns true if the field is included.
     * <p>
     * The predicate logic can be dynamic based on the contents of the message.
     */
    public boolean apply(T field, M message);
    
    /**
     * skip the @Deprecated fields while preserving the field number
     * 
     * @param field
     * @return
     */
    public boolean skipDeprecated(T field);
    
    /**
     * retrieve the fieldMap name to use
     * 
     * @param field
     * 
     * @return the fieldMap name to use
     */
    public String getFieldName(T field);
    
    /**
     * retrieve the field number to use
     * 
     * @param field
     * 
     * @return the field number to use
     */
    public int getFieldMapping(T field);
    
    /**
     * provides the recipe for creating {@link Field} descriptions
     * 
     * @return a protostuff Field description
     */
    public Field<M> createField(T field, int fieldMapping, IdStrategy strategy);
    
    public interface Factory<T, M>
    {
        /**
         * Creates a new predicate based from the args.
         */
        public RuntimePredicate<T, M> create(String[] args);
    }

}
