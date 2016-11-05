package org.iisiplusone.eareport.eaapi_ext;

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
