package Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class TestMedian {
	public static void main(String args[])  { 
		
		// let's create a map with Java releases and their code names 
		HashMap<Integer, Integer> codenames = new HashMap<Integer, Integer>(); 
		codenames.put(12,12); 
		codenames.put(13,1); 
		codenames.put(14,2); 

		
		
		System.out.println("HashMap before sorting, random order "); 
		Set<Entry<Integer, Integer>> entries = codenames.entrySet(); 
		for(Entry<Integer, Integer> entry : entries)
		{ 
			System.out.println(entry.getKey() + " ==> " + entry.getValue()); 
		}


			
		
		Comparator<Entry<Integer, Integer>> valueComparator = new Comparator<Entry<Integer,Integer>>() { 
			@Override public int compare(Entry<Integer, Integer> e1, Entry<Integer, Integer> e2) {
				Integer v1 = e1.getValue(); 
				Integer v2 = e2.getValue(); 
				return v1.compareTo(v2); } };
				
				// Sort method needs a List, so let's first convert Set to List in Java 
				List<Entry<Integer, Integer>> listOfEntries = new ArrayList<Entry<Integer, Integer>>(entries); 
				// sorting HashMap by values using comparator 
				Collections.sort(listOfEntries, valueComparator); 
				LinkedHashMap<Integer, Integer> sortedByValue = new LinkedHashMap<Integer, Integer>(listOfEntries.size());
				// copying entries from List to Map 
				for(Entry<Integer, Integer> entry : listOfEntries)
				{ sortedByValue.put(entry.getKey(), entry.getValue()); 
				} 
				System.out.println("HashMap after sorting entries by values "); 
				Set<Entry<Integer, Integer>> entrySetSortedByValue = sortedByValue.entrySet(); 
				for(Entry<Integer, Integer> mapping : entrySetSortedByValue)
				{ 	System.out.println(mapping.getKey() + " ==> " + mapping.getValue()); 
				}

			
		
	
		
}
	 
}
