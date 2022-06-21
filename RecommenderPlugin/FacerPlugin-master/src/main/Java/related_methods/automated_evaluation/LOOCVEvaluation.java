package related_methods.automated_evaluation;

import RelatedMethods.DataObjects.Method;
import RelatedMethods.CustomUtilities.HashMapSorter;
import RelatedMethods.db_access_layer.EvaluationDAL;
import related_methods._3_PopulateRelatedFeatures.ViewSampleMethodForClusters;

import java.sql.SQLException;
import java.util.*;

/**
 * @author shamsa
 * This class was created to perform the LOOCV evaluation for the EMSE paper
 * The idea is to not use holdout set of project for validation, instead use the same projects
 * from 101 repo and divide methods of a project into half and take it as test set 
 * and the rest of the methods form the validation set
 * The test set methods have to be filtered for method clones, because only they can be matched to features in similar projects
 * 
 * the remaining projects will be the source of recommendations
 *
 * LOOCV evaluation can handle single method input as context as well as multiple methods 
 */


public class LOOCVEvaluation {

	static int topN = 5;
	static String featureDesc = ""; 
	static boolean userfriendly = true;
	public static void main(String args[]) throws Exception
	{
		userfriendly = false;
		evaluateAllMinSup();
		
		//evaluateIndividually();

		
	/*	getRecStats(78, 21606, minSup);
		getRecStats(83, 23355, minSup);
		getRecStats(62, 13772, minSup);
		getRecStats(78, 22054, minSup);
		getRecStats(59, 13273, minSup);
		getRecStats(30, 1944, minSup);
		getRecStats(109, 33376, minSup);
		getRecStats(91, 26114, minSup);
		getRecStats(114, 36395, minSup);
		getRecStats(92, 26554, minSup);
		getRecStats(87, 24783, minSup);
		getRecStats(111, 34244, minSup);
		getRecStats(23, 1523, minSup);
		getRecStats(24, 1580, minSup);
		getRecStats(24, 1547, minSup);*/
	}

	private static void evaluateIndividually() throws Exception {
		
		ArrayList<Metrics> metrics = new ArrayList<Metrics>(); 
		//getRecStats(pid,mid,minsup)
		int minSup = 0;	
		
				//1
				//checkPermissionsSettingsAndShowAlert
				//yes
				//featureDesc="Check and add permissions for location access";
				//metrics.add(getRecStats(0, 31838, minSup));
				
				//2
				//creates a new memory cache to store the weather icons
				//yes
				//featureDesc = "creates a new memory cache to store the weather icons";
				//metrics.add(getRecStats(0, 33549, minSup));
				
				//3
				//move file
				//yes
				//featureDesc = "move file";
				//metrics.add(getRecStats(0, 2642, minSup));
				
				//4
				//save forecast
				//yes
				//featureDesc = "save forecast in database";
				//metrics.add(getRecStats(0, 28669, minSup));
				
				//5
				//isNetworkAvailable
				//yes
				//featureDesc = "check if network connection available";
				//metrics.add(getRecStats(0, 29947, minSup));
				
				//6
				//set path to prepare media player
				//yes
				//featureDesc = "set data source for media player";
				//metrics.add(getRecStats(0, 14435, minSup));

				//7
				//get Blueooth paired device name and address
				//yes
				//featureDesc = "receive paired device name and address";
				//metrics.add(getRecStats(0, 33, minSup));
			
				//8
				//sendHttpRequest to get weather
				//yes
				//featureDesc = "send Http Request to get weather";
				//metrics.add(getRecStats(0, 29298, minSup));
				
				//9
				//create new folder
				//yes
				//featureDesc = "create new folder";
				//metrics.add(getRecStats(0, 2250, minSup));

				//10
				//browse to file /directory
				//yes
				//featureDesc = "browse to file /directory";
				//metrics.add(getRecStats(0, 2616, minSup));
				
				//11
//				//get paired Bluetooth devices
				//yes
				//featureDesc = "update list of paired Bluetooth devices";
				//metrics.add(getRecStats(0, 80, minSup));			
//				
				//12
				//connect to a Bluetooth device
				//yes
				//featureDesc = "connect to a Bluetooth device";
				//metrics.add(getRecStats(0, 1161, minSup));		
				
				//13
//				//scanning for other Bluetooth devices
				//yes
				//featureDesc = "do discovery of Bluetooth devices";
				//metrics.add(getRecStats(0, 423, minSup));		
		
				//14
				//handles key presses to start stop pause media
				//featureDesc = "receive key press to start stop pause media";
				//metrics.add(getRecStats(0, 14490, minSup));		
				
				//15
				//write to file
				//featureDesc = "put file to cache";
				//metrics.add(getRecStats(0, 2971, minSup));	
				
				//16
				//draw bitmap
				//featureDesc = "draw bitmap";
				//metrics.add(getRecStats(0, 3017, minSup));	
		
		
		//new ones
		//featureDesc = "send message over Bluetooth";
		//metrics.add(getRecStats(0, 1066, minSup));
		//featureDesc = "play song";
		//metrics.add(getRecStats(0, 24068, minSup));
		//featureDesc = "download music";
		//metrics.add(getRecStats(0, 22968, minSup));
		featureDesc = "search for song";

		metrics.add(getRecStats(0, 15214, minSup));
		
	//////////////////////////////////////////////////////////////////			
				
			
//				metrics.add(getRecStats(63, 13952, minSup));
//				metrics.add(getRecStats(86, 24683, minSup));
//				metrics.add(getRecStats(67, 15214, minSup));
//				metrics.add(getRecStats(78, 22054, minSup));
//				metrics.add(getRecStats(95, 27220, minSup));
//				metrics.add(getRecStats(107, 31759, minSup));
//				metrics.add(getRecStats(114, 36395, minSup));		
//				metrics.add(getRecStats(111, 34244, minSup));			
//				metrics.add(getRecStats(59, 12784, minSup));
//				metrics.add(getRecStats(32, 2087, minSup));
//				metrics.add(getRecStats(56, 7619, minSup));		
//				metrics.add(getRecStats(56, 7214, minSup));
//				metrics.add(getRecStats(19, 1186, minSup));
//				metrics.add(getRecStats(19, 1161, minSup));
//				metrics.add(getRecStats(28, 1701, minSup));
//				metrics.add(getRecStats(2, 56, minSup));
				
					
				float cumulativePrecision = 0;
				float cumulativeSuccessRate = 0;
				float sum = 0;
				int hits = 0;
				int rankcount = 0;
				for(Metrics m: metrics)
				{
					if(m==null)//no recs for input method
					{
						
					}
					else
					{
						hits += 1;
						cumulativePrecision += m.precision;
						
						if(m.precision>0)
						{
							rankcount+=1;
							cumulativeSuccessRate += m.success_rate;
							sum = (float) (sum + (1f/m.rank));
						}	
					}
				}
				//System.out.print(minSup+"," + cumulativePrecision/hits);
				//System.out.print("," + cumulativeSuccessRate/metrics.size());
				//System.out.println("," + sum/rankcount);
				
	}

