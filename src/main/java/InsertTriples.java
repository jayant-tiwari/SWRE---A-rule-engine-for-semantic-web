import org.apache.jena.query.ResultSet;

import SWRE.Ontology2SDB2MySQL.*;
public class InsertTriples {
	public static void main(String args[]) throws Exception {
		String subject="mohit";
		String predicate="studies";
		String object="PS202";
		SDBUtilities.DBinit();
		String rs =SDBUtilities.insertTriples(subject,predicate,object);
		System.out.println(rs);
	}
}
