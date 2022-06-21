package Utilities;

import java.util.ArrayList;

public class Tester {
public static void main(String args[])
{
	ArrayList<Integer> someRelatedFeatures = new ArrayList<Integer>();
	someRelatedFeatures.add(10);
	someRelatedFeatures.add(22);
	someRelatedFeatures.add(3);
    for(int CID:someRelatedFeatures)
    {
    	System.out.println(CID);
    }
    someRelatedFeatures.subList(0, someRelatedFeatures.size()).clear();
    System.out.println(someRelatedFeatures);
}

}
