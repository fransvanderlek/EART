package org.iisiplusone.eart.generator;

import java.io.InputStream;

import org.iisiplusone.eart.contextmodel.IModelRepository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

public class EADocumentGeneratorModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	IXDocReportFactory provideXDocReportFactory(FreemarkerTemplateEngine freemarkerTemplateEngine) {

		FreemarkerXDocReportFactory fmRf = new FreemarkerXDocReportFactory();
		fmRf.setFreemarkerTemplateEngine(freemarkerTemplateEngine);
		
		return fmRf;
	}

	@Provides
	protected FreemarkerTemplateEngine provideFreemarkerTemplateEngine() {
		FreemarkerTemplateEngine fmkEngine = new FreemarkerTemplateEngine();
		fmkEngine.getFreemarkerConfiguration().setClassForTemplateLoading(XDocReportGenerator.class, "/");

		return fmkEngine;
	}

	@Provides
	public IDocumentGenerator provideGenerator(IModelRepository contextRepository,
			IXDocReportFactory xDocReportFactory) {
		XDocReportGenerator docgen = new XDocReportGenerator();
		docgen.setContextRepository(contextRepository);
		docgen.setxDocReportFactory(xDocReportFactory);
		return docgen;
	}

}
