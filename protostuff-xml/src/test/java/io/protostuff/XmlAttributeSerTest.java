/**
 * 
 */
package io.protostuff;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.BeforeClass;
import org.junit.Test;

import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author marug
 *
 *         erstellt 30.05.2017
 *
 */
public class XmlAttributeSerTest {

	private static String testDir = "src/test/resources/xml/";

	private static Map<String, byte[]> tests;

	@BeforeClass
	public static void before() throws IOException {
		tests = readTests(testDir);
	}

	@Test
	public void shouldSerBazXml() throws IOException {
        Baz target = new Baz();
        XmlIOUtil.mergeFrom(tests.get("baz-no-attributes.xml"), target, target.cachedSchema());
        String expected = new String(tests.get("baz-no-attributes.xml"))
        		.replaceAll("\\s+", " ").replace("> <", "><").trim();

        StringWriter writer = new StringWriter();
		Schema<Baz> bazSchema = RuntimeSchema.createFrom(Baz.class, new XmlRuntimePredicateFactory<Baz>(), 
				new DefaultIdStrategy());
        
        XmlIOUtil.writeTo(writer, target, bazSchema);
        assertTrue(expected.equals(writer.toString().substring(writer.toString().indexOf("?>")+2)));
	}

	@Test
	public void shouldSerBazXml1() throws IOException, XMLStreamException, FactoryConfigurationError {
        Baz target = new Baz();
        XmlIOUtil.mergeFrom(tests.get("baz-one-attribute.xml"), target, target.cachedSchema());
        String expected = new String(tests.get("baz-one-attribute.xml"))
        		.replaceAll("\\s+", " ").replace("> <", "><").trim();

        StringWriter writer = new StringWriter();
		Schema<Baz> bazSchema = RuntimeSchema.createFrom(Baz.class, new XmlRuntimePredicateFactory<Baz>(), 
				new DefaultIdStrategy());
		
		XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
		
        XmlIOUtil.writeTo(xmlWriter, target, bazSchema);
        String result = writer.toString();
        assertTrue(result.contains("<id>1</id>"));
	}

	@Test
	public void shouldSerBazXml2() throws IOException, XMLStreamException, FactoryConfigurationError {
        Baz target = new Baz();
        XmlIOUtil.mergeFrom(tests.get("baz-two-attributes.xml"), target, target.cachedSchema());
        String expected = new String(tests.get("baz-two-attributes.xml"))
        		.replaceAll("\\s+", " ").replace("> <", "><").trim();

        StringWriter writer = new StringWriter();
		Schema<Baz> bazSchema = RuntimeSchema.createFrom(Baz.class, new XmlRuntimePredicateFactory<Baz>(), 
				new DefaultIdStrategy());
		
		XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
		
        XmlIOUtil.writeTo(xmlWriter, target, bazSchema);
        String result = writer.toString();
        assertTrue(result.contains("<id>2</id>"));
	}

	@Test
	public void shouldSerBarXml() throws IOException {
		Bar target = new Bar();
		XmlIOUtil.mergeFrom(tests.get("bar.xml"), target, target.cachedSchema());
        String expected = new String(tests.get("bar.xml")).replaceAll("\\s+", " ").replace("> <", "><");

        StringWriter writer = new StringWriter();
		Schema<Bar> barSchema = RuntimeSchema.createFrom(Bar.class, new XmlRuntimePredicateFactory<Bar>(), 
				new DefaultIdStrategy());
        
        XmlIOUtil.writeTo(writer, target, barSchema);
        String result = writer.toString().substring(writer.toString().indexOf("?>")+2);
        System.out.println(expected);
        System.out.println(result);
        System.out.println();
        assertTrue(result.contains("<id>1</id>"));
        assertTrue(result.contains("<timestamp>567</timestamp>" ));
        assertTrue(result.contains("<name>one-attribute</name>"));
	}

	@Test
	public void shouldSerFooValueXml() throws XmlOutputException, IOException, XMLStreamException {
		FooValue target = new FooValue();
		Schema<FooValue> schema = RuntimeSchema.createFrom(FooValue.class, new XmlRuntimePredicateFactory<FooValue>(), 
				new DefaultIdStrategy());
        XmlIOUtil.mergeFrom(tests.get("foovalue.xml"), target, schema);
        String expected = new String(tests.get("foovalue.xml")).replaceAll("\\s+", " ").replace("> <", "><").trim();
		
        StringWriter writer = new StringWriter();
		XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
		
        XmlIOUtil.writeTo(xmlWriter, target, schema);
        assertTrue(expected.equals(writer.toString()));
		
	}

	@Test
	public void shouldSerBarValueXml() throws XMLStreamException, FactoryConfigurationError, XmlOutputException, IOException {
		DefaultIdStrategy idStrategy = new DefaultIdStrategy();
		Schema<FooValue> fooValueSchema = RuntimeSchema.createFrom(FooValue.class, new XmlRuntimePredicateFactory<FooValue>(), 
				idStrategy);
		idStrategy.registerPojo(FooValue.class, fooValueSchema);

		BarValue target = new BarValue();
		Schema<BarValue> schema = RuntimeSchema.createFrom(BarValue.class, new XmlRuntimePredicateFactory<BarValue>(), 
				idStrategy);
        XmlIOUtil.mergeFrom(tests.get("barvalue.xml"), target, schema);
        String expected = new String(tests.get("barvalue.xml"),Charset.forName("utf-8"))
        		.replaceAll("\\s+", " ").replace("> <", "><").trim();

        StringWriter writer = new StringWriter();
		XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
		
        XmlIOUtil.writeTo(xmlWriter, target, schema);
        assertTrue(expected.equals(writer.toString()));

	}
	
	
	private static Map<String, byte[]> readTests(String testDir) throws IOException {
		Map<String, byte[]> tests = new HashMap<String, byte[]>();
		String[] testfiles = new File(testDir).list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});
		for (String testfile : testfiles) {
			tests.put(testfile, Files.readAllBytes(Paths.get(testDir, testfile)));
		}
		return tests;
	}

}
