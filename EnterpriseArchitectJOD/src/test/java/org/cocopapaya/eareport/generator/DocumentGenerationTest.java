package org.cocopapaya.eareport.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.cocopapaya.eareport.contextmodel.EARepositoryModule;
import org.cocopapaya.eareport.generator.EADocumentGeneratorModule;
import org.cocopapaya.eareport.generator.IDocumentGenerator;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.table.Table;

import com.google.inject.Guice;
import com.google.inject.Injector;

import junit.framework.Assert;

public class DocumentGenerationTest {

	private Document generatedDocument;

	@Before
	public void setupBeforeTests() throws Exception {

		String eapPath = this.getClass().getResource("/ea_jod.eap").getPath().substring(1);

		Injector injector = Guice.createInjector(new EADocumentGeneratorModule(),
				new EARepositoryModule(eapPath, "Model/View_1/Package_1"));

		IDocumentGenerator docgen = injector.getInstance(IDocumentGenerator.class);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		docgen.generate(this.getClass().getResource("/document-generation.odt").openStream(), out);
		
		generatedDocument = Document.loadDocument( new ByteArrayInputStream(out.toByteArray()));

	}

	/**
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
				Assert.assertEquals("Table value mismatch at row=" + i + 1 + ", column=" + k, expectedTable[i][k],
						table.getRowByIndex(i + 1).getCellByIndex(k).getStringValue().trim());

			}
		}

	}

	@Test
	public void testTaggedValues() throws Exception {

		Table table = generatedDocument.getTableList().get(1);

		Assert.assertNotNull(table);

		Assert.assertEquals("Table heading error", "Name",
				table.getRowByIndex(0).getCellByIndex(0).getStringValue().trim());

		String[][] expectedTable = { { "Component_1", "classifier_notes_component1", "value_1", "value_2", "1.0" },
				{ "Component_2", "classifier_notes_component2", "", "", "1.0" },
				{ "Component_3", "classifier_notes_component3", "", "", "1.0" } };

		Assert.assertEquals("Table row size error:", expectedTable.length + 1, table.getRowCount());
		Assert.assertEquals("Table column size error:", expectedTable[0].length, table.getColumnCount());

		for (int i = 0; i < expectedTable.length; i++) {
			for (int k = 0; k < expectedTable[0].length; k++) {
				Assert.assertEquals("Table value mismatch at row=" + (i + 1) + ", column=" + k, expectedTable[i][k],
						table.getRowByIndex(i + 1).getCellByIndex(k).getStringValue().trim());

			}
		}

	}

}
