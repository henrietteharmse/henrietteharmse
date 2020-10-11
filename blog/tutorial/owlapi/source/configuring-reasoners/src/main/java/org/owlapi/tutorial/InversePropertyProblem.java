package org.owlapi.tutorial;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

public class InversePropertyProblem {
    public static void main(String[] args){
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            IRI ontologyIRIToLoad = IRI.create(args[0]);
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyIRIToLoad);
            OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
            IRI objectPropertyIRI = IRI.create("http://henrietteharmse.com/tutorial/owlapi/enabled_by");
//            IRI objectPropertyIRI = IRI.create("http://purl.obolibrary.org/obo/RO_0002333");
            OWLObjectProperty objectProperty = owlDataFactory.getOWLObjectProperty(objectPropertyIRI);
            EntitySearcher.getSuperProperties(objectProperty, ontology.importsClosure())
                    .forEach(superObjectPropertyExpression -> {
                        System.out.println(superObjectPropertyExpression.toString());
                    });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
