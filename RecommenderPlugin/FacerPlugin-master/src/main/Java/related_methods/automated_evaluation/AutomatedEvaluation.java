package related_methods.automated_evaluation;

import RelatedMethods.DataObjects.Method;
import RelatedMethods.CustomUtilities.Constants;
import RelatedMethods.CustomUtilities.HashMapSorter;
import related_methods._3_PopulateRelatedFeatures.ViewSampleMethodForClusters;
import related_methods._4_RetrieveRelatedFeatures.RetrieveRelatedFeatures;
import RelatedMethods.db_access_layer.DatabaseAccessLayer;
import RelatedMethods.db_access_layer.EvaluationDAL;
import related_methods.APIUsageSequenceExtraction.APIUsageSequenceExtraction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author shamsa
 * calculate the precision automatically
 *
 */
public class AutomatedEvaluation {

	//sample size is the number of functions from which APIs are recommended by some strategy
	public static int sample_size = 0;
	public static int total_sample_size = 0;
	public static ArrayList<Integer> IDs = new ArrayList<Integer>();
	
	public static final int SAME_FILE_RETRIEVAL_STRATEGY = 0;
	public static final int SAME_PROJECT_RETRIEVAL_STRATEGY = 1;
	public static final int MCS_RETRIEVAL_STRATEGY = 2;//if selected function belongs to some MCS, recommend from it
	
	public static final int NEIGHBORHOOD_RETRIEVAL_STRATEGY = 5;//find MCS of neighborhood methods to recommend from them, otherwise if no MCS of neighbors, then use simple neighbors
	public static final int NEIGHBORHOOD_PLUS_FILE_RETRIEVAL_STRATEGY = 8;// 5 plus 0
	
	public static final int MCS_PLUS_NEIGHBORHOOD_RETRIEVAL_STRATEGY = 3;//2 plus 5
	public static final int MCS_PLUS_NEIGHBORHOOD_FILE_RETRIEVAL_STRATEGY = 4;//2 plus 8
	
	
	public static final int JUST_NEIGHBORHOOD_RETRIEVAL_STRATEGY = 6;//recommend APIS from functions in neighborhood call-graph
	
	public static final int NEIGHBORHOOD_PLUS_MCS_RETRIEVAL_STRATEGY = 7;//5 plus 2	
	
	public static final int MCS_NEIGHBORHOOD_COMBINED_PLUS_FILE_RETRIEVAL_STRATEGY = 9;
	
	public static final int NEIGHBORHOOD_COMBINED_STRATEGY = 10;
	
	public static final int NEIGHBORHOODSMCS_RETRIEVAL_STRATEGY = 11;
	
	public static final int FACER_RETRIEVAL_STRATEGY = 12;
	
	public static final int MCMILLAN_STRATEGY = 13;
	
	public static final int ContextAware_FACER_STRATEGY = 14;
	
	public static DatabaseAccessLayer dbLayer2;
	
	public static ArrayList<Integer> relatedMethods = new ArrayList<Integer>();
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{	
		
		dbLayer2 = DatabaseAccessLayer.getInstance();		
		dbLayer2.initializeConnectorToRetrieveRelatedFeatures();
		
		int inputClusterID = 5001;
		int projectID = 62;
		int methodID = 38212;	
		boolean contextual = false;
		//evaluationStrategy1(inputClusterID);
		//evaluationStrategy2(inputClusterID, projectID);
		//evaluationStrategy3(methodID, projectID, contextual);

		//ViewSampleMethodForClusters.viewMethod(49028);
		//getMcsPlusNeighborhoodPlusFileRetrieval(49028, 68);		
		
		int strategy = FACER_RETRIEVAL_STRATEGY;
		
		if (strategy == FACER_RETRIEVAL_STRATEGY)
		{
//			System.out.println("music");
//			System.out.println("Q1");
//			InputParameter parameter = new InputParameter(0, 38212, 62);	
//			evaluateQueryUsingFACER(10, "music", strategy, parameter);
//			System.out.println("Q2");
//			 parameter = new InputParameter(5001,	49028,	68);	
//			evaluateQueryUsingFACER(10, "music", strategy, parameter);
//			System.out.println("Q3");
//			 parameter = new InputParameter(738,	3118,	12);	
//			evaluateQueryUsingFACER(10, "music", strategy, parameter);
//			
//			System.out.println("chat");
//			System.out.println("Q4");
//			 parameter = new InputParameter(0,	20064,	32);	
//			evaluateQueryUsingFACER(10, "chat", strategy, parameter);
//			
//			System.out.println("Q5");
//			 parameter = new InputParameter(0,	21564,	44);	
//			evaluateQueryUsingFACER(10, "chat", strategy, parameter);
//			
//			System.out.println("weather");
//			System.out.println("Q6");
//			 parameter = new InputParameter(0,	50126,	70);	
//			evaluateQueryUsingFACER(3, "weather", strategy, parameter);
//			
			System.out.println("file");
			System.out.println("Q7");
			InputParameter parameter = new InputParameter(190,	748,	3);	
			evaluateQueryUsingFACER(3, "file", strategy, parameter);	
			System.out.println("Q8");
			 parameter = new InputParameter(3091,	23145,	48);	
			evaluateQueryUsingFACER(3, "file", strategy, parameter);
			System.out.println("Q9");
			 parameter = new InputParameter(1173,	13188,	24);	
			evaluateQueryUsingFACER(3, "file", strategy, parameter);
			System.out.println("Q10");
			
//			//parameter = new InputParameter(1534,	22653,	47);//for free memory kill tasks	
//			
			 parameter = new InputParameter(2738, 20104,	33);//for scan for bluetooth devices to pair
			evaluateQueryUsingFACER(10, "chat", strategy, parameter);
			
			System.out.println("Sample size :" + sample_size);
			/*System.out.println("Related IDs are");
			for(Integer i: IDs){
				
				System.out.println(i);
			}*/
			
			
			
			
		}
		else
		{
			/*InputParameter parameter = new InputParameter(0, 38212, 62);	
			evaluateQuery(10, "music", strategy, parameter);
			
			parameter = new InputParameter(0,	20064,	32);	
			evaluateQuery(10, "chat", strategy, parameter);
			
			parameter = new InputParameter(0,	50126,	70);	
			evaluateQuery(3, "weather", strategy, parameter);
			
			parameter = new InputParameter(190,	748,	3);	
			evaluateQuery(3, "file", strategy, parameter);
			
			parameter = new InputParameter(3091,	23145,	48);	
			evaluateQuery(3, "file", strategy, parameter);
			
			parameter = new InputParameter(0,	21564,	44);	
			evaluateQuery(10, "chat", strategy, parameter);
			
			parameter = new InputParameter(5001,	49028,	68);	
			evaluateQuery(10, "music", strategy, parameter);
			
			parameter = new InputParameter(738,	3118,	12);	
			evaluateQuery(10, "music", strategy, parameter);
			
			parameter = new InputParameter(1173,	13188,	24);	
			evaluateQuery(3, "file", strategy, parameter);
			
			parameter = new InputParameter(1534,	22653,	47);	
			evaluateQuery(3, "file", strategy, parameter);*/
			
			
			InputParameter parameter = new InputParameter(0, 38212, 62);	
			evaluateQuery(10, "music", strategy, parameter);
			
			parameter = new InputParameter(5001,	49028,	68);	
			evaluateQuery(10, "music", strategy, parameter);
			
			parameter = new InputParameter(738,	3118,	12);	
			evaluateQuery(10, "music", strategy, parameter);			
			
			parameter = new InputParameter(0,	20064,	32);	
			evaluateQuery(10, "chat", strategy, parameter);
			
			parameter = new InputParameter(0,	21564,	44);	
			evaluateQuery(10, "chat", strategy, parameter);			
			
			parameter = new InputParameter(0,	50126,	70);	
			evaluateQuery(3, "weather", strategy, parameter);			
			
			parameter = new InputParameter(190,	748,	3);	
			evaluateQuery(3, "file", strategy, parameter);	
			
			parameter = new InputParameter(3091,	23145,	48);	
			evaluateQuery(3, "file", strategy, parameter);
			
			parameter = new InputParameter(1173,	13188,	24);	
			evaluateQuery(3, "file", strategy, parameter);
			
			parameter = new InputParameter(1534,	22653,	47);//for free memory kill tasks				
			//parameter = new InputParameter(2738, 20104,	33);//for scan for bluetooth devices to pair
			evaluateQuery(10, "chat", strategy, parameter);			
			
			System.out.println("Sample size :" + sample_size);
			System.out.println("Total Sample size :" + total_sample_size);
			
		}
		
		/*InputParameter parameter = new InputParameter(0, 38212, 62);	
		evaluateQuery(10, "music", strategy, parameter);
		
		parameter = new InputParameter(0,	20064,	32);	
		evaluateQuery(10, "chat", strategy, parameter);
		
		parameter = new InputParameter(0,	50126,	70);	
		evaluateQuery(3, "weather", strategy, parameter);
		
		parameter = new InputParameter(190,	748,	3);	
		evaluateQuery(3, "file", strategy, parameter);
		
		parameter = new InputParameter(3091,	23145,	48);	
		evaluateQuery(3, "file", strategy, parameter);
		
		parameter = new InputParameter(0,	21564,	44);	
		evaluateQuery(10, "chat", strategy, parameter);
		
		parameter = new InputParameter(5001,	49028,	68);	
		evaluateQuery(10, "music", strategy, parameter);
		
		parameter = new InputParameter(738,	3118,	12);	
		evaluateQuery(10, "music", strategy, parameter);
		
		parameter = new InputParameter(1173,	13188,	24);	
		evaluateQuery(3, "file", strategy, parameter);
		
		parameter = new InputParameter(1534,	22653,	47);	
		evaluateQuery(3, "file", strategy, parameter);*/
		
		
	}

