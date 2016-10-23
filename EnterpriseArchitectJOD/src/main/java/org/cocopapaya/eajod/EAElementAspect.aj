package org.cocopapaya.eajod;

import java.util.ArrayList;
import java.util.Collection;

import org.sparx.Element;
import org.sparx.Repository;
import org.sparx.TaggedValue;

public aspect EAElementAspect {

	public Element Element.getClassifier(){
		if( this.GetClassifierID() ==0 ){
			return this;
		}
		
		Repository repository = EARepositoryFactory.getInstance().getRepository();

		return repository.GetElementByID(this.GetClassifierID());
	}

	public boolean Element.getIsClassifier(){
		return this.GetClassifierID() ==0;
	}
	
	public String Element.getVersion(){
		return this.GetVersion();
	}

	
	public String Element.getName(){
		return this.GetName();
	}
	
	public String Element.getType(){
		return this.GetType();
	}
	
	public Collection<TaggedValue> Element.getTaggedValues(){
		
		Collection<TaggedValue> tags = new ArrayList<>();
		
		for( TaggedValue tg : this.GetTaggedValues() ){
			tags.add(tg);
		}
		
		return tags;
		
	}
	
	public String Element.getNotes(){
		return this.GetNotes();
	}
	
}
