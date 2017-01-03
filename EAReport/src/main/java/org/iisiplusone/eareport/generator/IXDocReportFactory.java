package org.iisiplusone.eareport.generator;

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;

public interface IXDocReportFactory {

	public IXDocReport getReport(InputStream template) throws IOException, XDocReportException;
}
