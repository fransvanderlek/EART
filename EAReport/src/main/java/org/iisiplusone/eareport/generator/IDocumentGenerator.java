package org.iisiplusone.eareport.generator;

import java.io.InputStream;
import java.io.OutputStream;

public interface IDocumentGenerator {

	public void generate(InputStream template, OutputStream result);

}