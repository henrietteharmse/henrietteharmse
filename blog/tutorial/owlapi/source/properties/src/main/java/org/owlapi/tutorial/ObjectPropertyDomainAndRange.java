package org.owlapi.tutorial;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ObjectPropertyDomainAndRange {
  private static Logger logger = LoggerFactory.getLogger(ObjectPropertyDomains.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  private static String ontologyIRI = "http://henrietteharmse.com/tutorial/objectproperties";
  
  public static void main(String[] args) {
    try {   
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      OWLDataFactory dataFactory = manager.getOWLDataFactory(); 
      
      IRI relatedClassPropertyIRI = IRI.create(ontologyIRI + "#relatedClass");
      IRI aClassIRI = IRI.create(ontologyIRI + "#A");
      IRI bClassIRI = IRI.create(ontologyIRI + "#B");
      
      OWLObjectProperty relatedClassProperty = dataFactory.getOWLObjectProperty(relatedClassPropertyIRI);
      
      OWLClass aClass = dataFactory.getOWLClass(aClassIRI);
      OWLClass bClass = dataFactory.getOWLClass(bClassIRI);
      
      dataFactory.getOWLObjectPropertyDomainAxiom(relatedClassProperty, aClass);
      dataFactory.getOWLObjectPropertyRangeAxiom(relatedClassProperty, bClass);
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }
}
