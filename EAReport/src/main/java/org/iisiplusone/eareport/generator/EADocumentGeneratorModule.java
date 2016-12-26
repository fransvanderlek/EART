package org.iisiplusone.eareport.generator;

import org.iisiplusone.eareport.contextmodel.IContextRepository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class EADocumentGeneratorModule extends AbstractModule {
	
	@Override
	protected void configure() {
	}
	
	@Provides
	public IDocumentGenerator provideGenerator(IContextRepository contextRepository){
		XDocReportGenerator docgen = new XDocReportGenerator();
		docgen.setContextRepository(contextRepository);
		return docgen;
	}

}
