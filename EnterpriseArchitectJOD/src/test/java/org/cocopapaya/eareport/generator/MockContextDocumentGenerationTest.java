package org.cocopapaya.eareport.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocopapaya.eareport.contextmodel.EARepositoryModule;
import org.cocopapaya.eareport.contextmodel.IContextRepository;
import org.cocopapaya.eareport.eaapi_adapter.MapAdapterProxy;
import org.cocopapaya.eareport.generator.EADocumentGeneratorModule;
import org.cocopapaya.eareport.generator.IDocumentGenerator;
import org.cocopapaya.eareport.testsupport.TestClass;
import org.cocopapaya.eareport.testsupport.TestDelegate;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.table.Table;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

import junit.framework.Assert;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class MockContextDocumentGenerationTest {

	private Document generatedDocument;

	TestClass example = new TestClass();

	Map<String,Object> adapter = MapAdapterProxy.instance(example);

	@Before
	public void setupBeforeTests() throws Exception {

		Injector injector = Guice.createInjector(new EADocumentGeneratorModule(), new AbstractModule() {

			@Override
			protected void configure() {
			}

			@Provides
			IContextRepository provideContextRepository() {
				return new IContextRepository() {

					@Override
					public Map<String, Object> getContext() {
						Map<String, Object> context = new HashMap<>();
						context.put("root", adapter);
						return context;
					}
				};
			}
		});

		IDocumentGenerator docgen = injector.getInstance(IDocumentGenerator.class);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		docgen.generate(this.getClass().getResource("/mockcontext-document-generation.odt").openStream(), out);

		generatedDocument = Document.loadDocument(new ByteArrayInputStream(out.toByteArray()));

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
		for (TestDelegate child : example.getChildren()) {
			rows.add(new String[] { child.getFoo() });
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
