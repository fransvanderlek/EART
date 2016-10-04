package org.cocopapaya.eajod;

import org.sparx.TaggedValue;

public aspect EATaggedValueAspect {
	
	public String TaggedValue.getName(){
		return this.GetName();
	}
	
	public String TaggedValue.getValue(){
		return this.GetValue();
	}

}
