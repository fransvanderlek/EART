package org.cocopapaya.uml.model;

public class UmlConnector extends UmlElement {

	private UmlElement client;
	private UmlElement supplier;
	
	public void setClient(UmlElement client) {
		this.client = client;
	}
	public void setSupplier(UmlElement supplier) {
		this.supplier = supplier;
	}
	public UmlElement getClient() {
		return client;
	}
	public UmlElement getSupplier() {
		return supplier;
	}
	
	
}
