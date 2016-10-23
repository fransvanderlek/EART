package org.cocopapaya.eareport.eaapi_adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

public class ObjectWrapper {

	@SuppressWarnings("serial")
	public Object wrap(Object input) {
		
		if( input !=null ){
			Class<?> clasz = input.getClass();

			if (ClassUtils.isPrimitiveOrWrapper(clasz) || input instanceof String || clasz.isEnum()) {
				return input;

			} else if (input instanceof Object[]) {
				return this.wrapEach( Arrays.asList((Object[]) input) );

			} else if (input instanceof Iterable) {
				return this.wrapEach( new ArrayList<Object>() {
					{
						for (Object item : (Iterable<?>) input) {
							add(item);
						}
					}
				} );

			} else {
				return new LazyMapAdapter(input);

			}
		} else {
			return null;
		}


	}
	
	private List<Object> wrapEach( List<Object> elements){
		List<Object> wrappedList = new ArrayList<>();
		
		for(Object element : elements){
			wrappedList.add(this.wrap(element));
		}
		
		return wrappedList;
		
		
	}
}
