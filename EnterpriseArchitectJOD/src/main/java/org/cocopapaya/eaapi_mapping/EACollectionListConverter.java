package org.cocopapaya.eaapi_mapping;

import java.util.ArrayList;
import java.util.List;

public class EACollectionListConverter implements ListConverter {

	@Override
	public List<Object> asList(Object input) {
		
		List<Object> theList = new ArrayList<>();
		org.sparx.Collection<? extends Object> eaCollection = (org.sparx.Collection<? extends Object>) input;
		
		for(Object ob : eaCollection){
			theList.add(ob);
		}
		return theList;
	}

	@Override
	public boolean canConvert(Object input) {
		return input instanceof org.sparx.Collection;
	}

}
