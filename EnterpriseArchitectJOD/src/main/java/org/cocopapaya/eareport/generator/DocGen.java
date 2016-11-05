package org.cocopapaya.eareport.generator;

import java.io.InputStream;
import java.io.OutputStream;

import org.cocopapaya.eareport.contextmodel.IContextRepository;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class DocGen implements IDocumentGenerator {

	private IContextRepository contextRepository;
	private DocumentTemplateFactory documentTemplateFactory;


	public void setContextRepository(IContextRepository contextRepository) {
		this.contextRepository = contextRepository;
	}


	public void setDocumentTemplateFactory(DocumentTemplateFactory documentTemplateFactory) {
		this.documentTemplateFactory = documentTemplateFactory;
	}


	@Override
	public void generate(InputStream templateStream, OutputStream resultStream) {

		try {
			DocumentTemplate template = documentTemplateFactory.getTemplate(templateStream);
			template.createDocument(contextRepository.getContext(), resultStream);

		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}

	}

}
