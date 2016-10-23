package org.cocopapaya.eadoc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocopapaya.eaapi_mapping.LazyMapAdapter;
import org.cocopapaya.eajod.EARepositoryImageSource;
import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;
import org.sparx.Repository;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;

public class EaDocumentGenerator {

	private Repository repository;
	private DocumentTemplate template;
	private String initialPackage;
	private String outputFileName;
	private Map<String,Object> injectedContext = new HashMap<>();
	
	
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}


	public void setInitialPackage(String initialPackage) {
		this.initialPackage = initialPackage;
	}


	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public void setInjectedContext(Map<String, Object> injectedContext) {
		this.injectedContext = injectedContext;
	}

	public void createDocument() throws IOException, DocumentTemplateException {

		Package target = getByPath(repository.GetModels(), this.initialPackage);
		Map <String, Object> context = new HashMap<>();
		context.put("rootPackage", new LazyMapAdapter(target));
		
		//TODO: add the repository itself to this context, to be available from the templates
		for(Diagram diagram : yieldDiagrams(target)){
			System.out.println("Registering diagram '"+diagram.GetName()+"' in template context.");
			context.put(diagram.GetName(), new EARepositoryImageSource(repository, diagram.GetDiagramGUID()));
		}
		context.putAll(injectedContext);
		
		System.out.println("Creating document "+this.outputFileName);
		this.template.createDocument(context, new FileOutputStream(outputFileName));
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

	public void setDocumentTemplate(DocumentTemplate template) {
		this.template = template;
	}

}
