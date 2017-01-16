package org.iisiplusone.eart.main_runner;

import static org.iisiplusone.eart.main_runner.EARTProperties.PropName.EapFile;
import static org.iisiplusone.eart.main_runner.EARTProperties.PropName.OutputFile;
import static org.iisiplusone.eart.main_runner.EARTProperties.PropName.RootPackage;
import static org.iisiplusone.eart.main_runner.EARTProperties.PropName.TemplateFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.iisiplusone.eart.contextmodel.EARepositoryModule;
import org.iisiplusone.eart.generator.EADocumentGeneratorModule;
import org.iisiplusone.eart.generator.IDocumentGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class EaDocShell {

	private static final Logger logger = LoggerFactory.getLogger(EaDocShell.class);
	
	private static final String DEFAULT_PROPERTIES_FILENAME = "eart.properties";

	public static void main(String[] args) throws Exception {

		Properties buildProps = new Properties();
		buildProps.load(EaDocShell.class.getResourceAsStream("/build.properties"));
		
		logger.info("Version : "+buildProps.getProperty("application.version"));
		
		EARTProperties properties = cmdLineProperties(args);

		Injector injector = Guice.createInjector(new EADocumentGeneratorModule(),
				new EARepositoryModule(properties.get(EapFile), properties.get(RootPackage)));

		IDocumentGenerator docgen = injector.getInstance(IDocumentGenerator.class);

		docgen.generate(new FileInputStream(properties.get(TemplateFile)),
				new FileOutputStream(properties.get(OutputFile)));

	}

	private static EARTProperties cmdLineProperties(String[] args) throws IOException, FileNotFoundException {
		String propFileName = DEFAULT_PROPERTIES_FILENAME;

		if (args.length > 0) {
			propFileName = args[0];
			logger.info("Using properties file " + propFileName);
		}

		EARTProperties properties = new EARTProperties();
		properties.load(new FileInputStream(propFileName));
		return properties;
	}
}
