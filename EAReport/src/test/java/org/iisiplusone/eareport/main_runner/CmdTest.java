package org.iisiplusone.eareport.main_runner;

import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.EapFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.OutputFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.TemplateFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
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
		propertiesFile = new File(this.getClass().getResource("/eareport.properties").getPath());

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

		Assert.assertTrue("File " + createdFile.getAbsolutePath() + "not found", createdFile.exists());

		XWPFDocument generatedDocument = new XWPFDocument(new FileInputStream(createdFile));
		XWPFTable table = generatedDocument.getTables().get(0);

		String[][] expectedTable = { { "ASSOCIATION_1", "Component_2", "Component_1", "association 1 notes" },
				{ "ASSOCIATION_2", "Component_2", "Component_3", "association 2 notes" } };

		for (int i = 0; i < 2; i++) {
			for (int k = 0; k < 4; k++) {
				Assert.assertEquals("Table value mismatch at row=" + (i + 1) + ", column=" + k, expectedTable[i][k],
						table.getRow(i + 1).getCell(k).getText().trim());
			}
		}
		
		//@TODO: verify diagram images
		//generatedDocument.getAllPictures()
	}

}
