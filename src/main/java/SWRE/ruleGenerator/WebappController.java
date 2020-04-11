package SWRE.ruleGenerator;

import SWRE.Ontology2SDB2MySQL.OWLUtilities;
import SWRE.Ontology2SDB2MySQL.SDBUtilities;
import SWRE.RuleJson;
import SWRE.ruleGenerator.RuleBox;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/Rule")
public class WebappController {

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
        return Response.ok().entity(existingRule).build();
    }


    @POST
    @Path("/userule")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN})
    public String updateDetails(RuleJson re){
        System.out.println("Aya");
        for(int i=0;i<re.getRules().size();i++){
            System.out.println(re.getRules().get(i));
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

        for (String s : classname) System.out.println(s);
        for (String s : properties) System.out.println(s);

        create.add(classname);
        create.add(properties);
        return Response.ok().entity(create).build();
    }
}
