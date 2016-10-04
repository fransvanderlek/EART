package org.cocopapaya.eajod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cocopapaya.eadoc.EaDocumentGenerator;
import org.cocopapaya.eainterface.EA_DiagramLink;
import org.cocopapaya.eainterface.EA_DocumentGenerator;
import org.cocopapaya.eainterface.EA_RepositoryFactory;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import freemarker.ext.beans.BeansWrapper;
import junit.framework.Assert;
import net.cmp4oaw.ea_com.connector.EA_Connector;
import net.cmp4oaw.ea_com.diagram.EA_Diagram;
import net.cmp4oaw.ea_com.diagram.EA_DiagramObject;
import net.cmp4oaw.ea_com.element.EA_Element;
import net.cmp4oaw.ea_com.exception.EA_PathNotFoundException;
import net.cmp4oaw.ea_com.factory.EA_ObjectFactory;
import net.cmp4oaw.ea_com.repository.EA_Package;
import net.cmp4oaw.ea_com.repository.EA_Repository;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

public class GeneratorTest {

	EA_Repository eaRepo;


	@Before
	public void setupBeforeTests() {

		EA_RepositoryFactory.registerEapFile(this.getClass().getResource("/ea_jod.eap").getPath());
		eaRepo = EA_RepositoryFactory.getInstance().getRepository();

	}

	@Test
	public void testEaInterfaceDiagramLinks() throws EA_PathNotFoundException {

		EA_Package pack = eaRepo.findPackageByPath("Model/View_1/Package_1");

		System.out.println("Opening " + pack.getName());

		System.out.println("Getting links for diagram " + pack.Diagrams.getAt(0).getName());

		Assert.assertEquals("Expected two links", 2, pack.Diagrams.getAt(0).getDiagramLinks().size());

		Assert.assertEquals("Name error.", "ASSOCIATION_1",
				pack.Diagrams.getAt(0).getDiagramLinks().get(0).getConnector().getName());

		Assert.assertEquals("Name error.", "ASSOCIATION_2",
				pack.Diagrams.getAt(0).getDiagramLinks().get(1).getConnector().getName());

		for (EA_DiagramLink link : pack.Diagrams.getAt(0).getDiagramLinks()) {
			System.out.println("Link: " + link.getConnector().getName());
		}
	}

	/**
	 * public fields are not exposed by default by freemarker
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExposePublicFields() throws Exception {

		EA_DocumentGenerator factory = new EA_DocumentGenerator();

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

		Document odfDoc = Document.loadDocument(outputFile);
		Table table = odfDoc.getTableList().get(0);

		String[][] expectedTable = { { "ASSOCIATION_1", "Component_2", "Component_1", "" },
				{ "ASSOCIATION_2", "Component_2", "Component_3", "" } };

		for (int i = 0; i < 2; i++) {
			for (int k = 0; k < 4; k++) {
				Assert.assertEquals("Table value mismatch at row=" + i + ", column=" + k, expectedTable[i][k],
						table.getRowByIndex(i + 1).getCellByIndex(k).getStringValue().trim());

			}
		}

	}

}