	private static void evaluateAllMinSup() throws Exception {
		
		//evaluationLOOCVallMethods();
		//getRecStats(pid,mid,minsup)
		//int minSup = 5;
		
		System.out.println("Evaluating " + RelatedMethods.CustomUtilities.Constants.DATABASE + " with minSup ");
		System.out.println("Min Support, Avg. Precision, Success Rate, MRR");
		System.out.println("Evaluating Top: " + topN );
		for(int minSup = 0; minSup <= 25; minSup += 5)
		{		
			ArrayList<Metrics> metrics = new ArrayList<Metrics>(); 

			//1
			//checkPermissionsSettingsAndShowAlert
			//yes
			metrics.add(getRecStats(0, 31838, minSup));
			
			//2
			//creates a new memory cache to store the weather icons
			//yes
			metrics.add(getRecStats(0, 33549, minSup));
			
			//3
			//move file
			//yes
			metrics.add(getRecStats(0, 2642, minSup));
			
			//4
			//save forecast
			//yes
			metrics.add(getRecStats(0, 28669, minSup));
			
			//5
			//isNetworkAvailable
			//yes
			metrics.add(getRecStats(0, 29947, minSup));
			
			//6
			//set path to prepare media player
			//yes
			metrics.add(getRecStats(0, 14435, minSup));

			//7
			//get Blueooth paired device name and address
			//yes
			metrics.add(getRecStats(0, 33, minSup));
		
			//8
			//sendHttpRequest to get weather
			//yes
			metrics.add(getRecStats(0, 29298, minSup));
			
			//9
			//create new folder
			//yes
			metrics.add(getRecStats(0, 2250, minSup));

			//10
			//browse to file /directory
			//yes
			metrics.add(getRecStats(0, 2616, minSup));
			
			//11
//			//get paired Bluetooth devices
			//yes
			metrics.add(getRecStats(0, 80, minSup));			
//			
			//12
			//connect to a Bluetooth device
			//yes
			metrics.add(getRecStats(0, 1161, minSup));		
			
			//13
//			//scanning for other Bluetooth devices
			//yes
			metrics.add(getRecStats(0, 423, minSup));		
	
			//14
			//handles key presses to start stop pause media
			metrics.add(getRecStats(0, 14490, minSup));		
			
			//15
			//write to file
			metrics.add(getRecStats(0, 2971, minSup));	
			
			//16
			//draw bitmap
			metrics.add(getRecStats(0, 3017, minSup));
			
			//new ones
			metrics.add(getRecStats(0, 1066, minSup));
			metrics.add(getRecStats(0, 24068, minSup));
			metrics.add(getRecStats(0, 22968, minSup));
			metrics.add(getRecStats(0, 15214, minSup));
			
		/*metrics.add(getRecStats(63, 13952, minSup));
		metrics.add(getRecStats(86, 24683, minSup));
		metrics.add(getRecStats(67, 15214, minSup));
		metrics.add(getRecStats(78, 22054, minSup));
		metrics.add(getRecStats(95, 27220, minSup));
		metrics.add(getRecStats(107, 31759, minSup));
		metrics.add(getRecStats(114, 36395, minSup));		
		metrics.add(getRecStats(111, 34244, minSup));			
		metrics.add(getRecStats(59, 12784, minSup));
		metrics.add(getRecStats(32, 2087, minSup));
		metrics.add(getRecStats(56, 7619, minSup));		
		metrics.add(getRecStats(56, 7214, minSup));
		metrics.add(getRecStats(19, 1186, minSup));
		metrics.add(getRecStats(19, 1161, minSup));
		metrics.add(getRecStats(28, 1701, minSup));
		metrics.add(getRecStats(2, 56, minSup));*/
		
			
		float cumulativePrecision = 0;
		float cumulativeSuccessRate = 0;
		float sum = 0;
		int hits = 0;
		int rankcount = 0;
		for(Metrics m: metrics)
		{
			if(m==null)//no recs for input method
			{
				
			}
			else
			{
				hits += 1;
				cumulativePrecision += m.precision;
				
				if(m.precision>0)
				{
					rankcount+=1;
					cumulativeSuccessRate += m.success_rate;
					sum = (float) (sum + (1f/m.rank));
				}	
			}
		}
		System.out.print(minSup+"," + cumulativePrecision/hits);
		System.out.print("," + cumulativeSuccessRate/metrics.size());
		System.out.println("," + sum/rankcount);
		}
	}

