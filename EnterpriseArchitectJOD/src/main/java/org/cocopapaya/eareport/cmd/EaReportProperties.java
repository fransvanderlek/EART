package org.cocopapaya.eareport.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class EaReportProperties {

	public enum PropName {
		EapFile("eap-file-location"), TemplateFile("template-file"), OutputFile("output-file"), RootPackage(
				"root-package");

		private String name;

		PropName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

	}

	private Properties properties = new Properties();

	public String get(PropName pName) {
		return this.properties.getProperty(pName.toString());
	}

	public void set(PropName pName, String value) {
		this.properties.setProperty(pName.toString(), value);
	}

	public void load(InputStream stream) throws IOException {
		this.properties.load(stream);
	}

	public void store(OutputStream stream, String comments) throws IOException {
		this.properties.store(stream, comments);
	}
}
