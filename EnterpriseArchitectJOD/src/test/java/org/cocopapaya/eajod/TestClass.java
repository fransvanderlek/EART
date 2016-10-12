package org.cocopapaya.eajod;

import java.util.Arrays;
import java.util.Collection;

public class TestClass {

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

}
