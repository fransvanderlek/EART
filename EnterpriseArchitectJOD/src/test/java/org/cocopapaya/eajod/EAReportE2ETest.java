package org.cocopapaya.eajod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import static org.cocopapaya.eareport.cmd.EaReportProperties.PropName.*;

import org.cocopapaya.eareport.cmd.EaDocShell;
import org.cocopapaya.eareport.cmd.EaReportProperties;
import org.cocopapaya.eareport.cmd.EaReportProperties.PropName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EAReportE2ETest {
	
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
			System.out.println("Setting "+filePropName+": "+correctedPath);
			properties.set(filePropName, correctedPath);
		}
		properties.store(new FileOutputStream(propertiesFile), "Modified for testing.");
	}

	@Test
	public void testWithDefaultPropertyFile() throws Exception {
		EaDocShell.main(new String[] { propertiesFile.getAbsolutePath() });

		File createdFile = new File(properties.get(OutputFile));

		Assert.assertTrue("File "+createdFile.getAbsolutePath()+"not found", createdFile.exists());
	}

}
