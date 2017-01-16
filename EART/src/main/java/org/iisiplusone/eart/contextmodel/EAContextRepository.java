package org.iisiplusone.eart.contextmodel;

import java.util.ArrayList;
import java.util.List;

import org.iisiplusone.eart.eaapi_adapter.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;
import org.sparx.Repository;


/**
 * Retrieves the EA project data via Enterprise Architect's repository class. 
 * <p>
 * Data returned includes (see also @link {@link EAProject}): 
 * <ul>
 * <li> EA model objects wrapped in <code>java.util.Map</code>
 * <li> images in the form of image provider classes
 * </ul>
 * 
 * <p>
 * An <code>initialPackage</code> string should be provided.
 * This string takes the form <code>part-1/part-2/part-3</code>.
 * This forms a path which will be used to search EA Model's packages. 
 * The package found will be returned by this class and be used as the basis for searching for diagrams:
 * only diagrams within that package will be available in the returned <code>EAProject</code> list.
 * The nearer to the root the target package is located, the wider the search for diagrams and the longer
 * the startup time.
 * Note an EA project can have multiple root packages (Models in the EA project).
 * The first part in the path must match the name of one of those model names immediately under EA project.
 * Remaining parts must match the name of <code>Package</code>s within that model, where each successive part
 * corresponds to a child package via <code>Package.GetPackages()</code>. 
 * 
 * <ul>
 * <li>If no <code>initialPackage</code> is provided, the result is undefined.
 * <li>If the form is not correct, the result is undefined.
 * <li>If the path does not match an existing <code>Package</code>, the result is undefined.
 * <li>Only one package can be selected, hence only one model on the EA project can be returned.
 * </ul>
 */
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
