package org.cocopapaya.eajod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.DiagramLink;
import org.sparx.Package;
import org.sparx.Repository;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class EaJod {

	public static void main(String[] args) throws Exception {
		
		String propFileName = "eajod.properties";

		if( args.length > 1){
			propFileName = args[1];
		}
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(propFileName));

		System.out.println("Opening repo.");
		EARepositoryFactory.registerEapFile(properties.getProperty("eap-file-location"));
		Repository repository = EARepositoryFactory.getInstance().getRepository();

		Package target = getByPath(repository.GetModels(), properties.getProperty("root-package"));
				
		System.out.println(properties.getProperty("root-package"));
		System.out.println(target.GetName());
		
		System.out.println("starting jod.");

		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		DocumentTemplate template = documentTemplateFactory.getTemplate(new File(properties.getProperty("template-file")));

		Map <String, Object> context = new HashMap<>();
		context.put("rootPackage", target);
		
		
		
		for(Diagram diagram : yieldDiagrams(target)){
			context.put(diagram.GetName(), new EARepositoryImageSource(repository, diagram.GetDiagramGUID()));
		}
		


		template.createDocument(context, new FileOutputStream(properties.getProperty("output-file")));

		System.out.println("Done.");


	}

	private static List<Diagram> yieldDiagrams(Package input){
		List<Diagram> diagrams = new ArrayList<>();		
		
		for(Diagram d: input.GetDiagrams()){
				diagrams.add(d);
						
			for( Package child : input.GetPackages()){
				diagrams.addAll(yieldDiagrams(child));
			}
						
		}		
		return diagrams; 
	}
	
	private static List<Diagram> yieldDiagrams(Collection<Package> packages){
		
		List<Diagram> diagrams = new ArrayList<>();
		
		for(Package pack : packages){
			diagrams.addAll(yieldDiagrams(pack));			
		}
		
		return diagrams;
	}
	
	private static Package getTest(){
		
		
		return new Package(null){
			
			public String getName(){
				return "test";
			}
			
			public List<Diagram> getDiagrams(){
				
				return new ArrayList<Diagram>(){{
					
					add( new Diagram(null){
						public String getName(){
							return "test diagram";
						}
						
						public List<DiagramLink> getDiagramLinks(){
							return new ArrayList<DiagramLink>(){{
								add( new DiagramLink(null){
									public boolean isHidden(){
										return false;
									}
									
								});
								
							}};
							
						}

						
					});
					
				}};

			}
		};
	}


	private static Package getByPath(Collection<Package> packs, String path){
		String[] parts = path.split("/");

		Collection<Package> children = packs;

		for(int i=0; i<parts.length; i++){

			String part = parts[i];
			//System.out.println("looking for part:"+part);


			for ( Package child : children){

				//System.out.println("child:"+child.GetName());
				if ( child.GetName().equals(part)){

					if(i==(parts.length-1)){
						return child;
					} else {
						children = child.GetPackages();
						break;
					}

				}
			}

		}

		return null;
	}

}
