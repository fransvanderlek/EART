package org.iisiplusone.eareport.generator;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.iisiplusone.eareport.contextmodel.EADiagramImage;
import org.iisiplusone.eareport.contextmodel.EAProject;
import org.iisiplusone.eareport.contextmodel.EARepositoryFactory;
import org.iisiplusone.eareport.contextmodel.IModelRepository;
import org.iisiplusone.eareport.eaapi_adapter.ObjectWrapper;
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
			
			for(EADiagramImage diagramImage : model.getDiagraImages()){
				FieldsMetadata metadata = new FieldsMetadata();
		        metadata.addFieldAsImage(diagramImage.getDiagramName());
		        report.setFieldsMetadata(metadata);
		        context.put(diagramImage.getDiagramName(), new EADiagramImageProvider( diagramImage ));
			}
						
			logger.info("Generating report.");
			report.process(context,result );
			logger.info("Done generating report.");
			
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		} 
	}

}
