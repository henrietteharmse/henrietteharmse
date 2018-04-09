package org.graphdb.rdf4j.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class CreateLocalRepository {
	private static Logger logger = LoggerFactory.getLogger(CreateLocalRepository.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	public static void main(String[] args) {
	try {		
		Path path = Paths.get(".").toAbsolutePath().normalize();
		String strRepositoryConfig = path.toFile().getAbsolutePath() + "/src/main/resources/repo-defaults.ttl";
		
	// Instantiate a local repository manager and initialize it
		RepositoryManager repositoryManager = new LocalRepositoryManager(new File("."));
		repositoryManager.initialize();

		// Instantiate a repository graph model
		TreeModel graph = new TreeModel();

		// Read repository configuration file
		InputStream config = new FileInputStream(strRepositoryConfig);
		RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
		rdfParser.setRDFHandler(new StatementCollector(graph));
		rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
		config.close();

		// Retrieve the repository node as a resource
		Resource repositoryNode =  Models.subject(graph
		    .filter(null, RDF.TYPE, RepositoryConfigSchema.REPOSITORY))
		    .orElseThrow(() -> new RuntimeException(
		        "Oops, no <http://www.openrdf.org/config/repository#> subject found!"));

		
		// Create a repository configuration object and add it to the repositoryManager
		RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
		repositoryManager.addRepositoryConfig(repositoryConfig);


		// Get the repository from repository manager, note the repository id set in configuration .ttl file
		Repository repository = repositoryManager.getRepository("graphdb-repo");

		// Open a connection to this repository
		RepositoryConnection repositoryConnection = repository.getConnection();

		// ... use the repository

		// Shutdown connection, repository and manager
		repositoryConnection.close();
		repository.shutDown();
		repositoryManager.shutDown();		
			
		} catch (Throwable t) {
			logger.error(WTF_MARKER, t.getMessage(), t);
		}		
	}
}
