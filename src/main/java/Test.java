import java.util.ArrayList;

import SWRE.ruleChaining.Chaining;
import SWRE.ruleGenerator.ImplicitRule;
import org.apache.jena.query.ResultSet;

import SWRE.Ontology2SDB2MySQL.*;
import SWRE.ruleGenerator.RuleBox;

public class Test {
	public static void main(String args[]) throws Exception {
//		SDBUtilities sdbUtilities = new SDBUtilities();
//		sdbUtilities.DBinit();
//		String status = sdbUtilities.ont2SDB2SQL();
//		System.out.println(status);


		//ImplicitRule.createImplicitRule();

		//String query = "SELECT ?Student ?Faculty { ?Faculty <http://www.iiitb.org/university#teaches> ?Course . ?Student <http://www.iiitb.org/university#opts> ?Course }";
//		String query = "SELECT ?Student1 ?Student2 { ?Student1 <http://www.iiitb.org/university#isFriendOf> ?Student2} order by ?Student1";
//		OWLUtilities owlUtilities = new OWLUtilities();
//		ResultSet rs = OWLUtilities.SDBQuery(query);
//		System.out.println(rs);


		RuleBox ruleBox = new RuleBox();
		ruleBox.init(true);
		ArrayList<ArrayList<String>> ruleList = ruleBox.getRules();
		ArrayList<ArrayList<String>> rule = new ArrayList<>();
		for(String a:ruleList.get(0))
			System.out.print(a + " ");
		System.out.println();
		Chaining.ForwardChaining(ruleList);

//		RuleBox obj = new RuleBox();
//		obj.init();
//
//
//		String[] ant = new String[7];
//		String[] con = new String[3];
//
//		ant[0]="?teacher";
//		ant[1]="teaches";
//		ant[2]="?course";
//		ant[3]="AND";
//		ant[4]="?student";
//		ant[5]="studies";
//		ant[6]="?course";
//
//		con[0]="student";
//		con[1]="isStudentOf";
//		con[2]="teacher";
//
//		obj.addRule(ant,con);
//
//		ant = new String[3];
//		ant[0]="teacher";
//		ant[1]="teaches";
//		ant[2]="course";
//		con = new String[3];
//		con[0]="course";
//		con[1]="offered_by";
//		con[2]="teacher";
//
//		obj.addRule(ant,con);
//
//		ArrayList<ArrayList<String>> ruleList = obj.getRules();
//		Chaining.ForwardChaining(ruleList);
//
//		SDBUtilities sdbUtilities = new SDBUtilities();
//		sdbUtilities.DBinit();
//		String query = "Select ?student ?teacher { ?student <http://www.iiitb.org/university#isStudentOf> ?teacher}";
//		ResultSet rs = sdbUtilities.SDBQuery(query);
//		System.out.println(rs);
//
//		query = "SELECT ?course ?teacher {?course <http://www.iiitb.org/university#offered_by> ?teacher}";
//		rs = sdbUtilities.SDBQuery(query);
//		System.out.println(rs);
//		SDBUtilities sdbUtilities = new SDBUtilities();
//		sdbUtilities.DBinit();
//		OWLUtilities owlUtilities = new OWLUtilities(sdbUtilities);
//
//		ArrayList<String> node = owlUtilities.getNode();
//		for(String a:node)
//			System.out.print(a + " ");
//		System.out.println();
//		ArrayList<String> predicate = owlUtilities.getObjectProperties();
//		for(String a:predicate)
//			System.out.print(a+ " ");
//		System.out.println();
		System.out.println("Done");
//		sdbUtilities.DBclose();
	}
}


