package org.iisiplusone.eart.contextmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Repository;

public class EARepositoryFactory {

	private static final Logger logger = LoggerFactory.getLogger(EARepositoryFactory.class);

	public static EARepositoryFactory INSTANCE;

	private Repository repo;
	private String eapFileLocation;

	/**
	 * Note file locations of the form /C:\path\to\file give internal errors on
	 * COM level!
	 * 
	 * @param eapFileLocation
	 */
	public static void registerEapFile(String eapFileLocation) {
		if( INSTANCE == null){
			INSTANCE = new EARepositoryFactory(eapFileLocation);
			
		} else {
			logger.warn("Multiple eap file registrations detected. Ignored.");
			
		}		
	}

	public static EARepositoryFactory getInstance() {
		return INSTANCE;
	}
	
	public Repository getRepository() {

		if (repo == null) {
			openRepository();
		}

		return repo;
	}

	private EARepositoryFactory(String eapFileLocation) {
		this.eapFileLocation = eapFileLocation;
	}

	private void openRepository() {

		logger.info("Opening eap file: " + this.eapFileLocation);

		repo = new Repository();
		repo.OpenFile(this.eapFileLocation);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("Requesting repository to close file.");
				repo.CloseFile();
				repo.Exit();
			}
		}));
	}
}
