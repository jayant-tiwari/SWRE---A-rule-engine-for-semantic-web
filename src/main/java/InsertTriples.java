import org.apache.jena.query.ResultSet;

import Ontology2SDB2MySQL.*;
public class InsertTriples {
	public static void main(String args[]) throws Exception {
		String filename = "/home/mohit/git/SWRE---A-rule-engine-for-semantic-web/University.owl";
		String namespace = "SWRE";
		String prefix = "http://www.iiitb.org/university#";
		String subject="mohit";
		String predicate="studies";
		String object="PS202";
		SDBUtilities.JDBCinit();
		String rs =SDBUtilities.Inserttriples(filename, namespace, prefix,subject,predicate,object);
		
		System.out.println(rs);
	}
}
