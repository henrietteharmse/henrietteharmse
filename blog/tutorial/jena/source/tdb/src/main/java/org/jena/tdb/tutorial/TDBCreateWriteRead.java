  package org.jena.tdb.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.dboe.base.file.Location;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class TDBCreateWriteRead {
  private static Logger logger = LoggerFactory.getLogger(TDBCreateWriteRead.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
    
  public static void main(String[] args) {
    try {
      // Create dataset
      Path path = Paths.get(".").toAbsolutePath().normalize();      
      String dbDir = path.toFile().getAbsolutePath() + "/db/"; 
      Location location = Location.create(dbDir);      
      Dataset dataset = TDB2Factory.connectDataset(location);
      
      // Create transaction for writing
      dataset.begin(ReadWrite.WRITE);
      UpdateRequest updateRequest = UpdateFactory.create("INSERT DATA {<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/name> \"Grace Hopper\" .}");
      UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
      updateProcessor.execute();
      dataset.commit();
      
      // create transaction for reading
      dataset.begin(ReadWrite.READ);
      QueryExecution qe = QueryExecutionFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o .}", dataset);
      for (ResultSet results = qe.execSelect(); results.hasNext();) {
        QuerySolution qs = results.next();
        String strValue = qs.get("?o").toString();
        logger.trace("value = " + strValue);
      }
      
      // Releasing dataset resources
      dataset.close();
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