	private static void showMethodBodies(ArrayList<Integer> uniqueMethods) throws Exception, SQLException, IOException {
		ViewSampleMethodForClusters.viewMethods(uniqueMethods);
		
	}

	private static void evaluateQuery(int numHoldOut, String category, int strategy, InputParameter parameter) throws Exception
	{
		//System.out.println("Test: " + category);
		for(int i=1;i<=numHoldOut;i++)
		{	
			sample_size = 0;
			Constants.HELDOUT_PROJECT_DATABASE = "jdbc:mysql://localhost/heldout_" + category + "_" + i + "?useSSL=false&user=root";
			//System.out.println("SubTest: " + i);
			executeStrategy(strategy, parameter);	
			System.out.println("Sample size actual: " + sample_size);
		}
		total_sample_size += sample_size;
	}
	
	private static void evaluateQueryUsingFACER(int numHoldOut, String category, int strategy, InputParameter parameter) throws Exception
	{
		relatedMethods = getRelatedMethodIDsUsingFACERRetrieval(parameter.methodID, parameter.projectID);
		//remove repeating method IDs
		ArrayList<Integer> uniqueMethods = new ArrayList<Integer>();
		int uniqueSampleSize = 0;
		for(int i: relatedMethods)
		{
			if(!uniqueMethods.contains(i))
				{
					uniqueMethods.add(i);
					uniqueSampleSize += 1;
				}
		}
		//show related methods
//		System.out.println("Related Methods using FACER retrieval are:");
//		for(int i: uniqueMethods)
//		{
//			System.out.println(i);
//		}
		
		System.out.println("Unique Sample Size: " + uniqueSampleSize);		
		
		//showMethodBodies
		//showMethodBodies(uniqueMethods);
		
		//getPrecisionRecall
		evaluatePrecisionRecall(numHoldOut, category, strategy, parameter);				
		
	}
	
	
	private static void evaluatePrecisionRecall(int numHoldOut, String category, int strategy, InputParameter parameter) throws Exception
	{
		//System.out.println("Test: " + category);
		for(int i=1;i<=numHoldOut;i++)
		{	
			sample_size = 0;
			Constants.HELDOUT_PROJECT_DATABASE = "jdbc:mysql://localhost/heldout_" + category + "_" + i + "?useSSL=false&user=root";
			//System.out.println("SubTest: " + i);
			getPrecisionRecall(strategy, parameter);	
			
		}
	}
	
	private static void getPrecisionRecall(int strategy,
			InputParameter parameter) throws Exception {
		
		//1.get distinct API usages from the API call table of the held out project
				Set<String> HeldOutAPIUsages = new HashSet<>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getHashedAPIUsages();	
					
				Set<String> recommendedAPIUsages = new HashSet<>();
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: relatedMethods)
				{		
					Set<String> retrievedAPIUsages = new HashSet<>();
					retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
					for(String usage: retrievedAPIUsages)
					{
						if(!recommendedAPIUsages.contains(usage))
						{
							recommendedAPIUsages.add(usage);
						}			
					}
				}
				dbLayer.closeConnector();	
				
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = recommendedAPIUsages.size();
				
