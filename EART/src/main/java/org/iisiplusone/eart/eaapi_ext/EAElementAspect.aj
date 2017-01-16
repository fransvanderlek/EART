package org.iisiplusone.eart.eaapi_ext;

import org.iisiplusone.eart.contextmodel.EARepositoryFactory;
import org.sparx.Element;
import org.sparx.Repository;

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
	
}
