package related_methods._4_RetrieveRelatedFeatures;

import RelatedMethods.db_access_layer.DatabaseAccessLayer;
import RelatedMethods.db_access_layer.EvaluationDAL;
import related_methods.APIUsageSequenceExtraction.APIUsageSequenceExtraction;
import related_methods._3_PopulateRelatedFeatures.ViewSampleMethodForClusters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

;

/**
 * @author shamsa
 * 
 */
public class RetrieveRelatedFeatures {
	public static DatabaseAccessLayer dbLayer2;
	public static int currentStrategy = 1;
	public static int numRecs = 0;
	public static ArrayList<Integer> IDs = new ArrayList<Integer>(); 
	
	public static void main(String args[]) throws Exception
	{		
		dbLayer2 = DatabaseAccessLayer.getInstance();		
		dbLayer2.initializeConnectorToRetrieveRelatedFeatures();
		
		
		int methodID = 4721;//48734;//20064;//58708;
		int clusterID = 24;//5001;//1576;//1584;//1129;//1987;//731;//617;//76;//58;//2249;//69;//484;//443
		int projectID = 13;//68;//32;//90;//85;//100;//18;//53;//90;

		//retrieveStrategy1(clusterID);
		//retrieveStrategy2(clusterID, projectID);
		//retrieveFromCallGraph(methodID, projectID);//this method was used for the manual results of ESEC paper i guess
		//retrieveUsingFACERS8(methodID, projectID);
		//retrieveUsingFACERS4(methodID, projectID);
		
		retrieveUsingFACERFINAL(methodID, projectID);
		System.out.println("No. of recs: " + numRecs);
		showIDs(IDs);
	}
	
	public static void showIDs(ArrayList<Integer> IDs)
	{
		System.out.println("Related IDs are");
		for(Integer i: IDs){
			
			System.out.println(i);
		}
	}

