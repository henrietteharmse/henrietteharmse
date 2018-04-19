package org.jena.ontology.tutorial;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class QueryOntology {
  private static Logger logger = LoggerFactory.getLogger(QueryOntology.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  
  
  public static void main(String[] args) {
    try {   
      Path path = Paths.get(".").toAbsolutePath().normalize();
      
      String ontology = path.toFile().getAbsolutePath() + "/src/main/resources/ontology.rdf";
      
      Model model = ModelFactory.createOntologyModel();
      model.read(ontology, "RDF/XML");
      String queryString = 
              "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
              "PREFIX ns: <http://review-analyzer.local/ontologies/reviews_2.owl#>"+
              "select *\r\n" + 
              "where {\r\n" + 
              "  ?Comment ns:review ?review .\r\n" + 
              "  ?Comment ns:raitng ?raitng .\r\n" + 
              "}";
      Query query = QueryFactory.create(queryString);
      QueryExecution qexec = QueryExecutionFactory.create(query, model);
      ResultSet results = qexec.execSelect();
      List<String> varNames = results.getResultVars();
      while (results.hasNext()) {
          QuerySolution soln = results.nextSolution();
          Literal name = soln.getLiteral("?review");
          System.out.println(name);
      }
    } catch (Throwable t) {
      logger.error(WTF_MARKER, t.getMessage(), t);
    }   
  }  
}