				for(String usage: recommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}			
				}
				
				float precision = (float)matchingAPICalls/totalAPICallsRecommended;
				System.out.println("Precision: " + precision);
				
				//calculate recall		
				float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
				System.out.println("Recall: " + recall);
	
	}

	private static void executeStrategy(int strategy, InputParameter parameter) throws Exception
	{
		if(strategy == MCMILLAN_STRATEGY)//0
		{
			if(parameter.clusterID!=0)
			mcMillanRetrieval(parameter.methodID, parameter.clusterID, parameter.projectID, false);
			
		}
		if(strategy == ContextAware_FACER_STRATEGY)//0
		{
			if(parameter.clusterID!=0)
				mcMillanRetrieval(parameter.methodID, parameter.clusterID, parameter.projectID, true);
		
		}
		
		
		
		if(strategy == SAME_FILE_RETRIEVAL_STRATEGY)//0
			sameFileRetrieval(parameter.methodID);
		if(strategy == SAME_PROJECT_RETRIEVAL_STRATEGY)//1
			sameProjectRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == MCS_RETRIEVAL_STRATEGY)//2
			mcsRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == MCS_PLUS_NEIGHBORHOOD_RETRIEVAL_STRATEGY)//3
			mcsPlusNeighborhoodRetrieval2(parameter.methodID, parameter.projectID);
		if(strategy == MCS_PLUS_NEIGHBORHOOD_FILE_RETRIEVAL_STRATEGY)//4
			mcsPlusNeighborhoodPlusFileRetrieval2(parameter.methodID, parameter.projectID);
		if(strategy == NEIGHBORHOOD_RETRIEVAL_STRATEGY)//5
			neighborhoodRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == JUST_NEIGHBORHOOD_RETRIEVAL_STRATEGY)//6
			justNeighborhoodRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == NEIGHBORHOOD_PLUS_MCS_RETRIEVAL_STRATEGY)//7
			neighborhoodPlusMCSRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == NEIGHBORHOOD_PLUS_FILE_RETRIEVAL_STRATEGY)//8
			neighborhoodPlusFileRetrieval(parameter.methodID, parameter.projectID);		
		if(strategy == MCS_NEIGHBORHOOD_COMBINED_PLUS_FILE_RETRIEVAL_STRATEGY)//9
			mcsNeighborhoodCombinedPlusFileRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == NEIGHBORHOOD_COMBINED_STRATEGY)//10
			neighborhoodCombined(parameter.methodID, parameter.projectID);
		if(strategy == NEIGHBORHOODSMCS_RETRIEVAL_STRATEGY)//11
			neighborhoodsMCSRetrieval(parameter.methodID, parameter.projectID);
		if(strategy == FACER_RETRIEVAL_STRATEGY)
			//FACERRetrieval(parameter.methodID, parameter.projectID);
			relatedMethods = getRelatedMethodIDsUsingFACERRetrieval(parameter.methodID, parameter.projectID);
				
			
			
	}
	
	private static void mcMillanRetrieval(int methodID, int clusterID,
			int projectID, boolean isWeightedByMCS) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	 //selects distinct API usages in query
				
				//get the other methods of the same file as the user selected input methodID
				ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
				
				
				methodIDsList = McMillanEvaluation.getMcMillanMethodRecommendations(methodID, clusterID, projectID, isWeightedByMCS, 0);
				IDs.addAll(methodIDsList);
				sample_size += methodIDsList.size();
				System.out.println("McMillan : "+ methodIDsList.size());
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages = new HashSet<>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: methodIDsList)
				{			
					Set<String> retrievedAPIUsages = new HashSet<>();
					retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
					RecommendedAPIUsages.addAll(retrievedAPIUsages);			
					
				}
				//uncomment below line to see methods
				//ViewSampleMethodForClusters.viewMethods(methodIDsList);
				dbLayer.closeConnector();	
				
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}			
				}
				//number of methods recommended
				
				//System.out.println("No. of methods recommended: " + methodIDsList.size());
				//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
				//calculate precision		
				float precision = (float)matchingAPICalls/totalAPICallsRecommended;
				System.out.println("Precision: " + precision);
				
				//calculate recall		
				float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
				System.out.println("Recall: " + recall);
				//hide for now
				//showMethodBodies(methodIDsList);
		
	}

	private static void neighborhoodCombined(int methodID, int projectID)throws Exception {
		//1.get distinct API usages from the API call table of the held out project
				
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();		
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeHeldoutDatabaseConnector();		
		HeldOutAPIUsages = dbLayer.getAPIUsages();	
		Set<String> recommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);
		
		
			dbLayer.initializeFoldDatabaseConnector();	
			for(int mID: calledMethodIDsList)
			{			
				recommendedAPIUsages.addAll(dbLayer.getHashedMethodAPICalls(mID));			

			}
			dbLayer.closeConnector();
			
		//add the neighbor methods MCS based functions	
			
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);				

			if(clusterIDsList.size()!=0)
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
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				}

				Integer[] ret = new Integer[merged.size()];
				for (int i=0; i < ret.length; i++)
				{ 
					ret[i] = merged.get(i);
				} 
				Set<String> RecommendedAPIUsagesFromMCS = new HashSet<String>();
				RecommendedAPIUsagesFromMCS = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

				recommendedAPIUsages.addAll(RecommendedAPIUsagesFromMCS);	
			}
	
			dbLayer.closeConnector();	

			
			//compare the API usages in hold out project with the API usages recommended
			int matchingAPICalls = 0;
			int totalAPICallsRecommended = recommendedAPIUsages.size();

			for(String usage: recommendedAPIUsages)
			{
				if(HeldOutAPIUsages.contains(usage))
				{
					matchingAPICalls += 1;
				}			
			}
			//number of methods recommended

			//System.out.println("No. of methods recommended: " + methodIDsList.size());
			//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
			//calculate precision		
			if(totalAPICallsRecommended != 0){
				float precision = (float)matchingAPICalls/totalAPICallsRecommended;
				System.out.println("Precision: " + precision);

				//calculate recall		
				float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
				System.out.println("Recall: " + recall);
			}
			else{
				System.out.println("No recommendations: no API calls recommended");
			}
		
		


	}

	private static void mcsNeighborhoodCombinedPlusFileRetrieval(int methodID,
			int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project

				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	

				//2.get the distinct API names from from the related methods recommended from mcs  
				Set<String> RecommendedAPIUsages = new HashSet<String>();				
				dbLayer.initializeFoldDatabaseConnector();
				
				//get recommendations from neighborhood
				Set<String> RecommendedAPIUsagesFromNeighborhood = getNeighborhoodRecommendations(methodID, projectID);

				//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
				//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

				//is method a clone class member? get cluster ID against method
				int clusterID = dbLayer2.getClusterID(methodID);

				if(clusterID != -1)//method belongs to a clone class
				{
					ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
					if(featureIDs.size()!=0)//method belongs to a clone structure
					{
						//iterate over feature ids list to get the clusterIDs
						Integer[] array = null;
						array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
						RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
						if(RecommendedAPIUsagesFromNeighborhood!=null)
							RecommendedAPIUsages.addAll(RecommendedAPIUsagesFromNeighborhood);
						//compare the API usages in hold out project with the API usages recommended
						int matchingAPICalls = 0;
						int totalAPICallsRecommended = RecommendedAPIUsages.size();
						//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
						for(String usage: RecommendedAPIUsages)
						{
							if(HeldOutAPIUsages.contains(usage))
							{
								matchingAPICalls += 1;
							}	
						}
						
						if(totalAPICallsRecommended > 0)
						{				
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);
							
							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else
						{
							//System.out.println("No recommendations: No API usages recommended or matching");
							sameFileRetrieval(methodID);
						}
						
						
					}
					else
					{
						//System.out.println("No recommendations: no MCS membership");
						sameFileRetrieval(methodID);
					}

				}
				else

				{
					//no results
					//System.out.println("No recommendations: no clone membership");
					sameFileRetrieval(methodID);

				}
				
		
	}

	private static void neighborhoodPlusFileRetrieval(int methodID,
			int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	

				//2.get the distinct API names from from the related methods recommended from mcs  
				Set<String> RecommendedAPIUsages = new HashSet<String>();
				dbLayer.initializeFoldDatabaseConnector();

				ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);
				//calledMethodIDsList.add(methodID);//add user selected method too 
				//if neighborhood methods exist then proceed else do nothing 
				if(calledMethodIDsList.size()!=0)
				{
					ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
					//if none of the neighborhood methods is part of an MCS
					if(clusterIDsList.size()==0)
					{
						//simply use the neighborhood methods API usages
						//get the API usages of those methods
						Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
						dbLayer.initializeFoldDatabaseConnector();	
						for(int mID: calledMethodIDsList)
						{			
							RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			

						}
						dbLayer.closeConnector();	

						if(RecommendedAPIUsages2.size()>0)
						{
							sample_size += calledMethodIDsList.size();
							System.out.println("FACER N: " + calledMethodIDsList.size());
							//compare the API usages in hold out project with the API usages recommended
							int matchingAPICalls = 0;
							int totalAPICallsRecommended = RecommendedAPIUsages2.size();

							for(String usage: RecommendedAPIUsages2)
							{
								if(HeldOutAPIUsages.contains(usage))
								{
									matchingAPICalls += 1;
								}			
							}
							//number of methods recommended

							//System.out.println("No. of methods recommended: " + methodIDsList.size());
							//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);

							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else{
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
							//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
						}

						Integer[] ret = new Integer[merged.size()];
						for (int i=0; i < ret.length; i++)
						{ 
							ret[i] = merged.get(i);
						} 
						
						sample_size += ret.length;
						System.out.println("FACER MCSn: " + ret.length);
						RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

						//compare the API usages in hold out project with the API usages recommended
						int matchingAPICalls = 0;
						int totalAPICallsRecommended = RecommendedAPIUsages.size();
						//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
						for(String usage: RecommendedAPIUsages)
						{
							if(HeldOutAPIUsages.contains(usage))
							{
								matchingAPICalls += 1;
							}
						}

						if(totalAPICallsRecommended != 0){
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);
			
							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else
						{
							//System.out.println("No recommendations: No API calls from MCS of neighbors");
							sameFileRetrieval(methodID);
						}

					}

				}
				else
				{
					//System.out.println("No recommendations: no call-graph based neighborhood");
					sameFileRetrieval(methodID);
				}

		
	}

	private static void neighborhoodPlusMCSRetrieval(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	

				//2.get the distinct API names from from the related methods recommended from mcs  
				Set<String> RecommendedAPIUsages = new HashSet<String>();
				dbLayer.initializeFoldDatabaseConnector();

				ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

				//if neighborhood methods exist then proceed else mcs retrieval
				if(calledMethodIDsList.size()!=0)
				{
					ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
					//if none of the neighborhood methods is part of an MCS
					if(clusterIDsList.size()==0)
					{
						//simply use the neighborhood methods API usages
						//get the API usages of those methods
						Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
						dbLayer.initializeFoldDatabaseConnector();	
						for(int mID: calledMethodIDsList)
						{			
							RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			

						}
						dbLayer.closeConnector();	

						if(RecommendedAPIUsages2.size()>0)
						{
							//compare the API usages in hold out project with the API usages recommended
							int matchingAPICalls = 0;
							int totalAPICallsRecommended = RecommendedAPIUsages2.size();

							for(String usage: RecommendedAPIUsages2)
							{
								if(HeldOutAPIUsages.contains(usage))
								{
									matchingAPICalls += 1;
								}			
							}
							//number of methods recommended

							//System.out.println("No. of methods recommended: " + methodIDsList.size());
							//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);

							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else{
							//System.out.println("No recommendations: no MCS membership and no neighborhood API calls");
							mcsRetrieval(methodID, projectID);
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
							//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
						}

						Integer[] ret = new Integer[merged.size()];
						for (int i=0; i < ret.length; i++)
						{ 
							ret[i] = merged.get(i);
						} 
						RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

						//compare the API usages in hold out project with the API usages recommended
						int matchingAPICalls = 0;
						int totalAPICallsRecommended = RecommendedAPIUsages.size();
						//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
						for(String usage: RecommendedAPIUsages)
						{
							if(HeldOutAPIUsages.contains(usage))
							{
								matchingAPICalls += 1;
							}
						}

						if(totalAPICallsRecommended != 0){
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);
			
							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else
						{
							//System.out.println("No recommendations: No API calls from MCS of neighbors");
							mcsRetrieval(methodID, projectID);
						}

					}

				}
				else
				{
					//System.out.println("No recommendations: no call-graph based neighborhood");
					mcsRetrieval(methodID, projectID);
				}

		
	}

	private static void neighborhoodRetrievalplusMCSFile(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			//if none of the neighborhood methods is part of an MCS
			if(clusterIDsList.size()==0)
			{
				//simply use the neighborhood methods API usages
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: calledMethodIDsList)
				{			
					RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			

				}
				dbLayer.closeConnector();	

				if(RecommendedAPIUsages2.size()>0)
				{
					sample_size += calledMethodIDsList.size();
					IDs.addAll(calledMethodIDsList);
					System.out.println("FACER N: "+ calledMethodIDsList.size());
					//compare the API usages in hold out project with the API usages recommended
					int matchingAPICalls = 0;
					int totalAPICallsRecommended = RecommendedAPIUsages2.size();

					for(String usage: RecommendedAPIUsages2)
					{
						if(HeldOutAPIUsages.contains(usage))
						{
							matchingAPICalls += 1;
						}			
					}
					//number of methods recommended

					//System.out.println("No. of methods recommended: " + methodIDsList.size());
					//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);

					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				if(RecommendedAPIUsages2.size()<=0 || sample_size < 10)
				{
					//System.out.println("No recommendations: no MCS membership and no neighborhood API calls");
					sameFileMCSRetrieval(methodID,projectID);
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
					IDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID));
					for(int x: array)
					{
						if(!merged.contains(x))
							merged.add(x);
					}
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				}

				Integer[] ret = new Integer[merged.size()];
				for (int i=0; i < ret.length; i++)
				{ 
					ret[i] = merged.get(i);
				} 
				sample_size += ret.length;
				System.out.println("FACER MCSn: "+ ret.length);
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}
				}

				if(totalAPICallsRecommended != 0){
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);
	
					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				if(totalAPICallsRecommended <= 0 || sample_size < 10)
				{
					//System.out.println("No recommendations: No API calls from MCS of neighbors");
					sameFileMCSRetrieval(methodID,projectID);
				}

			}

		}
		else
		{
			//System.out.println("No recommendations: no call-graph based neighborhood");
			sameFileMCSRetrieval(methodID,projectID);
		}


	}
	
	private static void neighborhoodRetrieval(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			//if none of the neighborhood methods is part of an MCS
			if(clusterIDsList.size()==0)
			{
				//simply use the neighborhood methods API usages
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: calledMethodIDsList)
				{			
					RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			

				}
				dbLayer.closeConnector();	

				if(RecommendedAPIUsages2.size()>0)
				{
					//compare the API usages in hold out project with the API usages recommended
					int matchingAPICalls = 0;
					int totalAPICallsRecommended = RecommendedAPIUsages2.size();

					for(String usage: RecommendedAPIUsages2)
					{
						if(HeldOutAPIUsages.contains(usage))
						{
							matchingAPICalls += 1;
						}			
					}
					//number of methods recommended

					//System.out.println("No. of methods recommended: " + methodIDsList.size());
					//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);

					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				else{
					System.out.println("No recommendations: no MCS membership and no neighborhood API calls");
					//sameFileMCSRetrieval(methodID,projectID);
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
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				}

				Integer[] ret = new Integer[merged.size()];
				for (int i=0; i < ret.length; i++)
				{ 
					ret[i] = merged.get(i);
				} 
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}
				}

				if(totalAPICallsRecommended != 0){
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);
	
					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				else
				{
					System.out.println("No recommendations: No API calls from MCS of neighbors");
					//sameFileMCSRetrieval(methodID,projectID);
				}

			}

		}
		else
		{
			System.out.println("No recommendations: no call-graph based neighborhood");
			//sameFileMCSRetrieval(methodID,projectID);
		}


	}
	
	
	private static void neighborhoodsMCSRetrieval(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);
			calledMethodIDsList.add(methodID);//add user selected method too 

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			//if neighborhood methods is part of an MCS
			if(clusterIDsList.size()!=0)
		
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
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				}

				Integer[] ret = new Integer[merged.size()];
				for (int i=0; i < ret.length; i++)
				{ 
					ret[i] = merged.get(i);
				} 
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}
				}

				if(totalAPICallsRecommended != 0){
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);
	
					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				else
				{
					System.out.println("No recommendations: No API calls from MCS of neighbors");
				}

			}

		}
		else
		{
			System.out.println("No recommendations: no call-graph based neighborhood");
		}


	}
	
	//this method is supposed to use s5 which is the most precise out of all neighborhood strategies
	private static Set<String> getNeighborhoodRecommendations(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
			

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			//if none of the neighborhood methods is part of an MCS
			if(clusterIDsList.size()==0)
			{
				//simply use the neighborhood methods API usages
				//get the API usages of those methods
				//Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: calledMethodIDsList)
				{			
					RecommendedAPIUsages.addAll(dbLayer.getHashedMethodAPICalls(mID));			

				}
				dbLayer.closeConnector();	

				//if(RecommendedAPIUsages.size()>0)
				//{
				//	return RecommendedAPIUsages;
				//}
				//else{
					//return getFileRecommendations(methodID);
				//}
			}
			if(clusterIDsList.size()!=0)
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
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				}

				Integer[] ret = new Integer[merged.size()];
				for (int i=0; i < ret.length; i++)
				{ 
					ret[i] = merged.get(i);
				} 
				Set<String> RecommendedAPIUsagesFromMCS = new HashSet<String>();
				RecommendedAPIUsagesFromMCS = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

				RecommendedAPIUsages.addAll(RecommendedAPIUsagesFromMCS);
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				

				//if(totalAPICallsRecommended != 0){
					//return RecommendedAPIUsages;
				//}
				//else
				//{
					//return getFileRecommendations(methodID);
				//}

			}

		}
		//else
		//{
			//return getFileRecommendations(methodID);
		//}
		
		return RecommendedAPIUsages;

	}
	
	private static void justNeighborhoodRetrieval(int methodID, int projectID) throws Exception {
		
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> recommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			sample_size += calledMethodIDsList.size();
			for(int mID: calledMethodIDsList)
			{	
				
				Set<String> retrievedAPIUsages = new HashSet<>();
				retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
				for(String usage: retrievedAPIUsages)
				{
					if(!recommendedAPIUsages.contains(usage))
					{
						recommendedAPIUsages.add(usage);
					}			
				}


			}
			dbLayer.closeConnector();	

			//compare the API usages in hold out project with the API usages recommended
			int matchingAPICalls = 0;
			int totalAPICallsRecommended = recommendedAPIUsages.size();

			for(String usage: recommendedAPIUsages)
			{
				if(HeldOutAPIUsages.contains(usage))
				{
					matchingAPICalls += 1;
				}			
			}
			//number of methods recommended

			//System.out.println("No. of methods recommended: " + methodIDsList.size());
			//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
			//calculate precision		
			if(totalAPICallsRecommended != 0){
				float precision = (float)matchingAPICalls/totalAPICallsRecommended;
				System.out.println("Precision: " + precision);

				//calculate recall		
				float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
				System.out.println("Recall: " + recall);
			}
			else{
				System.out.println("No recommendations: no API calls recommended");
			}
		}
		else
		{
			System.out.println("No recommendations: no call-graph based neighborhood");
		}


	}

	private static void mcsPlusNeighborhoodPlusFileRetrieval2(int methodID,
			int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project

				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	

				//2.get the distinct API names from from the related methods recommended from mcs  
				Set<String> RecommendedAPIUsages = new HashSet<String>();
				dbLayer.initializeFoldDatabaseConnector();

				//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
				//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

				//is method a clone class member? get cluster ID against method
				int clusterID = dbLayer2.getClusterID(methodID);

				if(clusterID != -1)//method belongs to a clone class
				{
					ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
					if(featureIDs.size()!=0)//method belongs to a clone structure
					{
						//iterate over feature ids list to get the clusterIDs
						Integer[] array = null;
						array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
						RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
					
						//compare the API usages in hold out project with the API usages recommended
						int matchingAPICalls = 0;
						int totalAPICallsRecommended = RecommendedAPIUsages.size();
						//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
						for(String usage: RecommendedAPIUsages)
						{
							if(HeldOutAPIUsages.contains(usage))
							{
								matchingAPICalls += 1;
							}	
						}
						
						if(totalAPICallsRecommended > 0)
						{				
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);
							
							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else
						{
							//System.out.println("No recommendations: No API usages recommended or matching");
							neighborhoodPlusFileRetrieval(methodID, projectID);
						}
						
						
					}
					else
					{
						//System.out.println("No recommendations: no MCS membership");
						neighborhoodPlusFileRetrieval(methodID, projectID);
					}

				}
				else
				{					
					//System.out.println("No recommendations: no clone membership");
					neighborhoodPlusFileRetrieval(methodID, projectID);
				}
				
		
	}
	private static void mcsPlusNeighborhoodPlusFileRetrieval(int methodID,
			int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

		//is method a clone class member? get cluster ID against method
		int clusterID = dbLayer2.getClusterID(methodID);
		ArrayList<Integer> featureIDs = new ArrayList<Integer>();
		if(clusterID != -1)
			featureIDs = dbLayer2.getFeatureIDs(clusterID);
		if(featureIDs != null && featureIDs.size() != 0)//method belongs to a clone class
		{
			
			
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
				//uncomment below line to view method bodies
				//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}	
				}

				//success rate


				if(matchingAPICalls >= 1)
				{

					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);

					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
			

		}
		else

		{
			ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

			//if descendant methods list is null then take teh methods in the same file
			if(calledMethodIDsList.size()==0)
			{
				//System.out.println("here");
				calledMethodIDsList = dbLayer2.getSameFileMethods(methodID);
			}
			
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			
			if(clusterIDsList.size()==0)
			{
				//tell me about it
				
				//System.out.println("Heads up!");
				//sameFileRetrieval(methodID);
				//simply use the neighborhood methods API usages
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: calledMethodIDsList)
				{			
					RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			
					
				}
				//ViewSampleMethodForClusters.viewMethods(calledMethodIDsList);
				dbLayer.closeConnector();	
				
				if(RecommendedAPIUsages2.size()>0)
				{
					//compare the API usages in hold out project with the API usages recommended
					int matchingAPICalls = 0;
					int totalAPICallsRecommended = RecommendedAPIUsages2.size();
					
					for(String usage: RecommendedAPIUsages2)
					{
						if(HeldOutAPIUsages.contains(usage))
						{
							matchingAPICalls += 1;
						}			
					}
					//number of methods recommended
					
					//System.out.println("No. of methods recommended: " + methodIDsList.size());
					//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);
					
					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				else
				{
					sameFileRetrieval(methodID);
				}
			}
			else
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
				//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
			}

			Integer[] ret = new Integer[merged.size()];
			for (int i=0; i < ret.length; i++)
			{ 
				ret[i] = merged.get(i);
			} 
			RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	
			//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(ret, projectID);
			
			//compare the API usages in hold out project with the API usages recommended
			int matchingAPICalls = 0;
			int totalAPICallsRecommended = RecommendedAPIUsages.size();
			//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
			for(String usage: RecommendedAPIUsages)
			{
				if(HeldOutAPIUsages.contains(usage))
				{
					matchingAPICalls += 1;
				}
			}

			//calculate precision		
			float precision = (float)matchingAPICalls/totalAPICallsRecommended;
			System.out.println("Precision: " + precision);

			//calculate recall		
			float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
			System.out.println("Recall: " + recall);

			}


		}
				
		
	}

	private static void getMcsPlusNeighborhoodPlusFileRetrieval(int methodID,
			int projectID) throws Exception {
		

		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

			

		//is method a clone class member? get cluster ID against method
		int clusterID = dbLayer2.getClusterID(methodID);
		ArrayList<Integer> featureIDs = new ArrayList<Integer>();
		if(clusterID != -1)
			featureIDs = dbLayer2.getFeatureIDs(clusterID);
		if(featureIDs != null && featureIDs.size() != 0)//method belongs to a clone class
		{
			
			
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
				//RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
				//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				

		}
		else

		{
			ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

			//if descendant methods list is null then take teh methods in the same file
			if(calledMethodIDsList.size()==0)
			{
				//System.out.println("here");
				calledMethodIDsList = dbLayer2.getSameFileMethods(methodID);
			}
			
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			
			if(clusterIDsList.size()==0)
			{
//tell me about it
				
				//System.out.println("Heads up!");
				//sameFileRetrieval(methodID);
				//simply use the neighborhood methods API usages
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: calledMethodIDsList)
				{			
					RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			
					
				}
				//ViewSampleMethodForClusters.viewMethods(calledMethodIDsList);
				dbLayer.closeConnector();	
				
				if(RecommendedAPIUsages2.size()>0)
				{
					//compare the API usages in hold out project with the API usages recommended
					int matchingAPICalls = 0;
					int totalAPICallsRecommended = RecommendedAPIUsages2.size();
					
					for(String usage: RecommendedAPIUsages2)
					{
						if(HeldOutAPIUsages.contains(usage))
						{
							matchingAPICalls += 1;
						}			
					}
	
				}
				else
				{
					sameFileRetrieval(methodID);
				}
				
			}
			else
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
				//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
			}

			Integer[] ret = new Integer[merged.size()];
			for (int i=0; i < ret.length; i++)
			{ 
				ret[i] = merged.get(i);
			} 
			//RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	
			ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(ret, projectID);
			
			

			}


		}
				
		
	}

	private static void mcsRetrieval(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project

		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

		//is method a clone class member? get cluster ID against method
		int clusterID = dbLayer2.getClusterID(methodID);

		if(clusterID != -1)//method belongs to a clone class
		{
			ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
			if(featureIDs.size()!=0)//method belongs to a clone structure
			{
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
				
				sample_size += array.length;
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
			
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}	
				}
				
				if(totalAPICallsRecommended > 0)
				{				
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);
					
					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				else
				{
					System.out.println("No recommendations: No API usages recommended or matching");
				}
				
				
			}
			else
			{
				System.out.println("No recommendations: no MCS membership");
			}

		}
		else

		{
			//no results
			System.out.println("No recommendations: no clone membership");

		}
		

	}
	
	
	private static void FACERRetrieval(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project

		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	

		//2.get the distinct API names from from the related methods recommended from mcs  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

		//is method a clone class member? get cluster ID against method
		int clusterID = dbLayer2.getClusterID(methodID);

		if(clusterID != -1)//method belongs to a clone class
		{
			ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
			if(featureIDs.size()!=0)//method belongs to a clone structure
			{
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
				IDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID));
				sample_size += array.length;
				System.out.println("FACER Mu: "+ array.length);
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
			
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
				for(String usage: RecommendedAPIUsages)
				{
					if(HeldOutAPIUsages.contains(usage))
					{
						matchingAPICalls += 1;
					}	
				}
				
				if(totalAPICallsRecommended > 0)
				{				
					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);
					
					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);
				}
				if(sample_size < 10 || totalAPICallsRecommended <= 0 )
				{
					//System.out.println("No recommendations: No API usages recommended or matching");
					neighborhoodRetrievalplusMCSFile(methodID, projectID);
				}
				
				
			}
			else
			{
				//System.out.println("No recommendations: no MCS membership");
				neighborhoodRetrievalplusMCSFile(methodID, projectID);
			}

		}
		else

		{
			//no results
			//System.out.println("No recommendations: no clone membership");
			neighborhoodRetrievalplusMCSFile(methodID, projectID);
		}
		

	}
	
	static ArrayList<Integer> getRelatedMethodIDsUsingFACERRetrieval(int methodID, int projectID) throws Exception {
		
		ArrayList<Integer> relatedMethods = new ArrayList<Integer>();
		
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();
		
		int clusterID = dbLayer2.getClusterID(methodID);
		if(clusterID != -1)//method belongs to a clone class
		{
			ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
			if(featureIDs.size()!=0)//method belongs to a clone structure
			{
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);				
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);			
				int totalAPICallsRecommended = RecommendedAPIUsages.size();		
				if(totalAPICallsRecommended > 0)
				{
					relatedMethods.addAll(ViewSampleMethodForClusters.getMethodIDsAgainstClusterIDs(array, projectID));					
					sample_size += array.length;
					System.out.println("FACER Mu: "+ array.length);
				}
			
				if(sample_size < 10 || totalAPICallsRecommended <= 0 )
				{
					//System.out.println("No recommendations: No API usages recommended or matching");
					relatedMethods.addAll(getNeighborhoodRetrievalplusMCSFileMethods(methodID, projectID));
				}	
			}
			else
			{
				//System.out.println("No recommendations: no MCS membership");
				relatedMethods.addAll(getNeighborhoodRetrievalplusMCSFileMethods(methodID, projectID));
			}
		}
		else
		{			
			//System.out.println("No recommendations: no clone membership");
			relatedMethods.addAll(getNeighborhoodRetrievalplusMCSFileMethods(methodID, projectID));
		}
		return relatedMethods;
	}
	
