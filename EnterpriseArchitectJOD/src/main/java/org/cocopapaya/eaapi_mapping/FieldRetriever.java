package org.cocopapaya.eaapi_mapping;

import java.lang.reflect.Field;

public class FieldRetriever implements Operation {

	final private Object target;
	final private Field field;
	
	public FieldRetriever(Object target2, Field field2) {
		this.target = target2;
		this.field = field2;
	}

	@Override
	public Object execute(Object... params) throws Exception {
		return this.field.get(target);
	}
	
	public String getName(){
		return this.field.getName();
	}
	
	public static boolean isRetrievable(Field field){
		return field.isAccessible();
	}

}
