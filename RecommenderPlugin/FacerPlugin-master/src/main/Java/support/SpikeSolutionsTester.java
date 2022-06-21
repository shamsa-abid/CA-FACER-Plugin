package support;

import java.util.ArrayList;

public class SpikeSolutionsTester {
public static void main(String args[])
{
	
	//System.out.println(tokenize4sourcerer("//*** My name is shamsaAbid shamsa_abid shamsaAbid-Perwaiz    ***////"));
	
	//System.out.println(tokenizeFQN4sourcerer("F:\\FACER_2020\\RawSourceCodeDataset\\Cloned\\bluetooth_5c7817fc3b09f5c70e6b9d8dde20c5d0d9255c8c\\BluetoothLeGatt-5c7817fc3b09f5c70e6b9d8dde20c5d0d9255c8c\\Application\\src\\main\\java\\com\\example\\android\\bluetoothlegatt\\SampleGattAttributes.java"));
	//continueandbreak();
	testme();
}
public static void testme()
{
	ArrayList<Integer> relatedFeatureIDs= new ArrayList<Integer>();
	relatedFeatureIDs.add(1);
	relatedFeatureIDs.add(1);
	relatedFeatureIDs.add(1);
	relatedFeatureIDs.add(1);
	relatedFeatureIDs.add(1);
	if(relatedFeatureIDs.size()>2)
		relatedFeatureIDs.subList(2, relatedFeatureIDs.size()).clear();
	System.out.print(relatedFeatureIDs.size());
	}
public static void continueandbreak()
{
    // Illustrating break statement (execution stops when value of i becomes to 4.)
    System.out.println("Break Statement\n....................");

    for(int i=1;i<=5;i++)
    {
        if(i==4) break;
        System.out.println(i);
    }

    // Illustrating continue statement (execution skipped when value of i becomes to 1.)
    System.out.println("Continue Statement\n....................");

    for(int i=1;i<=5;i++)
    {
    	System.out.println("mewo");
    	System.out.println("meow");
        if(i==1) {
        	System.out.println("mewo");
        	System.out.println("meow");
        	continue;
        }
        System.out.println(i);
        System.out.println(i);
        System.out.println(i);
        System.out.println(i);
        
    }
}

private static String tokenize4sourcerer(String input) {
	String tokens = "";
	String  result="";
	//removing special characters
	result = input.replaceAll("[^a-zA-Z0-9]", " ");
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



private static String tokenizeFQN4sourcerer(String currentFilePath) {
	String tokens = "";
	String result = "";
	//take a substring from the file name from src to .java
	result = currentFilePath.substring(currentFilePath.indexOf("\\src\\")+4, currentFilePath.indexOf(".java"));
	
	//removing special characters and numbers
	result = result.replaceAll("[^a-zA-Z]", " ");
	//tokenizing by space
	String[] tokenizedBySpaces = result.split("\\s+");
	//tokenize each term by camelcase 
	for(String term: tokenizedBySpaces)
	{
		for (String w : term.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {     			
			
 			tokens = tokens + " " + w;
 		}
	}
		
		return tokens;
}

}
