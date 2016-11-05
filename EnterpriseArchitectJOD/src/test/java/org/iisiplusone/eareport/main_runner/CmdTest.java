package org.iisiplusone.eareport.main_runner;

import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.EapFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.OutputFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.TemplateFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.iisiplusone.eareport.main_runner.EaDocShell;
import org.iisiplusone.eareport.main_runner.EaReportProperties;
import org.iisiplusone.eareport.main_runner.EaReportProperties.PropName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CmdTest {
	
	File propertiesFile;
	EaReportProperties properties;

	@Before
	public void setup() throws IOException {
		propertiesFile = new File(this.getClass().getResource("/eajod.properties").getPath());

		properties = new EaReportProperties();
		properties.load(new FileInputStream(propertiesFile));

		for (PropName filePropName : new PropName[] { EapFile, TemplateFile, OutputFile }) {
			String correctedPath = propertiesFile.getParentFile().getAbsolutePath() + File.separatorChar
					+ properties.get(filePropName);
			properties.set(filePropName, correctedPath);
		}
		properties.store(new FileOutputStream(propertiesFile), "Modified for testing.");
	}

	@Test
	public void testWithDefaultPropertyFile() throws Exception {
		EaDocShell.main(new String[] { propertiesFile.getAbsolutePath() });

		File createdFile = new File(properties.get(OutputFile));

		Assert.assertTrue("File "+createdFile.getAbsolutePath()+"not found", createdFile.exists());
		
		//TODO assert that a proper odt file was created (no load errors)
	}

}
