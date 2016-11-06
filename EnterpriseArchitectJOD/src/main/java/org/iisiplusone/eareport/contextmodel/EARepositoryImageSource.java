package org.iisiplusone.eareport.contextmodel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Repository;

import net.sf.jooreports.templates.image.AbstractInputStreamImageSource;

public class EARepositoryImageSource extends AbstractInputStreamImageSource {

	private static final Logger logger = LoggerFactory.getLogger(EARepositoryImageSource.class);

	private Repository eaRepository;
	private String imageGUID;
	private byte[] contents = null;

	public EARepositoryImageSource(Repository eaRepository, String imageGUID) {
		super();
		this.eaRepository = eaRepository;
		this.imageGUID = imageGUID;

	}

	@Override
	protected InputStream getInputStream() throws IOException {

		if( this.contents==null){			
			final File targetFile = writeDiagramFile(this.imageGUID);
			this.contents = loadByteContentsFromFile(targetFile);
			cleanUpFile(targetFile);
			
		}
		return new ByteArrayInputStream(this.contents);
	}
	
	private File writeDiagramFile( String diagramGuid){
		final File diagramFile = new File(UUID.randomUUID()+"_diagram_" + imageGUID + ".png");
		this.eaRepository.GetProjectInterface().PutDiagramImageToFile(this.imageGUID, diagramFile.getAbsolutePath(), 1);
		
		return diagramFile;
	}
	
	private byte[] loadByteContentsFromFile(File file) throws IOException{
		FileInputStream fis = new FileInputStream(file);
		
		byte[] theContents = IOUtils.toByteArray(fis);	
		fis.close();
		
		return theContents;
	}
	
	private void cleanUpFile(File file){
		
		Path p = Paths.get(file.getAbsolutePath()); 
		
		try {
			Files.delete(p);
			logger.info("Deleted "+file.getName());
			
		} catch (IOException e) {
			logger.error("Deletion of "+file.getName()+" failed --> "+e.getMessage());
			
		}
	}
}
