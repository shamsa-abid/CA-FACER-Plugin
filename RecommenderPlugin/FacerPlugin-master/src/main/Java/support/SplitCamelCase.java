package support;

public class SplitCamelCase {
	
	public static void main(String args[])
	{
		System.out.println(splitCamelCase("get_input_fromPath.Long"));
	
	}
	
	public static String splitCamelCase(String input)
	{String tokens = "";
	String  result="";
	//removing special characters and numbers
	result = input.replaceAll("[^a-zA-Z]", " ");
	//tokenizing by space
	String[] tokenizedBySpaces = result.split("\\s+");
	//tokenize each term by camelcase and underscore and hyphen
	for(String term: tokenizedBySpaces)
	{
		for (String w : term.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {     			
			
 			tokens = tokens + " " + w;
 		}
	}
		return tokens;
	}
}
//(? <! (^ | [A-Z] ))   (?=[A-Z])   |  (?<!^)   (      ?=[A-Z][a-z]          )