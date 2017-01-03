package org.iisiplusone.eareport.generator;

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

public class FreemarkerXDocReportFactory implements IXDocReportFactory {

	private FreemarkerTemplateEngine freemarkerTemplateEngine;

	public void setFreemarkerTemplateEngine(FreemarkerTemplateEngine freemarkerTemplateEngine) {
		this.freemarkerTemplateEngine = freemarkerTemplateEngine;
	}

	@Override
	public IXDocReport getReport(InputStream template) throws IOException, XDocReportException {
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(template, TemplateEngineKind.Freemarker);
		report.setTemplateEngine(this.freemarkerTemplateEngine);

		return report;
	}

}