	private static void evaluationLOOCVallMethods() throws Exception {
		//The main idea is to perform evaluations for emse paper 2020
		//please refer to presentation to dr sarah slides for the evaluation strategy
		//we need to test by min sup strategy and top N methods strategy
		//for these two we need to test FACER S3(from MCS only) and FACER S4(from neighborhood fallback)
		
		//int minSup = 15;
		int K = 5;
		//for point 5 repo support ranges from 4 to 28
		System.out.println("Evaluating " + RelatedMethods.CustomUtilities.Constants.DATABASE + " with minSup ");
		System.out.println("evaluateFACERLOOCVByMinSup");
		for(int minSup =5; minSup<=25; minSup+=5)
		{			
			//evaluateLOOCVByMinSup(minSup);
			
			evaluateFACERLOOCVByMinSup(minSup);
			
		}
		System.out.println("evaluateFACER_ESEC_LOOCVByMinSup");
		for(int minSup =5; minSup<=25; minSup+=5)
		{			
			//evaluateLOOCVByMinSup(minSup);
			
			
			evaluateFACER_ESEC_LOOCVByMinSup(minSup);
		}
		//System.out.println("Evaluating " + Utilities.Constants.DATABASE + " with top k features" );
		//for(int k =5; k<=25; k+=5)
		//{			
		//	evaluateLOOCVByTopK(k);			
		//}
		
		
		
		// hello this is the main idea
		// i want to input a query method from a project to the FACER-S3 algorithm
		// get a list of method recommendations from projects other than input method's project  
		// i want to compare those recommendations to the APIs of the input project 
		// i want to do this for all the projects
		//InputParameter parameter = new InputParameter(190,	748,	3);	
		//evaluate(RecommendationStrategy.FACER_RETRIEVAL_STRATEGY, 0.0f, parameter);
		//evaluate(RecommendationStrategy.C1_MCMILLAN_STRATEGY, 0.0f, parameter);
		//evaluate(RecommendationStrategy.C2_C1Ranked_ByMCS_STRATEGY, 0.0f, parameter);
	}

