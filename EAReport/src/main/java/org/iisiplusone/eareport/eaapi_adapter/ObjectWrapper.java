package org.iisiplusone.eareport.eaapi_adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decides whether, and how to wrap an object.
 * <p>
 * The wrapping is based on type of the provied object and follows:
 * <ul>
 * <li> primitive types (such as <code>int</code>, <code>float</code>) an their wrapper types (<code>Integer</code>,
 *  <code>Float</code>) etc are not wrapped
 * <li> <code>String</code>s are not wrapped
 * <li> <code>enum</code>s are not wrapped
 * <li> instances of <code>Array</code> or <code>Iterable</code> are converted to <code>List</code> and their elements
 * are wrapped if applicable
 * <li> any other class is wrapped by <code>MapAdapterProxy</code>
 * </ul>
 *
 */
public class ObjectWrapper {

	private static final Logger logger = LoggerFactory.getLogger(ObjectWrapper.class);

	
	/**
	 * Wraps an object if applicable.
	 * 
	 * @param input : the object to wrap based on the object's class
	 * @return the wrapping object or the input object if no wrapping is to be done
	 */
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
