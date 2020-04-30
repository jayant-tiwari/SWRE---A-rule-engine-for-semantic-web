package SWRE.ruleChaining;

import SWRE.Ontology2SDB2MySQL.OWLUtilities;
import SWRE.Ontology2SDB2MySQL.SDBUtilities;
import SWRE.ruleGenerator.RuleBox;

import java.util.ArrayList;
public class BackwardChaining {
    public static void executeRules(ArrayList<ArrayList<String> > rulesToExecute) throws Exception
    {
        String query = "";
        String subject = "";
        String object = "";
        String predicate = "";
        int tripleLength;
        String prefix = "http://www.iiitb.org/university#";
        ArrayList<String> rule =new ArrayList<String>();

        OWLUtilities owlUtilities =new OWLUtilities();
        for(int loop=rulesToExecute.size()-1;loop>=0;loop--) {

            ArrayList<ArrayList<String>> triples = new ArrayList<ArrayList<String>>();
            rule = rulesToExecute.get(loop);
            subject = rule.get(rule.size()-3);
            predicate = rule.get(rule.size()-2);
            object = rule.get(rule.size()-1);
            query = ForwardChaining.createQuery(rule,owlUtilities,prefix);
            System.out.println(query);
            triples = owlUtilities.SDBQuery(query,subject,object);
            tripleLength = triples.size();
            for (int loop1 = 0; loop1 < tripleLength; loop1++) {
                subject = triples.get(loop1).get(0);
                object = triples.get(loop1).get(1);
                owlUtilities.insertTriples(subject, predicate, object);
            }
        }
    }
    public static boolean backwardChaining(String subject, String predicate, String object) throws Exception {

        ArrayList<ArrayList<String>> possibleTargetValues = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> rulesToExecute = new ArrayList<ArrayList<String>>();
        ArrayList<String> lastIterationPredicates = new ArrayList<String>();
        ArrayList<String> currentIteartionPredicates = new ArrayList<String>();
//        ArrayList<String> checkrule = new ArrayList<String>();
        lastIterationPredicates.add(predicate);
        String query="";
        String currentPredicate="";
        String prefix = "http://www.iiitb.org/university#";
        subject = prefix + subject;
        object = prefix + object;
        boolean found = false;
        SDBUtilities sdbUtilities = new SDBUtilities();
        sdbUtilities.DBinit();
        RuleBox ruleBox = new RuleBox();
        ruleBox.init(true);
        ArrayList<ArrayList<String>> ruleList = ruleBox.getRules();
        System.out.println(ruleList.size());
        while(!lastIterationPredicates.isEmpty() && !found){
            query = "Select ?x ?y {?x " + "<" + prefix + predicate +"> ?y}";
            possibleTargetValues = OWLUtilities.SDBQuery(query,"x","y");
            int possibleTargetLength = possibleTargetValues.size();
            for(int loop=0;loop<possibleTargetLength;loop++) {
                if(subject.equals(possibleTargetValues.get(loop).get(0)) && object.equals(possibleTargetValues.get(loop).get(1))) {
                    found=true;
                    break;
                }
            }
            if(found == true)
                break;

            int lastIterationPredicatesLength = lastIterationPredicates.size();
            for(int outer_loop=0;outer_loop < lastIterationPredicatesLength; outer_loop++) {
                currentPredicate=lastIterationPredicates.get(outer_loop);
                for (int loop = 0; loop < ruleList.size(); loop++) {
                    if ((ruleList.get(loop).get((ruleList.get(loop).size()) - 2)).equals(currentPredicate)) {
                        rulesToExecute.add(ruleList.get(loop));
                        for(int antecedentPredicateIndex=1;antecedentPredicateIndex<((ruleList.get(loop).size()) - 3);antecedentPredicateIndex=antecedentPredicateIndex+4) {
                            currentIteartionPredicates.add(ruleList.get(loop).get(antecedentPredicateIndex));
                        }
                        executeRules(rulesToExecute);
                    }
                }
            }
            lastIterationPredicates.clear();
            for(int loop=0;loop<currentIteartionPredicates.size();loop++)
                lastIterationPredicates.add(currentIteartionPredicates.get(loop));
            currentIteartionPredicates.clear();
        }
        System.out.println("The result of the query by Backward Chaining is :" + found );
        return found;
    }
}