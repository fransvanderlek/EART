package org.iisiplusone.eareport.contextmodel;

import org.sparx.Repository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class EARepositoryModule extends AbstractModule {

	private String eapFileLocation;
	private String initialPackage;
	
	public EARepositoryModule(String eapFileLocation, String initialPackage) {
		super();
		this.eapFileLocation = eapFileLocation;
		this.initialPackage = initialPackage;
	}

	@Override
	protected void configure() {
		
	}

	@Provides
	public IContextRepository provideContextRepository() {

		EARepositoryFactory.registerEapFile(this.eapFileLocation);
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		EAContextRepository ctx = new EAContextRepository();
		ctx.setRepository(repository);
		ctx.setInitialPackage(this.initialPackage);

		return ctx;
	}

}
