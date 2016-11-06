package org.iisiplusone.eareport.contextmodel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.sparx.Repository;

import net.sf.jooreports.templates.image.AbstractInputStreamImageSource;

public class EARepositoryImageSource extends AbstractInputStreamImageSource {

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
			final File targetFile = new File(System.getProperty("java.io.tmpdir") +File.pathSeparator+UUID.randomUUID()+"_diagram_" + imageGUID + ".png");
			this.eaRepository.GetProjectInterface().PutDiagramImageToFile(this.imageGUID, targetFile.getAbsolutePath(), 1);
			
			this.contents = IOUtils.toByteArray(new FileInputStream(targetFile));			
			ShutdownHandler.registerFile(targetFile);					
		}


//		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//
//			/*
//			 * This method will attempt to delete the temporary image file
//			 * created by enterprise architect. Due to file locking this might
//			 * not succeed immediately, so we retry 3 times before giving up.
//			 * 
//			 * (non-Javadoc)
//			 * 
//			 * @see java.lang.Runnable#run()
//			 */
//			@Override
//			public void run() {
//				boolean deleteDone = false;
//				int maxRetries = 3;
//				int currentTry = 1;
//				long retryIntervalMs = 500;
//
//				while (!deleteDone && currentTry <= maxRetries) {
//
//					try {
//						// first we try without delay, sleep for 0 millis
//						Thread.sleep((currentTry - 1) * retryIntervalMs);
//
//					} catch (InterruptedException e) {
//						// restore interrupted state
//						// afterwards we continue to retry file deletion
//						Thread.currentThread().interrupt();
//					}
//					deleteDone = targetFile.delete();
//					currentTry++;
//				}
//
//				System.out.println("Deletion of file " + targetFile.getName() + " completed with success=" + deleteDone
//						+ " after " + currentTry + " attempt(s)");
//
//			}
//		}));

		return new ByteArrayInputStream(this.contents);
	}
}
