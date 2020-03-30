import org.apache.jena.query.ResultSet;

import Ontology2SDB2MySQL.*;

public class Test {
	public static void main(String args[]) throws Exception {
		String filename = "/home/parthendo/Desktop/sample.owl";
		String namespace = "SWRE";
		String prefix = "http://www.iiitb.ac.in/MovieOntology#";

		SDBUtilities.JDBCinit();
		//SDBUtilities.ont2SDB2SQL(filename, namespace, prefix);
		String query = "PREFIX foo:<http://www.iiitb.ac.in/MovieOntology#>" + 
		"SELECT distinct ?Actors  {?Actors foo:Acted_In ?Movie . ?Movie foo:hasRating foo:Low}";
		ResultSet rs = SDBUtilities.SDBQuery(query);
		System.out.print(rs);
		System.out.println("Done");
	}
}
