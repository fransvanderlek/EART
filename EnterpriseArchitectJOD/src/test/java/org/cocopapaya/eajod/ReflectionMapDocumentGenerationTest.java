package org.cocopapaya.eajod;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocopapaya.eareport.eaapi_adapter.LazyMapAdapter;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.table.Table;

import junit.framework.Assert;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class ReflectionMapDocumentGenerationTest {

	private Document generatedDocument;
	
	TestClass example = new TestClass();

	LazyMapAdapter adapter = new LazyMapAdapter(example);


	@Before
	public void setupBeforeTests() throws Exception {

		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();


		File theFile = new File(this.getClass().getResource("/reflection-map-adapter-test2.odt").getPath());

		DocumentTemplate template = documentTemplateFactory.getTemplate(theFile);

		String absolutePath = theFile.getAbsolutePath();
		String pathToFile = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		File outputFile = new File(pathToFile + File.separator + "reflection-map-adapter-test-out.odt");
		
		
		Map <String, Object> context = new HashMap<>();
		context.put("root", adapter);
		template.createDocument(context, new FileOutputStream(outputFile.getAbsolutePath()));		

		generatedDocument = Document.loadDocument(outputFile);

	}


	/**
	 * public fields are not exposed by default by freemarker
	 * 
	 * @throws Exception
	 */
	@Test
	public void testOutputSimpleProperties() throws Exception {


		Table table = generatedDocument.getTableList().get(0);

		String[][] expectedTable = { { example.getTitle(), example.GetSomething() } };

		for (int i = 0; i < expectedTable.length; i++) {
			for (int k = 0; k < expectedTable[i].length; k++) {
				Assert.assertEquals("Table value mismatch at row=" + i + ", column=" + k, expectedTable[i][k],
						table.getRowByIndex(i).getCellByIndex(k).getStringValue().trim());

			}
		}

	}
	
	@Test
	public void testList() throws Exception {
		
		Table table = generatedDocument.getTableList().get(1);
		
		Assert.assertNotNull(table);
		
		List<String[]> rows = new ArrayList<>();
		for( TestDelegate child : example.getChildren()){
			rows.add( new String[]{ child.getFoo()} );
		}

		String[][] expectedTable = new String[rows.size()][1];
		
		Assert.assertEquals("Table row size error:", rows.size(), table.getRowCount());
		
		for (int i = 0; i < rows.size(); i++) {
			for (int k = 0; k < rows.get(0).length; k++) {
				Assert.assertEquals("Table value mismatch at row=" + (i) + ", column=" + k, rows.get(i)[k],
						table.getRowByIndex(i).getCellByIndex(k).getStringValue().trim());

			}
		}
	
	}

}
