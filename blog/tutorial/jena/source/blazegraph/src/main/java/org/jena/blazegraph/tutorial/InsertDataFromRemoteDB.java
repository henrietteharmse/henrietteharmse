package org.jena.blazegraph.tutorial;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class InsertDataFromRemoteDB {
  private static Logger logger = LoggerFactory.getLogger(InsertDataFromRemoteDB.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
 
  
  private static final String SPARQL_ENDPOINT_1 = "http://localhost:8080/blazegraph/namespace/PersonData1/sparql";
  private static final String SPARQL_ENDPOINT_2 = "http://localhost:8080/blazegraph/namespace/PersonData2/sparql";
  
  private static String strQuery;
  
  static {
    strQuery = 
        "INSERT {"
        + "    ?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> ."
        + "    ?person <http://xmlns.com/foaf/0.1/name> ?name ."
        + "    ?person <http://xmlns.com/foaf/0.1/givenName> ?givenName ."
        + "    ?person <http://xmlns.com/foaf/0.1/surname> ?surname ."
        + "    ?person <http://xmlns.com/foaf/0.1/gender> ?gender ."
        + "    ?person <http://purl.org/dc/terms/description> ?desc ."
        + "    ?person <http://dbpedia.org/ontology/birthDate> ?birthDate ."
        + "    ?person <http://dbpedia.org/ontology/birthPlace> ?birthPlace ."
        + "    ?person <http://dbpedia.org/ontology/deathDate> ?deathDate ."
        + "    ?person <http://dbpedia.org/ontology/deathPlace> ?deathPlace .   "
        + "}  WHERE {"
        + "      SELECT ?person ?name ?givenName ?surname ?gender ?desc ?birthDate ?birthPlace ?deathDate ?deathPlace WHERE {"
        + "      SERVICE  "
        + SPARQL_ENDPOINT_1
        + "{ "
        + "      OPTIONAL {?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> .}"
        + "      OPTIONAL {?person <http://xmlns.com/foaf/0.1/name> ?name .}"
        + "      OPTIONAL {?person <http://xmlns.com/foaf/0.1/givenName> ?givenName .}"
        + "      OPTIONAL {?person <http://xmlns.com/foaf/0.1/surname> ?surname .}"
        + "      OPTIONAL {?person <http://xmlns.com/foaf/0.1/gender> ?gender .}"
        + "      OPTIONAL {?person <http://purl.org/dc/terms/description> ?desc .}"
        + "      OPTIONAL {?person <http://dbpedia.org/ontology/birthDate?> ?birthDate .}"
        + "      OPTIONAL {?person <http://dbpedia.org/ontology/birthPlace> ?birthPlace .}"
        + "      OPTIONAL {?person <http://dbpedia.org/ontology/deathDate> ?deathDate .}"
        + "      OPTIONAL {?person <http://dbpedia.org/ontology/deathPlace> ?deathPlace .}"
        + "    }"
        + "   }"
        + "  }";     
  }
  
  public static void main(String[] args) {
    try {   
      logger.trace("Query = " + strQuery);
      UpdateRequest updateRequest = UpdateFactory.create(strQuery);
      
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectionRequestTimeout(-1)
          .setConnectTimeout(-1)
          .setSocketTimeout(-1)
          .build();
      
      CloseableHttpClient client = HttpClientBuilder.create()
          .setDefaultRequestConfig(requestConfig)
          .build();
      
      UpdateProcessor updateProcessor = UpdateExecutionFactory.createRemote(updateRequest, SPARQL_ENDPOINT_2, client);
      updateProcessor.execute();   
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }
}
