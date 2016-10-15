package org.cocopapaya.eaapi_mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReflectionMapAdapter extends NullMap<String, Object> {

	private final List<ListConverter> listConverters = new ArrayList<>();

	private final Map<String, Operation> getValues = new HashMap<>();

	private final String asString;
	
	public ReflectionMapAdapter(Object innerObject) {
		this.getValues.putAll(this.collectProperties(innerObject));
		this.listConverters.addAll(defaultConverters());
		this.asString = innerObject.toString();

	}

	public void addListConverter(ListConverter converter) {
		this.listConverters.add(converter);
	}

	@Override
	public String toString() {
		return this.asString;
	}

	private Map<String, Operation> collectProperties(final Object obj) {

		Map<String, Operation> props = new HashMap<>();

		if (obj instanceof Map) {
			Map objectMap = (Map) obj;
			
			for( Object key : objectMap.keySet()){
				props.put(key.toString(), new Operation() {
					
					@Override
					public Object execute(Object... params) throws Exception {
						return convertObject( objectMap.get(key));
					}
				});
			}			
			return props;						
		}

		Class<? extends Object> clasz = obj.getClass();
		Method[] methods = clasz.getMethods();
		Field[] fields = clasz.getFields();

		for (Method method : methods) {

			if (isGetter(method)) {
				props.put(getterNameAsProperty(method), new Operation() {

					@Override
					public Object execute(Object... objects ) throws Exception {
						return convertObject(method.invoke(obj, new Object[] {}));
					}
				});
			}
		}

		for (Field field : fields) {

			if ( Modifier.isPublic(field.getModifiers())) {
				props.put(fieldNameAsProperty(field), new Operation() {

					@Override
					public Object execute(Object... objects ) throws Exception {
						return convertObject(field.get(obj));
					}
				});
			}
		}

		return props;
	}

	private List<ListConverter> defaultConverters() {
		List<ListConverter> convs = new ArrayList<>();

		convs.add(new ListConverter() {

			@Override
			public List<Object> asList(Object input) {
				return Arrays.asList((Object[]) input);
			}

			@Override
			public boolean canConvert(Object input) {
				return input instanceof Object[];
			}
		});

		convs.add(new ListConverter() {

			@Override
			public List<Object> asList(Object input) {
				Iterable iterable = (Iterable) input;
	
				return new ArrayList<Object>(){{					
					for( Object item : iterable){
						add(item);
					}					
				}};
			}

			@Override
			public boolean canConvert(Object input) {
				return input instanceof Iterable;
			}
		});

		return convs;
	}

	private Object convertObject(Object object) {
		for (ListConverter converter : this.listConverters) {

			if (converter.canConvert(object)) {
				List<Object> listOfMappers = new ArrayList<>();
				for (Object entry : converter.asList(object)) {

					// limitation: list of lists will not work.
					listOfMappers.add(new ReflectionMapAdapter(entry));
				}

				return listOfMappers;
			}
		}
		
		if (object instanceof String){
			return object.toString();
		}
		
		return new ReflectionMapAdapter(object);
	}

	private String decapitalize(String input) {
		return new String(new char[] { input.charAt(0) }).toLowerCase() + input.substring(1);
	}

	private String fieldNameAsProperty(Field field) {
		return this.decapitalize(field.getName());
	}

	private boolean isGetter(Method method) {
		return Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0
				&& method.getName().toLowerCase().startsWith("get") && method.getName().length() > 3;
	}

	private String getterNameAsProperty(Method input) {
		return this.decapitalize(input.getName().substring(3, input.getName().length()));
	}

	@Override
	public Object get(Object key) {
		try {
			//for future Map support, we probably want to convert here actually.
			return ((Operation) this.getValues.get(key)).execute();

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	public int size() {
		return this.getValues.size();
	}

	@Override
	public boolean isEmpty() {
		return this.getValues.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.getValues.keySet();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.getValues.containsKey(key);
	}
	
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		try {
			return new HashMap<String,Object>(){{
				for( String key : getValues.keySet()){
					this.put(key, getValues.get(key).execute());
				}
				
			}}.entrySet();
		} catch (Exception e) {
			throw new RuntimeException();
		}				
	}
	

}