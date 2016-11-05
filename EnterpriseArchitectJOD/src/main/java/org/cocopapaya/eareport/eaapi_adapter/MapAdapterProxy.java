package org.cocopapaya.eareport.eaapi_adapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;

public class MapAdapterProxy implements InvocationHandler {


	private Object innerObject;
	
	private final Map<String, Object> getValues = new HashMap<>();

	private boolean loaded = false;

	private PropertyCollector collector = new PropertyCollector();

	private static final List<String> supportedMethods = Arrays
			.asList(new String[] { "size", "get", "isEmpty", "entrySet", "keySet", "containsKey" });

	private MapAdapterProxy(Object innerObject){
		this.innerObject = innerObject;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> instance(Object wrap) {
		return (Map<String,Object>) Proxy.newProxyInstance(MapAdapterProxy.class.getClassLoader(),
				new Class[] { Map.class }, new MapAdapterProxy(wrap));
	}
	
	@Override
	public String toString() {
		return String.valueOf(innerObject);
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (supportedMethods.contains(method.getName())){
			this.ensureLoaded();
			return method.invoke(this.getValues, args);
			
		} else {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private void ensureLoaded() {
		if(!loaded){
			Map<String,Object> memberProperties = this.collector.collectProperties(this.innerObject);			
			for( String key : memberProperties.keySet()){
				this.getValues.put(key, this.wrap(memberProperties.get(key) ) );
			}
			this.loaded = true;
		}		
	}
	
	@SuppressWarnings("serial")
	private Object wrap(Object input) {
		
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
				return new MapAdapterProxy(input);

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
