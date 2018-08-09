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
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.profiles.violations.UseOfNonSimplePropertyInCardinalityRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class OWL2DLProfileViolations {
	private static Logger logger = LoggerFactory.getLogger(OWL2DLProfileViolations.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	public static void main(String[] args) {
  	try {		
      Path path = Paths.get(".").toAbsolutePath().normalize();
      String ontologyFile = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/SimpleRoles.ttl";  	  
      IRI ontologyFileIRI = IRI.create(ontologyFile);
      
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFileIRI);
      
      OWL2DLProfile profile = new OWL2DLProfile();
      OWLProfileReport report = profile.checkOntology(ontology);
      
      for(OWLProfileViolation violation :report.getViolations()) {
         System.out.println("Expression: " + violation.getExpression());
         System.out.println("Axiom: ");
         violation.getAxiom()
           .signature()
           .forEach(System.out::println);
         
         System.out.println("Violation class = " + violation.getClass().getSimpleName());
//         if (violation instanceof UseOfNonSimplePropertyInCardinalityRestriction) {
//           UseOfNonSimplePropertyInCardinalityRestriction currViolation = 
//               (UseOfNonSimplePropertyInCardinalityRestriction) violation;
//         }
           
      }
  	} catch (Throwable t) {
  		logger.error(WTF_MARKER, t.getMessage(), t);
  	}		
  }
}
