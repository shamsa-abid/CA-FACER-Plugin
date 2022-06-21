package RelatedMethods.CustomUtilities;

import java.util.ArrayList;

public class ArrayConverter {
public static void main(String args[])
{
	ArrayList<Integer> x = new ArrayList<Integer>();
	x.add(1);
	x.add(2);
	x.add(3);
	
	
	
	System.out.println(convertArrayToString(convertArrayListToArray(x)));
	
	}

public static Integer[] convertArrayListToArray(ArrayList<Integer> a)
{
	Integer [] result = new Integer[a.size()];
	for(int i=0; i<a.size(); i++)
	{	
		result[i] = a.get(i);
		
	}
	return result;
	}

public static String convertArrayToString(Integer[] a)
{
	String result = "";
	

	for(int i=0; i<a.length; i++)
	{	
		result = result + " " + a[i];
		
	}
	return result;
	}

}


