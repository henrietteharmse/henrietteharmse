package org.graphdb.rdf4j.tutorial;

import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SimpleInsertQueryExample {
  private static Logger logger = LoggerFactory.getLogger(SimpleInsertQueryExample.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  // GraphDB 
  private static final String GRAPHDB_SERVER = "http://localhost:7200/";
  private static final String REPOSITORY_ID = "PersonData";


  private static String strInsert;
  private static String strQuery;
  
  static {
    strInsert = 
        "INSERT DATA {"
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthDate> \"1906-12-09\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthPlace> <http://dbpedia.org/resource/New_York_City> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathDate> \"1992-01-01\"^^<http://www.w3.org/2001/XMLSchema#date> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathPlace> <http://dbpedia.org/resource/Arlington_County,_Virginia> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://purl.org/dc/terms/description> \"American computer scientist and United States Navy officer.\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/gender> \"female\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/givenName> \"Grace\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/name> \"Grace Hopper\" ."
         + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/surname> \"Hopper\" ."        
         + "}";
    
    strQuery = 
        "SELECT ?name FROM DEFAULT WHERE {" +
        "?s <http://xmlns.com/foaf/0.1/name> ?name .}";
  }  
  
  private static RepositoryConnection getRepositoryConnection() {
    Repository repository = new HTTPRepository(GRAPHDB_SERVER, REPOSITORY_ID);
    repository.initialize();
    RepositoryConnection repositoryConnection = repository.getConnection();
    return repositoryConnection;
  }
  
  private static void insert(RepositoryConnection repositoryConnection) {
    repositoryConnection.begin();
    
    Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
    updateOperation.execute();
    
    try {
      repositoryConnection.commit();
    } catch (Exception e) {
      if (repositoryConnection.isActive())
        repositoryConnection.rollback();
    }
  }

  private static void query(RepositoryConnection repositoryConnection) {
    TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, strQuery);
    TupleQueryResult result = null;
    try {
      result = tupleQuery.evaluate();
      while (result.hasNext()) {
        BindingSet bindingSet = result.next();

        SimpleLiteral name = (SimpleLiteral)bindingSet.getValue("name");
        logger.trace("name = " + name.stringValue());
      }
    }
    catch (QueryEvaluationException qee) {
      logger.error(WTF_MARKER, qee.getStackTrace().toString(), qee);
    } finally {
      result.close();
    }    
  }  
  
  public static void main(String[] args) {
    RepositoryConnection repositoryConnection = null;
    try {   
      repositoryConnection = getRepositoryConnection();
      
      insert(repositoryConnection);
      query(repositoryConnection);      
      
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    } finally {
      repositoryConnection.close();
    }
  }  
}
