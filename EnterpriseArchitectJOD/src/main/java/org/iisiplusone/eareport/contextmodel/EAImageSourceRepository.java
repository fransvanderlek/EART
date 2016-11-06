package org.iisiplusone.eareport.contextmodel;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.sparx.Diagram;
import org.sparx.Repository;

public class EAImageSourceRepository {
	
	private Repository repository;
	
	private Map<String,EARepositoryImageSource> sources = new HashMap<>();
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}	
	
	public EARepositoryImageSource getImageSourceForDiagram(Diagram diagram){
		
		String diagramUID = diagram.GetDiagramGUID();
		
		EARepositoryImageSource imageSource = sources.get(diagramUID);
		
		if(imageSource == null){
			imageSource = new EARepositoryImageSource(repository, diagramUID);
			sources.put(diagramUID, imageSource);
		}	
		
		return imageSource;
		
	}


}
