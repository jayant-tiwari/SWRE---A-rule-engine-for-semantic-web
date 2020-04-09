import java.util.*;
public class ForwardChaining{
	public static void main(String args[]) throws Exception
	{
		int len,i,j;
		String temp="";
		String query="";
		ArrayList<ArrayList<String> > rule = new ArrayList<ArrayList<String> >();
		String foo = "";
		foo = foo+"<http://www.iiitb.org/university#";
		List<String> queries = new ArrayList<String>();
		ArrayList<String> x = new ArrayList<String>();
		ArrayList<String> y = new ArrayList<String>();
		rule.add(new ArrayList<String>());
		rule.get(0).add(0,"teacher");
		rule.get(0).add(1,"teaches");
		rule.get(0).add(2,"course"); 
		rule.get(0).add(3,"OR"); 
		rule.get(0).add(4,"student"); 
		rule.get(0).add(5,"studies"); 
		rule.get(0).add(6,"course");
		rule.get(0).add(7,"student"); 
		rule.get(0).add(8,"is_student_of"); 
		rule.get(0).add(9,"teacher");
		rule.add(new ArrayList<String>());
				rule.get(1).add(0,"teacher");
        		rule.get(1).add(1,"teaches");
                rule.get(1).add(2,"course");
                rule.get(1).add(3,"AND");
                rule.get(1).add(4,"student");
                rule.get(1).add(5,"studies");
                rule.get(1).add(6,"course");
                rule.get(1).add(7,"student");
                rule.get(1).add(8,"is_student_of");
                rule.get(1).add(9,"teacher");
		//rule.add(x);
		for(i=0;i<rule.size();i++)
		{
		 temp = "";
		 query = "";
		 len = rule.get(i).size();
		 if(len>3 && (rule.get(i).get(3)).equals("AND"))
		 {
		 temp=temp+"SELECT ?"+rule.get(i).get(len-3)+" ?"+rule.get(i).get(len-1)+" { ";
		 for(j=0;j<len-3;j=j+3)
		 {
			temp=temp+"?"+(rule.get(i).get(j))+" ";
			temp=temp+foo+(rule.get(i).get(j+1))+"> ";
			temp=temp+"?"+rule.get(i).get(j+2)+" ";
			if(j+3<len-3)
			{
				temp=temp+". ";
				j++;
			}
		 }
		 temp=temp+"}";
		 //query=query+'"'+temp+'"';
		 Test.forwardChaining(temp,rule.get(i).get(len-2),rule.get(i).get(len-3),rule.get(i).get(len-1));
		 }
		 else if(len>3 && (rule.get(i).get(3)).equals("OR"))
		 {
			  temp=temp+"SELECT ?"+rule.get(i).get(len-3)+" ?"+rule.get(i).get(len-1)+" { ";
		 for(j=0;j<len-3;j=j+3)
                 {
                        temp=temp+"{ ?"+(rule.get(i).get(j))+" ";
                        temp=temp+foo+(rule.get(i).get(j+1))+"> ";
                        temp=temp+"?"+rule.get(i).get(j+2)+" } ";
                        if(j+3<len-3)
                        {
                                temp=temp+"UNION ";
                                j++;
                        }
                 }
                 temp=temp+"}";
                // query=query+'"'+temp+'"';
          Test.forwardChaining(temp,rule.get(i).get(len-2),rule.get(i).get(len-3),rule.get(i).get(len-1));
		 }
		 //System.out.println(temp);
		}
		
	}
} 