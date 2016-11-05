package org.cocopapaya.eareport.contextmodel;

import org.cocopapaya.eareport.eaapi_ext.EARepositoryFactory;
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
		EARepositoryContext ctx = new EARepositoryContext();
		ctx.setRepository(repository);
		ctx.setInitialPackage(this.initialPackage);

		return ctx;
	}

}
