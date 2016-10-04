package org.cocopapaya.eainterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocopapaya.eajod.EARepositoryImageSource;
import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;
import org.sparx.Repository;

import net.cmp4oaw.ea_com.diagram.EA_Diagram;
import net.cmp4oaw.ea_com.exception.EA_PathNotFoundException;
import net.cmp4oaw.ea_com.repository.EA_Package;
import net.cmp4oaw.ea_com.repository.EA_Repository;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;

public class EA_DocumentGenerator {

	private EA_Repository repository;
	private DocumentTemplate template;
	private String initialPackage;
	private String outputFileName;
	private Map<String,Object> injectedContext = new HashMap<>();
	
	
	
	public void setRepository(EA_Repository repository) {
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

	public void createDocument() throws IOException, DocumentTemplateException, EA_PathNotFoundException {

		EA_Package target = repository.findPackageByPath(this.initialPackage);
		Map <String, Object> context = new HashMap<>();
		context.put("rootPackage", target);
		
		//TODO: add the repository itself to this context, to be available from the templates
		for(EA_Diagram diagram : yieldDiagrams(target)){
			System.out.println("Registering diagram '"+diagram.getName()+"' in template context.");
			//context.put(diagram.getName(), new EARepositoryImageSource(repository, diagram.getDiagramGUID()));
		}
		context.putAll(injectedContext);
		
		System.out.println("Creating document "+this.outputFileName);
		this.template.createDocument(context, new FileOutputStream(outputFileName));
	}
	
	private List<EA_Diagram> yieldDiagrams(EA_Package input){
		List<EA_Diagram> diagrams = new ArrayList<>();		
		
		for(EA_Diagram d: input.Diagrams){
				diagrams.add(d);
						
			for( EA_Package child : input.Packages){
				diagrams.addAll(yieldDiagrams(child));
			}
						
		}		
		return diagrams; 
	}
	
	public void setDocumentTemplate(DocumentTemplate template) {
		this.template = template;
	}

}
