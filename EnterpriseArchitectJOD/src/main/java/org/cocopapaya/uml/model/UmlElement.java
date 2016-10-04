package org.cocopapaya.uml.model;

public class UmlElement {
	
	private String name;
	private UmlElement classifier;
	
	public void setName(String name) {
		this.name = name;
	}

	public void setClassifier(UmlElement classifier) {
		this.classifier = classifier;
	}

	public String getName(){
		return this.name;
	}
	
	public UmlElement getClassifier(){
		return this.classifier;
	}
	
	public boolean isClassifier(){
		return this.classifier == null;
	}

}
