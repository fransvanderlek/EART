package org.iisiplusone.eart.eaapi_ext;

import org.iisiplusone.eart.contextmodel.EARepositoryFactory;
import org.sparx.Connector;
import org.sparx.DiagramLink;
import org.sparx.Repository;

public aspect EADiagramLinkAspect {
	
	public boolean DiagramLink.isHidden(){
		return this.GetIsHidden();
	}

	
	public Connector DiagramLink.getConnector(){
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		
		return repository.GetConnectorByID(this.GetConnectorID());
		
	}
}
