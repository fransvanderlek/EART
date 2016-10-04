package org.cocopapaya.uml.model;

public class UmlDiagramLink extends UmlElement {
	
	private boolean hidden;
	private UmlConnector connector;	

	public boolean isHidden(){
		return this.hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setConnector(UmlConnector connector) {
		this.connector = connector;
	}

	public UmlConnector getConnector() {
		return connector;
	}	
	
}