	private static void evaluateLOOCVByTopK(int K) throws Exception {
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();
		ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		ArrayList<Float> avgPrecisionList = new  ArrayList<Float>();
		ArrayList<Float> avgRecallList = new  ArrayList<Float>();
		ArrayList<Float> avgSuccessRateList = new  ArrayList<Float>();
		ArrayList<Float> avgReciprocalRankList = new  ArrayList<Float>();
		
		ArrayList<Integer> projectIDsList = new  ArrayList<Integer>();
		projectIDsList = dbLayer.getProjectIDs();
		for(Integer pid: projectIDsList)
		{
			//System.out.println("getting cluster IDs of project:" + pid);
			ArrayList<Integer> clusterIDsList = dbLayer.getClusterIDs(pid);
			clusterIDsList.retainAll(nonSingletonClustersList);
			int hits = 0;
			float precision = 0;
			float recall = 0;
			float successrate = 0;
			ArrayList<Integer> rankList = new  ArrayList<Integer>();
			for(Integer cid: clusterIDsList )
			{
				
				ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
				ArrayList<Integer> testProjectFeatureIDs = new ArrayList<>();
				
				testProjectFeatureIDs.addAll(clusterIDsList);
				testProjectFeatureIDs.remove(cid);
				//System.out.println("getting reklated features of cid:" + cid);
				relatedFeatureIDs = getTopKRelatedFeatures(cid, K, dbLayer);
				if(relatedFeatureIDs.size()>0 && testProjectFeatureIDs.size()>0)
				{
					hits+=1;
					ArrayList<Integer> intersection = new ArrayList<Integer>(testProjectFeatureIDs);
					intersection.retainAll(relatedFeatureIDs);

					precision += (float) intersection.size() / relatedFeatureIDs.size();//keep accumulating for every project
					recall += (float) intersection.size() / testProjectFeatureIDs.size();
					
					float pr = (float) intersection.size() / relatedFeatureIDs.size();
					if(pr>0f)
					{
						successrate += 1;
					}
					
					int rank = 0;
					for(int FID : relatedFeatureIDs )
					{
						rank+=1;
						if(testProjectFeatureIDs.contains(FID))
						{
							break;
						}
							
					}
					rankList.add(rank);
				}
			}
			if(hits > 0)
				
			{
				avgPrecisionList.add(precision/hits);
				avgRecallList.add(recall/hits);
				avgSuccessRateList.add(successrate/clusterIDsList.size());
				float sum = 0;
				for(int i: rankList)
				{
					sum = (float) (sum + (1f/i));
				}
				avgReciprocalRankList.add(sum/rankList.size());
			}
			
			
		}
		float averagePrecision = (float) ((avgPrecisionList.stream().mapToDouble(i -> i).sum())/avgPrecisionList.size());
		float averageRecall = (float) ((avgRecallList.stream().mapToDouble(i -> i).sum())/avgRecallList.size());
		float averageSuccessRate = (float) ((avgSuccessRateList.stream().mapToDouble(i -> i).sum())/avgSuccessRateList.size());
		float averageMRR = (float)((avgReciprocalRankList.stream().mapToDouble(i -> i).sum())/avgReciprocalRankList.size());
		System.out.println(K + "," + averagePrecision + "," + averageRecall + "," + averageSuccessRate + "," + averageMRR);
		
	}

	private static ArrayList<Integer> getTopKRelatedFeatures(Integer cid, int k,
			EvaluationDAL dbLayer) throws SQLException {
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//<featureID,support>
			Map<Integer,Integer> featureIDs = dbLayer.getFeatureIDs(cid);//higher support features first
			if(featureIDs.size()!=0)//method belongs to a clone structure
			{
				 
				//iterate over feature ids list to get the clusterIDs
				 Set set = featureIDs.entrySet();
			     Iterator i = set.iterator();
			      while(i.hasNext() ) {
			         Map.Entry me = (Map.Entry)i.next();
			         int featureID = Integer.parseInt(me.getKey().toString());
			         ArrayList<Integer> relatedFeatureIDs = dbLayer.retrieveRelatedClusterIDs(featureID,cid);
			         //only add uptil k features
			         for(int FID : relatedFeatureIDs)
			         {
				         if(relatedFeatures.size() < k)
				         {
				        	 relatedFeatures.add(FID);
				         }
				         else
				        	 break;
			         }
			         if(relatedFeatures.size() >= k)
			        	 break;
			      }
						
			
			}
		
		return relatedFeatures;
	}

