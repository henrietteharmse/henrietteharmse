package org.owlapi.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ObjectPropertyDomains {
	private static Logger logger = LoggerFactory.getLogger(ObjectPropertyDomains.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	private static String ontologyIRI = "http://www.semanticweb.org/tutorial/ontologies/2018/3/untitled-ontology-763";
	
	public static void main(String[] args) {
  	try {		
      Path path = Paths.get(".").toAbsolutePath().normalize();
      String ontologyFile = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/PropertyDomains.owl";  	  
      IRI ontologyFileIRI = IRI.create(ontologyFile);

      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
  	  OWLDataFactory dataFactory = manager.getOWLDataFactory();
  	  OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFileIRI);
  	  
  	  IRI lastNamePropertyIRI = IRI.create(ontologyIRI + "#hasLastName");
  	  OWLDataProperty lastNameProperty = dataFactory.getOWLDataProperty(lastNamePropertyIRI);
  	  List<OWLClassExpression> domainClasses = 
  	      ontology
  	      .dataPropertyDomainAxioms(lastNameProperty)
  	      .map(OWLDataPropertyDomainAxiom::getDomain)
  	      .collect(Collectors.toList());
  	  
  	  for (OWLClassExpression owlClass : domainClasses) {
        logger.trace("Domain class = " + owlClass);
      } 
  	  
//  	  IRI relatedClassPropertyIRI = IRI.create(ontologyIRI + "#relatedClass");
//  	  OWLObjectProperty relatedClass = dataFa
  	} catch (Throwable t) {
  		logger.error(WTF_MARKER, t.getMessage(), t);
  	}		
  }
}
