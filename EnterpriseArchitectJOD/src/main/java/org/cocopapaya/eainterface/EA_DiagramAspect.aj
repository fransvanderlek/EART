package org.cocopapaya.eainterface;

import java.util.ArrayList;
import java.util.List;

import net.cmp4oaw.ea_com.diagram.EA_Diagram;
import net.cmp4oaw.ea_com.diagram.EA_DiagramObject;
import net.cmp4oaw.ea_com.common.EA_Collection;

import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.DiagramLink;
import org.sparx.DiagramObject;

public aspect EA_DiagramAspect {

	public List<EA_DiagramLink> EA_Diagram.getDiagramLinks() {

		Diagram eaDiagram = EA_RepositoryFactory.getInstance().getRepository().getDiagramById(this.getDiagramID());

		List<EA_DiagramLink> links = new ArrayList<>();

		for (DiagramLink link : eaDiagram.GetDiagramLinks()) {
			links.add(new EA_DiagramLink(link));
		}

		return links;
	}

	public List<EA_DiagramObject> EA_Diagram.getObjects() {
		
		List<EA_DiagramObject> objs = new ArrayList<EA_DiagramObject>();
		
		for( int i=0; i< this.DiagramObjects.count; i++){
			objs.add(this.DiagramObjects.getAt(i));
		}
		
		return objs;
	}

}
