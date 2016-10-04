package org.cocopapaya.eadoc;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.cocopapaya.eajod.EARepositoryFactory;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.DefaultObjectWrapper;
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
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(propFileName));
		
		factory = new EaDocumentGenerator();
		
		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		BeansWrapper objectWrapper = (BeansWrapper) documentTemplateFactory.getFreemarkerConfiguration().getObjectWrapper();
		objectWrapper.setExposeFields(true);
		DocumentTemplate template = documentTemplateFactory.getTemplate(new File(properties.getProperty("template-file")));

		factory.setDocumentTemplate(template);
		factory.setInitialPackage(properties.getProperty("root-package"));
		factory.setOutputFileName(properties.getProperty("output-file"));
		EARepositoryFactory.registerEapFile(properties.getProperty("eap-file-location"));
		factory.setRepository(EARepositoryFactory.getInstance().getRepository());
			
		factory.createDocument();
	}

}
