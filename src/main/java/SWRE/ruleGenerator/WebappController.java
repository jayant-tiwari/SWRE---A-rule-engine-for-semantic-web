package SWRE.ruleGenerator;

import SWRE.Ontology2SDB2MySQL.OWLUtilities;
import SWRE.Ontology2SDB2MySQL.SDBUtilities;
import org.apache.jena.base.Sys;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/Rule")
public class WebappController {

    /*
     * For each run, certain rules get stored in the cache i.e. rules selected by the user for one run
     */
    ArrayList<ArrayList<String> > ruleCache = null;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Response showRules() throws Exception {

        RuleBox obj = new RuleBox();
        obj.init();
        ArrayList<ArrayList<String>> Rules = obj.getRules();
        if(Rules == null)   return Response.noContent().build();
        ArrayList<String> existingRule = new ArrayList<>();
        int rulesLen = Rules.size();
        for(int i = 0; i < rulesLen; i++){
            String tempRule = "If: ";
            ArrayList<String> Rule = Rules.get(i);
            int ruleLen = Rule.size();
            for(int j = 0; j < ruleLen; j++){
                if(j == ruleLen-3)
                    tempRule = tempRule + "Then: ";
                tempRule = tempRule + Rule.get(j) + " ";
            }
            existingRule.add(tempRule);
        }
        // REMOVE THIS LINE##########################################
        for(String s:existingRule)System.out.println(s);

        return Response.ok().entity(existingRule).build();
    }

    /*
     *
     */
    @POST
    @Path("/existingRules")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN})
    public String updateDetails(RuleJson ruleIndex) throws Exception {

        int len = ruleIndex.getRules().size();
        ruleCache = new ArrayList<>();

        RuleBox ruleBox = new RuleBox();
        ruleBox.init();
        ArrayList<ArrayList<String>> rules = ruleBox.getRules();

        for(int i=0;i<len;i++){
            int idx = Integer.parseInt(ruleIndex.getRules().get(i));
            ruleCache.add(rules.get(idx));
        }

        return "done";
    }

    @Path("/getNode")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchClasses() throws Exception {
        System.out.println("hello");
        SDBUtilities sdbUtilities = new SDBUtilities();
        sdbUtilities.DBinit();
        OWLUtilities owlUtilities = new OWLUtilities(sdbUtilities);
        ArrayList<String>classname= owlUtilities.getNode();
        ArrayList<String>properties= owlUtilities.getObjectProperties();
        ArrayList<ArrayList<String>>create= new ArrayList<ArrayList<String>>();

        create.add(classname);
        create.add(properties);
        return Response.ok().entity(create).build();
    }

    @POST
    @Path("/newRule")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN})
    public String creatNewRule(CreateRule re) throws Exception {

        System.out.println("New Rule");
        int antLen = re.getAntecedent().size();
        int conLen = re.getConsequent().size();
        String[] antecedent = new String[antLen];
        String[] consequent = new String[conLen];

        for(int i=0;i<antLen;i++){
            antecedent[i] = re.getAntecedent().get(i);
        }
        for(int i=0;i<3;i++){
            consequent[i] = re.getConsequent().get(i);
        }

        RuleBox ruleBox = new RuleBox();
        ruleBox.init();
        ruleBox.addRule(antecedent,consequent);
        System.out.println("Rule Added");
        ArrayList<ArrayList<String>> rule = ruleBox.getRules();
        for(int i=0;i<rule.size();i++){
            for(int j=0;j<rule.get(i).size();j++){
                System.out.print(rule.get(i).get(j)+" ");
            }
            System.out.println();
        }
        return "done";
    }
}
