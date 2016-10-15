package org.cocopapaya.eaapi_mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectReflector {

	final private Object target;

	public ObjectReflector(Object target) {
		super();
		this.target = target;
	}

	public List<Getter> getters() {
		Class<? extends Object> clasz = target.getClass();
		List<Getter> getters = new ArrayList<>();

		for (Method method : clasz.getMethods()) {
			if (Getter.isGetter(method)) {
				getters.add(new Getter(this.target, method));
			}
		}

		return getters;
	}

	public List<FieldRetriever> fields() {
		Class<? extends Object> clasz = target.getClass();
		List<FieldRetriever> fields = new ArrayList<>();

		for (Field field : clasz.getFields()) {
			if (FieldRetriever.isRetrievable(field)) {
				fields.add(new FieldRetriever(this.target, field));
			}
		}

		return fields;
	}


}
