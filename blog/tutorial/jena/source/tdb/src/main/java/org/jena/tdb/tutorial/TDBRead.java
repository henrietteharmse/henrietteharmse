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

public class TDBRead {
  private static Logger logger = LoggerFactory.getLogger(TDBRead.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  private static String strQuery;
  
  static {
    strQuery = 
        "INSERT DATA {"
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthDate> \"1906-12-9\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthPlace> <http://dbpedia.org/resource/New_York_City> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathDate> \"1992-1-1\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathPlace> <http://dbpedia.org/resource/Arlington_County,_Virginia> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://purl.org/dc/terms/description> \"American computer scientist and United States Navy officer.\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/gender> \"female\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/givenName> \"Grace\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/name> \"Grace Hopper\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/surname> \"Hopper\" ."        
         + "}";
  }   
  public static void main(String[] args) {
    try {   
      Path path = Paths.get(".").toAbsolutePath().normalize();      
      String dbDir = path.toFile().getAbsolutePath() + "/db/"; 
      
      Location location = Location.create(dbDir);
      
      Dataset dataset = TDB2Factory.connectDataset(location);
  
      dataset.begin(ReadWrite.READ);
      QueryExecution qe = QueryExecutionFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o .}", dataset);
      for (ResultSet results = qe.execSelect(); results.hasNext();) {
        QuerySolution qs = results.next();
        String strValue = qs.get("?o").toString();
        logger.trace("value = " + strValue);
      }

      dataset.close();
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
