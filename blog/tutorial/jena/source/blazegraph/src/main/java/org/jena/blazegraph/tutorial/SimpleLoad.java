package org.jena.blazegraph.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SimpleLoad {
  private static Logger logger = LoggerFactory.getLogger(SimpleLoad.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  private static final String SPARQL_ENDPOINT = "http://localhost:8080/blazegraph/namespace/PersonData2/sparql/PersonData2/sparql";
  
  public static void main(String[] args) {
    try {   
      Path path = Paths.get(".").toAbsolutePath().normalize();
      
      String data = path.toFile().getAbsolutePath() + "/src/main/resources/PersonData.ttl";
      
      RDFConnection conn = RDFConnectionFactory.connect(SPARQL_ENDPOINT);
      conn.load(data);
      conn.close();
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
