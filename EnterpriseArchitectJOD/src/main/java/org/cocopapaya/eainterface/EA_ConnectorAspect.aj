package org.cocopapaya.eainterface;

import net.cmp4oaw.ea_com.connector.EA_ConnectorEnd;
import net.cmp4oaw.ea_com.connector.EA_Connector;

public aspect EA_ConnectorAspect {
	
	public EA_ConnectorEnd EA_Connector.getClientEnd(){
		return this.ClientEnd();
	}
	
	public EA_ConnectorEnd EA_Connector.getSupplierEnd(){
		return this.SupplierEnd();
	}

}
