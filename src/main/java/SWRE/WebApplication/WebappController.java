package SWRE.WebApplication;

import SWRE.Ontology2SDB2MySQL.OWLUtilities;
import SWRE.Ontology2SDB2MySQL.SDBUtilities;
import SWRE.ruleChaining.Chaining;
import SWRE.ruleGenerator.CreateRule;
import SWRE.ruleGenerator.RuleBox;
import SWRE.ruleGenerator.RuleJson;
import org.apache.jena.base.Sys;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
 * This class deals with all the web-application based operations. All the methods declared and defined are in
 * order of the chronology from which they are being called from the web-application.
 * Methods:
 * 1. selectOntology
 * 2.
 */

@Path("/Rule")
public class WebappController {

    /*
     * Method to load ontology via uploading a file
     */
    @POST
    @Path("/fileUpload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("dbname") String database,
            @FormDataParam("prefixname") String prefix,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        
        String fileLocation = "/home/mohit/Music/SWRE---A-rule-engine-for-semantic-web/" + fileDetail.getFileName();
        System.out.println(database);
        System.out.println(prefix);
        System.out.println(fileLocation);
        try {
            FileOutputStream out ;
            int read = 0;
            byte[] bytes = new byte[1024];
            out = new FileOutputStream(new File(fileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {e.printStackTrace();}
        String output = "File successfully uploaded to : " + fileLocation;
        System.out.println(output);
        return Response.ok().entity(output).build();
    }
    /*
     * For each run, certain rules get stored in the cache i.e. rules selected by the user for one run
     */
    static private ArrayList<ArrayList<String> > ruleCache = null;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Response showRules() throws Exception {

        RuleBox obj = new RuleBox();
        obj.init(true);
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

    /*
     *
     */
    @POST
    @Path("/existingRules")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response updateDetails(RuleJson ruleIndex) throws Exception {

        System.out.println("Existing Rules");
        int len = ruleIndex.getRules().size();
        ruleCache = new ArrayList<>();

        RuleBox ruleBox = new RuleBox();
        ruleBox.init(true);
        ArrayList<ArrayList<String>> rules = ruleBox.getRules();

        for(int i=0;i<len;i++){
            int idx = Integer.parseInt(ruleIndex.getRules().get(i));
            ruleCache.add(rules.get(idx));
        }
        System.out.println("\t\t\t*****************FORWARD CHAINING*****************");
        Chaining.ForwardChaining(ruleCache);
        System.out.println("\t\t\t######Selected Explicit Rules Parsed######");
        ruleBox.init(false);
        ruleCache = ruleBox.getRules();
        Chaining.ForwardChaining(ruleCache);
        System.out.println("\t\t\t######Implicit Rules Parsed######");
        return Response.ok().build();
    }

    @Path("/getNode")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchClasses() throws Exception {
        System.out.println("Fetching Class and ObjectProperty");
        OWLUtilities owlUtilities = new OWLUtilities();
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
        ruleBox.init(true);
        ruleBox.addRule(antecedent,consequent);
        return "done";
    }

    /*
    *  get query function
    */
    @POST
    @Path("/getQuery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getQuery( RuleJson re) throws Exception {

        String subject = re.getRules().get(0);
        String predicate = re.getRules().get(1);
        String object = re.getRules().get(2);
        String query="";
        OWLUtilities owlUtilities = new OWLUtilities();
        if(subject.charAt(0)!='?') {
            query = "SELECT " +  object + "{ <http://www.iiitb.org/university#" + subject + "><http://www.iiitb.org/university#" + predicate + "> " + object + "}";
            ArrayList<String>result = owlUtilities.SDBQuery(query,object);
            return Response.ok().entity(result).build();
        }
        else if(object.charAt(0)!='?'){
            query = "SELECT " + subject + "{ " + subject + " <http://www.iiitb.org/university#" + predicate + "> <http://www.iiitb.org/university#" + object + ">}";
            ArrayList<String> result = owlUtilities.SDBQuery(query,subject);
            return Response.ok().entity(result).build();
        }
        else{
            query = "SELECT " + subject + " " + object + "{ " + subject + " <http://www.iiitb.org/university#" + predicate + "> " + object + "}";
            ArrayList<ArrayList<String>> result = owlUtilities.SDBQuery(query,subject,object);
            return Response.ok().entity(result).build();
        }
    }


}

