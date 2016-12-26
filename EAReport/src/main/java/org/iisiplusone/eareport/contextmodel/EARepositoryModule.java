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
	protected Repository provideRepository(){
		EARepositoryFactory.registerEapFile(this.eapFileLocation);
		Repository repository = EARepositoryFactory.getInstance().getRepository();
		
		return repository;
	}
	
	@Provides
	protected EAImageSourceRepository provideImageSourceRepository(Repository repository){
		EAImageSourceRepository imageSourceRepository = new EAImageSourceRepository();
		imageSourceRepository.setRepository(repository);
		return imageSourceRepository;
	}

	@Provides
	public IContextRepository provideContextRepository(EAImageSourceRepository imageSourceRepository, Repository repository) {

		EAContextRepository ctx = new EAContextRepository();
		ctx.setRepository(repository);
		ctx.setInitialPackage(this.initialPackage);
		ctx.setImageSourceRepository(imageSourceRepository);

		return ctx;
	}

}
