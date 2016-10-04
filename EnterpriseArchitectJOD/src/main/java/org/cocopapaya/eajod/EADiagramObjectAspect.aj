package org.cocopapaya.eajod;

import org.sparx.Element;
import org.sparx.Repository;
import org.sparx.DiagramObject;

public aspect EADiagramObjectAspect {
	
	public Element DiagramObject.getElement(){
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		
		return repository.GetElementByID(this.GetElementID());
	}
	

}
