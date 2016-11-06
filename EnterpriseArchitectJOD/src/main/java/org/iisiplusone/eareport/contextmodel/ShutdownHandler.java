package org.iisiplusone.eareport.contextmodel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.sparx.Repository;

public class ShutdownHandler {
		
	static {
		Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() {
			
			@Override
			public void run() {
				
				repository.ShowWindow(1);
				repository.GetProjectInterface().ShowWindow(1);
				
				//according to sparx we should never call this method, see http://www.sparxsystems.com/enterprise_architect_user_guide/9.3/automation/project_2.html
				//however, this appears to be the only way of forcing EA to let go of the temporary diagram files so that they
				//can be deleted
				repository.GetProjectInterface().Exit(); 
				repository.CloseFile();
				repository.Exit();
								
				for(File file : ShutdownHandler.toBeDeltedFiles){
					
					Path p = Paths.get(file.getAbsolutePath()); 
					
					try {
						Files.delete(p);
						
					} catch (IOException e) {
						System.out.println("Deletion of "+file.getName()+"failed --> "+e.getMessage());
						
					}
					
								
				}
				
			}
		}));
	}
	
	private static Repository repository;
	
	private static List<File> toBeDeltedFiles = new ArrayList<>();
	
	public static void registerRepository(Repository repository){
		ShutdownHandler.repository = repository;
	}
	
	public static void registerFile(File toBeDeleted){
		ShutdownHandler.toBeDeltedFiles.add(toBeDeleted);
	}

}
