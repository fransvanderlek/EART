package org.cocopapaya.eajod;

import java.io.File;

import org.cocopapaya.eadoc.EaDocumentGenerator;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.table.Table;
import org.sparx.Repository;

import freemarker.ext.beans.BeansWrapper;
import junit.framework.Assert;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class DocumentGenerationTest {

	private Document generatedDocument;


	@Before
	public void setupBeforeTests() throws Exception {
		
		String eapPath = this.getClass().getResource("/ea_jod.eap").getPath().substring(1);
		
		System.out.println("passing "+eapPath+" to repository factory.");

		EARepositoryFactory.registerEapFile(eapPath);
		Repository eaRepo = EARepositoryFactory.getInstance().getRepository();
		
		EaDocumentGenerator factory = new EaDocumentGenerator();

		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		BeansWrapper objectWrapper = (BeansWrapper) documentTemplateFactory.getFreemarkerConfiguration()
				.getObjectWrapper();
		objectWrapper.setExposeFields(true);

		File theFile = new File(this.getClass().getResource("/diagram_links.odt").getPath());

		DocumentTemplate template = documentTemplateFactory.getTemplate(theFile);

		String absolutePath = theFile.getAbsolutePath();
		String pathToFile = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		File outputFile = new File(pathToFile + File.separator + "diagram_links_out.odt");
		factory.setDocumentTemplate(template);
		factory.setInitialPackage("Model/View_1/Package_1");
		factory.setOutputFileName(outputFile.getAbsolutePath());
		factory.setRepository(eaRepo);

		factory.createDocument();

		generatedDocument = Document.loadDocument(outputFile);

	}


	/**
	 * public fields are not exposed by default by freemarker
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExposePublicFields() throws Exception {


		Table table = generatedDocument.getTableList().get(0);

		String[][] expectedTable = { { "ASSOCIATION_1", "Component_2", "Component_1", "" },
				{ "ASSOCIATION_2", "Component_2", "Component_3", "" } };

		for (int i = 0; i < 2; i++) {
			for (int k = 0; k < 4; k++) {
				Assert.assertEquals("Table value mismatch at row=" + i+1 + ", column=" + k, expectedTable[i][k],
						table.getRowByIndex(i + 1).getCellByIndex(k).getStringValue().trim());

			}
		}

	}
	
	@Test
	public void testTaggedValues() throws Exception {
		
		Table table = generatedDocument.getTableList().get(1);
		
		Assert.assertNotNull(table);
		
		Assert.assertEquals("Table heading error", "Name", table.getRowByIndex(0).getCellByIndex(0).getStringValue().trim());

		String[][] expectedTable = { 
				{ "Component_1", "classifier_notes_component1", "value_1", "value_2","1.0" },
				{ "Component_2", "classifier_notes_component2", "", "","1.0" },
				{ "Component_3", "classifier_notes_component3", "", "","1.0" }
		};
		
		Assert.assertEquals("Table row size error:", expectedTable.length+1, table.getRowCount());
		Assert.assertEquals("Table column size error:", expectedTable[0].length, table.getColumnCount());
		
		for (int i = 0; i < expectedTable.length; i++) {
			for (int k = 0; k < expectedTable[0].length; k++) {
				Assert.assertEquals("Table value mismatch at row=" + (i+1) + ", column=" + k, expectedTable[i][k],
						table.getRowByIndex(i + 1).getCellByIndex(k).getStringValue().trim());

			}
		}
	
	}

}
