package related_methods._3_PopulateRelatedFeatures;

import RelatedMethods.db_access_layer.DatabaseAccessLayer;
import RelatedMethods.DataObjects.Method;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public class ViewClusterMethods {

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException
	{	//15 and 8 is a really great suggestion of related features
		//input cluster ID
		/*13774
		13777
		13773
		13762
		13754
		13688*/
		//viewMethodBodies(13774);	
		
		int clusterID = 13774;
		int methodID = 0;
		
		//printCIDMID();
		
		viewMethodBodiesNew();
		//viewMethodBodiesWithAPISizeRange();
		//viewMethodBodiesWithAPISizeRange(2135);
		//sarahEvaluation();
		//shafayEvaluation();
		//hamidEvaluation();
		//m_126methods_for_interCGevaluation();
	}

	private static void m_126methods_for_interCGevaluation()throws ClassNotFoundException, SQLException, IOException {
	
		//these methods for emse paper submitted first 14 clone groups
				viewMethodBodiesWithAPISizeRange(19);
				viewMethodBodiesWithAPISizeRange(445);
				viewMethodBodiesWithAPISizeRange(2884);
				viewMethodBodiesWithAPISizeRange(687);
				viewMethodBodiesWithAPISizeRange(2012);
				viewMethodBodiesWithAPISizeRange(2851);
				viewMethodBodiesWithAPISizeRange(338);
				viewMethodBodiesWithAPISizeRange(3811);		
				viewMethodBodiesWithAPISizeRange(1809);
				viewMethodBodiesWithAPISizeRange(491);
				viewMethodBodiesWithAPISizeRange(1000);
				viewMethodBodiesWithAPISizeRange(4032);
				viewMethodBodiesWithAPISizeRange(1708);
				viewMethodBodiesWithAPISizeRange(2);		
				
				//these methods are added after revision
				viewMethodBodiesWithAPISizeRange(1957);
				viewMethodBodiesWithAPISizeRange(2057);
				viewMethodBodiesWithAPISizeRange(1645);
				viewMethodBodiesWithAPISizeRange(1117);
				viewMethodBodiesWithAPISizeRange(2339);
				viewMethodBodiesWithAPISizeRange(2419);
				viewMethodBodiesWithAPISizeRange(489);
				viewMethodBodiesWithAPISizeRange(1852);		
				viewMethodBodiesWithAPISizeRange(206);
				viewMethodBodiesWithAPISizeRange(4254);
				viewMethodBodiesWithAPISizeRange(410);
				viewMethodBodiesWithAPISizeRange(204);
				viewMethodBodiesWithAPISizeRange(331);
				viewMethodBodiesWithAPISizeRange(135);
				
				//these methods added for second major revision
			viewMethodBodiesWithAPISizeRange(35);
			viewMethodBodiesWithAPISizeRange(38);
			viewMethodBodiesWithAPISizeRange(45);
			viewMethodBodiesWithAPISizeRange(46);
			viewMethodBodiesWithAPISizeRange(69);
			viewMethodBodiesWithAPISizeRange(77);
			viewMethodBodiesWithAPISizeRange(89);
			viewMethodBodiesWithAPISizeRange(181);
			viewMethodBodiesWithAPISizeRange(197);
			viewMethodBodiesWithAPISizeRange(259);
			viewMethodBodiesWithAPISizeRange(342);
			viewMethodBodiesWithAPISizeRange(343);
			viewMethodBodiesWithAPISizeRange(348);
			viewMethodBodiesWithAPISizeRange(352);
			viewMethodBodiesWithAPISizeRange(453);
			viewMethodBodiesWithAPISizeRange(456);
			viewMethodBodiesWithAPISizeRange(460);
			viewMethodBodiesWithAPISizeRange(478);
			viewMethodBodiesWithAPISizeRange(487);
			viewMethodBodiesWithAPISizeRange(490);
			viewMethodBodiesWithAPISizeRange(493);
			viewMethodBodiesWithAPISizeRange(494);
			viewMethodBodiesWithAPISizeRange(495);
			viewMethodBodiesWithAPISizeRange(496);
			viewMethodBodiesWithAPISizeRange(497);
			viewMethodBodiesWithAPISizeRange(543);
			viewMethodBodiesWithAPISizeRange(557);
			viewMethodBodiesWithAPISizeRange(565);
			viewMethodBodiesWithAPISizeRange(589);
			viewMethodBodiesWithAPISizeRange(590);
			viewMethodBodiesWithAPISizeRange(591);
			viewMethodBodiesWithAPISizeRange(592);
			viewMethodBodiesWithAPISizeRange(593);

			viewMethodBodiesWithAPISizeRange(594);
			viewMethodBodiesWithAPISizeRange(604);
			viewMethodBodiesWithAPISizeRange(608);
			viewMethodBodiesWithAPISizeRange(626);
			viewMethodBodiesWithAPISizeRange(627);
			viewMethodBodiesWithAPISizeRange(644);
			viewMethodBodiesWithAPISizeRange(646);
			viewMethodBodiesWithAPISizeRange(648);
			viewMethodBodiesWithAPISizeRange(649);
			viewMethodBodiesWithAPISizeRange(651);
			viewMethodBodiesWithAPISizeRange(681);
			viewMethodBodiesWithAPISizeRange(697);
			viewMethodBodiesWithAPISizeRange(705);
			viewMethodBodiesWithAPISizeRange(706);
			viewMethodBodiesWithAPISizeRange(772);
			viewMethodBodiesWithAPISizeRange(850);
			viewMethodBodiesWithAPISizeRange(901);
			viewMethodBodiesWithAPISizeRange(902);
			viewMethodBodiesWithAPISizeRange(904);
			viewMethodBodiesWithAPISizeRange(906);
			viewMethodBodiesWithAPISizeRange(908);
			viewMethodBodiesWithAPISizeRange(911);
			viewMethodBodiesWithAPISizeRange(912);
			viewMethodBodiesWithAPISizeRange(913);
			viewMethodBodiesWithAPISizeRange(916);
			viewMethodBodiesWithAPISizeRange(917);
			viewMethodBodiesWithAPISizeRange(918);
			viewMethodBodiesWithAPISizeRange(919);
			viewMethodBodiesWithAPISizeRange(920);
			viewMethodBodiesWithAPISizeRange(921);
			viewMethodBodiesWithAPISizeRange(924);
			viewMethodBodiesWithAPISizeRange(925);
			viewMethodBodiesWithAPISizeRange(926);

		
		viewMethodBodiesWithAPISizeRange(928);
		viewMethodBodiesWithAPISizeRange(929);
		viewMethodBodiesWithAPISizeRange(930);
		viewMethodBodiesWithAPISizeRange(931);
		viewMethodBodiesWithAPISizeRange(932);
		viewMethodBodiesWithAPISizeRange(933);
		viewMethodBodiesWithAPISizeRange(935);
		viewMethodBodiesWithAPISizeRange(936);
		viewMethodBodiesWithAPISizeRange(937);
		viewMethodBodiesWithAPISizeRange(938);
		viewMethodBodiesWithAPISizeRange(882);
		viewMethodBodiesWithAPISizeRange(1855);
		viewMethodBodiesWithAPISizeRange(2061);
		viewMethodBodiesWithAPISizeRange(2384);
		viewMethodBodiesWithAPISizeRange(2922);
		viewMethodBodiesWithAPISizeRange(3046);
		viewMethodBodiesWithAPISizeRange(3609);
		viewMethodBodiesWithAPISizeRange(3736);
		viewMethodBodiesWithAPISizeRange(3927);
		viewMethodBodiesWithAPISizeRange(4031);
		viewMethodBodiesWithAPISizeRange(4076);
		viewMethodBodiesWithAPISizeRange(4200);
		viewMethodBodiesWithAPISizeRange(4833);
		viewMethodBodiesWithAPISizeRange(645);
		viewMethodBodiesWithAPISizeRange(4346);
		viewMethodBodiesWithAPISizeRange(4545);
		viewMethodBodiesWithAPISizeRange(527);
		viewMethodBodiesWithAPISizeRange(603);
		viewMethodBodiesWithAPISizeRange(956);
		viewMethodBodiesWithAPISizeRange(1043);
		viewMethodBodiesWithAPISizeRange(1446);
		viewMethodBodiesWithAPISizeRange(1466);
	

	}
	
	private static void sarahEvaluation() throws ClassNotFoundException, SQLException, IOException {
		viewMethodBodiesWithAPISizeRange(35);
		viewMethodBodiesWithAPISizeRange(38);
		viewMethodBodiesWithAPISizeRange(45);
		viewMethodBodiesWithAPISizeRange(46);
		viewMethodBodiesWithAPISizeRange(69);
		viewMethodBodiesWithAPISizeRange(77);
		viewMethodBodiesWithAPISizeRange(89);
		viewMethodBodiesWithAPISizeRange(181);
		viewMethodBodiesWithAPISizeRange(197);
		viewMethodBodiesWithAPISizeRange(259);
		viewMethodBodiesWithAPISizeRange(342);
		viewMethodBodiesWithAPISizeRange(343);
		viewMethodBodiesWithAPISizeRange(348);
		viewMethodBodiesWithAPISizeRange(352);
		viewMethodBodiesWithAPISizeRange(453);
		viewMethodBodiesWithAPISizeRange(456);
		viewMethodBodiesWithAPISizeRange(460);
		viewMethodBodiesWithAPISizeRange(478);
		viewMethodBodiesWithAPISizeRange(487);
		viewMethodBodiesWithAPISizeRange(490);
		viewMethodBodiesWithAPISizeRange(493);
		viewMethodBodiesWithAPISizeRange(494);
		viewMethodBodiesWithAPISizeRange(495);
		viewMethodBodiesWithAPISizeRange(496);
		viewMethodBodiesWithAPISizeRange(497);
		viewMethodBodiesWithAPISizeRange(543);
		viewMethodBodiesWithAPISizeRange(557);
		viewMethodBodiesWithAPISizeRange(565);
		viewMethodBodiesWithAPISizeRange(589);
		viewMethodBodiesWithAPISizeRange(590);
		viewMethodBodiesWithAPISizeRange(591);
		viewMethodBodiesWithAPISizeRange(592);
		viewMethodBodiesWithAPISizeRange(593);

		
	}
	private static void shafayEvaluation() throws ClassNotFoundException, SQLException, IOException {
	
		viewMethodBodiesWithAPISizeRange(594);
		viewMethodBodiesWithAPISizeRange(604);
		viewMethodBodiesWithAPISizeRange(608);
		viewMethodBodiesWithAPISizeRange(626);
		viewMethodBodiesWithAPISizeRange(627);
		viewMethodBodiesWithAPISizeRange(644);
		viewMethodBodiesWithAPISizeRange(646);
		viewMethodBodiesWithAPISizeRange(648);
		viewMethodBodiesWithAPISizeRange(649);
		viewMethodBodiesWithAPISizeRange(651);
		viewMethodBodiesWithAPISizeRange(681);
		viewMethodBodiesWithAPISizeRange(697);
		viewMethodBodiesWithAPISizeRange(705);
		viewMethodBodiesWithAPISizeRange(706);
		viewMethodBodiesWithAPISizeRange(772);
		viewMethodBodiesWithAPISizeRange(850);
		viewMethodBodiesWithAPISizeRange(901);
		viewMethodBodiesWithAPISizeRange(902);
		viewMethodBodiesWithAPISizeRange(904);
		viewMethodBodiesWithAPISizeRange(906);
		viewMethodBodiesWithAPISizeRange(908);
		viewMethodBodiesWithAPISizeRange(911);
		viewMethodBodiesWithAPISizeRange(912);
		viewMethodBodiesWithAPISizeRange(913);
		viewMethodBodiesWithAPISizeRange(916);
		viewMethodBodiesWithAPISizeRange(917);
		viewMethodBodiesWithAPISizeRange(918);
		viewMethodBodiesWithAPISizeRange(919);
		viewMethodBodiesWithAPISizeRange(920);
		viewMethodBodiesWithAPISizeRange(921);
		viewMethodBodiesWithAPISizeRange(924);
		viewMethodBodiesWithAPISizeRange(925);
		viewMethodBodiesWithAPISizeRange(926);

	}

	private static void hamidEvaluation() throws ClassNotFoundException, SQLException, IOException {
		
	viewMethodBodiesWithAPISizeRange(928);
	viewMethodBodiesWithAPISizeRange(929);
	viewMethodBodiesWithAPISizeRange(930);
	viewMethodBodiesWithAPISizeRange(931);
	viewMethodBodiesWithAPISizeRange(932);
	viewMethodBodiesWithAPISizeRange(933);
	viewMethodBodiesWithAPISizeRange(935);
	viewMethodBodiesWithAPISizeRange(936);
	viewMethodBodiesWithAPISizeRange(937);
	viewMethodBodiesWithAPISizeRange(938);
	viewMethodBodiesWithAPISizeRange(882);
	viewMethodBodiesWithAPISizeRange(1855);
	viewMethodBodiesWithAPISizeRange(2061);
	viewMethodBodiesWithAPISizeRange(2384);
	viewMethodBodiesWithAPISizeRange(2922);
	viewMethodBodiesWithAPISizeRange(3046);
	viewMethodBodiesWithAPISizeRange(3609);
	viewMethodBodiesWithAPISizeRange(3736);
	viewMethodBodiesWithAPISizeRange(3927);
	viewMethodBodiesWithAPISizeRange(4031);
	viewMethodBodiesWithAPISizeRange(4076);
	viewMethodBodiesWithAPISizeRange(4200);
	viewMethodBodiesWithAPISizeRange(4833);
	viewMethodBodiesWithAPISizeRange(645);
	viewMethodBodiesWithAPISizeRange(4346);
	viewMethodBodiesWithAPISizeRange(4545);
	viewMethodBodiesWithAPISizeRange(527);
	viewMethodBodiesWithAPISizeRange(603);
	viewMethodBodiesWithAPISizeRange(956);
	viewMethodBodiesWithAPISizeRange(1043);
	viewMethodBodiesWithAPISizeRange(1446);
	viewMethodBodiesWithAPISizeRange(1466);
}



	private static void viewMethodBodiesWithAPISizeRange() throws ClassNotFoundException, SQLException, IOException {
//		viewMethodBodiesWithAPISizeRange(2135);
//		viewMethodBodiesWithAPISizeRange(1972);
//		viewMethodBodiesWithAPISizeRange(1926);
//		viewMethodBodiesWithAPISizeRange(1716);
//		viewMethodBodiesWithAPISizeRange(267);
//		viewMethodBodiesWithAPISizeRange(2014);
//		viewMethodBodiesWithAPISizeRange(4901);
//		viewMethodBodiesWithAPISizeRange(3788);		
//		viewMethodBodiesWithAPISizeRange(1091);
//		viewMethodBodiesWithAPISizeRange(2930);
//		viewMethodBodiesWithAPISizeRange(2947);
//		viewMethodBodiesWithAPISizeRange(5250);
//		viewMethodBodiesWithAPISizeRange(2843);
//		viewMethodBodiesWithAPISizeRange(3302);		
//		viewMethodBodiesWithAPISizeRange(422);
//		viewMethodBodiesWithAPISizeRange(172);		
//		viewMethodBodiesWithAPISizeRange(4524);
//		viewMethodBodiesWithAPISizeRange(112);
//		viewMethodBodiesWithAPISizeRange(640);
//		viewMethodBodiesWithAPISizeRange(5089);
//		viewMethodBodiesWithAPISizeRange(2294);
//		viewMethodBodiesWithAPISizeRange(4660);		
//		viewMethodBodiesWithAPISizeRange(113);
//		viewMethodBodiesWithAPISizeRange(1005);		
//		viewMethodBodiesWithAPISizeRange(2908);
//		viewMethodBodiesWithAPISizeRange(2763);
//		viewMethodBodiesWithAPISizeRange(4);
		
		//these methods for emse paper submitted first 14 clone groups
		viewMethodBodiesWithAPISizeRange(19);
		viewMethodBodiesWithAPISizeRange(445);
		viewMethodBodiesWithAPISizeRange(2884);
		viewMethodBodiesWithAPISizeRange(687);
		viewMethodBodiesWithAPISizeRange(2012);
		viewMethodBodiesWithAPISizeRange(2851);
		viewMethodBodiesWithAPISizeRange(338);
		viewMethodBodiesWithAPISizeRange(3811);		
		viewMethodBodiesWithAPISizeRange(1809);
		viewMethodBodiesWithAPISizeRange(491);
		viewMethodBodiesWithAPISizeRange(1000);
		viewMethodBodiesWithAPISizeRange(4032);
		viewMethodBodiesWithAPISizeRange(1708);
		viewMethodBodiesWithAPISizeRange(2);		
		
		//these methods are added after revision
		viewMethodBodiesWithAPISizeRange(1957);
		viewMethodBodiesWithAPISizeRange(2057);
		viewMethodBodiesWithAPISizeRange(1645);
		viewMethodBodiesWithAPISizeRange(1117);
		viewMethodBodiesWithAPISizeRange(2339);
		viewMethodBodiesWithAPISizeRange(2419);
		viewMethodBodiesWithAPISizeRange(489);
		viewMethodBodiesWithAPISizeRange(1852);		
		viewMethodBodiesWithAPISizeRange(206);
		viewMethodBodiesWithAPISizeRange(4254);
		viewMethodBodiesWithAPISizeRange(410);
		viewMethodBodiesWithAPISizeRange(204);
		viewMethodBodiesWithAPISizeRange(331);
		viewMethodBodiesWithAPISizeRange(135);		
		
		
		
	}



	private static void viewMethodBodiesNew() throws ClassNotFoundException,
			SQLException, IOException {
		
		viewMethodBodiesNew(3375);
		/*viewMethodBodiesNew(1972);
		viewMethodBodiesNew(1926);
		viewMethodBodiesNew(1716);
		viewMethodBodiesNew(267);
		viewMethodBodiesNew(2014);
		viewMethodBodiesNew(4901);
		viewMethodBodiesNew(3788);		
		viewMethodBodiesNew(1091);
		viewMethodBodiesNew(2930);
		viewMethodBodiesNew(2947);
		viewMethodBodiesNew(5250);
		viewMethodBodiesNew(2843);
		viewMethodBodiesNew(3302);		
		viewMethodBodiesNew(422);
		viewMethodBodiesNew(172);		
		viewMethodBodiesNew(4524);
		viewMethodBodiesNew(112);
		viewMethodBodiesNew(640);
		viewMethodBodiesNew(5089);
		viewMethodBodiesNew(2294);
		viewMethodBodiesNew(4660);		
		viewMethodBodiesNew(113);
		viewMethodBodiesNew(1005);		
		viewMethodBodiesNew(2908);
		viewMethodBodiesNew(2763);
		viewMethodBodiesNew(4);*/
	}



	private static void printCIDMID() throws ClassNotFoundException,
			SQLException {
		print(2135);
		print(1972);
		print(1926);
		print(1716);
		print(267);
		print(2014);
		print(4901);
		print(3788);		
		print(1091);
		print(2930);
		print(2947);
		print(5250);
		print(2843);
		print(3302);		
		print(422);
		print(172);		
		print(4524);
		print(112);
		print(640);
		print(5089);
		print(2294);
		print(4660);		
		print(113);
		print(1005);		
		print(2908);
		print(2763);
		print(4);
	}



	private static void print(int clusterID) throws ClassNotFoundException, SQLException {
		//printTopThree(clusterID);
		printByAPICallSizeRange(clusterID);

		
	}



	private static void printByAPICallSizeRange(int clusterID) throws ClassNotFoundException, SQLException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();		
		
		
		ArrayList<Method> methods = dbLayer.getMethods(clusterID);
		ArrayList<String> list1 = new ArrayList<String>();
		//and display their bodies
		//iterate over methodIDs and display each method body
		
		
		//for each method get their API counts;
		ArrayList<Integer> APICallsCount = new ArrayList<Integer>();
		 
		for(Method m: methods)
		{
			APICallsCount.add(dbLayer.getMethodUniqueAPICount(m.id));					
		
		}
		
		int smallestMID = 0;
		int largestMID = 0;
		int medianMID = 0;
		
		HashMap<Integer, Integer> methodIDAPICountMap = new HashMap<Integer, Integer>(); 
	
		//populate hashmap
		
		for(int i = 0; i < methods.size(); i++)
		{
			methodIDAPICountMap.put(methods.get(i).id, APICallsCount.get(i));
		}
		
		//sort hashmap
		
		Set<Entry<Integer, Integer>> entries = methodIDAPICountMap.entrySet(); 
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
				
				Set<Entry<Integer, Integer>> entrySetSortedByValue = sortedByValue.entrySet(); 
				
				int elements = entrySetSortedByValue.size();
				int counter = 0;
				for(Entry<Integer, Integer> mapping : entrySetSortedByValue)
				{ 	
					if(counter==0)
						smallestMID = mapping.getKey();								
					
					if(counter == entrySetSortedByValue.size()/2-1)							
						medianMID = mapping.getKey();	
					
					if(counter == entrySetSortedByValue.size()-1)							
						largestMID = mapping.getKey();
					
					//System.out.println(mapping.getKey() + " ==> " + mapping.getValue()); 
					counter+=1;
				}
	
		
		for(Method m: methods)
		{
			if(m.id == smallestMID || m.id == largestMID || m.id == medianMID)
			{			
			System.out.println(clusterID + "," + m.id);
			
			}
			
		}

	}



	private static void printTopThree(int clusterID)
			throws ClassNotFoundException, SQLException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();			
		ArrayList<Method> methods = dbLayer.getMethods(clusterID);
		int methodsToDisplay = 3;
		int count = 0;
		for(Method m: methods)
		{
			count += 1;					
			System.out.println(clusterID+"," + m.id);			
			if (count == methodsToDisplay)
				break;
		}		
		
		dbLayer.closeConnector();
	}



	private static void viewMethodBodiesNew(int clusterID) throws ClassNotFoundException, SQLException, IOException{
		//get the method IDs in this cluster along with projectID and fileName 
		System.out.println("**********************************");
		System.out.println("Cluster ID: "+ clusterID);
		System.out.println("**********************************");
		
				DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
				dbLayer.initializeConnectorToDisplayMethodBodies();		
				
				
				ArrayList<Method> methods = dbLayer.getMethods(clusterID);
				ArrayList<String> list1 = new ArrayList<String>();
				//and display their bodies
				//iterate over methodIDs and display each method body
				int methodsToDisplay = 5;
				int count = 0;
				for(Method m: methods)
				{
					count += 1;
					
					ArrayList<String> body = getMethodBody(m);
					
					System.out.println("-----------------------------");
					System.out.println("Project ID:" + m.projectID);
					System.out.println("Method ID:" + m.id);
					System.out.println("Method Name:" + m.name);
					System.out.println("File Name:" + m.file_name);
					System.out.println("Method Body:");
					
					for(String s: body)
					{
						System.out.println(s);
					}					
					
					if(list1.size() == 0)
					{
						list1 = dbLayer.getMethodAPICalls(m.id);
					}
					else
					{
						ArrayList<String> list2 = dbLayer.getMethodAPICalls(m.id);
						list1.retainAll(list2);
						
					}
					//System.out.println("Method API calls:");
					if (count == methodsToDisplay)
						break;
				}
				

			
				System.out.println("Common API Calls:");
				for(String s: list1)
				{
					System.out.println(s);
				}
				dbLayer.closeConnector();
		
	}

	private static void viewMethodBodiesWithAPISizeRange(int clusterID) throws ClassNotFoundException, SQLException, IOException{
		//get the method IDs in this cluster along with projectID and fileName 
		//System.out.println("**********************************");
		System.out.print("Cluster ID: "+ clusterID+",");
		
		//System.out.println("**********************************");
		
				DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
				dbLayer.initializeConnectorToDisplayMethodBodies();		
				
				
				ArrayList<Method> methods = dbLayer.getMethods(clusterID);
				ArrayList<String> list1 = new ArrayList<String>();
				//and display their bodies
				//iterate over methodIDs and display each method body
				
				
				//for each method get their API counts;
				ArrayList<Integer> APICallsCount = new ArrayList<Integer>();
				 
				for(Method m: methods)
				{
					APICallsCount.add(dbLayer.getMethodUniqueAPICount(m.id));					
				
				}
				
				int smallestMID = 0;
				int largestMID = 0;
				int medianMID = 0;
				
				HashMap<Integer, Integer> methodIDAPICountMap = new HashMap<Integer, Integer>(); 
			
				//populate hashmap
				
				for(int i = 0; i < methods.size(); i++)
				{
					methodIDAPICountMap.put(methods.get(i).id, APICallsCount.get(i));
				}
				
				//sort hashmap
				
				Set<Entry<Integer, Integer>> entries = methodIDAPICountMap.entrySet(); 
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
						
						Set<Entry<Integer, Integer>> entrySetSortedByValue = sortedByValue.entrySet(); 
						
						int elements = entrySetSortedByValue.size();
						int counter = 0;
						for(Entry<Integer, Integer> mapping : entrySetSortedByValue)
						{ 	
							if(counter==0)
								smallestMID = mapping.getKey();								
							
							if(counter == entrySetSortedByValue.size()/2-1)							
								medianMID = mapping.getKey();	
							
							if(counter == entrySetSortedByValue.size()-1)							
								largestMID = mapping.getKey();
							
							//System.out.println(mapping.getKey() + " ==> " + mapping.getValue()); 
							counter+=1;
						}
			
				//get two more methods, one with highest api density and one with lowest api density
				int highdensityMID = dbLayer.getHighDensityMethod(clusterID);	
				int lowdensityMID = dbLayer.getLowDensityMethod(clusterID);
				
				for(Method m: methods)
				{
					if(m.id == smallestMID || m.id == largestMID || m.id == medianMID || m.id == highdensityMID || m.id == lowdensityMID)
					{
						/*
					ArrayList<String> body = getMethodBody(m);
					//System.out.println(clusterID+","+m.id);
					System.out.println("-----------------------------");
					System.out.println("Project ID:" + m.projectID);
					System.out.println("Method ID:" + m.id);
					//System.out.println(clusterID +","+m.id);
					System.out.println("Method Name:" + m.name);
					System.out.println("File Name:" + m.file_name);
					System.out.println("Method Body:");
					
					for(String s: body)
					{
						System.out.println(s);
					}				
					*/
					if(list1.size() == 0)
					{
						list1 = dbLayer.getMethodAPICalls(m.id);
					}
					else
					{
						ArrayList<String> list2 = dbLayer.getMethodAPICalls(m.id);
						list1.retainAll(list2);
						
					}
					//break;
					}
					
				}
				

			
				/*System.out.println("Common API Calls:");
				for(String s: list1)
				{
					System.out.println(s);
				}*/
				
				System.out.println(list1.size());
				dbLayer.closeConnector();
		
	}


	private static Integer getMethodUniqueAPICount(int id) {
		// TODO Auto-generated method stub
		return null;
	}



	private static int getMedianIndex(ArrayList<Integer> values) {
		Collections.sort(values);
	     int median;
	     // get count of scores
	     int totalElements = values.size();
	   
	        median = values.get(values.size() / 2);
	
	  return median;
	}



	private static void viewMethodBodies(int clusterID)
			throws ClassNotFoundException, SQLException, IOException {
		//get the method IDs in this cluster along with projectID and fileName 
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();				
		ArrayList<Method> methods = dbLayer.getMethods(clusterID);
		//and display their bodies
		//iterate over methodIDs and display each method body
		for(Method m: methods)
		{
			ArrayList<String> body = getMethodBody(m);
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			System.out.println("-----------------------------");
			System.out.println("Project ID:" + m.projectID);
			System.out.println("Method ID:" + m.id);
			System.out.println("Method Name:" + m.name);
			System.out.println("File Name:" + m.file_name);
			System.out.println("Method Body:");
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		}
		dbLayer.closeConnector();
	}
	
	

	public static ArrayList<String> getMethodBody(Method m) throws IOException
	{
		ArrayList<String> s = new ArrayList<String>();
				
			String file_path = m.file_name;
			int from_line = m.from_line_num;
			int to_line = m.to_line_num;
			
			int line_num = from_line;
			try{
			while(line_num <= to_line)
			{
				String line = Files.readAllLines(Paths.get(file_path)).get(line_num-1);
				s.add(line);
				line_num++;
			}
			}catch(Exception ex)
			{
				System.out.println("No data available...something wrong with file");
			}
		
		return s; 
		
	}
	
}
