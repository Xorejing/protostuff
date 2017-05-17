/**
 * 
 */
package io.protostuff;

import static io.protostuff.SerializableObjects.baz;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * Testing attribute deserialisation
 * 
 * @author i78v86e
 *
 *         erstellt 17.05.2017
 *
 */
public class XmlAttributeDeserTest {

	private String testcaseDir = "src/test/resources/xml/";

	@Test
	public void shouldDeserBaz() {
		Baz bazCompare = new Baz();
		bazCompare.setId(1);
		bazCompare.setName("no-attribute");
		bazCompare.setTimestamp(1234L);
		String baz = readBaz(testcaseDir, "baz-no-attribute.xml");
		Baz newBaz = new Baz();
		XmlIOUtil.mergeFrom(baz.getBytes(StandardCharsets.UTF_8), newBaz, newBaz.cachedSchema());
		assertTrue(bazCompare.equals(newBaz));
	}

	@Test
	public void shouldDeserBazWithAttribute() {
		Baz bazCompare = new Baz();
		bazCompare.setId(2);
		bazCompare.setName("with-attribute");
		bazCompare.setTimestamp(5678L);
		String baz = readBaz(testcaseDir, "baz-with-attribute.xml");
		System.out.println(baz);
		Baz newBaz = new Baz();
		XmlIOUtil.mergeFrom(baz.getBytes(StandardCharsets.UTF_8), newBaz, newBaz.cachedSchema());
		System.out.println(newBaz.toString());
		assertTrue(bazCompare.equals(newBaz));
	}

	private String readBaz(String testcaseDir, String testfile) {
		try {
			return new String(Files.readAllBytes(Paths.get(testcaseDir, testfile)), StandardCharsets.UTF_8);
		} catch (IOException exception) {
			return null;
		}

	}
}
