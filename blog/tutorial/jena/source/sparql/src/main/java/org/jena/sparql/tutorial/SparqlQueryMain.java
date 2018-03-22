package org.jena.sparql.tutorial;



import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SparqlQueryMain {
	private static Logger logger = LoggerFactory.getLogger(SparqlQueryMain.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	private static final String SPARQL_ENDPOINT = "http://192.168.1.6:7200/repositories/DBPediaPersonData";
	
	public static void main(String[] args) {
		try {		
		  RDFConnection conn = RDFConnectionFactory.connect(SPARQL_ENDPOINT, SPARQL_ENDPOINT, SPARQL_ENDPOINT);
		  
		  StringBuilder stringBuilder = new StringBuilder();
		  stringBuilder.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
		  stringBuilder.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
		  stringBuilder.append("PREFIX dbo: <http://dbpedia.org/ontology/>");
		  stringBuilder.append("PREFIX dbr: <http://dbpedia.org/resource/>"); 
      stringBuilder.append("PREFIX foaf:<http://xmlns.com/foaf/0.1/>");
	    stringBuilder.append("PREFIX purl: <http://purl.org/dc/terms/>");
	    stringBuilder.append("SELECT DISTINCT ?c1 WHERE {");    
	    stringBuilder.append("?c1 rdf:type dbo:Person .} LIMIT 1");		  	        
	        
		  Query query = QueryFactory.create(stringBuilder.toString());
		  conn.queryResultSet(query, ResultSetFormatter::out);
		} catch (Throwable t) {
			logger.error(WTF_MARKER, t.getMessage(), t);
		}		
	}
}
