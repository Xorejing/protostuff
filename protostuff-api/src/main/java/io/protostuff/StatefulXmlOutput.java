/**
 * 
 */
package io.protostuff;

/**
 * @author marug
 *
 */
public interface StatefulXmlOutput extends StatefulOutput {

	
	public StatefulXmlOutput getAttributeOutput();

	public StatefulXmlOutput getValueOutput();

}
