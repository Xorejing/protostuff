/**
 * 
 */
package io.protostuff;

import io.protostuff.runtime.RuntimePredicate;

/**
 * @author marug
 *
 * erstellt 29.05.2017
 *
 */
public class XmlRuntimePredicateFactory<T> implements RuntimePredicate.Factory<T> 
{

	@Override
	public RuntimePredicate<T> create(String[] args) 
	{
		return new RuntimePredicate<T>() 
		{
			@Override
			public boolean apply(Input input, T message) 
			{
				return input instanceof XmlInput;
			}

			@Override
			public boolean apply(Output output, T message) 
			{
				return output instanceof XmlOutput;
			}

			@Override
			public Output getAttributeOutput(Output output) {
				try
				{
				XmlOutput xmlOutput = XmlOutput.class.cast(output);
				return xmlOutput.getXmlAttributeOutput();
				} catch (ClassCastException e) 
				{
					return output;
				}
			}
			
			@Override
			public Output getValueOutput(Output output) {
				try
				{
				XmlOutput xmlOutput = XmlOutput.class.cast(output);
				return xmlOutput.getXmlValueOutput();
				} catch (ClassCastException e) 
				{
					return output;
				}			
			}
		};
	}

}