static ArrayList<Integer> getRelatedMethodIDsUsingFACERRetrievalLeaveOutProject(int methodID, int projectID) throws Exception {
		
		ArrayList<Integer> relatedMethods = new ArrayList<Integer>();
		
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();
		if(dbLayer2 == null)
		{
			dbLayer2 = DatabaseAccessLayer.getInstance();		
			dbLayer2.initializeConnectorToRetrieveRelatedFeatures();
		}
		int clusterID = dbLayer2.getClusterID(methodID);
		if(clusterID != -1)//method belongs to a clone class
		{
			ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
			if(featureIDs.size()!=0)//method belongs to a clone structure
			{
				//iterate over feature ids list to get the clusterIDs
				Integer[] array = null;
				array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);				
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromOtherProjects(array, projectID, false);			
				int totalAPICallsRecommended = RecommendedAPIUsages.size();		
				if(totalAPICallsRecommended > 0)
				{
					relatedMethods.addAll(ViewSampleMethodForClusters.getMethodIDsAgainstClusterIDs(array, projectID));					
					sample_size += array.length;
					System.out.println("FACER Mu: "+ array.length);
				}
			
			}
			
		}
		
		return relatedMethods;
	}
	
	
	private static ArrayList<Integer> getNeighborhoodRetrievalplusMCSFileMethods(
			int methodID, int projectID) throws Exception {
		
		ArrayList<Integer> relatedMethods = new  ArrayList<Integer>();

		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();				
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();

		ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

		//if neighborhood methods exist then proceed else do nothing 
		if(calledMethodIDsList.size()!=0)
		{
			ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
			//if none of the neighborhood methods is part of an MCS
			if(clusterIDsList.size()==0)
			{
				//simply use the neighborhood methods API usages
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: calledMethodIDsList)
				{			
					RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			

				}
				dbLayer.closeConnector();	

				if(RecommendedAPIUsages2.size()>0)
				{
					sample_size += calledMethodIDsList.size();
					relatedMethods.addAll(calledMethodIDsList);
					System.out.println("FACER N: "+ calledMethodIDsList.size());

				}
				if(RecommendedAPIUsages2.size()<=0 || sample_size < 10)
				{
					//System.out.println("No recommendations: no MCS membership and no neighborhood API calls");
					relatedMethods.addAll(getSameFileMCSRetrievalMethods(methodID,projectID));
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
					relatedMethods.addAll(ViewSampleMethodForClusters.getMethodIDsAgainstClusterIDs(array, projectID));
					for(int x: array)
					{
						if(!merged.contains(x))
							merged.add(x);
					}
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
				}

				Integer[] ret = new Integer[merged.size()];
				for (int i=0; i < ret.length; i++)
				{ 
					ret[i] = merged.get(i);
				} 
				sample_size += ret.length;
				System.out.println("FACER MCSn: "+ ret.length);
				RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	


				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();


				if(totalAPICallsRecommended <= 0 || sample_size < 10)
				{
					//System.out.println("No recommendations: No API calls from MCS of neighbors");
					relatedMethods.addAll(getSameFileMCSRetrievalMethods(methodID,projectID));
				}

			}

		}
		else
		{
			//System.out.println("No recommendations: no call-graph based neighborhood");
			relatedMethods.addAll(getSameFileMCSRetrievalMethods(methodID,projectID));
		}
		return relatedMethods;


		
	}

	private static ArrayList<Integer> getClusterIDsFromMCSMembership(
			ArrayList<Integer> calledMethodIDsList) throws SQLException {
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
		return clusterIDsList;
	}

	private static ArrayList<Integer> getNeighborhoodMethods(int methodID)
			throws Exception {
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
		return calledMethodIDsList;
	}

	private static ArrayList<Integer> getSameFileMCSRetrievalMethods(int methodID,
			int projectID) throws Exception {
		
		ArrayList<Integer> relatedMethods = new ArrayList<Integer>();
		
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();

		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		methodIDsList = dbLayer2.getSameFileMethods(methodID);
		Set<String> RecommendedAPIUsages = new HashSet<>();		
		dbLayer.initializeFoldDatabaseConnector();
		ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(methodIDsList);
		if(clusterIDsList.size()!=0)//if MCS membership of same file methods leads to cluster IDs					
		{
			Integer[] array = null;
			ArrayList<Integer> merged = new ArrayList<Integer>();
			for(int cID: clusterIDsList)
			{
				
				//display the related features/functions
				array = (RetrieveRelatedFeatures.retrieveRelatedClusterIDs(cID));
				relatedMethods.addAll(ViewSampleMethodForClusters.getMethodIDsAgainstClusterIDs(array, projectID));
				for(int x: array)
				{
					if(!merged.contains(x))
						merged.add(x);
				}
				//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
			}

			Integer[] ret = new Integer[merged.size()];
			for (int i=0; i < ret.length; i++)
			{ 
				ret[i] = merged.get(i);
			} 
			sample_size += ret.length;
			System.out.println("FACER MCSf: " + ret.length);
			RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

			//uncomment below line to see methods
			//ViewSampleMethodForClusters.viewMethods(methodIDsList);
			dbLayer.closeConnector();	
			int totalAPICallsRecommended = RecommendedAPIUsages.size();
		}
		//if(clusterIDsList.size()==0 || sample_size < 10)
			//relatedMethods.addAll(getSameFileRetrievalMethods(methodID));
		return relatedMethods;
		
	}

	private static ArrayList<Integer> getSameFileRetrievalMethods(int methodID) throws Exception {
		
		ArrayList<Integer> relatedMethods = new ArrayList<Integer>();
		
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();				
				ArrayList<Integer> methodIDsList = new ArrayList<Integer>();	
				
				methodIDsList = dbLayer2.getSameFileMethods(methodID);
				relatedMethods.addAll(methodIDsList);
				sample_size += methodIDsList.size();
				System.out.println("FACER F: "+ methodIDsList.size());
				//get the API usages of those methods
				Set<String> RecommendedAPIUsages = new HashSet<>();		
				dbLayer.initializeFoldDatabaseConnector();	
				for(int mID: methodIDsList)
				{			
					Set<String> retrievedAPIUsages = new HashSet<>();
					retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
					RecommendedAPIUsages.addAll(retrievedAPIUsages);			
					
				}
				//uncomment below line to see methods
				//ViewSampleMethodForClusters.viewMethods(methodIDsList);
				dbLayer.closeConnector();	
				
				//compare the API usages in hold out project with the API usages recommended
				int matchingAPICalls = 0;
				int totalAPICallsRecommended = RecommendedAPIUsages.size();
				return relatedMethods;
				
				
		
	}

	private static void mcsPlusNeighborhoodRetrieval2(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project

				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	

				//2.get the distinct API names from from the related methods recommended from mcs  
				Set<String> RecommendedAPIUsages = new HashSet<String>();
				dbLayer.initializeFoldDatabaseConnector();

				//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
				//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

				//is method a clone class member? get cluster ID against method
				int clusterID = dbLayer2.getClusterID(methodID);

				if(clusterID != -1)//method belongs to a clone class
				{
					ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(clusterID);
					if(featureIDs.size()!=0)//method belongs to a clone structure
					{
						//iterate over feature ids list to get the clusterIDs
						Integer[] array = null;
						array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
						RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);
					
						//compare the API usages in hold out project with the API usages recommended
						int matchingAPICalls = 0;
						int totalAPICallsRecommended = RecommendedAPIUsages.size();
						//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
						for(String usage: RecommendedAPIUsages)
						{
							if(HeldOutAPIUsages.contains(usage))
							{
								matchingAPICalls += 1;
							}	
						}
						
						if(totalAPICallsRecommended > 0)
						{				
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);
							
							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
						else
						{
							//System.out.println("No recommendations: No API usages recommended or matching");
							neighborhoodRetrieval(methodID, projectID);
						}
						
						
					}
					else
					{
						//System.out.println("No recommendations: no MCS membership");
						neighborhoodRetrieval(methodID, projectID);
					}

				}
				else

				{
					//no results
					//System.out.println("No recommendations: no clone membership");
					neighborhoodRetrieval(methodID, projectID);

				}
				
	}
	
	private static void mcsPlusNeighborhoodRetrieval(int methodID, int projectID) throws Exception {
		//1.get distinct API usages from the API call table of the held out project
				ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeHeldoutDatabaseConnector();
				HeldOutAPIUsages = dbLayer.getAPIUsages();	

				//2.get the distinct API names from from the related methods recommended from mcs  
				Set<String> RecommendedAPIUsages = new HashSet<String>();
				dbLayer.initializeFoldDatabaseConnector();

				//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
				//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	

				//is method a clone class member? get cluster ID against method
				int clusterID = dbLayer2.getClusterID(methodID);
				ArrayList<Integer> featureIDs = new ArrayList<Integer>();
				if(clusterID != -1)
					featureIDs = dbLayer2.getFeatureIDs(clusterID);
				if(featureIDs != null && featureIDs.size() != 0)//method belongs to a clone class
				{
					
					
						//iterate over feature ids list to get the clusterIDs
						Integer[] array = null;
						array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(clusterID);
						RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID, false);

						//compare the API usages in hold out project with the API usages recommended
						int matchingAPICalls = 0;
						int totalAPICallsRecommended = RecommendedAPIUsages.size();
						//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
						for(String usage: RecommendedAPIUsages)
						{
							if(HeldOutAPIUsages.contains(usage))
							{
								matchingAPICalls += 1;
							}	
						}

						if(matchingAPICalls >= 1)
						{

							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);

							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
					

				}
				else

				{
					ArrayList<Integer> calledMethodIDsList = getNeighborhoodMethods(methodID);

					//if neighborhood methods list is null then just simply do nothing
					if(calledMethodIDsList.size()!=0)
					{
						//System.out.println("here");
						
					
					
					ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
					
					if(clusterIDsList.size()==0)
					{
						//simply use the neighborhood methods API usages
						//get the API usages of those methods
						Set<String> RecommendedAPIUsages2 = new HashSet<String>();		
						dbLayer.initializeFoldDatabaseConnector();	
						for(int mID: calledMethodIDsList)
						{			
							RecommendedAPIUsages2.addAll(dbLayer.getHashedMethodAPICalls(mID));			
							
						}
						dbLayer.closeConnector();	
						
						if(RecommendedAPIUsages2.size()>0)
						{
							//compare the API usages in hold out project with the API usages recommended
							int matchingAPICalls = 0;
							int totalAPICallsRecommended = RecommendedAPIUsages2.size();
							
							for(String usage: RecommendedAPIUsages2)
							{
								if(HeldOutAPIUsages.contains(usage))
								{
									matchingAPICalls += 1;
								}			
							}
							//number of methods recommended
							
							//System.out.println("No. of methods recommended: " + methodIDsList.size());
							//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
							//calculate precision		
							float precision = (float)matchingAPICalls/totalAPICallsRecommended;
							System.out.println("Precision: " + precision);
							
							//calculate recall		
							float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
							System.out.println("Recall: " + recall);
						}
					}
					else
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
						//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
					}

					Integer[] ret = new Integer[merged.size()];
					for (int i=0; i < ret.length; i++)
					{ 
						ret[i] = merged.get(i);
					} 
					RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

					//compare the API usages in hold out project with the API usages recommended
					int matchingAPICalls = 0;
					int totalAPICallsRecommended = RecommendedAPIUsages.size();
					//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
					for(String usage: RecommendedAPIUsages)
					{
						if(HeldOutAPIUsages.contains(usage))
						{
							matchingAPICalls += 1;
						}
					}

					//calculate precision		
					float precision = (float)matchingAPICalls/totalAPICallsRecommended;
					System.out.println("Precision: " + precision);

					//calculate recall		
					float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
					System.out.println("Recall: " + recall);

					}

					}
				}
	}

	private static Set<String> getFileRecommendations(int methodID) throws Exception
	{
		//1.get distinct API usages from the API call table of the held out project
		//ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		//HeldOutAPIUsages = dbLayer.getAPIUsages();	 //selects distinct API usages in query
		
		//get the other methods of the same file as the user selected input methodID
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();
		
		methodIDsList = dbLayer2.getSameFileMethods(methodID);
		
		//get the API usages of those methods
		Set<String> RecommendedAPIUsages = new HashSet<>();		
		dbLayer.initializeFoldDatabaseConnector();	
		for(int mID: methodIDsList)
		{			
			Set<String> retrievedAPIUsages = new HashSet<>();
			retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
			RecommendedAPIUsages.addAll(retrievedAPIUsages);			
			
		}
		//uncomment below line to see methods
		//ViewSampleMethodForClusters.viewMethods(methodIDsList);
		dbLayer.closeConnector();	
		return RecommendedAPIUsages;
		
	}
	
	private static void sameFileRetrieval(int methodID) throws Exception
	{
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	 //selects distinct API usages in query
		
		//get the other methods of the same file as the user selected input methodID
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();
		
		methodIDsList = dbLayer2.getSameFileMethods(methodID);
		IDs.addAll(methodIDsList);
		sample_size += methodIDsList.size();
		System.out.println("FACER F: "+ methodIDsList.size());
		//get the API usages of those methods
		Set<String> RecommendedAPIUsages = new HashSet<>();		
		dbLayer.initializeFoldDatabaseConnector();	
		for(int mID: methodIDsList)
		{			
			Set<String> retrievedAPIUsages = new HashSet<>();
			retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
			RecommendedAPIUsages.addAll(retrievedAPIUsages);			
			
		}
		//uncomment below line to see methods
		//ViewSampleMethodForClusters.viewMethods(methodIDsList);
		dbLayer.closeConnector();	
		
		//compare the API usages in hold out project with the API usages recommended
		int matchingAPICalls = 0;
		int totalAPICallsRecommended = RecommendedAPIUsages.size();
		
		for(String usage: RecommendedAPIUsages)
		{
			if(HeldOutAPIUsages.contains(usage))
			{
				matchingAPICalls += 1;
			}			
		}
		//number of methods recommended
		
		//System.out.println("No. of methods recommended: " + methodIDsList.size());
		//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
		//calculate precision		
		float precision = (float)matchingAPICalls/totalAPICallsRecommended;
		System.out.println("Precision: " + precision);
		
		//calculate recall		
		float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
		System.out.println("Recall: " + recall);
		
	}
	
	private static void sameFileMCSRetrieval(int methodID, int projectID) throws Exception
	{
		//1.get distinct API usages from the API call table of the held out project
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	 //selects distinct API usages in query

		//get the other methods of the same file as the user selected input methodID
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();

		methodIDsList = dbLayer2.getSameFileMethods(methodID);
		


		//get the API usages of those methods
		Set<String> RecommendedAPIUsages = new HashSet<>();		
		dbLayer.initializeFoldDatabaseConnector();
		ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(methodIDsList);
		//if file methods are part of an MCS
		if(clusterIDsList.size()!=0)

			//if MCS membership of neighborhood methods leads to cluster IDs
		{
			Integer[] array = null;
			ArrayList<Integer> merged = new ArrayList<Integer>();
			for(int cID: clusterIDsList)
			{
				//index+=1;
				//display the related features/functions

				array = (RetrieveRelatedFeatures.retrieveRelatedClusterIDs(cID));
				IDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID));
				for(int x: array)
				{
					if(!merged.contains(x))
						merged.add(x);
				}
				//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
			}

			Integer[] ret = new Integer[merged.size()];
			for (int i=0; i < ret.length; i++)
			{ 
				ret[i] = merged.get(i);
			} 
			sample_size += ret.length;
			System.out.println("FACER MCSf: " + ret.length);
			RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, false);	

			//uncomment below line to see methods
			//ViewSampleMethodForClusters.viewMethods(methodIDsList);
			dbLayer.closeConnector();	

			//compare the API usages in hold out project with the API usages recommended
			int matchingAPICalls = 0;
			int totalAPICallsRecommended = RecommendedAPIUsages.size();

			for(String usage: RecommendedAPIUsages)
			{
				if(HeldOutAPIUsages.contains(usage))
				{
					matchingAPICalls += 1;
				}			
			}

			//calculate precision		
			float precision = (float)matchingAPICalls/totalAPICallsRecommended;
			System.out.println("Precision: " + precision);

			//calculate recall		
			float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
			System.out.println("Recall: " + recall);

		}
		if(clusterIDsList.size()==0 || sample_size < 10)
			sameFileRetrieval(methodID);
		

	}

	
	private static void sameProjectRetrieval(int methodID, int projectID) throws Exception
	{
		//1.get distinct API usages from the API call table of the held out project
		Set<String> HeldOutAPIUsages = new HashSet<>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getHashedAPIUsages();	
		
		//get the other methods of the same file as the user selected input methodID
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();
		
		methodIDsList = dbLayer2.getSameProjectMethods(projectID);
		sample_size += methodIDsList.size();
		
		//get the API usages of those methods
		//ArrayList<String> RecommendedAPIUsages = new ArrayList<String>();	
		Set<String> recommendedAPIUsages = new HashSet<>();
		dbLayer.initializeFoldDatabaseConnector();	
		for(int mID: methodIDsList)
		{		
			Set<String> retrievedAPIUsages = new HashSet<>();
			retrievedAPIUsages = dbLayer.getHashedMethodAPICalls(mID);
			for(String usage: retrievedAPIUsages)
			{
				if(!recommendedAPIUsages.contains(usage))
				{
					recommendedAPIUsages.add(usage);
				}			
			}
			
					
		}
		dbLayer.closeConnector();	
		
		//compare the API usages in hold out project with the API usages recommended
		int matchingAPICalls = 0;
		int totalAPICallsRecommended = recommendedAPIUsages.size();
		
		for(String usage: recommendedAPIUsages)
		{
			if(HeldOutAPIUsages.contains(usage))
			{
				matchingAPICalls += 1;
			}			
		}
		//number of methods recommended
		
		//System.out.println("No. of methods recommended: " + methodIDsList.size());
		//System.out.println("Total no. of API calls recommended: " + totalAPICallsRecommended);
		//calculate precision		
		float precision = (float)matchingAPICalls/totalAPICallsRecommended;
		System.out.println("Precision: " + precision);
		
		//calculate recall		
		float recall = (float)matchingAPICalls/HeldOutAPIUsages.size();
		System.out.println("Recall: " + recall);
		
	}
	
	
	private static void evaluationStrategy3(int methodID, int projectID, boolean contextual) throws Exception, SQLException, ClassNotFoundException {
		//1.get distinct API usages from the API call table of the held out project
		
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	
		
		//2.get the distinct API names from from the related methods recommended.  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();
		
		//DatabaseAccessLayer dbLayer2 = DatabaseAccessLayer.getInstance();		
		//dbLayer2.initializeConnectorToRetrieveRelatedFeatures();	
		
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
	    calledMethodIDsList = APIUsageSequenceExtraction.getDescendantMethodIDs(methodID, cumulatingMethodIDs);
		ArrayList<Integer> ancestorMethodsList = APIUsageSequenceExtraction.getHostMethods(methodID);
		calledMethodIDsList.addAll(ancestorMethodsList);
		//if descendant methods list is null then take teh methods in the same file
		if(calledMethodIDsList.size()==0)
		{
			//System.out.println("here");
			calledMethodIDsList = dbLayer2.getSameFileMethods(methodID);
		}
		
		ArrayList<Integer> clusterIDsList = getClusterIDsFromMCSMembership(calledMethodIDsList);
		
		if(clusterIDsList.size()==0)
		{
			calledMethodIDsList = dbLayer2.getSameFileMethods(methodID);
			for (int mID: calledMethodIDsList)
			{
				int cID = dbLayer2.getClusterID(mID);
				if(cID!= -1)
				{
					ArrayList<Integer> featureIDs = dbLayer2.getFeatureIDs(cID);
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
		

		Integer[] array = null;
		ArrayList<Integer> merged = new ArrayList<Integer>();
		for(int cID: clusterIDsList)
		{
			//index+=1;
			//display the related features/functions
					
			array = (RetrieveRelatedFeatures.retrieveRelatedClusterIDs(cID));
			for(int x: array)
			{
				merged.add(x);
			}
			//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(array, projectID);
		}
		
		Integer[] ret = new Integer[merged.size()];
	    for (int i=0; i < ret.length; i++)
	    { 
	        ret[i] = merged.get(i);
	    } 
		RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(ret, projectID, contextual);	
		
		//compare the API usages in hold out project with the API usages recommended
		int matchingAPICalls = 0;
		int totalAPICallsRecommended = RecommendedAPIUsages.size();
		//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
		for(String usage: RecommendedAPIUsages)
		{
			if(HeldOutAPIUsages.contains(usage))
			{
				matchingAPICalls += 1;
			}
				
			
		}
		
		//calculate precision		
		float precision = (float)matchingAPICalls/totalAPICallsRecommended;
		System.out.println(precision);
		
	}

	private static void evaluationStrategy1(int inputClusterID)
			throws Exception, SQLException, ClassNotFoundException {
		//1.get distinct API usages from the API call table of the held out project
		
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	
		
		//2.get the distinct API names from from the related methods recommended.  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();
		Integer[] array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(inputClusterID);
		
		RecommendedAPIUsages = viewMethodsAgainstClusterIDs(array);	
		
		//compare the API usages in hold out project with the API usages recommended
		int matchingAPICalls = 0;
		int totalAPICallsRecommended = RecommendedAPIUsages.size();
		//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
		for(String usage: RecommendedAPIUsages)
		{
			if(HeldOutAPIUsages.contains(usage))
			{
				matchingAPICalls += 1;
			}
				
			
		}
		
		//calculate precision		
		float precision = (float)matchingAPICalls/totalAPICallsRecommended;
		System.out.println(precision);
	}
	
	private static void evaluationStrategy2(int inputClusterID, int projectID)
			throws Exception, SQLException, ClassNotFoundException {
		//1.get distinct API usages from the API call table of the held out project
		
		ArrayList<String> HeldOutAPIUsages = new ArrayList<String>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeHeldoutDatabaseConnector();
		HeldOutAPIUsages = dbLayer.getAPIUsages();	
		
		//2.get the distinct API names from from the related methods recommended.  
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		dbLayer.initializeFoldDatabaseConnector();
		Integer[] array = RetrieveRelatedFeatures.retrieveRelatedClusterIDs(inputClusterID);
		
		RecommendedAPIUsages = viewMethodsAgainstClusterIDsFromSameProject(array, projectID,false);	
		
		//compare the API usages in hold out project with the API usages recommended
		int matchingAPICalls = 0;
		int totalAPICallsRecommended = RecommendedAPIUsages.size();
		//HeldOutAPIUsages.contains(RecommendedAPIUsages.get(0));
		for(String usage: RecommendedAPIUsages)
		{
			if(HeldOutAPIUsages.contains(usage))
			{
				matchingAPICalls += 1;
			}
		}
		
		//calculate precision		
		float precision = (float)matchingAPICalls/totalAPICallsRecommended;
		System.out.println(precision);
	}
	
	private static Set<String> viewMethodsAgainstClusterIDsFromSameProject(
			Integer[] clustersList, int projectID, boolean contextual) throws Exception {
				
				Set<String> RecommendedAPIUsages = new HashSet<>();
				
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeFoldDatabaseConnector();	
				
				//get the first method  in each cluster along with projectID and fileName 
				ArrayList<Method> methodsList = new ArrayList<Method>();
				for(Integer clusterID: clustersList)
				{
					Method method = dbLayer.getMethodFromProject(clusterID, projectID);
					if(method.id == 0)
					{
						if(contextual)
							method = getContextuallySimilarMethod(clusterID, projectID);
						else
							method = dbLayer.getFirstMethod(clusterID);
						
					}
					methodsList.add(method);
					
				}
				
				//
				for(Method m: methodsList)
				{			
					RecommendedAPIUsages.addAll(dbLayer.getMethodAPICalls(m));			
					
				}
				dbLayer.closeConnector();
				return RecommendedAPIUsages;
	}
	
	private static Set<String> viewMethodsAgainstClusterIDsFromOtherProjects(
			Integer[] clustersList, int projectID, boolean contextual) throws Exception {
				
				Set<String> RecommendedAPIUsages = new HashSet<>();
				
				EvaluationDAL dbLayer = EvaluationDAL.getInstance();
				dbLayer.initializeFoldDatabaseConnector();	
				
				//get the first method  in each cluster along with projectID and fileName 
				ArrayList<Method> methodsList = new ArrayList<Method>();
				for(Integer clusterID: clustersList)
				{
					Method method = dbLayer.getMethodFromOtherProject(clusterID, projectID);
					
					methodsList.add(method);
					
				}
				
				//
				for(Method m: methodsList)
				{			
					RecommendedAPIUsages.addAll(dbLayer.getMethodAPICalls(m));			
					
				}
				dbLayer.closeConnector();
				return RecommendedAPIUsages;
	}

	private static Method getContextuallySimilarMethod(int clusterID, int projectID) throws Exception{
		
		//get a list of methods with their projectIDs against the input clusterID
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeFoldDatabaseConnector();	
		ArrayList<Method> methodsList = new ArrayList<Method>();
		methodsList = dbLayer.getMethodsByClusterID(clusterID);
		
		//get the similarity scores of methods wrt similarity of their project with user project
		 Map<Method, Float> methodScores= getSimilarityScores("Project_"+projectID, methodsList);
		//sort the projectIDs in order of similarity to input projectID
		 methodScores = HashMapSorter.sortMethodsByComparator(methodScores, false);
		//return the method belonging to the most similar project 
		 Entry<Method, Float> entry = methodScores.entrySet().iterator().next();
		 Method key = entry.getKey();
		 
		return key;
		
	}
	
	private static Map<Method, Float> getSimilarityScores(String activeProject,
			ArrayList<Method> methodsList) throws IOException {
		
		Map<Method, Float> result = new HashMap<Method, Float>();
		
		//read all projectNames and their scores in a HashMap
		Map<String, Float> projects = new HashMap<String, Float>();
		List<String> allLines = Files.readAllLines(Paths.get("F:\\PhD\\PhD Defense\\Code\\FASeR_Recommender\\similarityscores\\" + activeProject + ".txt"));
		for (String line : allLines) {
			//get the project name from the second occurrence of tab
			int firstIndex = line.indexOf('\t');
			int secondIndex = line.indexOf('\t', firstIndex+1);
			String pName = line.substring(firstIndex+1, secondIndex);
			pName = pName.replace(".txt", "");
			
			firstIndex = line.indexOf('\t',firstIndex+1);
			//System.out.println(line.substring(firstIndex+1));
			
			Float score = Float.parseFloat(line.substring(firstIndex+1));
			if(score != null)
				projects.put(pName, score);
			else
				System.out.println("A null score");
			
		}
		
		
		for(Method m: methodsList)
		{
			String projectName = "Project_" + m.projectID;
				if(projects.get(projectName) != null)
					result.put(m, projects.get(projectName));
			
		}
		
		return result;
	}

	
	public static Set<String> viewMethodsAgainstClusterIDs(Integer[] clustersList)
			throws Exception {
		
		Set<String> RecommendedAPIUsages = new HashSet<String>();
		
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeFoldDatabaseConnector();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getFirstMethod(clusterID);
			methodsList.add(method);
			
		}
		
		//
		for(Method m: methodsList)
		{			
			RecommendedAPIUsages.addAll(dbLayer.getMethodAPICalls(m));			
			
		}
		dbLayer.closeConnector();
		return RecommendedAPIUsages;
	}
}
