package org.cocopapaya.eareport.contextmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocopapaya.eareport.eaapi_adapter.MapAdapterProxy;
import org.cocopapaya.eareport.eaapi_adapter.ObjectWrapper;
import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;
import org.sparx.Repository;

public class EARepositoryContext implements IContextRepository {
	
	private String initialPackage;
	private Repository repository;
	
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
			System.out.println("Registering diagram '"+diagram.GetName()+"' in template context.");
			context.put(diagram.GetName(), new EARepositoryImageSource(repository, diagram.GetDiagramGUID()));
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
		
		System.out.println("Opening package : "+path);
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
		
		System.out.println("Failed to find '"+part+"', could only resolve up till "+foundPath);

		return null;
	}

}
