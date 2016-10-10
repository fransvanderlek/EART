package org.cocopapaya.eaapi_mapping;

import java.util.List;

public interface ListConverter {

	public List<Object> asList(Object input);

	public boolean canConvert(Object input);
}