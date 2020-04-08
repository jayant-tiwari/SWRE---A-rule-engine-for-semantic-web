package SWRE.ruleGenerator;

import SWRE.ruleGenerator.RuleBox;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
            String tempRule = "";
            ArrayList<String> Rule = Rules.get(i);
            int ruleLen = Rule.size();
            for(int j = 0; j < ruleLen; j++){
                tempRule = tempRule + Rule.get(j) + " ";
            }
            existingRule.add(tempRule);
        }
        return Response.ok().entity(existingRule).build();
    }
}
