package org.jena.rules.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.jena.dboe.base.file.Location;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.Builtin;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.MapBuiltinRegistry;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class CustomRuleMain {
  private static Logger logger = LoggerFactory.getLogger(CustomRuleMain.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
    
  public static void main(String[] args) {
    try {
      Path path = Paths.get(".").toAbsolutePath().normalize();
      
      // Load RDF data
      String data = path.toFile().getAbsolutePath() + "/src/main/resources/data1.ttl";
      Model model = ModelFactory.createDefaultModel();
      model.read(data);
      
      // Load rules
      String rules = path.toFile().getAbsolutePath() + "/src/main/resources/student1.rules";
      Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL(rules));
     
      InfModel infModel = ModelFactory.createInfModel(reasoner, model);  
      infModel.validate();
      for (StmtIterator i = infModel.listStatements(); i.hasNext(); ) {
        Statement stmt = i.nextStatement();
        logger.trace("Statememt = " + PrintUtil.print(stmt));
      }      
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