	private static void retrieveUsingFACERFINAL(int methodID, int projectID) throws Exception {

		currentStrategy = 1;
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		if(!dbLayer.relatedFeaturesInitialized)
		 dbLayer.initializeConnectorToRetrieveRelatedFeatures();	
		
		projectID = dbLayer.getProjectID(methodID);

		//is method a clone class member? get cluster ID against method
		int clusterID = dbLayer.getClusterID(methodID);

		if(clusterID != -1)//method belongs to a clone class
		{
			ArrayList<Integer> featureIDs = dbLayer.getFeatureIDs(clusterID);
			if(featureIDs != null & featureIDs.size()!=0)//method belongs to a clone structure
			{
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
				System.out.println("From MCS of selected function");
				IDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID));	
				
				numRecs += array.length;
			}
			if(featureIDs.size()!=0 || numRecs < 10)
			{
				//neighborhoodRetrievalplusMCSFile(methodID, projectID);
				nextRetrievalStrategy(methodID, projectID);
			}

		}
		else

		{
			
			//neighborhoodRetrievalplusMCSFile(methodID, projectID);
			nextRetrievalStrategy(methodID, projectID);
		}
		

	}
	
	
	
	private static void neighborhoodRetrievalplusMCSFile(int methodID,
			int projectID) throws Exception {	

		currentStrategy = 2;
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		if(!dbLayer.relatedFeaturesInitialized)
		 dbLayer.initializeConnectorToRetrieveRelatedFeatures();	
		
		//get from neighborhood
		//get called methods
		LinkedHashMap<Integer, Integer> calledMethodsList = APIUsageSequenceExtraction.getCalledMethods2(methodID);
		ArrayList<Integer> calledMethodIDsList = new ArrayList<Integer>();
		// Get a set of the entries
		Set set = calledMethodsList.entrySet();		      
		// Get an iterator
		Iterator it = set.iterator();		      
		// Display elements
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry)it.next();
			calledMethodIDsList.add((Integer) me.getKey());
		}
		ArrayList<Integer> cumulatingMethodIDs = new ArrayList<Integer>();
		//ArrayList<Integer> descendantMethodsList = APIUsageSequenceExtraction.getDescendantMethodIDs(methodID, cumulatingMethodIDs);
		//calledMethodIDsList = APIUsageSequenceExtraction.getDescendantMethodIDs(methodID, cumulatingMethodIDs);

		//get host methods
		ArrayList<Integer> ancestorMethodsList = APIUsageSequenceExtraction.getHostMethods(methodID);
		//combine host methods with called methods
		calledMethodIDsList.addAll(ancestorMethodsList);
		//calledMethodIDsList.add(methodID);//add user selected method too 
		ArrayList<Integer> clusterIDsList = new ArrayList<Integer>();

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			//get MCS against the neighborhood methods and their composing clusterIDs
			
			for (int mID: calledMethodIDsList)
			{

				int cID = dbLayer.getClusterID(mID);
				if(cID!= -1)
				{
					ArrayList<Integer> featureIDs2 = dbLayer.getFeatureIDs(cID);
					if(featureIDs2.size()!=0)
					{
						//add the clusterID to clusterIDsList
						if(!clusterIDsList.contains(cID))
						{
							clusterIDsList.add(cID);
						}
					}

				}
			}
			//if none of the neighborhood methods is part of an MCS
			//if neighborhood methods exist then proceed else same file MCS retrieval
			if(clusterIDsList.size()==0)
			{
				//simply use the neighborhood methods API usages
				if(calledMethodIDsList.size()>0)
				{
					currentStrategy=3;
					for(int mID: calledMethodIDsList)
					{			
						System.out.println("From Neighborhood of Same File");
						ViewSampleMethodForClusters.viewMethod(mID);	
						IDs.add(mID);
						numRecs += 1;
					}	
				}
				if(calledMethodIDsList.size()==0 || numRecs < 10)
				{
					//System.out.println("No recommendations: no MCS membership and no neighborhood API calls");
					//sameFileMCSRetrieval(methodID, projectID);
					nextRetrievalStrategy(methodID, projectID);
				}

				
			}
			else
				//if MCS membership of neighborhood methods leads to cluster IDs
			{
				currentStrategy = 2;
				Integer[] array = null;
				ArrayList<Integer> merged = new ArrayList<Integer>();
				for(int cID: clusterIDsList)
				{
					//index+=1;
					//display the related features/functions

					array = (RetrieveRelatedFeatures.retrieveRelatedClusterIDs(cID));
					System.out.println("From Neighborhoods MCS");
					IDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID));
					numRecs += array.length;
				}
			}

		}
		if(calledMethodIDsList.size() == 0 || numRecs < 10)
		{
			//System.out.println("No recommendations: no call-graph based neighborhood");
			//sameFileMCSRetrieval(methodID,projectID);
			nextRetrievalStrategy(methodID, projectID);
		}

	}
	
	public static void nextRetrievalStrategy(int methodID, int projectID) throws Exception
	{
		switch(currentStrategy){  
		    //Case statements  
		    case 1: neighborhoodRetrievalplusMCSFile(methodID, projectID);  
		    break;  
		    case 2: sameFileMCSRetrieval(methodID, projectID); //this will never execute
		    break;  
		    case 3: sameFileMCSRetrieval(methodID, projectID);  
		    break;  
		    //case 4: sameFileRetrieval(methodID);  
		    //break;  
		    //Default case statement  
		    default:System.out.println("No more recommendations");  
	    }  
		
		
	}

	private static void sameFileMCSRetrieval(int methodID, int projectID) throws Exception, SQLException, IOException {

		currentStrategy = 4;
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		if(!dbLayer.relatedFeaturesInitialized)
		 dbLayer.initializeConnectorToRetrieveRelatedFeatures();	
		
		//1.get distinct API usages from the API call table of the held out project
		//get the other methods of the same file as the user selected input methodID
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();

		methodIDsList = dbLayer.getSameFileMethods(methodID);			

		ArrayList<Integer> clusterIDsList = new ArrayList<Integer>();
		for(int mID: methodIDsList)
		{				
			int cID = dbLayer.getClusterID(mID);
			if(cID!= -1)
			{
				ArrayList<Integer> featureIDs2 = dbLayer.getFeatureIDs(cID);
				if(featureIDs2.size()!=0)
				{
					//add the clusterID to clusterIDsList
					if(!clusterIDsList.contains(cID))
					{
						clusterIDsList.add(cID);
					}
				}

			}
		}
		//if neighborhood methods is part of an MCS
		if(clusterIDsList.size()!=0)

			//if MCS membership of neighborhood methods leads to cluster IDs
		{
			
			Integer[] array = null;
			for(int cID: clusterIDsList)
			{
				//index+=1;
				//display the related features/functions
				array = (RetrieveRelatedFeatures.retrieveRelatedClusterIDs(cID));	
				System.out.println("From Same File MCS");
				IDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID));	
				numRecs += array.length;
			}						
											
		}
		//uncomment below line to see methods
		//ViewSampleMethodForClusters.viewMethods(methodIDsList);
		else
		{
			nextRetrievalStrategy(methodID, projectID);
		}




	}

	private static void retrieveUsingFACERS8(int methodID, int projectID) throws Exception {

		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();		

		//get from neighborhood
			//get called methods
			LinkedHashMap<Integer, Integer> calledMethodsList = APIUsageSequenceExtraction.getCalledMethods2(methodID);
			ArrayList<Integer> calledMethodIDsList = new ArrayList<Integer>();
			
			Set set = calledMethodsList.entrySet();  		
			Iterator it = set.iterator();
			while(it.hasNext()) {
				Map.Entry me = (Map.Entry)it.next();
				calledMethodIDsList.add((Integer) me.getKey());
			}
			ArrayList<Integer> cumulatingMethodIDs = new ArrayList<Integer>();
			//ArrayList<Integer> descendantMethodsList = APIUsageSequenceExtraction.getDescendantMethodIDs(methodID, cumulatingMethodIDs);
			//calledMethodIDsList = APIUsageSequenceExtraction.getDescendantMethodIDs(methodID, cumulatingMethodIDs);
	
			//get host methods
			ArrayList<Integer> ancestorMethodsList = APIUsageSequenceExtraction.getHostMethods(methodID);
			//combine host methods with called methods
			calledMethodIDsList.addAll(ancestorMethodsList);
		//calledMethodIDsList.add(methodID);//add user selected method too 
		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			//get MCS against the neighborhood methods and their composing clusterIDs
			ArrayList<Integer> clusterIDsList = new ArrayList<Integer>();
			for (int mID: calledMethodIDsList)
			{

				int cID = dbLayer2.getClusterID(mID);
				if(cID!= -1)
				{
					ArrayList<Integer> featureIDs2 = dbLayer2.getFeatureIDs(cID);
					if(featureIDs2.size()!=0)
					{
						//add the clusterID to clusterIDsList
						if(!clusterIDsList.contains(cID))
						{
							clusterIDsList.add(cID);
						}
					}

				}
			}
			//if none of the neighborhood methods is part of an MCS
			if(clusterIDsList.size()==0)
			{
				if(calledMethodIDsList.size()>0)
				{
					for(int mID: calledMethodIDsList)
					{			
						ViewSampleMethodForClusters.viewMethod(mID);		
					}	
				}
				else
				{
					//System.out.println("No recommendations: no MCS membership and no neighborhood API calls");
					sameFileRetrieval(methodID);
				}
			}
			else
				//if MCS membership of neighborhood methods leads to cluster IDs
			{
				Integer[] array = null;
				ArrayList<Integer> merged = new ArrayList<Integer>();
				for(int cID: clusterIDsList)
				{
					//index+=1;
					//display the related features/functions

					array = (RetrieveRelatedFeatures.retrieveRelatedClusterIDs(cID));
					for(int x: array)
					{
						if(!merged.contains(x))
							merged.add(x);
					}
					ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
					
				}

				

			}

		}
		else
		{
			//System.out.println("No recommendations: no call-graph based neighborhood");
			sameFileRetrieval(methodID);
		}

	}
	private static void sameFileRetrieval(int methodID) throws Exception
	{		
		System.out.println("From Same File");
		//get the other methods of the same file as the user selected input methodID
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();		
		methodIDsList = dbLayer2.getSameFileMethods(methodID);	
		IDs.addAll(methodIDsList);
		numRecs+=methodIDsList.size();
		ViewSampleMethodForClusters.viewMethods(methodIDsList);
	}

	private static void retrieveFromCallGraph(int methodID, int projectID) throws Exception {
			
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		if(!dbLayer.relatedFeaturesInitialized)
		 dbLayer.initializeConnectorToRetrieveRelatedFeatures();	
		
		ArrayList<Integer> cumulatingMethodIDs = new ArrayList<Integer>();
		//get called methods
		LinkedHashMap<Integer, Integer> calledMethodsList = APIUsageSequenceExtraction.getCalledMethods2(methodID);
		ArrayList<Integer> calledMethodIDsList = new ArrayList<Integer>();
		
	      Set set = calledMethodsList.entrySet();
	      Iterator i = set.iterator();
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         calledMethodIDsList.add((Integer) me.getKey());
	      }
		//comment below line to allow only immediate call methods
	    //calledMethodIDsList = APIUsageSequenceExtraction.getDescendantMethodIDs(methodID, cumulatingMethodIDs);
	    //get host methods
		ArrayList<Integer> ancestorMethodsList = APIUsageSequenceExtraction.getHostMethods(methodID);
		calledMethodIDsList.addAll(ancestorMethodsList);
		//if neighborhood methods list is null then take the methods in the same file
		if(calledMethodIDsList.size()==0)
		{
			//System.out.println("here");
			calledMethodIDsList = dbLayer.getSameFileMethods(methodID);
		}
		
		//get MCS and clusterIDs against the neighborhood methods
		ArrayList<Integer> clusterIDsList = new ArrayList<Integer>();
		for (int mID: calledMethodIDsList)
		{
			int cID = dbLayer.getClusterID(mID);
			if(cID!= -1)
			{
				ArrayList<Integer> featureIDs = dbLayer.getFeatureIDs(cID);
				if(featureIDs.size()!=0)
				{
					//add the clusterID to clusterIDsList
					if(!clusterIDsList.contains(cID))
						{
							clusterIDsList.add(cID);
						}
				}
			}
			
		}

		//new added if - if no MCS membership, get same file methods and their MCS
		if(clusterIDsList.size()==0)
		{
			calledMethodIDsList = dbLayer.getSameFileMethods(methodID);
			for (int mID: calledMethodIDsList)
			{
				int cID = dbLayer.getClusterID(mID);
				if(cID!= -1)
				{
					ArrayList<Integer> featureIDs = dbLayer.getFeatureIDs(cID);
					if(featureIDs.size()!=0)
					{
						//add the clusterID to clusterIDsList
						if(!clusterIDsList.contains(cID))
							{
								clusterIDsList.add(cID);
							}
					}
				}
				
			}
		}
		
		int recommendations = 0;
		Integer[] array =  null;		
		for(int cID: clusterIDsList)
		{
			//display the related features/functions
			
			array = retrieveRelatedClusterIDs(cID);
			recommendations += array.length;
			//if(recommendations>=30)
			//{
				//recommendations -= array.length;
				//break;
			//}
			ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);	
		}
				
		
		System.out.println("No. of results: " + recommendations);
		
	}

	private static void retrieveStrategy1(int clusterID) throws SQLException,
			ClassNotFoundException, IOException {
		
		System.out.println("Starting!");		
		Integer[] array;		
		array = retrieveRelatedClusterIDs(clusterID);
		ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array);

	}
	
	private static void retrieveStrategy2(int clusterID, int projectID) throws SQLException,
	ClassNotFoundException, IOException {
		
		System.out.println("Starting!");		
		Integer[] array;		
		array = retrieveRelatedClusterIDs(clusterID);
		ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
	}
	

	

	public static Integer[] retrieveRelatedClusterIDs(
			int clusterID) throws SQLException, ClassNotFoundException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		if(!dbLayer.relatedFeaturesInitialized)
			dbLayer.initializeConnectorToRetrieveRelatedFeatures();	
		//1. retrieve all featureIDs against a selected clusterID
		ArrayList<Integer> featureIDs = dbLayer.getFeatureIDs(clusterID);
		//2. for each feature, get the member clusterIDs except for user selected clusterID
		String[] clustersList;
		ArrayList<Integer> clusterIDsList = new ArrayList<Integer>();
		clusterIDsList.add(clusterID);//adding the input cluster first
		for(Integer fID: featureIDs)
		{
			ArrayList<Integer> IDs = dbLayer.getclusterIDs(clusterID, fID);
			for (int id: IDs)
				if(!clusterIDsList.contains(id))
					clusterIDsList.add(id);
		}
		
		Integer[] array = new Integer[clusterIDsList.size()];
		array = clusterIDsList.toArray(array);
		//3. call the viewMethodsAgainstClusterIDs method to get related methods 
		//(these clusters contain the top representative method for now)
		//dbLayer.closeConnector();	
		return array;
	}
	
}