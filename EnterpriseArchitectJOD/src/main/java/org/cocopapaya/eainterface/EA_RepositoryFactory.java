package org.cocopapaya.eainterface;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.cmp4oaw.ea_com.repository.EA_Repository;

public class EA_RepositoryFactory {

	private static final String DEFAULT = "DEFAULT";
	private EA_Repository repo;
	private String eapFileLocation;
	private static Map<String, EA_RepositoryFactory> INSTANCES = new HashMap<>();

	public static boolean registerEapFile(String eapFileLocation) {
		
		boolean registrationOccured = false;		
		
		if( !INSTANCES.containsKey(DEFAULT)){
			INSTANCES.put(DEFAULT, new EA_RepositoryFactory(eapFileLocation));
			registrationOccured = true;
		}
		
		return registrationOccured;
		
	}

	public static EA_RepositoryFactory getInstance() {

		return INSTANCES.get(DEFAULT);
	}

	private EA_RepositoryFactory(String eapFileLocation) {
		this.eapFileLocation = eapFileLocation;

	}

	private void openRepository() {

		System.out.println("Opening eap file: " + this.eapFileLocation);
		
		File eapFile = new File(this.eapFileLocation);
		
		if(eapFile.exists()){
			repo = EA_Repository.getInstance();
			repo.openModel(eapFile.getAbsolutePath());

			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("Requesting repository to close file.");
					repo.closeModel();

				}
			}));
			
		} else{
			throw new RuntimeException("Could not open "+eapFile.getAbsolutePath());
		}
		


	}

	public EA_Repository getRepository() {

		if (repo == null) {
			openRepository();
		}

		return repo;
	}

}
