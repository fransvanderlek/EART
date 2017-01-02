package org.iisiplusone.eareport.eaapi_adapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is wrapped around a target object and provides read-only access to
 * properties of that object via the object's exposed getter methods. This class
 * is only meant to be used as <code>java.util.Map</code> implementation, hence
 * it implements part of the <code>Map</code> interface (see
 * {@link MapAdapterProxy#supportedMethods}). In relation to that, the
 * constructor is made private and a factory method is provided which only
 * returns a <code>Map</code> ({@link MapAdapterProxy#instance(Object)}).
 * <p>
 * Methods that write to a <code>Map</code> like <code>put</code> are not
 * supported.
 * <p>
 * Actual wrapping is done via {@link ObjectMapper}, while actual decisions on
 * which getter methods's values to map is done in {@link PropertyCollector}.
 * <p>
 * Mapped values and references are kept in this object's memory afterwards and
 * subsequent lookups will always be from memory. Any updates in the underlying
 * proxied object are not reflected.
 */
public class MapAdapterProxy implements InvocationHandler {

	private static final Logger logger = LoggerFactory.getLogger(MapAdapterProxy.class);
	private Object innerObject;
	private final Map<String, Object> properties = new HashMap<>();
	private boolean loaded = false;
	private PropertyCollector collector = new PropertyCollector();
	private ObjectWrapper wrapper = new ObjectWrapper();
	private static final List<String> supportedMethods = Arrays
			.asList(new String[] { "size", "get", "isEmpty", "entrySet", "keySet", "containsKey" });

	private MapAdapterProxy(Object target) {
		this.innerObject = target;
	}

	/**
	 * Factory method. This class should only be used as a
	 * <code>java.util.Map</code> implementor.
	 * 
	 * @param target
	 *            : the target object to proxy
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> instance(Object target) {
		logger.debug("Creating proxy for " + target.getClass() + " : " + target);
		return (Map<String, Object>) Proxy.newProxyInstance(MapAdapterProxy.class.getClassLoader(),
				new Class[] { Map.class }, new MapAdapterProxy(target));
	}

	@Override
	public String toString() {
		return String.valueOf(innerObject);
	}

	/**
	 * Only <code>java.util.Map</code> methods are actually supported. Matching
	 * happens via method names.
	 * <p>
	 * If a supported method is invoked, this class ensures the property values
	 * from the proxied object are loaded into
	 * {@link MapAdapterProxy#properties } <code>Map</code> before dispatching
	 * the method invocation. The method will be invoked not on the proxied
	 * object but rather on {@link MapAdapterProxy#properties }
	 * <p>
	 * 
	 * @throws UnsupportedOperationException
	 *             if a method is invoked which is not on the supported list.
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 *      java.lang.reflect.Method, java.lang.Object[])
	 * 
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		String methodName = method.getName();
		logger.debug("Intercepting method " + methodName + " ( " + args + " ) ");

		if (supportedMethods.contains(methodName)) {
			this.ensureLoaded();
			return method.invoke(this.properties, args);

		} else {
			throw new UnsupportedOperationException(
					methodName + " is not supported, only [" + String.join(",", supportedMethods) + "] are supported.");
		}

	}

	/**
	 * If this method is invoked for the first time on this instance, it will
	 * invoke <code>PropertyCollector</code> to collect any properties and their
	 * values from the proxied object. The values will then be suitably wrapped
	 * by <code>ObjectWrapper</code>.
	 * <p>
	 * The result is kept in {@link MapAdapterProxy#properties }. The value of
	 * {@link MapAdapterProxy#loaded } is set to true. If this method is called
	 * again, nothing will happen.
	 */
	private void ensureLoaded() {
		if (!loaded) {
			logger.debug("Loading data for " + this.innerObject);
			Map<String, Object> memberProperties = this.collector.collectProperties(this.innerObject);
			for (String key : memberProperties.keySet()) {
				logger.debug("Wrapping value for key " + key);
				this.properties.put(key, this.wrapper.wrap(memberProperties.get(key)));
			}
			this.loaded = true;
			logger.debug("Done loading data for " + this.innerObject);
		}
	}

}
