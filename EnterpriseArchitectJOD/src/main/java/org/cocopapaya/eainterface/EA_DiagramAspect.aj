package org.cocopapaya.eainterface;

import java.util.ArrayList;
import java.util.List;

import net.cmp4oaw.ea_com.diagram.EA_Diagram;

import org.sparx.Diagram;
import org.sparx.DiagramLink;

public aspect EA_DiagramAspect {
	
	public List<EA_DiagramLink> EA_Diagram.getDiagramLinks(){
		
		Diagram eaDiagram = EA_RepositoryFactory.getInstance().getRepository().getDiagramById(this.getDiagramID());
		
		List<EA_DiagramLink> links = new ArrayList<>();
		
		for( DiagramLink link : eaDiagram.GetDiagramLinks()){
			links.add(new EA_DiagramLink(link));
		}
		
		return links;
	}

}
