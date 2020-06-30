package org.owlapi.tutorial;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class EntailmentCheck {
	private static Logger logger = LoggerFactory.getLogger(EntailmentCheck.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	private static String ontologyIRI = "https://henrietteharmse.com/tutorial/entailmentchecks";
	
	public static void main(String[] args) {
  	try {
  		String ontologyFile = "file:" + System.getProperty("user.dir") + "/src/main/resources/EntailmentsCheck.owl";
      	IRI ontologyFileIRI = IRI.create(ontologyFile);

      	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
  	  	OWLDataFactory dataFactory = manager.getOWLDataFactory();
  	  	OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFileIRI);
		OWLReasonerFactory reasonerFactory = new ReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

		OWLClass classA = dataFactory.getOWLClass(ontologyIRI + "#A");
		OWLClass classD = dataFactory.getOWLClass(ontologyIRI + "#D");
		OWLAxiom dSubclassOfA = dataFactory.getOWLSubClassOfAxiom(classD, classA);

		reasoner.isConsistent();
		System.out.println("D subclass of A = " + reasoner.isEntailed(dSubclassOfA));

  	} catch (Throwable t) {
  		logger.error(WTF_MARKER, t.getMessage(), t);
  	}		
  }
}
