package org.cocopapaya.eareport.eaapi_adapter;

import java.util.List;
import java.util.Map;

import org.cocopapaya.eareport.eaapi_adapter.LazyMapAdapter;
import org.cocopapaya.eareport.testsupport.TestClass;
import org.cocopapaya.eareport.testsupport.TestDelegate;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class MapAdapterTest {

	TestClass example = new TestClass();

	LazyMapAdapter adapter = new LazyMapAdapter(example);

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
	public void testMapCollection() {

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

		Assert.assertEquals("Size error when accessing [children]", example.getArrayChildren().length,
				childList.size());

		int index = 0;
		for (TestDelegate delegate : example.getArrayChildren()) {
			String expectedFoo = delegate.getFoo();
			String actualFoo = ((Map) childList.get(index)).get("foo").toString();
			Assert.assertEquals("Error comparing foo of child[" + index + "]", expectedFoo, actualFoo);

			index++;

		}

	}

	@Test
	public void testRetrievePublicField() {
		String expectedField = "myName";
		Assert.assertEquals("Error retrieving field [" + expectedField + "]", example.MyName,
				adapter.get(expectedField).toString());
	}
	
	@Test
	public void testRetrieveMapProps(){
		
		String propName = "prop1";
		String propField = "someProps";
		
		String expected = example.someProps.get("prop1").toString();
		String actual = ((Map) adapter.get(propField)).get(propName).toString(); 
		
		Assert.assertEquals("Error retrieving  [" + propField + "]["+propName+"]", expected,actual);
			
	}
	
	@Test
	public void testRetrieveMapObject(){
		
		String propName = "delegate";
		String propField = "someProps";
		
		String expected = ((TestDelegate) example.someProps.get("delegate")).getFoo();
		String actual = ((Map) ((Map) adapter.get(propField)).get(propName)).get("foo").toString(); 
		
		Assert.assertEquals("Error retrieving [" + propField + "]["+propName+"]", expected,actual);
			
	}
	
	@Test
	public void testIntegerProperty(){
		Assert.assertEquals("Failure when accessing [myInteger]", example.getMyInteger(), adapter.get("myInteger"));
	}
	
	@Test
	public void testIntProperty(){
		Assert.assertEquals("Failure when accessing [myInt]", example.getMyInt(), adapter.get("myInt"));
	}

}