	private static void evaluateLOOCVByMinSup(int minSup) throws Exception {
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		
		ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		//System.out.println("nonSingletonClustersList:"+nonSingletonClustersList.size());
		
		ArrayList<Float> avgPrecisionList = new  ArrayList<Float>();
		ArrayList<Float> avgRecallList = new  ArrayList<Float>();
		ArrayList<Float> avgSuccessRateList = new  ArrayList<Float>();
		ArrayList<Float> avgReciprocalRankList = new  ArrayList<Float>();
		
		ArrayList<Integer> projectIDsList = new  ArrayList<Integer>();
		projectIDsList = dbLayer.getProjectIDs();
		for(Integer pid: projectIDsList)
		{
			//System.out.println("getting cluster IDs of project:" + pid);
			ArrayList<Integer> clusterIDsList = dbLayer.getClusterIDs(pid);
			//System.out.println("clusterIDsList:"+clusterIDsList.size());
			clusterIDsList.retainAll(nonSingletonClustersList);
			//System.out.println("clusterIDsList:"+clusterIDsList.size());
			int hits = 0;
			float precision = 0;
			float recall = 0;
			float successrate = 0;
			ArrayList<Integer> rankList = new  ArrayList<Integer>();
			for(Integer cid: clusterIDsList )
			{
				
				ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
				ArrayList<Integer> testProjectFeatureIDs = new ArrayList<>();				
					
				testProjectFeatureIDs.addAll(clusterIDsList);
				testProjectFeatureIDs.remove(cid);
					
				
				//System.out.println("getting reklated features of cid:" + cid);
				relatedFeatureIDs = getRelatedFeatures(cid, minSup, dbLayer);
				//System.out.println("relatedFeatureIDs:"+relatedFeatureIDs.size());
				if(relatedFeatureIDs.size()>0 && testProjectFeatureIDs.size()>0)
				{
					hits+=1;
					ArrayList<Integer> intersection = new ArrayList<Integer>(testProjectFeatureIDs);
					intersection.retainAll(relatedFeatureIDs);

					precision += (float) intersection.size() / relatedFeatureIDs.size();//keep accumulating for every project
					recall += (float) intersection.size() / testProjectFeatureIDs.size();
					float pr = (float) intersection.size() / relatedFeatureIDs.size();
					if(pr>0f)
					{
						successrate += 1;
					}
					int rank = 0;
					for(int FID : relatedFeatureIDs )
					{
						rank+=1;
						if(testProjectFeatureIDs.contains(FID))
						{
							break;
						}
							
					}
					rankList.add(rank);
				}
			}
			if(hits > 0)
				
			{
				avgPrecisionList.add(precision/hits);
				avgRecallList.add(recall/hits);
				avgSuccessRateList.add(successrate/clusterIDsList.size());
				float sum = 0;
				for(int i: rankList)
				{
					sum += 1/i;
				}
				avgReciprocalRankList.add(sum/rankList.size());
			}
			
			
		}
		float averagePrecision = (float) ((avgPrecisionList.stream().mapToDouble(i -> i).sum())/avgPrecisionList.size());
		float averageRecall = (float) ((avgRecallList.stream().mapToDouble(i -> i).sum())/avgRecallList.size());
		float averageSuccessRate = (float) ((avgSuccessRateList.stream().mapToDouble(i -> i).sum())/avgSuccessRateList.size());
		float averageMRR = (float)((avgReciprocalRankList.stream().mapToDouble(i -> i).sum())/avgReciprocalRankList.size());
		System.out.println(minSup + "," + averagePrecision + "," + averageRecall + "," + averageSuccessRate + "," + averageMRR);
		
		
	}

	
	private static void evaluateFACERLOOCVByMinSup(int minSup) throws Exception {
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		
		ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		//System.out.println("nonSingletonClustersList:"+nonSingletonClustersList.size());
		
		ArrayList<Float> avgPrecisionList = new  ArrayList<Float>();
		ArrayList<Float> avgRecallList = new  ArrayList<Float>();
		ArrayList<Float> avgSuccessRateList = new  ArrayList<Float>();
		ArrayList<Float> avgReciprocalRankList = new  ArrayList<Float>();
		
		ArrayList<Integer> projectIDsList = new  ArrayList<Integer>();
		projectIDsList = dbLayer.getProjectIDs();
		//projectIDsList.add(37);
		int numQueries = 0;
		int hits = 0;
		float precision = 0;
		float recall = 0;
		float successrate = 0;
		ArrayList<Integer> rankList = new  ArrayList<Integer>();
		for(Integer pid: projectIDsList)
		{
			//System.out.println("getting cluster IDs of project:" + pid);
			//get files of a project
			int MID = dbLayer.getFirstFeatureMethod(pid);
			if(MID == 0)
				continue;
			numQueries += 1;
				ArrayList<Integer> groundTruthFeatureIDs = dbLayer.getClusterIDs(pid);
				//System.out.println("clusterIDsList:"+clusterIDsList.size());
				groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
				//System.out.println("clusterIDsList:"+clusterIDsList.size());
				
				ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
				
				relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
				//System.out.println("relatedFeatureIDs:"+relatedFeatureIDs.size());
				if(relatedFeatureIDs.size()>0 && groundTruthFeatureIDs.size()>0)
				{
					if(relatedFeatureIDs.size()>topN)
						relatedFeatureIDs.subList(topN, relatedFeatureIDs.size()).clear();
					hits+=1;
					ArrayList<Integer> intersection = new ArrayList<Integer>(groundTruthFeatureIDs);
					intersection.retainAll(relatedFeatureIDs);

					precision += (float) intersection.size() / relatedFeatureIDs.size();//keep accumulating for every project
					recall += (float) intersection.size() / groundTruthFeatureIDs.size();
					float pr = (float) intersection.size() / relatedFeatureIDs.size();
					if(pr>0f)
					{
						successrate += 1;
						int rank = 0;
						for(int FID : relatedFeatureIDs )
						{
							rank+=1;
							if(groundTruthFeatureIDs.contains(FID))
							{
								break;
							}
								
						}
						rankList.add(rank);
					}
					
				}
				
				
			
			
			
			

			//System.out.println("Done project "+pid);
		}
		
		if(hits > 0)
			
		{
			
			float sum = 0;
			for(int i: rankList)
			{
				sum += 1/i;
			}
			
			
			float averagePrecision = (float) precision/hits;
			float averageRecall = (float) recall/hits;
			float averageSuccessRate = (float) successrate/numQueries;
			float averageMRR = (float)sum/rankList.size();
			System.out.println(minSup + "," + averagePrecision + "," + averageRecall + "," + averageSuccessRate + "," + averageMRR);
			
		}
		
		
		
		
	}
	
