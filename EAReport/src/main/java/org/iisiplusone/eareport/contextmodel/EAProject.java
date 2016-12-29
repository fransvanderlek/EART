package org.iisiplusone.eareport.contextmodel;

import java.util.ArrayList;
import java.util.List;

public class EAProject {
	
	private List<EADiagramImage> diagramImages = new ArrayList<>();
	private Object rootContext;

	public void addDiagramImage(EADiagramImage eaDiagramImage) {
		this.diagramImages.add(eaDiagramImage);		
	}

	public void setRootContext(Object object) {
		this.rootContext = object;		
	}

	public List<EADiagramImage> getDiagraImages() {
		return this.diagramImages;
	}

	public Object getRootContext() {
		return this.rootContext;
	}

}
