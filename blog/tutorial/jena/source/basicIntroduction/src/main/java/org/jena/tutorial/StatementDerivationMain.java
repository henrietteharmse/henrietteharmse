package org.jena.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import org.apache.jena.sparql.function.library.leviathan.log;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class StatementDerivationMain {
  private static Logger logger = LoggerFactory.getLogger(BaseClassesMain.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  
  public static void printStatements(InfModel m, Resource s, Property p, Resource o) {
    for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
        Statement stmt = i.nextStatement();
        logger.trace("Statememt = " + PrintUtil.print(stmt));
        for (Iterator<Derivation> derivationsIter = m.getDerivation(stmt); derivationsIter.hasNext();) {
          Derivation derivation = (Derivation) derivationsIter.next();
          logger.trace("Derivation = " +  derivation);
        }        
    }
  }
  
  public static void main(String[] args) {
    try {   
      Path path = Paths.get(".").toAbsolutePath().normalize();
      String dataFile = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/person.ttl";
      String schemaFile = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/personSchema.ttl";
      
      Model schema = FileManager.get().loadModel(dataFile);
      Model data = FileManager.get().loadModel(schemaFile);
      
      Resource config = ModelFactory.createDefaultModel()
          .createResource()
          .addProperty(ReasonerVocabulary.PROPderivationLogging, "true");
      Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);

      InfModel infModel = ModelFactory.createInfModel(reasoner, schema, data);
      Resource colin = infModel.getResource("http://example.com/ns#Colin");
      
      printStatements(infModel, colin, RDF.type, null);
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }
}
