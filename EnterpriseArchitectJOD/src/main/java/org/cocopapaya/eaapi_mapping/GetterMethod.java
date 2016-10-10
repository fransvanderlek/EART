package org.cocopapaya.eaapi_mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetterMethod {
	
	private final Method inner; 
	
	private GetterMethod(Method inner){
		this.inner = inner;
	}

	public static GetterMethod instance(Method input){
		if ( input.isAccessible() && input.getParameterCount()==0 && input.getName().toLowerCase().startsWith("get") ){
			return new GetterMethod(input);
		}
		else{
			return null;
		}				
	}
	
	public String nameAsProperty(){
		
		String nameMinusGet = inner.getName().substring(3, inner.getName().length());
		
		
		return new String(new char[]{ nameMinusGet.charAt(0)}).toLowerCase()+ nameMinusGet.substring(1) ;
	}
	
	public Object invoke(Object target) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return this.inner.invoke(target, new Object[]{});
		
	}
}
