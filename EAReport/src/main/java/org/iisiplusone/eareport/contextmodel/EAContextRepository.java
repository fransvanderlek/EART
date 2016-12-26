package org.iisiplusone.eareport.contextmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iisiplusone.eareport.eaapi_adapter.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;
import org.sparx.Repository;

public class EAContextRepository implements IContextRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(EAContextRepository.class);
	
	private String initialPackage;
	private Repository repository;
	private EAImageSourceRepository imageSourceRepository;
	
	public void setImageSourceRepository(EAImageSourceRepository imageSourceRepository) {
		this.imageSourceRepository = imageSourceRepository;
	}

	public void setInitialPackage(String initialPackage) {
		this.initialPackage = initialPackage;
	}
	
	public void setRepository(Repository repository){
		this.repository = repository;
	}
	
	@Override
	public Map<String, Object> getContext() {
		
		Map <String, Object> context = new HashMap<>();
		
		Package target = getByPath(repository.GetModels(), this.initialPackage);
		context.put("rootPackage", (new ObjectWrapper()).wrap(target));
		
		//TODO: add the repository itself to this context, to be available from the templates
		for(Diagram diagram : yieldDiagrams(target)){
			logger.info("Registering diagram '"+diagram.GetName()+"' in template context.");
			context.put(diagram.GetName(), imageSourceRepository.getImageSourceForDiagram(diagram));
		}
		
		return context;
	}
	
	private List<Diagram> yieldDiagrams(Package input){
		List<Diagram> diagrams = new ArrayList<>();		
		
		for(Diagram d: input.GetDiagrams()){
				diagrams.add(d);
						
			for( Package child : input.GetPackages()){
				diagrams.addAll(yieldDiagrams(child));
			}
						
		}		
		return diagrams; 
	}
	
	private Package getByPath(Collection<Package> packs, String path){
		
		logger.info("Opening package : "+path);
		String[] parts = path.split("/");
		String foundPath = "";
		String part="";
		Collection<Package> children = packs;

		for(int i=0; i<parts.length; i++){

			part = parts[i];
			for ( Package child : children){

				if ( child.GetName().equals(part)){

					foundPath+=part;
					
					if(i==(parts.length-1)){
						return child;
						
					} else {						
						foundPath+="/";
						children = child.GetPackages();
						break;
					}

				}
			}
		}
		
		logger.error("Failed to find '"+part+"', could only resolve up till "+foundPath);

		return null;
	}

}
