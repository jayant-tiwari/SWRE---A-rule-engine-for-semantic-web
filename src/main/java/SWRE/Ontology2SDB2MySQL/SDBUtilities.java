package SWRE.Ontology2SDB2MySQL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.sdb.SDBFactory;
import org.apache.jena.sdb.Store;
import org.apache.jena.sdb.StoreDesc;
import org.apache.jena.sdb.sql.JDBC;
import org.apache.jena.sdb.sql.SDBConnection;
import org.apache.jena.sdb.store.DatabaseType;
import org.apache.jena.sdb.store.DatasetStore;
import org.apache.jena.sdb.store.LayoutType;

/*
 * Description:	Enables various utilities required to import an RDF/XML ontology to a relational database. This can also be used as a relational
 * 				triple store.
 * 				Semantic Web ontology is stored in MySQL using SDB Jena which is abstracted over JDBC. Various model implementation e.g. GraphDB
 * 				can be used to represent the ontology. 
 * 				SPARQL can be used to query the data.
 * 				All the code snippets are extracted from https://jena.apache.org/documentation/sdb/
 * 				Further examples taken into consideration are https://github.com/apache/jena/tree/master/jena-sdb/src-examples/sdb/examples
 */
public class SDBUtilities {
	
	// JDBC Configuration Variables
	private static String jdbcURL;
	private static String dbusername;
	private static String dbpassword;
	private static String jdbcDriver;
	private static String ontology;
	private static String ontologyPrefix;
	private static String ontologyNamespace;
	
	public SDBUtilities() {
		// TODO Auto-generated constructor stub
		jdbcURL = null;
		dbusername = null;
		dbpassword = null;
		jdbcDriver = null;
		ontology = null;
		ontologyPrefix = null;
		ontologyNamespace = null;
	}
	
	// Reads the configuration file and updates JDBC variables 
	public static void DBinit() throws Exception{
		InputStream inputStream = SDBUtilities.class.getClassLoader().getResourceAsStream("dbconfig.properties");
		Properties property = new Properties();
		property.load(inputStream);
		jdbcURL = (String)property.get("SDB_URL");
		dbusername = (String)property.get("SDB_USERNAME");
		dbpassword = (String)property.get("SDB_PASSWORD");
		setJdbcDriver((String)property.get("JDBC_DRIVER"));
		ontology = property.getProperty("ONTOLOGY");
		ontologyPrefix = property.getProperty("ONTOLOGY_PREFIX");
		ontologyNamespace = property.getProperty("ONTOLOGY_NAMESPACE");
	}

	/*
	 * Function takes in an OWL RDF/XML file, a namespace and the main prefix
	 * If connects the database for the first time, creates necessary changes in the database i.e. tables
	 * Creates a SDB Store and parses the OWL into relational model 
	 */
	public static void ont2SDB2SQL() {
		
		/* Creates a storage description for the MySQL Database, here TripleNodeHash means that each node in the RDF/XML file
		 * will be provided a hash and the triples table will consists all the hashes.
		 * The specified database is MySQL
		 */
		StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
		
		JDBC.loadDriverMySQL();
		// Connecting SDB to JDBC
		SDBConnection connection = new SDBConnection(jdbcURL, dbusername, dbpassword);
		// Each table/database in SDB is considered as a store
		Store store = SDBFactory.connectStore(connection, storeDesc);
		// One time operation. Formats the tables in database namely, Nodes, Triples, Prefix and Quads
		store.getTableFormatter().create();
		// These models are provided by Jena. Use GraphDB if you want to store graphical data in relational format
		Model model = SDBFactory.connectDefaultModel(store);
		model.setNsPrefix(ontologyNamespace,ontologyPrefix);
		model.read(ontology);
		model.commit();
		store.close();
		connection.close();
	}
	
	/*
	 * Kills the database in MySQL 
	 */
	public static void dbstoreKill() {
		StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
		JDBC.loadDriverMySQL();
		SDBConnection connection = new SDBConnection(jdbcURL, dbusername, dbpassword);
		Store store = SDBFactory.connectStore(connection, storeDesc);
		store.getTableFormatter().truncate();
		store.close();
		connection.close();
	}

	/*
	 * Takes a SPARQL Query in the input and returns the respective output
	 */
	
	public static ResultSet SDBQuery(String queryString) {
		
		ResultSet rs = null;
		StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
		JDBC.loadDriverMySQL();
		SDBConnection connection = new SDBConnection(jdbcURL, dbusername, dbpassword);
		Store store = SDBFactory.connectStore(connection, storeDesc);
		Dataset dataset = DatasetStore.create(store);
        Query query = QueryFactory.create(queryString) ;
        try ( QueryExecution qe = QueryExecutionFactory.create(query, dataset) ) {
            rs = qe.execSelect() ;
            ResultSetFormatter.out(rs) ;
        }
		return rs;
	}
	
	public static ArrayList<ArrayList<String>> SDBQuery(String queryString, String consequentSubject, String consequentObject) {
		
	ResultSet rs = null;
	StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
	JDBC.loadDriverMySQL();
	SDBConnection connection = new SDBConnection(jdbcURL, dbusername, dbpassword);
	Store store = SDBFactory.connectStore(connection, storeDesc);
	Dataset dataset = DatasetStore.create(store);
	Query query = QueryFactory.create(queryString) ;
	ArrayList<ArrayList<String> > triple = new ArrayList<ArrayList<String> >();
	try ( QueryExecution qe = QueryExecutionFactory.create(query, dataset) ) {
		rs = qe.execSelect() ;
            
		while(rs.hasNext()) {
			ArrayList<String> temp = new ArrayList<String>();
			QuerySolution q = rs.next();
    			
			temp.add(q.get(consequentSubject).toString());
			temp.add(q.get(consequentObject).toString());
			triple.add(temp);
			}
		}
		store.close();
		connection.close();
		return triple;
	}
	
	/*
	 * This method, creates a new resource for the predicate if the predicate is missing and if not, inserts the triple into the database
	 */
	public static String insertTriples(String subject,String predicate,String object) {

		StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
		JDBC.loadDriverMySQL();
		// Connecting SDB to JDBC
		SDBConnection connection = new SDBConnection(jdbcURL, dbusername, dbpassword);
		// Each table/database in SDB is considered as a store
		Store store = SDBFactory.connectStore(connection, storeDesc);
		// These models are provided by Jena. Use GraphDB if you want to store graphical data in relational format
		Model model = SDBFactory.connectDefaultModel(store);
		model.setNsPrefix(ontologyNamespace,ontologyPrefix);
		model.read(ontology);
		//create new triples
		org.apache.jena.rdf.model.Resource Subject = model.createResource(ontologyPrefix + subject);
		Property Predicate = model.createProperty(ontologyPrefix + predicate);
		org.apache.jena.rdf.model.Resource Object = model.createResource(ontologyPrefix + object);
		//add triples in data base
		model.add(Subject,Predicate,Object);
		model.commit();
		store.close();
		connection.close();
		return "success";
	}

	public static String getJdbcDriver() {
		return jdbcDriver;
	}

	public static void setJdbcDriver(String jdbcDriver) {
		SDBUtilities.jdbcDriver = jdbcDriver;
	}

	public static String getOntologyPrefix(){ return ontologyPrefix; }
	public static String getOntologyNamespace () { return ontologyNamespace; }
	public static String getOntology(){ return ontology; }
}
