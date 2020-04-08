import java.util.ArrayList;

import org.apache.jena.query.ResultSet;

import SWRE.Ontology2SDB2MySQL.*;
import SWRE.ruleGenerator.RuleBox;

public class Test {
	public static void main(String args[]) throws Exception {
//		String filename = "University.owl";
//		String namespace = "SWRE";
//		String prefix = "http://www.iiitb.org/university#";
////
//		SDBUtilities.JDBCinit();
////		// Create Database
//		SDBUtilities.ont2SDB2SQL(filename, namespace, prefix);
//		String query =  "PREFIX foo:<http://www.iiitb.org/university#>" +
//						"SELECT *  WHERE{?s foo:studies foo:PS202}";
//		ResultSet rs = SDBUtilities.SDBQuery(query);
//		System.out.print(rs);
		String[] ant = new String[7];
		  ant[0]="teacher";
		  ant[1]="teaches";
		  ant[2]="course";
		  ant[3]="AND";
		  ant[4]="student";
		  ant[5]="enrolledIn";
		  ant[6]="course";
		  String[] con = new String[3];
		  con[0]="student";
	 	  con[1]="isStudentOf";
		  con[2]="teacher";
		RuleBox obj = new RuleBox();
		obj.init();
		obj.addRule(ant,con);
//		ArrayList<ArrayList<String>> arr = obj.getRules();
//		int l = arr.size();
//		System.out.println();
//		for(int i=0;i<l;i++) {
//			ArrayList<String> temp = arr.get(i);
//			int l1 = temp.size();
//			for(int j=0;j<l1;j++)
//				System.out.print(temp.get(j)+" ");
//			System.out.println();
//		}
		System.out.println("Done");
	}
}
