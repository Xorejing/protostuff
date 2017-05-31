/**
 * 
 */
package io.protostuff.runtime;

import io.protostuff.Input;
import io.protostuff.Output;

/**
 * @author marug
 *
 */
public interface RuntimePredicate<T> 
{

	public boolean apply(Input input, T message);
	
	public boolean apply(Output output, T message);
	
	public Output getAttributeOutput(Output output);

	public Output getValueOutput(Output output);

	public interface Factory<T> 
	{
		/**
		 * Creates a new predicate based from the args.
		 * 
		 * @param args
		 * 
		 * @return a new predicate based from the args.
		 */
		public RuntimePredicate<T> create(String[] args);
	}

}
