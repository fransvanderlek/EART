package org.iisiplusone.eareport.generator;

import org.iisiplusone.eareport.contextmodel.IModelRepository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

public class EADocumentGeneratorModule extends AbstractModule {
	
	@Override
	protected void configure() {
	}
	
	@Provides
	protected FreemarkerTemplateEngine provideFreemarkerTemplateEngine(){
		FreemarkerTemplateEngine fmkEngine = new FreemarkerTemplateEngine();
		fmkEngine.getFreemarkerConfiguration().setClassForTemplateLoading(XDocReportFreemarkerGenerator.class, "/");

		return fmkEngine;
	}
	
	@Provides
	public IDocumentGenerator provideGenerator(IModelRepository contextRepository, FreemarkerTemplateEngine freemarkerTemplateEngine){
		XDocReportFreemarkerGenerator docgen = new XDocReportFreemarkerGenerator();
		docgen.setContextRepository(contextRepository);
		docgen.setFreemarkerTemplateEngine(freemarkerTemplateEngine);
		return docgen;
	}

}
