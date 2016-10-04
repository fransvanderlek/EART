package org.cocopapaya.eainterface;

import org.sparx.DiagramLink;

import net.cmp4oaw.ea_com.connector.EA_Connector;
import net.cmp4oaw.ea_com.exception.EA_ObjectNotFoundException;
import net.cmp4oaw.ea_com.factory.EA_ObjectFactory;

public class EA_DiagramLink {

	private DiagramLink diagramLink;
	private EA_Connector connector;

	public EA_DiagramLink(DiagramLink link) {
		this.diagramLink = link;
	}

	public EA_Connector getConnector() {
		if (this.connector == null) {
			this.connector = loadConnector();
		}

		return this.connector;
	}

	private EA_Connector loadConnector() {
		try {
			return (EA_Connector) EA_ObjectFactory.getObjectById(EA_Connector.class,
					this.diagramLink.GetConnectorID());

		} catch (EA_ObjectNotFoundException e) {
			return null;
		}
	}
	
	public boolean isHidden(){
		return this.diagramLink.GetIsHidden();
	}

}
