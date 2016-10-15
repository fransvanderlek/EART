package org.cocopapaya.eajod;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestClass {
	
	public String MyName = "test";

	public String getTitle() {
		return "hello";
	}

	public String GetSomething() {
		return "some-thing";
	}

	public String nonGetter() {
		return "not-a-getter";
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
