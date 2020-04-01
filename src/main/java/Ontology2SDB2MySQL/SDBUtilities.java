package Ontology2SDB2MySQL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
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
	private static String jdbcURL = null;
	private static String dbusername = null;
	private static String dbpassword = null;
	private static String jdbcDriver = null;
	
	public SDBUtilities() {
		// TODO Auto-generated constructor stub
	}
	
	// Reads the configuration file and updates JDBC variables 
	public static void JDBCinit() throws Exception{
		FileInputStream fis = null;
		try {
			 fis = new FileInputStream("dbconfig.prop");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties property = new Properties();
		property.load(fis);
		jdbcURL = (String)property.get("SDB_URL");
		dbusername = (String)property.get("SDB_USERNAME");
		dbpassword = (String)property.get("SDB_PASSWORD");
		setJdbcDriver((String)property.get("JDBC_DRIVER"));
	}
	
	/*
	 * Function takes in an OWL RDF/XML file, a namespace and the main prefix
	 * If connects the database for the first time, creates necessary changes in the database i.e. tables
	 * Creates a SDB Store and parses the OWL into relational model 
	 */
	public static void ont2SDB2SQL(String filename, String namespace, String rdfprefix) {
		
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
		model.setNsPrefix(namespace,rdfprefix);
		model.read(filename);
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
	public static String Inserttriples(String filename, String namespace, String rdfprefix,String sub,String pred,String obj) {

		StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
		JDBC.loadDriverMySQL();
		// Connecting SDB to JDBC
		SDBConnection connection = new SDBConnection(jdbcURL, dbusername, dbpassword);
		// Each table/database in SDB is considered as a store
		Store store = SDBFactory.connectStore(connection, storeDesc);
		// These models are provided by Jena. Use GraphDB if you want to store graphical data in relational format
		Model model = SDBFactory.connectDefaultModel(store);
		model.setNsPrefix(namespace,rdfprefix);
		model.read(filename);
		//create new triples
		org.apache.jena.rdf.model.Resource subject = model.createResource(rdfprefix+sub);
		Property predicate = model.createProperty(rdfprefix+pred);
		org.apache.jena.rdf.model.Resource object = model.createResource(rdfprefix+obj);
		//add triples in data base
		model.add(subject,predicate,object);
		model.commit();
		store.close();
		connection.close();
		return "done";
	}

	public static String getJdbcDriver() {
		return jdbcDriver;
	}

	public static void setJdbcDriver(String jdbcDriver) {
		SDBUtilities.jdbcDriver = jdbcDriver;
	}
}
