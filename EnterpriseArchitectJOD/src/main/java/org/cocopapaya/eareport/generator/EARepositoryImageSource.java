package org.cocopapaya.eareport.generator;

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

	public EARepositoryImageSource(Repository eaRepository, String imageGUID) {
		super();
		this.eaRepository = eaRepository;
		this.imageGUID = imageGUID;

	}

	@Override
	protected InputStream getInputStream() throws IOException {

		final File targetFile = new File("diagram_" + imageGUID + ".png");
		this.eaRepository.GetProjectInterface().PutDiagramImageToFile(this.imageGUID, targetFile.getAbsolutePath(), 1);
		byte[] contents = IOUtils.toByteArray(new FileInputStream(targetFile));
		targetFile.deleteOnExit();
				
		return new ByteArrayInputStream(contents);
	}
}
