package org.owlapi.tutorial;


import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class ElkReasoner {
    private static Logger logger = LoggerFactory.getLogger(ElkReasoner.class);

    public static OWLOntology loadOntology(String ontologyToLoad)
            throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        IRI ontologyIRIToLoad = IRI.create(ontologyToLoad);
        long startTime = System.currentTimeMillis();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyIRIToLoad);
        long endTime = System.currentTimeMillis();
        logger.trace("Time to load ontology = " + (endTime - startTime)/1000 + " seconds.");

        OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
        IRI objectPropertyIRI = IRI.create("http://purl.obolibrary.org/obo/RO_0002333");
        OWLObjectProperty objectProperty = owlDataFactory.getOWLObjectProperty(objectPropertyIRI);
        EntitySearcher.getSuperProperties(objectProperty, ontology.importsClosure())
                .forEach(superObjectPropertyExpression -> {logger.debug(superObjectPropertyExpression.toString());});
        return ontology;
    }

    public static OWLReasoner configureELK(OWLOntology ontology) {

        OWLReasonerFactory factory = new ElkReasonerFactory();

        logger.trace("Memory used before precomputeInferences = " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
        long startTime = System.currentTimeMillis();

        OWLReasoner reasoner = factory.createReasoner(ontology);
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.OBJECT_PROPERTY_HIERARCHY,
                InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.DISJOINT_CLASSES);

        long endTime = System.currentTimeMillis();
        logger.trace("Memory used after precomputeInferences = " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
        logger.trace("Time to precomputeInferences = " + (endTime - startTime)/1000 + " seconds.");

        return reasoner;
    }

    public static void doConsistencyCheck(OWLReasoner reasoner) {
        long startTime = System.currentTimeMillis();
        boolean isConsistent =  reasoner.isConsistent();
        long endTime = System.currentTimeMillis();
        logger.trace("Memory used after running reasoner = " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
        logger.trace("Time to do consistency check = " + (endTime - startTime)/1000 + " seconds.");
        logger.trace("Ontology is consistent = " + isConsistent);
    }

    public static void doUnstatisfiabilityCheck(OWLReasoner reasoner){
        long startTime = System.currentTimeMillis();
        Stream<OWLClass> unsatisfiableClassesStream = reasoner.unsatisfiableClasses();
        logger.trace("Number of unsatisfiable classes found = " + unsatisfiableClassesStream.count());
        long endTime = System.currentTimeMillis();
        logger.trace("Memory used after running checking unsatisfiable classes = " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
        logger.trace("Time to check unsatisfiable classes = " + (endTime - startTime)/1000 + " seconds.");
    }

    public static void main(String[] args) {
        try {
            OWLOntology ontology = loadOntology(args[0]);
            OWLReasoner reasoner = configureELK(ontology);

            doConsistencyCheck(reasoner);
            doUnstatisfiabilityCheck(reasoner);

        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }
}
