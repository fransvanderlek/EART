package org.iisiplusone.eart.generator;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.iisiplusone.eart.contextmodel.EADiagramImage;
import org.iisiplusone.eart.contextmodel.EAProject;
import org.iisiplusone.eart.contextmodel.IModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XDocReportGenerator implements IDocumentGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(XDocReportGenerator.class);
	
	private IModelRepository contextRepository;
	private IXDocReportFactory xDocReportFactory;
	
	public void setxDocReportFactory(IXDocReportFactory xDocReportFactory) {
		this.xDocReportFactory = xDocReportFactory;
	}

	public void setContextRepository(IModelRepository contextRepository) {
		this.contextRepository = contextRepository;
	}

	@Override
	public void generate(InputStream template, OutputStream result) {
		
		try {
			IXDocReport report = xDocReportFactory.getReport(template);
			
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