	private static void evaluateFACER_ESEC_LOOCVByMinSup(int minSup) throws Exception {
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		
		ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		//System.out.println("nonSingletonClustersList:"+nonSingletonClustersList.size());
		
		ArrayList<Float> avgPrecisionList = new  ArrayList<Float>();
		ArrayList<Float> avgRecallList = new  ArrayList<Float>();
		ArrayList<Float> avgSuccessRateList = new  ArrayList<Float>();
		ArrayList<Float> avgReciprocalRankList = new  ArrayList<Float>();
		
		ArrayList<Integer> projectIDsList = new  ArrayList<Integer>();
		projectIDsList = dbLayer.getProjectIDs();
		//projectIDsList.add(37);
		int numQueries = 0;
		int hits = 0;
		float precision = 0;
		float recall = 0;
		float successrate = 0;
		ArrayList<Integer> rankList = new  ArrayList<Integer>();
		for(Integer pid: projectIDsList)
		{
			//System.out.println("getting cluster IDs of project:" + pid);
			//get files of a project
			int MID = dbLayer.getFirstFeatureMethod(pid);
			if(MID == 0)
				continue;
			numQueries += 1;
				ArrayList<Integer> groundTruthFeatureIDs = dbLayer.getClusterIDs(pid);
				//System.out.println("clusterIDsList:"+clusterIDsList.size());
				groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
				//System.out.println("clusterIDsList:"+clusterIDsList.size());
				
				ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
				
				relatedFeatureIDs = getRelatedFeatures_ESEC_FACER(MID, minSup, dbLayer);
				//System.out.println("relatedFeatureIDs:"+relatedFeatureIDs.size());
				if(relatedFeatureIDs.size()>0 && groundTruthFeatureIDs.size()>0)
				{
					if(relatedFeatureIDs.size()>topN)
						relatedFeatureIDs.subList(topN, relatedFeatureIDs.size()).clear();
					hits+=1;
					ArrayList<Integer> intersection = new ArrayList<Integer>(groundTruthFeatureIDs);
					intersection.retainAll(relatedFeatureIDs);

					precision += (float) intersection.size() / relatedFeatureIDs.size();//keep accumulating for every project
					recall += (float) intersection.size() / groundTruthFeatureIDs.size();
					float pr = (float) intersection.size() / relatedFeatureIDs.size();
					if(pr>0f)
					{
						successrate += 1;
						int rank = 0;
						for(int FID : relatedFeatureIDs )
						{
							rank+=1;
							if(groundTruthFeatureIDs.contains(FID))
							{
								break;
							}
								
						}
						rankList.add(rank);
					}
					
				}
				
				
			
			
			
			

			//System.out.println("Done project "+pid);
		}
		
		if(hits > 0)
			
		{
			
			float sum = 0;
			for(int i: rankList)
			{
				sum += 1/i;
			}
			
			
			float averagePrecision = (float) precision/hits;
			float averageRecall = (float) recall/hits;
			float averageSuccessRate = (float) successrate/numQueries;
			float averageMRR = (float)sum/rankList.size();
			System.out.println(minSup + "," + averagePrecision + "," + averageRecall + "," + averageSuccessRate + "," + averageMRR);
			
		}
		
		
		
		
	}
	

	private static ArrayList<Integer> getRelatedFeatures_ESEC_FACER(int mID,
			int minSup, EvaluationDAL dbLayer)throws SQLException  {
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//get CID
		int CID = dbLayer.getCID(mID);
		//if has CID
		if(CID!=0)
		{
			//getRelatedFeatures normal way
			relatedFeatures = getRelatedFeatures(CID, minSup, dbLayer);
			
		}
		

		
		return relatedFeatures;
	}

	private static ArrayList<Integer> getRelatedFeaturesFACER(int mID,
			int minSup, EvaluationDAL dbLayer) throws SQLException {
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//get CID
		int CID = dbLayer.getCID(mID);
		//if has CID
		if(CID!=0)
		{
			//getRelatedFeatures normal way
			relatedFeatures = getRelatedFeatures(CID, minSup, dbLayer);
			
			//if no features, get from NeighborhoodMethods
			if(relatedFeatures.size()==0)
			{
				relatedFeatures = getRelatedFeaturesFromNeighborhood(mID,CID, minSup, dbLayer);
				
			}
			else
			{
				//System.out.println("Rec from MCS");
			}
		}
		else
		{
		//if no CID
			relatedFeatures = getRelatedFeaturesFromNeighborhood(mID,CID, minSup, dbLayer);
			
		}

		
		return relatedFeatures;
	}

