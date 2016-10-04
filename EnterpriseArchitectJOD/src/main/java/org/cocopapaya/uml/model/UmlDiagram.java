package org.cocopapaya.uml.model;

import java.util.List;

public class UmlDiagram extends UmlPackage {

	private List<UmlDiagramLink> diagramLinks;

	public List<UmlDiagramLink> getDiagramLinks() {
		return diagramLinks;
	}

	public void setDiagramLinks(List<UmlDiagramLink> diagramLinks) {
		this.diagramLinks = diagramLinks;
	}
	

}
