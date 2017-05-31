/**
 * 
 */
package io.protostuff;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.RuntimeSchema;

public class XmlAttributeDeserTest {

	private static String testDir = "src/test/resources/xml/";

	private static Map<String, byte[]> tests;

	@BeforeClass
	public static void before() throws IOException {
		tests = readTests(testDir);
	}

	@Test
	public void shouldDeserBazXml() {
		Baz toCompare = new Baz();
		toCompare.setId(0);
		toCompare.setName("no-attributes");
		toCompare.setTimestamp(1234);

        Baz target = new Baz();
        XmlIOUtil.mergeFrom(tests.get("baz-no-attributes.xml"), target, target.cachedSchema());
        assertTrue(toCompare.equals(target));
	}

	@Test
	public void shouldDeserBarXml()  {
		Bar toCompare = new Bar();
		toCompare.setSomeBaz(new Baz());
		toCompare.getSomeBaz().setId(1);
		toCompare.getSomeBaz().setName("one-attribute");
		toCompare.getSomeBaz().setTimestamp(567);

        Bar target = new Bar();
        XmlIOUtil.mergeFrom(tests.get("bar.xml"), target, target.cachedSchema());
        assertTrue(toCompare.equals(target));
	}

	@Test
	public void shouldDeserFooValueXml() {
		FooValue toCompare = new FooValue();
		toCompare.setId(4);
		toCompare.setName("myFoo");
		toCompare.setValue(4293781);

		FooValue target = new FooValue();
		Schema<FooValue> schema = RuntimeSchema.createFrom(FooValue.class, new XmlRuntimePredicateFactory<FooValue>(), 
				new DefaultIdStrategy());
        XmlIOUtil.mergeFrom(tests.get("foovalue.xml"), target, schema);
        assertTrue(toCompare.toString().equals(target.toString()));
	}

	@Test
	public void shouldDeserBarValueXml() {
		DefaultIdStrategy idStrategy = new DefaultIdStrategy();
		Schema<FooValue> fooValueSchema = RuntimeSchema.createFrom(FooValue.class, new XmlRuntimePredicateFactory<FooValue>(), 
				idStrategy);
		idStrategy.registerPojo(FooValue.class, fooValueSchema);
		
		FooValue toCompare = new FooValue();
		toCompare.setId(4711);
		toCompare.setName("barFoo");
		toCompare.setValue(42);
		BarValue bar = new BarValue();
		bar.setFoo(toCompare);

		BarValue target = new BarValue();
		Schema<BarValue> schema = RuntimeSchema.createFrom(BarValue.class, new XmlRuntimePredicateFactory<BarValue>(), 
				idStrategy);
        XmlIOUtil.mergeFrom(tests.get("barvalue.xml"), target, schema);
        System.out.println(bar.toString());
        assertTrue(bar.toString().equals(target.toString()));
	}

	@Test
	public void shouldDeserBazXml1() {
		Baz toCompare = new Baz();
		toCompare.setId(1);
		toCompare.setName("one-attribute");
		toCompare.setTimestamp(567);

        Baz target = new Baz();
        XmlIOUtil.mergeFrom(tests.get("baz-one-attribute.xml"), target, target.cachedSchema());
        assertTrue(toCompare.equals(target));
	}

	@Test
	public void shouldDeserBazXml2() {
		Baz toCompare = new Baz();
		toCompare.setId(2);
		toCompare.setName("two-attributes");
		toCompare.setTimestamp(890);

        Baz target = new Baz();
        XmlIOUtil.mergeFrom(tests.get("baz-two-attributes.xml"), target, target.cachedSchema());
        assertTrue(toCompare.equals(target));
	}
	
	@Test
	public void shouldDeserAndPipeBaz() throws Exception {
		Baz toCompare = new Baz();
		toCompare.setId(1);
		toCompare.setName("one-attribute");
		toCompare.setTimestamp(567);

		RuntimeSchema<Baz> schema = RuntimeSchema.createFrom(Baz.class, new XmlRuntimePredicateFactory<Baz>(), 
				new DefaultIdStrategy());
		Pipe.Schema<Baz> pipeSchema = schema.getPipeSchema();

		XmlPipeTest.protostuffRoundTrip(toCompare, schema, pipeSchema);
		
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