	private static ArrayList<Integer> getRelatedFeaturesFromNeighborhood(
			int mID, int CIDofMID, int minSup, EvaluationDAL dbLayer) throws SQLException {
		
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//getNeighborhoodMethods CIDs
		
		ArrayList<Integer> neighborhoodCIDs = dbLayer.getNeighborhoodCIDs(mID,CIDofMID);
		
		Map<Integer,Integer> featureIDs  = new LinkedHashMap<Integer,Integer>();
		for(int CID: neighborhoodCIDs)
		{
			//<featureID,support>
			featureIDs.putAll(dbLayer.getFeatureIDs(CID, minSup));//higher support features first		
			
		}
		if(featureIDs.size()!=0)//belongs to a clone structure
		{
			//sort the featureIDs map
			featureIDs = HashMapSorter.sortHashMapByValuesTest2(featureIDs);
			//iterate over feature ids list to get the clusterIDs
			 Set set = featureIDs.entrySet();
		     Iterator i = set.iterator();
		      while(i.hasNext()) {
		         Map.Entry me = (Map.Entry)i.next();
		         ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs2(Integer.parseInt(me.getKey().toString()));	
		         for(int CID:someRelatedFeatures)
		         {
		        	//this check of not equal to neighborhood CIDs shouldnt be here
		        	 //if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.contains(CID))
		        	 if(!relatedFeatures.contains(CID)&& CID!=CIDofMID)
		        		 relatedFeatures.add(CID);
		         }
		      }
		      //System.out.println("Rec from call heirarchy neighborhood");		
		     
		}
		else
		{
			neighborhoodCIDs = dbLayer.getsameFielNeighborhoodCIDs(mID,CIDofMID);			
			featureIDs  = new LinkedHashMap<Integer,Integer>();
			for(int CID: neighborhoodCIDs)
			{
				//<featureID,support>
				featureIDs.putAll(dbLayer.getFeatureIDs(CID, minSup));//higher support features first		
				
			}
			if(featureIDs.size()!=0)//belongs to a clone structure
			{
					//System.out.println("Rec from same file neighborhood");
				
				//sort the featureIDs map
				featureIDs = HashMapSorter.sortHashMapByValuesTest2(featureIDs);
				//iterate over feature ids list to get the clusterIDs
				 Set set = featureIDs.entrySet();
			     Iterator i = set.iterator();
			      while(i.hasNext()) {
			         Map.Entry me = (Map.Entry)i.next();
			         ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs2(Integer.parseInt(me.getKey().toString()));	
			         for(int CID:someRelatedFeatures)
			         {
			        	 //this check of not equal to neighborhood CIDs shouldnt be here
			        	 //if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.contains(CID))
			        	 if(!relatedFeatures.contains(CID) && CID!=CIDofMID)
			        		 relatedFeatures.add(CID);
			         }
			      }
						
			     
			}
		}
		
		return relatedFeatures;
	}

	private static ArrayList<Integer> getRelatedFeatures(Integer cid, int minSup, EvaluationDAL dbLayer) throws SQLException {
		
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//<featureID,support>
			Map<Integer,Integer> featureIDs = dbLayer.getFeatureIDs(cid, minSup);//higher support features first
			if(featureIDs.size()!=0)//method belongs to a clone structure
			{
				//iterate over feature ids list to get the clusterIDs
				 Set set = featureIDs.entrySet();
			     Iterator i = set.iterator();
			      while(i.hasNext()) {
			         Map.Entry me = (Map.Entry)i.next();
			        ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs(Integer.parseInt(me.getKey().toString()),cid);
			        for(int CID:someRelatedFeatures)
			        {
			        	
			        	if(!relatedFeatures.contains(CID))
			        		relatedFeatures.add(CID);
			        }
			      }
						
			
			}
		
		return relatedFeatures;
	}

	private static void evaluate(int strategy, float thresh, InputParameter parameter) throws Exception {
		RecommendationStrategy recStrategy = new RecommendationStrategy();
		recStrategy.setRecommendationStrategy(strategy);
		recStrategy.set_similarityThreshold(thresh);
		System.out.println(recStrategy.getRecommendationStrategyName());
		//System.out.println("Q7");
		
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();
		methodIDsList = getRecommendations(recStrategy, parameter);	
		evaluateRecommendations(methodIDsList, parameter);
	}

