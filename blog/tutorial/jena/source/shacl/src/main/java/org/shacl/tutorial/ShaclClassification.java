package org.shacl.tutorial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.spin.util.JenaUtil;

public class ShaclClassification {
  private static Logger logger = LoggerFactory.getLogger(ShaclValidation.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");

  public static void main(String[] args) {
    try {   
      Path path = Paths.get(".").toAbsolutePath().normalize();
   
      String data = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/bakery.ttl";
      String shape = "file:" + path.toFile().getAbsolutePath() + "/src/main/resources/bakeryRules.ttl";
      
      Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
      Model dataModel = JenaUtil.createDefaultModel();
      dataModel.read(data);
      InfModel infModel = ModelFactory.createInfModel(reasoner, dataModel);
      ValidityReport validity = infModel.validate();
      if (!validity.isValid()) {
        logger.trace("Conflicts");
        for (Iterator i = validity.getReports(); i.hasNext(); ) {
          logger.trace(" - " + i.next());
        }        
      } else {
        Model shapeModel = JenaUtil.createDefaultModel();
        shapeModel.read(shape);
        Model inferenceModel = JenaUtil.createDefaultModel(); 
        inferenceModel = RuleUtil.executeRules(infModel, shapeModel, inferenceModel, null);        
        String inferences = path.toFile().getAbsolutePath() + "/src/main/resources/inferences.ttl";
        File inferencesFile = new File(inferences);
        inferencesFile.createNewFile();     
        OutputStream reportOutputStream = new FileOutputStream(inferencesFile);        
        RDFDataMgr.write(reportOutputStream, inferenceModel, RDFFormat.TTL);
      }
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }
}

