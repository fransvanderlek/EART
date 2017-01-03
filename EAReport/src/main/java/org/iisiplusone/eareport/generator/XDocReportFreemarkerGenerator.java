package org.iisiplusone.eareport.generator;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.iisiplusone.eareport.contextmodel.EADiagramImage;
import org.iisiplusone.eareport.contextmodel.EAProject;
import org.iisiplusone.eareport.contextmodel.IModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

public class XDocReportFreemarkerGenerator implements IDocumentGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(XDocReportFreemarkerGenerator.class);
	
	private IModelRepository contextRepository;
	private FreemarkerTemplateEngine freemarkerTemplateEngine;


	public void setFreemarkerTemplateEngine(FreemarkerTemplateEngine freemarkerTemplateEngine) {
		this.freemarkerTemplateEngine = freemarkerTemplateEngine;
	}

	public void setContextRepository(IModelRepository contextRepository) {
		this.contextRepository = contextRepository;
	}

	@Override
	public void generate(InputStream template, OutputStream result) {
		
		try {
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					template, TemplateEngineKind.Freemarker);
			report.setTemplateEngine(this.freemarkerTemplateEngine);
			
			Map<String,Object> context = new HashMap<>();			
			EAProject model = this.contextRepository.getEAProject();
			
			context.put("rootPackage", model.getRootContext());
			
			FieldsMetadata metadata = new FieldsMetadata();
			
			for(EADiagramImage diagramImage : model.getDiagraImages()){				
		        metadata.addFieldAsImage(diagramImage.getDiagramName());		        
		        context.put(diagramImage.getDiagramName(), new EADiagramImageProvider( diagramImage ));
			}
			
			report.setFieldsMetadata(metadata);
						
			logger.info("Generating report.");
			report.process(context,result );
			logger.info("Done generating report.");
			
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		} 
	}

}
