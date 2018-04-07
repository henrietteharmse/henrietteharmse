package org.jena.tdb.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.dboe.base.file.Location;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


/**
 * This example is expected to fail due to 2 datasets pointing to the same underlying RDF store. It will thow an 
 * org.apache.jena.dboe.transaction.txn.TransactionException with error message:
 * Currently in an active transaction.
 * 
 * @author Henriette Harmse
 *
 */
public class TDBMultipleDatasets1LocationFailure {
  private static Logger logger = LoggerFactory.getLogger(TDBMultipleDatasets1LocationFailure.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  private static String strQuery1;
  private static String strQuery2;
  
  static {
    strQuery1 = 
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
    
    strQuery2 = 
        "INSERT DATA {"
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthDate> \"1906-12-9\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthPlace> <http://dbpedia.org/resource/New_York_City> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathDate> \"1992-1-1\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathPlace> <http://dbpedia.org/resource/Arlington_County,_Virginia> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://purl.org/dc/terms/description> \"American computer scientist and United States Navy officer.\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/gender> \"male\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/givenName> \"Alan\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/name> \"Alan Turing\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/surname> \"Turing\" ."        
         + "}";    
  }   
  
  public static void main(String[] args) {
    try {   
      Path path = Paths.get(".").toAbsolutePath().normalize();      
      String dbDir = path.toFile().getAbsolutePath() + "/db/"; 
      
      Location location = Location.create(dbDir);
      
      Dataset dataset1 = TDB2Factory.connectDataset(location);
      Dataset dataset2 = TDB2Factory.connectDataset(location);
      logger.debug("Dataset1 = " + dataset1.asDatasetGraph().hashCode());
      logger.debug("Dataset2 = " + dataset2.asDatasetGraph().hashCode());
      logger.debug("Dataset1.isBackedByTDB = " + TDB2Factory.isBackedByTDB(dataset1));
      
      dataset1.begin(ReadWrite.WRITE);
        dataset2.begin(ReadWrite.WRITE);
      UpdateRequest updateRequest1 = UpdateFactory.create(strQuery1);
      UpdateRequest updateRequest2 = UpdateFactory.create(strQuery2);
      UpdateProcessor updateProcessor1 = UpdateExecutionFactory.create(updateRequest1, dataset1);
      UpdateProcessor updateProcessor2 = UpdateExecutionFactory.create(updateRequest2, dataset2);
      
      updateProcessor1.execute();
      updateProcessor2.execute();
      dataset1.commit();
      dataset2.commit();
      dataset1.close();
      dataset2.close();
      
      dataset1.close();
      dataset2.close();
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
