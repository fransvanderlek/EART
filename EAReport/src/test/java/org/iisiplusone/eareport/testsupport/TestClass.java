package org.iisiplusone.eareport.testsupport;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestClass {
	
	public String MyName = "test";
	
	public boolean isGood(){
		return true;
	}

	public String getTitle() {
		return "hello";
	}

	public String GetSomething() {
		return "some-thing";
	}

	public String nonGetter() {
		return "not-a-getter";
	}
	
	public Integer getMyInteger(){
		return new Integer(3);
	}
	
	public int getMyInt(){
		return 6;
	}

	public TestDelegate getDelegate() {
		return new TestDelegate();
	}

	public Collection<TestDelegate> getChildren() {
		return Arrays.asList(new TestDelegate[] { new TestDelegate(), new TestDelegate() });
	}
	
	public TestDelegate[] getArrayChildren(){
		
		return new TestDelegate[] { new TestDelegate(), new TestDelegate() };
	}
	
	public Map someProps = new HashMap(){{
		put("prop1", "prop2");
		put("delegate", new TestDelegate());
	}};

}
