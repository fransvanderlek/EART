package org.cocopapaya.eajod;

import java.util.List;
import java.util.Map;

import org.cocopapaya.eaapi_mapping.EACollectionListConverter;
import org.cocopapaya.eaapi_mapping.ReflectionMapAdapter;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class MapAdapterTest {

	TestClass example = new TestClass();

	ReflectionMapAdapter adapter = new ReflectionMapAdapter(example);
	
	@Before
	public void setup(){
		adapter.addListConverter(new EACollectionListConverter());
	}

	@Test
	public void testSimpleGetter() {

		Assert.assertEquals("Failure when accessing [title]", example.getTitle(), adapter.get("title").toString());
	}

	@Test
	public void testUppercaseGetter() {

		Assert.assertEquals("Failure when accessing [something]", example.GetSomething(),
				adapter.get("something").toString());
	}

	@Test
	public void testNesting() {

		Assert.assertEquals("Failure when accessing [delegate][foo]", example.getDelegate().getFoo(),
				((Map) adapter.get("delegate")).get("foo").toString());
	}

	@Test
	public void testList() {

		List childList = (List) adapter.get("children");

		Assert.assertEquals("Size error when accessing [children]", example.getChildren().size(), childList.size());

		int index = 0;
		for (TestDelegate delegate : example.getChildren()) {
			String expectedFoo = delegate.getFoo();
			String actualFoo = ((Map) childList.get(index)).get("foo").toString();
			Assert.assertEquals("Erro comparing foo of child[" + index + "]", expectedFoo, actualFoo);

			index++;

		}

	}
	
	@Test
	public void testArray() {

		List childList = (List) adapter.get("arrayChildren");

		Assert.assertEquals("Size error when accessing [children]", example.getArrayChildren().length, childList.size());

		int index = 0;
		for (TestDelegate delegate : example.getArrayChildren()) {
			String expectedFoo = delegate.getFoo();
			String actualFoo = ((Map) childList.get(index)).get("foo").toString();
			Assert.assertEquals("Erro comparing foo of child[" + index + "]", expectedFoo, actualFoo);

			index++;

		}

	}

}
