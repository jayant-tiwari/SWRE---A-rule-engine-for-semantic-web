import org.apache.jena.query.ResultSet;

import Ontology2SDB2MySQL.*;
public class InsertTriples {
	public static void main(String args[]) throws Exception {
		String filename = "/home/mohit/git/SWRE---A-rule-engine-for-semantic-web/Movie-Ontology.owl";
		String namespace = "SWRE";
		String prefix = "http://www.iiitb.ac.in/MovieOntology#";
		String subject="mohit";
		String predicate="Acted_In";
		String object="Don";
		SDBUtilities.JDBCinit();
		String rs =SDBUtilities.Inserttriples(filename, namespace, prefix,subject,predicate,object);
		
		System.out.println(rs);
	}
}
