package org.iisiplusone.eareport.generator;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.iisiplusone.eareport.contextmodel.EARepositoryFactory;
import org.iisiplusone.eareport.contextmodel.IContextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;
import freemarker.template.Configuration;

public class XDocReportFreemarkerGenerator implements IDocumentGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(XDocReportFreemarkerGenerator.class);
	
	private IContextRepository contextRepository;
	private FreemarkerTemplateEngine freemarkerTemplateEngine;


	public void setFreemarkerTemplateEngine(FreemarkerTemplateEngine freemarkerTemplateEngine) {
		this.freemarkerTemplateEngine = freemarkerTemplateEngine;
	}

	public void setContextRepository(IContextRepository contextRepository) {
		this.contextRepository = contextRepository;
	}

	@Override
	public void generate(InputStream template, OutputStream result) {
		
		try {
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					template, TemplateEngineKind.Freemarker);
			
			Map<String,Object> context = this.contextRepository.getContext();	
			
			for(java.util.Map.Entry<String, Object> entry : context.entrySet()){
				if( entry.getValue() instanceof IImageProvider){
					FieldsMetadata metadata = new FieldsMetadata();
		            metadata.addFieldAsImage(entry.getKey());
		            report.setFieldsMetadata(metadata);
				}				
			}
			
			report.setTemplateEngine(this.freemarkerTemplateEngine);
			
			report.process(context,result );
			
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		} 
	}

}
