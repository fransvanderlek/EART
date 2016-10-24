package org.cocopapaya.eareport.cmd;

import static org.cocopapaya.eareport.cmd.EaReportProperties.PropName.EapFile;
import static org.cocopapaya.eareport.cmd.EaReportProperties.PropName.OutputFile;
import static org.cocopapaya.eareport.cmd.EaReportProperties.PropName.RootPackage;
import static org.cocopapaya.eareport.cmd.EaReportProperties.PropName.TemplateFile;

import java.io.File;
import java.io.FileInputStream;

import org.cocopapaya.eareport.eaapi.EARepositoryFactory;
import org.cocopapaya.eareport.generator.EaDocumentGenerator;

import freemarker.ext.beans.BeansWrapper;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class EaDocShell {
	
	private static final String DEFAULT_PROPERTIES_FILENAME = "eajod.properties";
	static EaDocumentGenerator factory;

	public static void main(String[] args) throws Exception {
		
		String propFileName = DEFAULT_PROPERTIES_FILENAME;

		if( args.length > 0){
			propFileName = args[0];
			System.out.println("Using properties file "+propFileName);
		}
		
		EaReportProperties properties = new EaReportProperties();
		properties.load(new FileInputStream(propFileName));
		
		factory = new EaDocumentGenerator();
		
		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		BeansWrapper objectWrapper = (BeansWrapper) documentTemplateFactory.getFreemarkerConfiguration().getObjectWrapper();
		objectWrapper.setExposeFields(true);
		DocumentTemplate template = documentTemplateFactory.getTemplate(new File(properties.get(TemplateFile)));

		factory.setDocumentTemplate(template);
		factory.setInitialPackage(properties.get(RootPackage));
		factory.setOutputFileName(properties.get(OutputFile));
		EARepositoryFactory.registerEapFile(properties.get(EapFile));
		factory.setRepository(EARepositoryFactory.getInstance().getRepository());
			
		factory.createDocument();
	}

}
