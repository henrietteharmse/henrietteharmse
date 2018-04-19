package org.jena.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class BaseClassesMain {
	private static Logger logger = LoggerFactory.getLogger(BaseClassesMain.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	public static void main(String[] args) {
		try {		
			Path path = Paths.get(".").toAbsolutePath().normalize();
			String ontologyFile = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/BaseClasses.owl";
			
			OntModel defaultModel = ModelFactory.createOntologyModel();
			defaultModel.read(ontologyFile);
			logger.debug("Model = " + defaultModel);
			
			for (ExtendedIterator<OntClass> i =  defaultModel.listHierarchyRootClasses(); i.hasNext();) {
				OntClass ontClass = i.next();
				logger.debug("Base class = " + ontClass);
			}
		} catch (Throwable t) {
			logger.error(WTF_MARKER, t.getMessage(), t);
		}		
	}
}
