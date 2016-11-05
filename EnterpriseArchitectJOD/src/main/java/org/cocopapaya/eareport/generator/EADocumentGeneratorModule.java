package org.cocopapaya.eareport.generator;

import org.cocopapaya.eareport.contextmodel.IContextRepository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import net.sf.jooreports.templates.DocumentTemplateFactory;

public class EADocumentGeneratorModule extends AbstractModule {
	
	@Override
	protected void configure() {
	}
	
	@Provides
	public IDocumentGenerator provideGenerator(IContextRepository contextRepository){
		DocGen docgen = new DocGen();
		docgen.setContextRepository(contextRepository);
		docgen.setDocumentTemplateFactory(new DocumentTemplateFactory());
		
		return docgen;
	}

}
