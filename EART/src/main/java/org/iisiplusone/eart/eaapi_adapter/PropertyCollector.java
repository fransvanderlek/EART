package org.iisiplusone.eart.eaapi_adapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Retrieves properties and property values from an object.
 * <p>
 * For the purposes of this class, a property is retrievable by the following logic: :
 * <ul>
 * <li>any parameterless public method that has a return and starts with 'get' (case insensitive)
 * <li>any parameterless public method that has a return and starts with 'is' (case insensitive)
 * <li>any public field
 * <li>in case of <code>java.util.Map</code>, then the string representation of the key names are treated as 
 * properties, with the corresponding value as the property value.
 * </ul>
 *  
 * The name of the property will depend on how it is retrieved. 
 * <p>
 * In case of methods, the part after the
 * prefix ('get' or 'is') is decapitalized and the result is the property name.
 * <p>
 * In case of fields, the field name is decapitalized and the result is the property name.
 * <p>
 * In case of <code>Map</code>s, the string of the key is kept unmodified as property name
 * <p>
 * If from this procedure the same property name is retrieved twice, the result is that the last one found
 * is kept.
 *
 */
public class PropertyCollector {

	@SuppressWarnings("rawtypes")
	public Map<String, Object> collectProperties(final Object input) {
		Map<String, Object> props = new HashMap<>();

		if (input != null) {

			if (input instanceof Map) {
				for (Object entry : ((Map) input).entrySet()) {
					Entry entr = (Entry) entry;
					props.put(String.valueOf(entr.getKey()), entr.getValue());
				}

			} else {
				Class<? extends Object> clasz = input.getClass();
				Method[] methods = clasz.getMethods();
				Field[] fields = clasz.getFields();

				try {
					for (Method method : methods) {
						if (isGetter(method)) {
							props.put(getterNameAsProperty(method), method.invoke(input, new Object[] {}));
						}
					}

					for (Field field : fields) {
						if (Modifier.isPublic(field.getModifiers())) {
							props.put(fieldNameAsProperty(field), field.get(input));
						}
					}

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}			
		}

		return props;
	}

	private String decapitalize(String input) {
		return new String(new char[] { input.charAt(0) }).toLowerCase() + input.substring(1);
	}

	private String fieldNameAsProperty(Field field) {
		return this.decapitalize(field.getName());
	}

	private boolean isGetter(Method method) {
		return Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0
				&& ((method.getName().toLowerCase().startsWith("get") && method.getName().length() > 3)
						|| (method.getName().toLowerCase().startsWith("is") && method.getName().length() > 2));
	}

	private String getterNameAsProperty(Method input) {
		String nameLower = input.getName().toLowerCase();

		if (nameLower.startsWith("get")) {
			return this.decapitalize(input.getName().substring(3));

		} else if (nameLower.startsWith("is")) {
			return this.decapitalize(input.getName().substring(2));

		} else {
			return input.getName();
		}

	}

}
