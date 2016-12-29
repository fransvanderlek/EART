package org.iisiplusone.eareport.contextmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Repository;


public class EADiagramImage {

	private static final Logger logger = LoggerFactory.getLogger(EADiagramImage.class);

	private Repository eaRepository;
	private String imageGUID;
	private String diagramName;
	private byte[] contents = null;
	
	public EADiagramImage(Repository eaRepository, String imageGUID, String diagramName) {
		this.eaRepository = eaRepository;
		this.imageGUID = imageGUID;
		this.diagramName = diagramName;

	}
	
	public byte[] getImageBytes() throws IOException {
		if (this.contents == null) {
			final File targetFile = writeDiagramFile(this.imageGUID);
			this.contents = loadByteContentsFromFile(targetFile);
			cleanUpFile(targetFile);

		}
		
		return this.contents;
	}
	
	public String getDiagramName(){
		return this.diagramName;
	}
	
	private File writeDiagramFile(String diagramGuid) {
		final File diagramFile = new File(UUID.randomUUID() + "_diagram_" + diagramGuid + ".png");
		this.eaRepository.GetProjectInterface().PutDiagramImageToFile(diagramGuid, diagramFile.getAbsolutePath(), 1);
		logger.info("Wrote " + diagramFile.getName());
		
		return diagramFile;
	}

	private byte[] loadByteContentsFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] theContents = IOUtils.toByteArray(fis);
		fis.close();

		return theContents;
	}

	private void cleanUpFile(File file) {

		Path p = Paths.get(file.getAbsolutePath());

		try {
			Files.delete(p);
			logger.info("Deleted " + file.getName());

		} catch (IOException e) {
			logger.error("Deletion of " + file.getName() + " failed --> " + e.getMessage());

		}
	}



}
