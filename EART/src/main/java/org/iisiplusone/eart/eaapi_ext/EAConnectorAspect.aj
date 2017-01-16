package org.iisiplusone.eart.eaapi_ext;

import org.iisiplusone.eart.contextmodel.EARepositoryFactory;
import org.sparx.Connector;
import org.sparx.Element;
import org.sparx.Repository;


public aspect EAConnectorAspect {
	
	public Element Connector.getClient(){
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		
		return repository.GetElementByID(this.GetClientID());
		
	}
	
	public Element Connector.getSupplier(){
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		
		return repository.GetElementByID(this.GetSupplierID());
		
	}
}
