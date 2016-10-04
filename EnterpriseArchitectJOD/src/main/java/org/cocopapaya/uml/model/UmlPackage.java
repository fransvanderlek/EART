package org.cocopapaya.uml.model;

import java.util.List;

public class UmlPackage extends UmlElement {
	
	private List<UmlPackage> children;
	private List<UmlDiagram> diagrams;
	
	public List<UmlPackage> getChildren(){
		return this.children;
	}
	
	public void setChildren(List<UmlPackage> children) {
		this.children = children;
	}

	public void setDiagrams(List<UmlDiagram> diagrams) {
		this.diagrams = diagrams;
	}

	public List<UmlDiagram> getDiagrams(){
		return this.diagrams;
	}
}
