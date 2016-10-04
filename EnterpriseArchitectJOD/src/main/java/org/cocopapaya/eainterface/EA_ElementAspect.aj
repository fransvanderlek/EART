package org.cocopapaya.eainterface;

import net.cmp4oaw.ea_com.element.EA_Element;
import net.cmp4oaw.ea_com.exception.EA_ObjectNotFoundException;

public aspect EA_ElementAspect {
	
	public boolean EA_Element.getIsClassifier(){
		return this.getClassifierID() == 0;
		
	}
	
	public EA_Element EA_Element.getClassifier(){
		
		EA_Element element = null;
		
		if( this.getClassifierID() !=0){
			
			try {
				element = new EA_Element();
				
				element.setObj(
				EA_RepositoryFactory.getInstance().getRepository().getElementById(this.getClassifierID()));
				
			} catch (EA_ObjectNotFoundException e) {
				// will cause function to return null
			}
		}
		
		return element;
		
	}

}
