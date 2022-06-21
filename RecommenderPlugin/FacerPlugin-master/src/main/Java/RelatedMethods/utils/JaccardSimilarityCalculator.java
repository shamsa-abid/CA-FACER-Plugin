package RelatedMethods.utils;

import clustering.JaccardSimilarity;

import java.util.*;


public class JaccardSimilarityCalculator {

	public static void main(String args[])
	{
//		List<String> list1 = new ArrayList<String>();
//		List<String> list2 = new ArrayList<String>();
//		list1.add("shamsa");
//		list1.add("is");
//		list1.add("nice");
//		list1.add("good");
//		list1.add("nice");
//		list1.add("nice");
//		list2.add("shamsa");list2.add("is");list2.add("good");list2.add("good");list2.add("good");list2.add("good");
//		System.out.println(calculateJaccardSimilarity(list1, list2));


//		List<Integer> list1 = new ArrayList<Integer>();
//		List<Integer> list2 = new ArrayList<Integer>();
//		list1.add(12);
//		list1.add(13);
//		list1.add(14);
//		list1.add(15);
//		list1.add(16);
//		list1.add(17);
//		list2.add(12);list2.add(13);list2.add(14);
//		System.out.println(calculateJaccardSimilarityIntegers(list1, list2));

		String sequence1 = "12 13 14 15";
		String sequence2 ="12 13";
		//tokenize sequence in integer arraylist
		List<String> arrayList = new ArrayList<String>    (Arrays.asList(sequence1.split(" ")));
		List<Integer> intSeq1 = new ArrayList<Integer>();
		for(String fav:arrayList){
			intSeq1.add(Integer.parseInt(fav.trim()));
		}
		arrayList = new ArrayList<String>    (Arrays.asList(sequence2.split(" ")));
		List<Integer> intSeq2 = new ArrayList<Integer>();
		for(String fav:arrayList){
			intSeq2.add(Integer.parseInt(fav.trim()));
		}
		System.out.println(JaccardSimilarity.calculateJaccardSimilarityIntegers(intSeq1, intSeq2));

	}
	
	public static double calculateJaccardSimilarity(List<String> list1, List<String> list2){
		List<String> unions = new ArrayList<String>();
		List<String> intersections = new ArrayList<String>();
		double similarity;
		unions = findUnion(list1, list2);
		/*
		System.out.print("Union: ");
		for (int i=0; i<unions.size(); i++){
			System.out.print(unions.get(i) + '\t');
		}
		System.out.println();
		*/
		// Intersection of 1st two methods	 
		intersections = findIntersection(list1, list2);
		/*
		System.out.print("Intersection: ");
		for (int i=0; i<intersections.size(); i++){
			System.out.print(intersections.get(i) + '\t');
		}
		System.out.println();
		*/
		similarity = jaccardSimilarity(intersections, unions);
		//System.out.println("Similarity: " + similarity);
		return similarity;
	}

	public static double calculateJaccardSimilarityIntegers(List<Integer> list1, List<Integer> list2){
		List<Integer> unions = new ArrayList<Integer>();
		List<Integer> intersections = new ArrayList<Integer>();
		double similarity;
		unions = findUnion(list1, list2);
		/*
		System.out.print("Union: ");
		for (int i=0; i<unions.size(); i++){
			System.out.print(unions.get(i) + '\t');
		}
		System.out.println();
		*/
		// Intersection of 1st two methods
		intersections = findIntersection(list1, list2);
		/*
		System.out.print("Intersection: ");
		for (int i=0; i<intersections.size(); i++){
			System.out.print(intersections.get(i) + '\t');
		}
		System.out.println();
		*/
		similarity = jaccardSimilarity(intersections, unions);
		//System.out.println("Similarity: " + similarity);
		return similarity;
	}
	
	public static <T> List<T> findUnion(List<T> list1, List<T> list2) {
		Set<T> set = new HashSet<T>();
		set.addAll(list1);
		set.addAll(list2);
		return new ArrayList<T>(set);
	}

	public static <T> List<T> findIntersection(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();
		for (T t : list1) {
			if(list2.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}

	public static <T> double jaccardSimilarity(List<T> list1, List<T> list2){
		float intersectionCount = list1.size();
		float unionCount = list2.size();
		return intersectionCount/unionCount;
	}
}
