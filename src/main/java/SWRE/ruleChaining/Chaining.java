package SWRE.ruleChaining;

import SWRE.Ontology2SDB2MySQL.SDBUtilities;

import java.util.ArrayList;
import java.util.List;

public class Chaining {

    /*
     * RIGHT NOW, QUERY WITH EITHER ALL ANDS (or) ALL ORS ARE ONLY SUPPORTED
     * EDIT THIS METHOD AFTER FURTHER UPDATES
     */

    public static String createQuery(ArrayList<String> Rule, String prefix){

        String query = "";
        int len = Rule.size();

        // ONLY AND CONNECTOR
        if(len>3 && (Rule.get(3)).equals("AND")) {

            query = query + "SELECT ?" + Rule.get(len-3)+" ?" + Rule.get(len-1)+" { ";
            for(int loop = 0; loop < len-3; loop = loop+3) {

                query = query + "?" + (Rule.get(loop))+" ";
                query = query + prefix + (Rule.get(loop+1)) + "> ";
                query = query + "?" + Rule.get(loop+2) + " ";

                // For AND connector
                if(loop+3<len-3) {
                    query = query + ". ";
                    loop++;
                }
            }
            query  = query + "}";
        }
        // ONLY OR CONNECTOR
        else if(len>3 && (Rule.get(3)).equals("OR")) {
            query = query + "SELECT ?" + Rule.get(len - 3) + " ?" + Rule.get(len - 1) + " { ";
            for (int loop = 0; loop < len - 3; loop = loop + 3) {
                query = query + "{ ?" + (Rule.get(loop)) + " ";
                query = query + prefix + (Rule.get(loop + 1)) + "> ";
                query = query + "?" + Rule.get(loop + 2) + " } ";
                if (loop + 3 < len - 3) {
                    query = query + "UNION ";
                    loop++;
                }
            }
            query = query + "}";
        }
        return query;
    }

    /*
     * Method to implement forward chaining
     */
    public static void ForwardChaining(ArrayList<ArrayList<String>> ruleList) throws Exception {

        SDBUtilities sdbUtilities = new SDBUtilities();
        sdbUtilities.DBinit();
        String prefix = sdbUtilities.getOntologyPrefix();
        int ruleListLength = ruleList.size();
        for(int loop = 0; loop < ruleListLength; loop ++){

            // Extract each rule from the rule
            ArrayList<String> Rule = ruleList.get(loop);
            // Create query for the current rule
            String query = createQuery(Rule, prefix);
            // Obtaing triples due to the above query
            int ruleLength = Rule.size();
            String subject = Rule.get(ruleLength-3);
            String object = Rule.get(ruleLength-1);
            ArrayList<ArrayList<String>> triples = sdbUtilities.SDBQuery(query,subject,object);
            // Update the fact table for triples obtained from the above query

        }
    }
}
Test.forwardChaining(temp,rule.get(i).get(len-2),rule.get(i).get(len-3),rule.get(i).get(len-1));