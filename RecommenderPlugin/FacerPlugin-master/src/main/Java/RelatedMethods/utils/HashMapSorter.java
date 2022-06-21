package RelatedMethods.utils;

import repository.Method;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class HashMapSorter {
	
	public static boolean ASC = true;
    public static boolean DESC = false;
    
public static void main(String args[]) throws IOException
{
	
	List<String> allLines = Files.readAllLines(Paths.get("F:\\PhD\\4thYear\\FOCUS-ICSE19-artifact-evaluation\\crossminer-FOCUS-4c02746\\dataset\\SH_S\\evaluation\\round1\\Similarities\\000a7d6989abec22bf0a8336d350d8a97ccda5fa.txt"));
	for (String line : allLines) {
		//get the project name from the second occurrence of tab
		int firstIndex = line.indexOf('\t');
		int secondIndex = line.indexOf(".txt\t", firstIndex+1);
		System.out.println(line.substring(firstIndex+1, secondIndex));
		
		firstIndex = line.indexOf('\t',firstIndex+1);
		System.out.println(line.substring(firstIndex+1));
		
		System.out.println(line.substring(firstIndex+1, secondIndex));
	}
	
	HashMap<String,Float> map = new HashMap<String,Float>();
	map.put("froyo", 1.1f);
	map.put("abby", 0.1f);
	map.put("denver", 0.9f);
	map.put("frost", 0.4f);
	map.put("daisy", 0.5f);
	map = (HashMap<String, Float>) sortByComparator(map,DESC);
	 map.entrySet().forEach(entry->{
		    System.out.println(entry.getKey() + " " + entry.getValue());  
		 });
}

public static LinkedHashMap< String, Float> sortHashMapByValues(
        HashMap<String, Float> passedMap) {
    List<String> mapKeys = new ArrayList<>(passedMap.keySet());
    List<Float> mapValues = new ArrayList<>(passedMap.values());
    Collections.sort(mapValues);
    Collections.sort(mapKeys);
    

    LinkedHashMap<String, Float> sortedMap =
        new LinkedHashMap<>();

    Iterator<Float> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
        Float val = valueIt.next();
        Iterator<String> keyIt = mapKeys.iterator();

        while (keyIt.hasNext()) {
            String key = keyIt.next();
            Float comp1 = passedMap.get(key);
            Float comp2 = val;

            if (comp1.equals(comp2)) {
                keyIt.remove();
                sortedMap.put(key, val);
                break;
            }
        }
    }
    return sortedMap;
}

private static Map<String, Float> sortByComparator(Map<String, Float> unsortMap, final boolean order)
{

	
    List<Entry<String, Float>> list = new LinkedList<Entry<String, Float>>(unsortMap.entrySet());

    // Sorting the list based on values
    Collections.sort(list, new Comparator<Entry<String, Float>>()
    {
        public int compare(Entry<String, Float> o1,
                Entry<String, Float> o2)
        {
            if (order)
            {
                return o1.getValue().compareTo(o2.getValue());
            }
            else
            {
                return o2.getValue().compareTo(o1.getValue());

            }
        }
    });

    // Maintaining insertion order with the help of LinkedList
    Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
    for (Entry<String, Float> entry : list)
    {
        sortedMap.put(entry.getKey(), entry.getValue());
    }

    return sortedMap;
}

public static Map<Method, Float> sortMethodsByComparator(Map<Method, Float> unsortMap, final boolean order)
{
	Map<Method, Float> sortedMap = new LinkedHashMap<Method, Float>();
	if(unsortMap.entrySet().iterator().next().getValue() >= 0.5f)
	{	
	    List<Entry<Method, Float>> list = new LinkedList<Entry<Method, Float>>(unsortMap.entrySet());
	
	    // Sorting the list based on values
	    Collections.sort(list, new Comparator<Entry<Method, Float>>()
	    {
	        public int compare(Entry<Method, Float> o1,
	                Entry<Method, Float> o2)
	        {
	            if (order)
	            {
	                return o1.getValue().compareTo(o2.getValue());
	            }
	            else
	            {
	                return o2.getValue().compareTo(o1.getValue());
	
	            }
	        }
	    });
	
	    // Maintaining insertion order with the help of LinkedList
	   
	    for (Entry<Method, Float> entry : list)
	    {
	        sortedMap.put(entry.getKey(), entry.getValue());
	    }
	    return sortedMap;
	}
	else
		return unsortMap;

    
}
}
