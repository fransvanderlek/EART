package org.iisiplusone.eareport.eaapi_adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectWrapper {

	private static final Logger logger = LoggerFactory.getLogger(ObjectWrapper.class);

	
	@SuppressWarnings("serial")
	public Object wrap(Object input) {
		
		if( input !=null ){
			Class<?> clasz = input.getClass();

			if (ClassUtils.isPrimitiveOrWrapper(clasz) || input instanceof String || clasz.isEnum()) {
				
				logger.debug("Skipping wrapping for type "+clasz);
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
				logger.debug("Wrapping type "+clasz);
				return MapAdapterProxy.instance(input);

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
