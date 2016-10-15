package org.cocopapaya.eaapi_mapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Getter implements Operation {

	final private Object target;
	final private Method method;

	public Getter(Object target, Method method) {
		super();
		this.target = target;
		this.method = method;
	}

	@Override
	public Object execute(Object... params) throws Exception {

		return this.method.invoke(target, new Object[] {});
	}

	public String getName() {
		return this.method.getName();
	}

	public static boolean isGetter(Method method) {
		return Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0
				&& method.getName().toLowerCase().startsWith("get") && method.getName().length() > 3;
	}
}
