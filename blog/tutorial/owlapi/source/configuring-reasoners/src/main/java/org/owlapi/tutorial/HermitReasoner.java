package org.owlapi.tutorial;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class HermitReasoner {
    private static Logger logger = LoggerFactory.getLogger(HermitReasoner.class);

    public static OWLOntology loadOntology(String ontologyToLoad)
            throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        IRI ontologyIRIToLoad = IRI.create(ontologyToLoad);
        long startTime = System.currentTimeMillis();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyIRIToLoad);
        long endTime = System.currentTimeMillis();
        logger.trace("Time to load ontology = " + (endTime - startTime)/1000 + " seconds.");
        return ontology;
    }

    public static OWLReasoner configureHermit(OWLOntology ontology) {
        Configuration configuration = new Configuration();
        configuration.existentialStrategyType = Configuration.ExistentialStrategyType.INDIVIDUAL_REUSE;
        configuration.blockingStrategyType = Configuration.BlockingStrategyType.ANCESTOR;
        configuration.freshEntityPolicy = FreshEntityPolicy.DISALLOW;

        logger.trace("Memory used before precomputeInferences = " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
        long startTime = System.currentTimeMillis();

        OWLReasoner reasoner = new Reasoner(configuration, ontology);
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
        long endTime = System.currentTimeMillis();
        logger.trace("Memory used after running checking unsatisfiable classes = " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
        logger.trace("Time to check unsatisfiable classes = " + (endTime - startTime)/1000 + " seconds.");
    }

    public static void main(String[] args) {
        try {
            OWLOntology ontology = loadOntology(args[0]);
            OWLReasoner reasoner = configureHermit(ontology);
            doConsistencyCheck(reasoner);
            doUnstatisfiabilityCheck(reasoner);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }
}
