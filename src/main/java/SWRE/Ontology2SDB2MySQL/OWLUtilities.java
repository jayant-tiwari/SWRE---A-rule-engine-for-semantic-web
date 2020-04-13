package SWRE.Ontology2SDB2MySQL;

/*
 * Layer of abtraction between the OWL file and the sdb database. This class supports addition of data into the SQL data
 * and query from it i.e. the CRUD operations on the relational triple store
 */

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.sdb.SDBFactory;
import org.apache.jena.sdb.store.DatasetStore;

import java.util.ArrayList;

public class OWLUtilities {

    private static SDBUtilities sdbUtilities = null;

    public OWLUtilities() throws Exception {
        sdbUtilities = new SDBUtilities();
        sdbUtilities.DBinit();
    }

    public OWLUtilities(SDBUtilities sdbUtilities){
        this.sdbUtilities = sdbUtilities;
    }

    /*
     * This method, creates a new resource for the predicate if the predicate is missing and if not, inserts the triple into the database
     */
    public static String insertTriples(String subject,String predicate,String object) {

        Model model = SDBFactory.connectDefaultModel(sdbUtilities.getStore());
        model.setNsPrefix(sdbUtilities.getOntologyNamespace(),sdbUtilities.getOntologyPrefix());
        model.read(sdbUtilities.getOntology());
        //create new triples
        org.apache.jena.rdf.model.Resource Subject = model.createResource( subject);
        Property Predicate = model.createProperty(sdbUtilities.getOntologyPrefix() + predicate);
        org.apache.jena.rdf.model.Resource Object = model.createResource( object);
        //add triples in data base
        model.add(Subject,Predicate,Object);
        model.commit();

        return "success";
    }

    /*
     * For each new predicate(object property), some data-properties are tagged with them. Thus, a triple needs to be
     * inserted for each of the new predicate
     */

    public static String insertTriples(String rdf, String owl ,String subject , String predicate, String object) {

        Model model = SDBFactory.connectDefaultModel(sdbUtilities.getStore());
        model.setNsPrefix(sdbUtilities.getOntologyNamespace(),sdbUtilities.getOntologyPrefix());
        model.read(sdbUtilities.getOntology());
        //create new triples
        org.apache.jena.rdf.model.Resource Subject = model.createResource(sdbUtilities.getOntologyPrefix() + subject);
        Property Predicate = model.createProperty(rdf + predicate);
        org.apache.jena.rdf.model.Resource Object = model.createResource( owl + object);
        //add triples in data base
        model.add(Subject, Predicate, Object);
        model.commit();

        return "success";
    }

    /*
     * Below are overloaded methods of SDBQuery, each serving a different purpose
     * Methods take a SPARQL Query in the input and returns the respective output
     */

    public static ResultSet SDBQuery(String queryString) {

        /*
         * This method is for demo i.e. used to query the sdbjena as in SPARQL API
         * The method gives exact resultset as what the SPARQL API will return without any editting
         */

        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<>();
        Dataset dataset = DatasetStore.create(sdbUtilities.getStore());
        Query query = QueryFactory.create(queryString) ;
        try ( QueryExecution qe = QueryExecutionFactory.create(query, dataset) ) {
            rs = qe.execSelect() ;
            ResultSetFormatter.out(rs) ;
        }
        return rs;
    }

    /*
     * This method take a SPARQL string query, a plausible consequent subject and object and returns the triples
     */
    public static ArrayList<ArrayList<String>> SDBQuery(String queryString, String consequentSubject, String consequentObject) {

        ResultSet rs = null;
        Dataset dataset = DatasetStore.create(sdbUtilities.getStore());
        Query query = QueryFactory.create(queryString) ;
        ArrayList<ArrayList<String> > triple = new ArrayList<ArrayList<String> >();
        try ( QueryExecution qe = QueryExecutionFactory.create(query, dataset) ) {
            rs = qe.execSelect() ;

            while(rs.hasNext()) {
                ArrayList<String> temp = new ArrayList<String>();
                QuerySolution q = rs.next();

                temp.add(q.get(consequentSubject).toString());
                temp.add(q.get(consequentObject).toString());
                triple.add(temp);
            }
        }
        return triple;
    }

    /*
     * This method uses a hard coded SPARQL query and returns all the nodes
     * Each of the nodes, also termed as class in OWL can be queried and retrieved using this method
     * The class name is followed by an URI therefore, the string is trimmed too
     */
    public static ArrayList<String> getNode() {

        ArrayList<String> node = new ArrayList<String>();
        ResultSet rs = null;
        Dataset dataset = DatasetStore.create(sdbUtilities.getStore());
        String queryString =    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                                "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
                                "SELECT ?x  WHERE { ?x rdf:type owl:Class}";
        String outputColumnHeader = "x";
        Query query = QueryFactory.create(queryString);

        try ( QueryExecution qe = QueryExecutionFactory.create(query, dataset) ) {
            rs = qe.execSelect() ;
            while(rs.hasNext()) {

                QuerySolution querySolution = rs.next();
                String tempNode = querySolution.get(outputColumnHeader).toString();
                String[] Node = tempNode.split("#",0);
                node.add("?" + Node[Node.length-1]);
            }
        }
        return node;
    }

    /*
     * This method retrieves all the possible predicates present in the ontology
     */
    public static ArrayList<String> getObjectProperties() {

        ArrayList<String> objectProperty = new ArrayList<String>();
        ResultSet rs = null;
        Dataset dataset = DatasetStore.create(sdbUtilities.getStore());
        String queryString =    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                                "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
                                "SELECT distinct ?p WHERE { ?x ?p ?q . ?p rdf:type owl:ObjectProperty .}";

        String outputColumnHeader = "p";
        Query query = QueryFactory.create(queryString) ;

        try ( QueryExecution qe = QueryExecutionFactory.create(query, dataset) ) {
            rs = qe.execSelect() ;
            while(rs.hasNext()) {

                QuerySolution querySolution = rs.next();
                String tempObjectProperty = querySolution.get(outputColumnHeader).toString();
                String[] trimmedObjectProperty = tempObjectProperty.split("#",0);
                objectProperty.add(trimmedObjectProperty[trimmedObjectProperty.length-1]);
            }
        }
        return objectProperty;
    }
}
