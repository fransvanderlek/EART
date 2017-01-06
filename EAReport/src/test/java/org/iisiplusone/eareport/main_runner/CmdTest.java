package org.iisiplusone.eareport.main_runner;

import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.EapFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.OutputFile;
import static org.iisiplusone.eareport.main_runner.EaReportProperties.PropName.TemplateFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.iisiplusone.eareport.main_runner.EaDocShell;
import org.iisiplusone.eareport.main_runner.EaReportProperties;
import org.iisiplusone.eareport.main_runner.EaReportProperties.PropName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sparx.Project;
import org.sparx.Repository;

import fr.opensagres.xdocreport.core.io.IOUtils;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

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

		final Map<String, byte[]> capturedImages = new HashMap<>();

		new MockUp<Project>() {

			@Mock
			public boolean PutDiagramImageToFile(Invocation invocation, String diagramGuid, String pathToFile,
					int arg3) {

				// Let EA repository generate the diagram image normally
				boolean result = invocation.proceed(diagramGuid, pathToFile, arg3);

				// read the image and keep for later
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(pathToFile);
					// capturedImages.put(pathToFile, IOUtils.toByteArray(fis));

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fis != null) {
						IOUtils.closeQuietly(fis);
					}
				}

				// return whatever the result was according the EA Repository
				// class
				return result;
			}
		};

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

		capturedImages.put("Diagram_1",
				org.apache.commons.io.IOUtils.toByteArray(CmdTest.class.getResourceAsStream("/Diagram_1.png")));
		capturedImages.put("Diagram_2",
				org.apache.commons.io.IOUtils.toByteArray(CmdTest.class.getResourceAsStream("/Diagram_2.png")));

		for (Entry<String, byte[]> contents : capturedImages.entrySet()) {
			boolean found = false;

			for (XWPFPictureData data : generatedDocument.getAllPictures()) {
				if (Arrays.equals(contents.getValue(), data.getData())) {
					found = true;
					break;
				}
			}

			if (!found) {
				Assert.fail("Could not find match for image with filepath " + contents.getKey());
			}
		}

	}

}
