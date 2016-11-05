package org.iisiplusone.eareport.main_runner;

import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.EapFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.OutputFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.RootPackage;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.TemplateFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.iisiplusone.eareport.contextmodel.EARepositoryModule;
import org.iisiplusone.eareport.generator.EADocumentGeneratorModule;
import org.iisiplusone.eareport.generator.IDocumentGenerator;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class EaDocShell {

	private static final String DEFAULT_PROPERTIES_FILENAME = "eajod.properties";

	public static void main(String[] args) throws Exception {

		EaReportProperties properties = cmdLineProperties(args);

		Injector injector = Guice.createInjector(new EADocumentGeneratorModule(),
				new EARepositoryModule(properties.get(EapFile), properties.get(RootPackage)));

		IDocumentGenerator docgen = injector.getInstance(IDocumentGenerator.class);

		docgen.generate(new FileInputStream(properties.get(TemplateFile)),
				new FileOutputStream(properties.get(OutputFile)));

	}

	private static EaReportProperties cmdLineProperties(String[] args) throws IOException, FileNotFoundException {
		String propFileName = DEFAULT_PROPERTIES_FILENAME;

		if (args.length > 0) {
			propFileName = args[0];
			System.out.println("Using properties file " + propFileName);
		}

		EaReportProperties properties = new EaReportProperties();
		properties.load(new FileInputStream(propFileName));
		return properties;
	}
}
