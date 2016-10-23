package org.cocopapaya.eaapi_mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LazyMapAdapter extends NullMap<String, Object> {

	private final Map<String, Object> getValues = new HashMap<>();

	private Object innerObject;
	
	private boolean loaded = false;
	
	private ObjectWrapper wrapper = new ObjectWrapper();
	
	private PropertyCollector collector = new PropertyCollector();

	public LazyMapAdapter(Object innerObject) {
		this.innerObject = innerObject;
	}
	
	@Override
	public String toString() {
		return String.valueOf(innerObject);
	}

	
	@Override
	public Object get(Object key) {
		ensureLoaded();
		return this.getValues.get(key);
	}

	@Override
	public int size() {
		ensureLoaded();
		return this.getValues.size();
	}

	@Override
	public boolean isEmpty() {
		ensureLoaded();
		return this.getValues.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		ensureLoaded();
		return this.getValues.keySet();
	}

	@Override
	public boolean containsKey(Object key) {
		ensureLoaded();
		return this.getValues.containsKey(key);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		ensureLoaded();
		return this.getValues.entrySet();
	}


	private void ensureLoaded() {
		if(!loaded){
			Map<String,Object> memberProperties = this.collector.collectProperties(this.innerObject);			
			for( String key : memberProperties.keySet()){
				this.getValues.put(key, this.wrapper.wrap(memberProperties.get(key) ) );
			}
			this.loaded = true;
		}		
	}

}