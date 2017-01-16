package org.iisiplusone.eart.generator;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.iisiplusone.eart.contextmodel.EADiagramImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.document.images.AbstractImageProvider;
import fr.opensagres.xdocreport.document.images.SimpleImageInfo;

public class EADiagramImageProvider extends AbstractImageProvider {

	private static final Logger logger = LoggerFactory.getLogger(EADiagramImageProvider.class);

	private EADiagramImage diagramImage;
	
	private byte[] contents = null;
	
	public EADiagramImageProvider(EADiagramImage diagramImage) {
		super(false);
		this.diagramImage = diagramImage;
	}
	
	@Override
	public void write(OutputStream output) throws IOException {
		
		ensureLoaded();
		
		IOUtils.write(this.contents, output);
	}

	private void ensureLoaded() throws IOException {
		if (this.contents == null) {
			this.contents = this.diagramImage.getImageBytes();
		}
	}

	@Override
	public ImageFormat getImageFormat() {
		try {
			SimpleImageInfo imageInfo = getImageInfo();
			if (imageInfo == null) {
				return null;
			}
			return imageInfo.getMimeType();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected SimpleImageInfo loadImageInfo() throws IOException {
		ensureLoaded();
		return new SimpleImageInfo(this.contents);
	}

	@Override
	protected boolean doIsValid() {
		return true;
	}


	
}