	private static void evaluateRecommendations(
			ArrayList<Integer> methodIDsList, InputParameter parameter) throws Exception {
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		
		//get the API usages from the user method's host project from all other methods
		Set<String> ValidationAPIUsages = new HashSet<>();
		ValidationAPIUsages = dbLayer.getHashedMethodAPICallsFromOtherMethods(parameter.methodID, parameter.projectID);	
		
		//get the API usages from the methodIDsList
		Set<String> recommendedAPIUsages = new HashSet<>();
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
		//calculate precision and recall and output here		
		//compare the API usages in validation set with the API usages recommended
		int matchingAPICalls = 0;
		int totalAPICallsRecommended = recommendedAPIUsages.size();
		
		for(String usage: recommendedAPIUsages)
		{
			if(ValidationAPIUsages.contains(usage))
			{
				matchingAPICalls += 1;
			
			}			
		}
		
		float precision = (float)matchingAPICalls/totalAPICallsRecommended;
		System.out.println("Precision: " + precision);
		
		//calculate recall		
		int relevant = ValidationAPIUsages.size();
		float recall = (float)matchingAPICalls/relevant;
		System.out.println("Recall: " + recall);

		
	}

	private static ArrayList<Integer> getRecommendations(RecommendationStrategy recStrategy, InputParameter parameter) throws Exception {
		ArrayList<Integer> relatedMethodsList = new ArrayList<Integer>();
		relatedMethodsList = RecommendationStrategy.executeStrategy(recStrategy.getRecommendationStrategy(), parameter);
		return relatedMethodsList;
	}
	
	private static Metrics getRecStats(int pid, int MID, int minSup) throws Exception
	{
		Metrics metrics = new Metrics();
		//System.out.println("Recs for method: "+ MID);
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		pid = dbLayer.getProjectID(MID);
		if(userfriendly)
		{
		String projectCategory = dbLayer.getProjectName(pid);
		System.out.println("Evaluation ID:" + MID);
		System.out.println("Application Category:"+ projectCategory.substring(0,projectCategory.indexOf("_")));
		}
		ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		ArrayList<Integer> groundTruthFeatureIDs = dbLayer.getClusterIDs(pid);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		
		relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
		//System.out.print("No. of recommendations:"+relatedFeatureIDs.size()+",");
		
		int hits = 0;
		float precision = 0;
		float successrate = 0;
		int rank = 0;
		
		
		if(relatedFeatureIDs.size()>0 && groundTruthFeatureIDs.size()>0)
		{
			if(relatedFeatureIDs.size()>topN)
				relatedFeatureIDs.subList(topN, relatedFeatureIDs.size()).clear();
			if(userfriendly)
			{
			//comment till // for making method bodies disappear in execution output
			Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];  
			int index = 0;
			for(int i:relatedFeatureIDs)
			{
				clusterIDs[index] = i;
				index++;
			}
			Method m = dbLayer.getMethodDetails(MID);
			System.out.println("Your Method's Feature Description: "+featureDesc);
			System.out.println("-------------------------------------------------------");
			System.out.println("Your Method's Body: ");
			//System.out.println("======================================");
			ArrayList<String> body = ViewSampleMethodForClusters.getMethodBody(m);
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);	
			
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("-------------------------------------------------------");
			System.out.println("Your Method's API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
			System.out.println("======================================");
			System.out.println("Related Methods:");
			System.out.println("======================================");
			
			ViewSampleMethodForClusters.viewMethodsAgainstClusterIDsNew(clusterIDs,pid);
			//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(clusterIDs);
			}
			//
			hits+=1;
			ArrayList<Integer> intersection = new ArrayList<Integer>(groundTruthFeatureIDs);
			intersection.retainAll(relatedFeatureIDs);
			//System.out.println("relatedFeatureIDs size: " + relatedFeatureIDs.size());
			precision += (float) intersection.size() / relatedFeatureIDs.size();//keep accumulating for every project
			float pr = (float) intersection.size() / relatedFeatureIDs.size();
			if(pr>0f)
			{
				successrate += 1;
				
				for(int FID : relatedFeatureIDs )
				{
					rank+=1;
					if(groundTruthFeatureIDs.contains(FID))
					{
						break;
					}
						
				}
				
			}
			//System.out.println(minSup + "," + precision);
			metrics.precision = precision;
			metrics.success_rate = successrate;
			metrics.rank = rank;
			System.out.println(precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	public static ArrayList<Integer> getRelatedMethodIDsForUI(int MID) throws Exception
	{	ArrayList<Integer> MIDsList = new ArrayList<Integer>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		int pid = dbLayer.getProjectID(MID);		
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();		
		relatedFeatureIDs = getRelatedFeaturesFACER(MID, 0, dbLayer);		
		
		if(relatedFeatureIDs.size()>0 )
		{
			if(relatedFeatureIDs.size()>topN)
				relatedFeatureIDs.subList(topN, relatedFeatureIDs.size()).clear();
			
			//comment till // for making method bodies disappear in execution output
			Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];  
			int index = 0;
			for(int i:relatedFeatureIDs)
			{
				clusterIDs[index] = i;
				index++;
			}			
			MIDsList = ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(clusterIDs,pid);
		
		}
		
		return MIDsList;
		
	}
}
