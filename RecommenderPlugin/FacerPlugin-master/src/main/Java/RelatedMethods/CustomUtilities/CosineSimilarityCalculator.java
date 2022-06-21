package RelatedMethods.CustomUtilities;



import java.util.*;

public class CosineSimilarityCalculator {

	public static void main(String args[])
	{
		 List<Integer> list1 = Arrays.asList(1,2,3,4,5,6,6,6);
		 List<Integer> list2 = Arrays.asList(1,2, 9, 9, 12);
	      //Scenario 1
	      Set<Integer> set1 = new HashSet<>(list1);    
	      System.out.println(set1);
	      Set<Integer> set2 = new HashSet<>(list2);    
	      System.out.println(set1);
	      System.out.println(computeCosineSimilarity(set1,set2) );
		
	}
	
	public static double computeCosineSimilarity(Set<Integer> v1,Set<Integer> v2) 
	{
		Set<Integer> intersection = new HashSet<Integer>(v1);
		intersection.retainAll(v2);
		float similarity = (float) (intersection.size()/ Math.sqrt(v1.size() * v2.size()));
		return similarity;
		
	}

	public static double computeJaccardSimilarity(Set<Integer> v1,Set<Integer> v2)
	{
		Set<Integer> intersection = new HashSet<Integer>(v1);
		intersection.retainAll(v2);

		Set<Integer> unionset = new HashSet<Integer>();
		unionset.addAll(v1);
		unionset.addAll(v2);

		float similarity = (float) (intersection.size()/ (unionset.size()));
		return similarity;
	}
	


}
