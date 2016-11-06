package org.iisiplusone.eareport.eaapi_ext;

import org.sparx.Element;
import org.sparx.Repository;
import org.iisiplusone.eareport.contextmodel.EARepositoryFactory;
import org.sparx.DiagramObject;

public aspect EADiagramObjectAspect {
	
	public Element DiagramObject.getElement(){
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		
		return repository.GetElementByID(this.GetElementID());
	}
	

}
