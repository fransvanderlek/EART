package org.cocopapaya.eajod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import net.cmp4oaw.ea_com.common.EA_Collection;
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

public class EAApiTest {

	EA_Repository eaRepo;

	EA_Diagram diagram;

	@Before
	public void setupBeforeTests() throws EA_PathNotFoundException {

		EA_RepositoryFactory.registerEapFile(this.getClass().getResource("/ea_jod.eap").getPath());
		eaRepo = EA_RepositoryFactory.getInstance().getRepository();
		EA_Package pack = eaRepo.findPackageByPath("Model/View_1/Package_1");

		System.out.println("Opening " + pack.getName());
		diagram = pack.Diagrams.getAt(0);

	}

	@Test
	public void testEaInterfaceDiagramLinks() {

		System.out.println("Getting links for diagram " + diagram.getName());

		Assert.assertEquals("Expected two links", 2, diagram.getDiagramLinks().size());

		Assert.assertEquals("Name error.", "ASSOCIATION_1", diagram.getDiagramLinks().get(0).getConnector().getName());

		Assert.assertEquals("Name error.", "ASSOCIATION_2", diagram.getDiagramLinks().get(1).getConnector().getName());

		String[] expectedConnectorNames = { "ASSOCIATION_1", "ASSOCIATION_2", };

		for (int i = 0; i < diagram.getDiagramLinks().size(); i++) {

			EA_DiagramLink link = diagram.getDiagramLinks().get(i);
			Assert.assertEquals("Name error.", expectedConnectorNames[i], link.getConnector().getName());
		}
	}

	@Test
	public void testDiagramObjectClassifiers() {
		EA_Collection<EA_DiagramObject> diagramObjects = diagram.DiagramObjects;

		Assert.assertEquals("Number of objects on diagram error.", 3, diagramObjects.count);

		List<String> expectedClassifiers = Arrays.asList(new String[] { "Component_1", "Component_2", "Component_3"});
		List<String> foundClassifiers = new ArrayList<>();

		for (EA_DiagramObject object : diagramObjects) {

			String classifier = object.getElement().getClassifier().getName();
			Assert.assertTrue("Unexpected classifier <" + classifier + "> in diagram <"+diagram.getName()+">",
					expectedClassifiers.contains(classifier));
			foundClassifiers.add(classifier);
		}

		for (String expected : expectedClassifiers) {
			Assert.assertTrue("Classfier <" + expected + "> missing from diagram <"+diagram.getName()+">",
					foundClassifiers.contains(expected));
		}

	}
	
	@Test
	public void testDiagramObjectTypes() {
		EA_Collection<EA_DiagramObject> diagramObjects = diagram.DiagramObjects;

		Assert.assertEquals("Number of objects on diagram error.", 3, diagramObjects.count);

		String expectedType = "Component";
		
		for (EA_DiagramObject object : diagramObjects) {

			String foundType = object.getElement().getType();
			Assert.assertEquals("Unexpected type", expectedType, foundType); 
		}


	}

}
