package clustering;

import java.util.*;

public class JaccardSimilarity {
    public static double computeJaccardsCoefficient(String p1, String p2) {
        Collection<String> tweet1 = new TreeSet<String>(Arrays.asList(p1.split(" ")));

        Collection<String> tweet2 = new TreeSet<String>(Arrays.asList(p2.split(" ")));

        Collection<String> intersectionOfTweets = new TreeSet<String>(
                tweet1);
        intersectionOfTweets.retainAll(tweet2);

        Collection<String> unionOfTweets = new TreeSet<String>(tweet1);
        unionOfTweets.addAll(tweet2);

        double jaccardsCoefficient = (double) intersectionOfTweets.size()
                / (double) unionOfTweets.size();
        return jaccardsCoefficient;
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
    public static void main(String args[])
    {
        double score = computeJaccardsCoefficient("alpha beta gamma","alpha beta x y z e r w t y");
        System.out.println(score);
    }

}
