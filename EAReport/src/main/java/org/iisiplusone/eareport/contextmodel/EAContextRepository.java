package org.iisiplusone.eareport.contextmodel;

import java.util.ArrayList;
import java.util.List;

import org.iisiplusone.eareport.eaapi_adapter.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;
import org.sparx.Repository;

public class EAContextRepository implements IModelRepository {

	private static final Logger logger = LoggerFactory.getLogger(EAContextRepository.class);

	private String initialPackage;
	private Repository repository;

	public void setInitialPackage(String initialPackage) {
		this.initialPackage = initialPackage;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@Override
	public EAProject getEAProject() {
		
		EAProject eaModel = new EAProject();		
		Package target = getByPath(repository.GetModels(), this.initialPackage);		
		eaModel.setRootContext((new ObjectWrapper()).wrap(target));
		
		for(Diagram diagram : yieldDiagrams(target)){
			logger.info("Registering diagram '"+diagram.GetName()+"' in template context.");
			eaModel.addDiagramImage( new EADiagramImage(this.repository, diagram.GetDiagramGUID(), diagram.GetName()));
		}
		
		return eaModel;
	}

	private List<Diagram> yieldDiagrams(Package input) {
		List<Diagram> diagrams = new ArrayList<>();

		for (Diagram d : input.GetDiagrams()) {
			diagrams.add(d);

			for (Package child : input.GetPackages()) {
				diagrams.addAll(yieldDiagrams(child));
			}

		}
		return diagrams;
	}

	private Package getByPath(Collection<Package> packs, String path) {

		logger.info("Opening package : " + path);
		String[] parts = path.split("/");
		String foundPath = "";
		String part = "";
		Collection<Package> children = packs;

		for (int i = 0; i < parts.length; i++) {

			part = parts[i];
			for (Package child : children) {

				if (child.GetName().equals(part)) {

					foundPath += part;

					if (i == (parts.length - 1)) {
						return child;

					} else {
						foundPath += "/";
						children = child.GetPackages();
						break;
					}

				}
			}
		}

		logger.error("Failed to find '" + part + "', could only resolve up till " + foundPath);

		return null;
	}

}
