package related_methods.automated_evaluation;

import RelatedMethods.db_access_layer.EvaluationDAL;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;





public class McMillanEvaluation {
	public static HashMap<Integer, Double> projectSimilarities = new HashMap<Integer, Double>();
	public static HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
	public static Map<Integer, Double> featurePredictionScores = new HashMap<Integer, Double>();
	public static int previousUserFunctionID = 0;
	static EvaluationDAL dbLayer;
	
	public static void main(String args[]) throws Exception
	{		
		//find top 20 projects similar to user selected function
		int userFunctionID = 33;
		int userCloneID = 8;
		dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();
		
		ArrayList<Integer> projectIDs = getTop20SimilarProjects(userFunctionID, userCloneID);
		//get prediction score for each function of those projects which is a clone class member (a feature in other words)
		HashMap<Integer, Set<Integer>> projectCloneIDs = getFeaturesFromProjects(projectIDs);
		Map<Integer, Double> clonePredictionScores = getPredictionScores(projectCloneIDs);
		//get the top 10 functions against clones
		ArrayList<Integer> methodIDs = getTop10Functions(clonePredictionScores, projectCloneIDs);
		//System.out.println(methodIDs);
		//compare the APIs of methods with those of heldout set
	
		//get precision and recall
	}
	
	public static ArrayList<Integer> getMcMillanMethodRecommendations(int MID, int CID, int PID, boolean isWeightedByMCS, float similarityThreshold) throws Exception
	{
		dbLayer = EvaluationDAL.getInstance();
		if(!EvaluationDAL.initializedFoldDBCon)
			dbLayer.initializeFoldDatabaseConnector();
		projectSimilarities = new HashMap<Integer, Double>();
		projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		previousUserFunctionID = 0;
		
		ArrayList<Integer> topMethodsList = new ArrayList<Integer>();
		ArrayList<Integer> projectIDs = new ArrayList<Integer>();
		if(similarityThreshold > 0 && similarityThreshold <= 1)
			projectIDs = getSimilarProjectsByThreshold(MID, CID, similarityThreshold);
		else			
			projectIDs = getTop20SimilarProjects(MID, CID);
		//get prediction score for each function of those projects which is a clone class member (a feature in other words)
		HashMap<Integer, Set<Integer>> projectCloneIDs = getFeaturesFromProjects(projectIDs);
		Map<Integer, Double> clonePredictionScores = new HashMap<Integer, Double>();
		if(isWeightedByMCS)
			clonePredictionScores = getMCSBasedPredictionScores(projectCloneIDs);
		else
			clonePredictionScores = getPredictionScores(projectCloneIDs);
		//you can use this line to return clone ids instead of methods
		Set<Integer> cloneIDs =  clonePredictionScores.keySet();
		//get the top 10 functions against clones
		topMethodsList = getTop10Functions(clonePredictionScores, projectCloneIDs);
		dbLayer.closeConnector();
		return topMethodsList;		
	}

	public static ArrayList<Integer> getMcMillanFeatureRecommendations(boolean tuned, int Nprojs, double gamma, int MID, int CID, ArrayList<Integer> contextFeatureIDs, int PID, boolean isWeightedByMCS, float similarityThreshold) throws Exception
	{
		dbLayer = EvaluationDAL.getInstance();
		if(!EvaluationDAL.initializedFoldDBCon)
			dbLayer.initializeFoldDatabaseConnector();

		projectSimilarities = new HashMap<Integer, Double>();
		projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		previousUserFunctionID = 0;
		
		ArrayList<Integer> topMethodsList = new ArrayList<Integer>();
		ArrayList<Integer> projectIDs = new ArrayList<Integer>();
		if(similarityThreshold > 0 && similarityThreshold <= 1)
			projectIDs = getSimilarProjectsByThreshold(MID, CID, similarityThreshold);
		else
		{
			//Date startPredScore= new Date();
			//projectIDs = getTop20SimilarProjects(MID, CID);
			projectIDs = getTop20SimilarProjectsByContext(tuned, Nprojs, MID, CID, contextFeatureIDs, PID);
			//displayStartStopTime(startPredScore, "time for gettop neighbor projects: ");
		}
		//get prediction score for each function of those projects which is a clone class member (a feature in other words)
		HashMap<Integer, Set<Integer>> projectCloneIDs = getFeaturesFromProjects(projectIDs);
		Map<Integer, Double> clonePredictionScores = new HashMap<Integer, Double>();
		if(isWeightedByMCS)
			clonePredictionScores = getMCSBasedPredictionScores(projectCloneIDs);
		else {
			//Date startPredScore= new Date();
			clonePredictionScores = getPredictionScores(projectCloneIDs);
			//displayStartStopTime(startPredScore, "time for get prediction scores: ");
		}
		
		//process the prediction scores
		//process clones and scores to get optimal	
		
		Set<Integer> cloneIDs = new HashSet<Integer>();		
		if(tuned)
		{
		Iterator it = clonePredictionScores.entrySet().iterator();
		
		/*boolean isFirst = false;
		int count = 0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        double score = (double) pair.getValue();
	        if(count == 0 && score >= 0.5)
	        {
	        	isFirst = true;
	        	cloneIDs.add((int)pair.getKey());
	        	
	        }
	        if(count > 0 && isFirst && score >= 0.5)
	        {	        
	        	cloneIDs.add((int)pair.getKey());
	        }
	        if(count == 0 && score < 0.5 ) //add only one
	        {
	        	cloneIDs.add((int)pair.getKey());
	        }
	        
	        count++;
	    }*/
		 	while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        double score = (double) pair.getValue();
		        if(score >= gamma)
		        {
		        	cloneIDs.add((int)pair.getKey());		        	
		        }		       
		    }
		}
		else
		{
			cloneIDs =  clonePredictionScores.keySet();
		}
	    
		//you can use this line to return clone ids instead of methods
		//Set<Integer> cloneIDs =  clonePredictionScores.keySet();
		//convert set to arraylist
		 ArrayList<Integer> cloneIDslist = convertSetToList(cloneIDs); 
		//get the top 10 functions against clones
		//topMethodsList = getTop10Functions(clonePredictionScores, projectCloneIDs);
		//dbLayer.closeConnector();
		 
	
		 
		return cloneIDslist;		
	}

	public static ArrayList<Integer> getMcMillanFeatureRecommendationsB(boolean tuned, int Nprojs, double gamma, ArrayList<Integer> contextFeatureIDs, int PID, boolean isWeightedByMCS, float similarityThreshold) throws Exception
	{
		dbLayer = EvaluationDAL.getInstance();
		if(!EvaluationDAL.initializedFoldDBCon)
			dbLayer.initializeFoldDatabaseConnector();

		projectSimilarities = new HashMap<Integer, Double>();
		projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		previousUserFunctionID = 0;

		ArrayList<Integer> topMethodsList = new ArrayList<Integer>();
		ArrayList<Integer> projectIDs = new ArrayList<Integer>();
		//if(similarityThreshold > 0 && similarityThreshold <= 1)
			//projectIDs = getSimilarProjectsByThreshold(MID, CID, similarityThreshold);
		//else
		{
			//Date startPredScore= new Date();
			//projectIDs = getTop20SimilarProjects(MID, CID);
			projectIDs = getTop20SimilarProjectsByContextB(tuned, Nprojs, contextFeatureIDs, PID);
			//displayStartStopTime(startPredScore, "time for gettop neighbor projects: ");
		}
		//get prediction score for each function of those projects which is a clone class member (a feature in other words)
		HashMap<Integer, Set<Integer>> projectCloneIDs = getFeaturesFromProjects(projectIDs);
		Map<Integer, Double> clonePredictionScores = new HashMap<Integer, Double>();
		if(isWeightedByMCS)
			clonePredictionScores = getMCSBasedPredictionScores(projectCloneIDs);
		else {
			//Date startPredScore= new Date();
			clonePredictionScores = getPredictionScores(projectCloneIDs);
			//displayStartStopTime(startPredScore, "time for get prediction scores: ");
		}

		//process the prediction scores
		//process clones and scores to get optimal

		Set<Integer> cloneIDs = new HashSet<Integer>();
		if(tuned)
		{
			Iterator it = clonePredictionScores.entrySet().iterator();

		/*boolean isFirst = false;
		int count = 0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        double score = (double) pair.getValue();
	        if(count == 0 && score >= 0.5)
	        {
	        	isFirst = true;
	        	cloneIDs.add((int)pair.getKey());

	        }
	        if(count > 0 && isFirst && score >= 0.5)
	        {
	        	cloneIDs.add((int)pair.getKey());
	        }
	        if(count == 0 && score < 0.5 ) //add only one
	        {
	        	cloneIDs.add((int)pair.getKey());
	        }

	        count++;
	    }*/
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				double score = (double) pair.getValue();
				if(score >= gamma)
				{
					cloneIDs.add((int)pair.getKey());
				}
			}
		}
		else
		{
			cloneIDs =  clonePredictionScores.keySet();
		}

		//you can use this line to return clone ids instead of methods
		//Set<Integer> cloneIDs =  clonePredictionScores.keySet();
		//convert set to arraylist
		ArrayList<Integer> cloneIDslist = convertSetToList(cloneIDs);
		//get the top 10 functions against clones
		//topMethodsList = getTop10Functions(clonePredictionScores, projectCloneIDs);
		//dbLayer.closeConnector();



		return cloneIDslist;
	}

	public static ArrayList<Integer> getMcMillanFeatureRecommendationsNew(boolean tuned, int Nprojs, double gamma, int MID, int CID, ArrayList<Integer> contextFeatureIDs, ArrayList<Integer> contextProjectsMappings, HashMap<Integer, Set<Integer>> contextProjectCloneIDs, HashMap<Integer, Double> projectIDScores, int PID, boolean isWeightedByMCS, float similarityThreshold) throws Exception
	{
		dbLayer = EvaluationDAL.getInstance();
		if(!EvaluationDAL.initializedFoldDBCon)
			dbLayer.initializeFoldDatabaseConnector();

		projectSimilarities = new HashMap<Integer, Double>();
		//projectSimilarities = projectIDScores;
		projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		previousUserFunctionID = 0;

		ArrayList<Integer> topMethodsList = new ArrayList<Integer>();
		ArrayList<Integer> projectIDs = new ArrayList<Integer>();
		if(similarityThreshold > 0 && similarityThreshold <= 1)
			projectIDs = getSimilarProjectsByThreshold(MID, CID, similarityThreshold);
		else
		{
			//Date startPredScore= new Date();
			//projectIDs = getTop20SimilarProjects(MID, CID);
//commented out for now
			projectIDs = getTop20SimilarProjectsByContextNew(tuned, Nprojs, MID, CID, contextFeatureIDs, contextProjectsMappings, contextProjectCloneIDs,  PID);

			//projectIDs = contextProjectsMappings;
			//displayStartStopTime(startPredScore, "time for gettop neighbor projects: ");
		}
		//get prediction score for each function of those projects which is a clone class member (a feature in other words)
		//HashMap<Integer, Set<Integer>> projectCloneIDs = getFeaturesFromProjects(projectIDs);

		ArrayList<Integer> cloneIDslist = new ArrayList<>();
		//projectCloneIDs should contain only those projects that are in projectSimailarities
		projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		if(projectSimilarities!=null) {
			for (Map.Entry<Integer, Double> entry : projectSimilarities.entrySet()) {
				int projectID = entry.getKey();
				projectCloneIDs.put(projectID, contextProjectCloneIDs.get(projectID));
			}
			//projectCloneIDs = contextProjectCloneIDs;

			Map<Integer, Double> clonePredictionScores = new HashMap<Integer, Double>();
			if (isWeightedByMCS)
				clonePredictionScores = getMCSBasedPredictionScores(projectCloneIDs);
			else {
				//Date startPredScore = new Date();
				clonePredictionScores = getPredictionScores(projectCloneIDs);
				//displayStartStopTime(startPredScore, "time for get prediction scores: ");
			}

			//process the prediction scores
			//process clones and scores to get optimal



		/*boolean isFirst = false;
		int count = 0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        double score = (double) pair.getValue();
	        if(count == 0 && score >= 0.5)
	        {
	        	isFirst = true;
	        	cloneIDs.add((int)pair.getKey());

	        }
	        if(count > 0 && isFirst && score >= 0.5)
	        {
	        	cloneIDs.add((int)pair.getKey());
	        }
	        if(count == 0 && score < 0.5 ) //add only one
	        {
	        	cloneIDs.add((int)pair.getKey());
	        }

	        count++;
	    }*/

			Set<Integer> cloneIDs = new HashSet<Integer>();
			if (tuned) {
				Iterator it = clonePredictionScores.entrySet().iterator();

				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					double score = (double) pair.getValue();
					if (score >= gamma) {
						cloneIDs.add((int) pair.getKey());
					}
				}
			} else {
				cloneIDs = clonePredictionScores.keySet();
			}

			featurePredictionScores = clonePredictionScores;
			//you can use this line to return clone ids instead of methods
			//Set<Integer> cloneIDs =  clonePredictionScores.keySet();
			//convert set to arraylist
			cloneIDslist = convertSetToList(cloneIDs);
			//get the top 10 functions against clones
			//topMethodsList = getTop10Functions(clonePredictionScores, projectCloneIDs);
			//dbLayer.closeConnector();

		}

		return cloneIDslist;
	}

	private static void displayStartStopTime(Date startTime, String s) throws FileNotFoundException, UnsupportedEncodingException {
		final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		System.out.println("Start Date and " +s+ f.format(startTime));
		System.out.println("Stop Date and " +s+  f.format(new Date()));


	}


	public static <T> ArrayList<T> convertSetToList(Set<T> set) 
    { 
        // create an empty list 
        ArrayList<T> list = new ArrayList<>(); 
  
        // push each element in the set into the list //upto 50 only
        for (T t : set) {
        	if(list.size()<50)
        		list.add(t); 
        }
  
        // return the list 
        return list; 
    } 
	private static ArrayList<Integer> getTop10Functions(
			Map<Integer, Double> clonePredictionScores, HashMap<Integer, Set<Integer>> projectCloneIDs) throws Exception {
		//use the clone ID and its project id to get the methodID for ten cloneIDs
		ArrayList<Integer> methodIDs = new ArrayList<Integer>();
		Set<Integer> projectIDs =  projectCloneIDs.keySet();
		Set<Integer> cloneIDs =  clonePredictionScores.keySet();
		//EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		//dbLayer.initializeFoldDatabaseConnector();	
		for(int CID : cloneIDs){
			methodIDs.add(dbLayer.getMethodByClusterID(CID, projectIDs));
			if(methodIDs.size()==15)
				break;
		}
		//dbLayer.closeConnector();	
		return methodIDs;
	}

	private static Map<Integer, Double> getPredictionScores(
			HashMap<Integer, Set<Integer>> topProjectCloneIDs) {

		float sum_of_similarities = findSumOfValues(projectSimilarities);
		HashMap <Integer,Double> predScores= new HashMap <Integer,Double> ();
		Set<Integer> projectIDs = topProjectCloneIDs.keySet();
		//for each cloneID/feature from the top 20 projects,
		for(Map.Entry<Integer, Set<Integer>> entry : topProjectCloneIDs.entrySet())
		{
			int projectID = entry.getKey();
			Set<Integer> cloneIDs = entry.getValue();
			for(int CID: cloneIDs)
			{
				float numerator_simscore = 0f;
				//for each top 20 project,
				for(int PID: projectIDs)
				{			
					//if the feature is in a project then accumulate sim score of project
					if(isFeatureInProject(CID, PID, topProjectCloneIDs))
					{
						numerator_simscore+= projectSimilarities.get(PID);
						
					}
				
				}
				if(numerator_simscore != 0)
				{
					//divide accumulated sim scores with summation of all sim scores
					double predScore = (float) numerator_simscore/sum_of_similarities;
					//put the result in a hash map <cloneID, pred score>
					//put score only if greater than 0.5
					//if(predScore > 0.3)
					predScores.put(CID, predScore);
				}
			}
		}
		//sort the scores map
		HashMap<Integer, Double> sortedPredScores = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(predScores);
		
		//return sorted scores
		return sortedPredScores;
		
	}

	private static boolean isFeatureInProject(int cID, int pID,
			HashMap<Integer, Set<Integer>> topProjectCloneIDs) {
		
		Set<Integer> cloneIDs = topProjectCloneIDs.get(pID);	
		
		return cloneIDs.contains(cID);
	}

	private static float findSumOfValues(
			HashMap<Integer, Double> projectSimilarities2) {
		float sum = 0f;
		for (Map.Entry<Integer, Double> entry : projectSimilarities2.entrySet()) {
		     sum += entry.getValue();
		}
		return sum;
	}

	private static HashMap<Integer, Set<Integer>> getFeaturesFromProjects(
			ArrayList<Integer> projectIDs) throws Exception {
		
		//EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		//dbLayer.initializeFoldDatabaseConnector();	
		//get list of cloneIDs from a list of projects
		HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		projectCloneIDs = dbLayer.getProjectCloneIDs(projectIDs);
		
		//dbLayer.closeConnector();
		return projectCloneIDs;
	}

	private static ArrayList<Integer> getTop20SimilarProjects(
			int userFunctionID, int userCloneID) throws Exception {
		
		ArrayList<Integer> top20Projects= new ArrayList<Integer>();		
		//get connection to 101 repo
		
		//get list of cloneIDs from all projects in a hash map <projectID, cloneIDs>	
		if(previousUserFunctionID == 0)
		{
			previousUserFunctionID = userFunctionID;
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
		}
		else if(userFunctionID != previousUserFunctionID)
		{
			projectCloneIDs = new HashMap<Integer, Set<Integer>>();
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
			previousUserFunctionID = userFunctionID;
		}
		//make a Set of Integers from the userFunctionCloneID
		Set<Integer>  v_user = new HashSet<Integer>();	
		v_user.add(userCloneID);
		
		//iterate over hashmap
		for (Map.Entry<Integer, Set<Integer>> entry : projectCloneIDs.entrySet()) {
		    int projectID = entry.getKey();
		    //make a Set of Integers from the cloneIDs of a project
		    Set<Integer>v_cloneIDs = entry.getValue();			
			//make a Set of float containing similarity scores of each project 
		    if(v_cloneIDs.size()>0)
		    {
			    double similarity = RelatedMethods.CustomUtilities.CosineSimilarityCalculator.computeCosineSimilarity(v_user, v_cloneIDs);
				if(similarity > 0)
				{
					//System.out.println("Similarity hai! ");
					//get cosine similarity measures and store in the float Map
					projectSimilarities.put(projectID, similarity);
				}
		    }
		}		
		//get top 20 projects
		HashMap<Integer, Double> sortedProjectSimilarities = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(projectSimilarities);
		//return ArrayList<Integer> of top 20 projects
		ArrayList<Integer> topProjectIDs = new ArrayList<Integer> ();
		for(int i: sortedProjectSimilarities.keySet())
		{
			topProjectIDs.add(i);
			if(topProjectIDs.size()==20)
				break;
		}
		
		//dbLayer.closeConnector();
		return topProjectIDs;
	}
	
	private static ArrayList<Integer> getTop20SimilarProjectsByContext(boolean tuned, int Nprojs,
																	   int userFunctionID, int userCloneID, ArrayList<Integer> contextFeatureIDs, int pid) throws Exception {
		
		//get list of cloneIDs from all projects in a hash map <projectID, cloneIDs>
		if(previousUserFunctionID == 0)
		{
			previousUserFunctionID = userFunctionID;
			//based on API usages in user function get similar projects
			//Date startTime = new Date();
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
			//displayStartStopTime(startTime, "Time for getCOntextBasedProjCloneIDs:  ");

		}
		else if(userFunctionID != previousUserFunctionID)
		{
			//projectCloneIDs = new HashMap<Integer, Set<Integer>>();
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
			previousUserFunctionID = userFunctionID;
		}
		//make a Set of Integers from the userFunctionCloneID
		Set<Integer>  v_user = new HashSet<Integer>();	
		v_user.add(userCloneID);
		//for a bigger context add more cloneIDs from the host project of user function
		//HashSet<Integer>  contextCloneIDs = new HashSet<Integer>();	
		//contextCloneIDs = dbLayer.getContextCloneIDs(userFunctionID,pid);
		//Iterator<Integer> itr = contextCloneIDs.iterator(); // traversing over HashSet 
		
		for (int i:contextFeatureIDs) 
		{ 
			//if(v_user.size() < contextCloneIDs.size()/2)//i only want to add half of the features of context
			v_user.add(i); 
		}
		//System.out.println("The context features of size " + v_user.size() + " are as follows: ");
		//for(int FID: v_user)
		//	System.out.print(FID+",");
		//System.out.println();
		//iterate over hashmap
		for (Map.Entry<Integer, Set<Integer>> entry : projectCloneIDs.entrySet()) {
		    int projectID = entry.getKey();
		    //make a Set of Integers from the cloneIDs of a project
		    Set<Integer>v_cloneIDs = entry.getValue();			
			//make a Set of float containing similarity scores of each project 
		    if(v_cloneIDs.size()>0)
		    {
			    double similarity = RelatedMethods.CustomUtilities.CosineSimilarityCalculator.computeCosineSimilarity(v_user, v_cloneIDs);
				if(similarity > 0)
				{
					//System.out.println("Similarity hai! ");
					//get cosine similarity measures and store in the float Map
					projectSimilarities.put(projectID, similarity);
				}
		    }
		}		
		//get top 20 projects
		HashMap<Integer, Double> sortedProjectSimilarities = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(projectSimilarities);
		//return ArrayList<Integer> of top 20 projects
		ArrayList<Integer> topProjectIDs = new ArrayList<Integer> ();
		int topN = 0;
		if(tuned)
			topN=Nprojs;
		else
			topN=Nprojs;
		for(int i: sortedProjectSimilarities.keySet())
		{
			topProjectIDs.add(i);
			if(topProjectIDs.size()==topN)
				break;
		}
		
		//dbLayer.closeConnector();
		return topProjectIDs;
	}

	private static ArrayList<Integer> getTop20SimilarProjectsByContextB(boolean tuned, int Nprojs,
																	    ArrayList<Integer> contextFeatureIDs, int pid) throws Exception {

		projectCloneIDs = dbLayer.getProjectCloneIDsMinusOne(pid);
		//get list of cloneIDs from all projects in a hash map <projectID, cloneIDs>

		//make a Set of Integers from the userFunctionCloneID
		Set<Integer>  v_user = new HashSet<Integer>();
		//v_user.add(userCloneID);
		//for a bigger context add more cloneIDs from the host project of user function
		//HashSet<Integer>  contextCloneIDs = new HashSet<Integer>();
		//contextCloneIDs = dbLayer.getContextCloneIDs(userFunctionID,pid);
		//Iterator<Integer> itr = contextCloneIDs.iterator(); // traversing over HashSet

		for (int i:contextFeatureIDs)
		{
			//if(v_user.size() < contextCloneIDs.size()/2)//i only want to add half of the features of context
			v_user.add(i);
		}
		//System.out.println("The context features of size " + v_user.size() + " are as follows: ");
		//for(int FID: v_user)
		//	System.out.print(FID+",");
		//System.out.println();
		//iterate over hashmap
		for (Map.Entry<Integer, Set<Integer>> entry : projectCloneIDs.entrySet()) {
			int projectID = entry.getKey();
			//make a Set of Integers from the cloneIDs of a project
			Set<Integer>v_cloneIDs = entry.getValue();
			//make a Set of float containing similarity scores of each project
			if(v_cloneIDs.size()>0)
			{
				double similarity = RelatedMethods.CustomUtilities.CosineSimilarityCalculator.computeCosineSimilarity(v_user, v_cloneIDs);
				if(similarity > 0)
				{
					//System.out.println("Similarity hai! ");
					//get cosine similarity measures and store in the float Map
					projectSimilarities.put(projectID, similarity);
				}
			}
			int i = 1;
		}
		//get top 20 projects
		HashMap<Integer, Double> sortedProjectSimilarities = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(projectSimilarities);
		//return ArrayList<Integer> of top 20 projects
		ArrayList<Integer> topProjectIDs = new ArrayList<Integer> ();
		int topN = 0;
		if(tuned)
			topN=Nprojs;
		else
			topN=Nprojs;
		for(int i: sortedProjectSimilarities.keySet())
		{
			topProjectIDs.add(i);
			if(topProjectIDs.size()==topN)
				break;
		}

		//dbLayer.closeConnector();
		return topProjectIDs;
	}


	private static ArrayList<Integer> getTop20SimilarProjectsByContextNew(boolean tuned, int Nprojs,
																		  int userFunctionID, int userCloneID, ArrayList<Integer> contextFeatureIDs, ArrayList<Integer> contextProjectsMappings, HashMap<Integer, Set<Integer>> contextProjectCloneIDs, int pid) throws Exception {

		projectCloneIDs = contextProjectCloneIDs;
		//projectCloneIDs = dbLayer.getProjectCloneIDsMinusOne(pid);
//		//get list of cloneIDs from all projects in a hash map <projectID, cloneIDs>
//		if(previousUserFunctionID == 0)
//		{
//			previousUserFunctionID = userFunctionID;
//			//based on API usages in user function get similar projects
//			//Date startTime = new Date();
//			projectCloneIDs = dbLayer.getContextBasedProjectCloneIDs(contextProjectsMappings);
//			//displayStartStopTime(startTime, "Time for getCOntextBasedProjCloneIDs:  ");
//
//		}
//		else if(userFunctionID != previousUserFunctionID)
//		{
//			//projectCloneIDs = new HashMap<Integer, Set<Integer>>();
//			projectCloneIDs = dbLayer.getContextBasedProjectCloneIDs(contextProjectsMappings);
//			previousUserFunctionID = userFunctionID;
//		}
		//make a Set of Integers from the userFunctionCloneID
		Set<Integer>  v_user = new HashSet<Integer>();
		//v_user.add(userCloneID);
		//for a bigger context add more cloneIDs from the host project of user function
		//HashSet<Integer>  contextCloneIDs = new HashSet<Integer>();
		//contextCloneIDs = dbLayer.getContextCloneIDs(userFunctionID,pid);
		//Iterator<Integer> itr = contextCloneIDs.iterator(); // traversing over HashSet

		for (int i:contextFeatureIDs)
		{
			//if(v_user.size() < contextCloneIDs.size()/2)//i only want to add half of the features of context
			v_user.add(i);
		}
		//System.out.println("The context features of size " + v_user.size() + " are as follows: ");
		//for(int FID: v_user)
		//	System.out.print(FID+",");
		//System.out.println();
		//iterate over hashmap
		for (Map.Entry<Integer, Set<Integer>> entry : projectCloneIDs.entrySet()) {
			int projectID = entry.getKey();
			//make a Set of Integers from the cloneIDs of a project
			Set<Integer>v_cloneIDs = entry.getValue();
			//make a Set of float containing similarity scores of each project
			if(v_cloneIDs.size()>0)
			{
				double similarity = RelatedMethods.CustomUtilities.CosineSimilarityCalculator.computeCosineSimilarity(v_user, v_cloneIDs);
				if(similarity > 0)
				{
					//System.out.println("Similarity hai! ");
					//get cosine similarity measures and store in the float Map
					projectSimilarities.put(projectID, similarity);
				}
			}
		}
		//get top 20 projects
		HashMap<Integer, Double> sortedProjectSimilarities = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(projectSimilarities);
		//return ArrayList<Integer> of top 20 projects
		ArrayList<Integer> topProjectIDs = new ArrayList<Integer> ();
		int topN = 0;
		if(tuned)
			topN=Nprojs;
		else
			topN=Nprojs;
		for(int i: sortedProjectSimilarities.keySet())
		{
			topProjectIDs.add(i);
			if(topProjectIDs.size()==topN)
				break;
		}

		//dbLayer.closeConnector();
		return topProjectIDs;
	}



	public static HashMap<Integer, Double> getTop10SimilarProjectsByContext(
			int userFunctionID,  int userCloneID, ArrayList<Integer> contextFeatureIDs) throws Exception {
		dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();
		ArrayList<Integer> top20Projects= new ArrayList<Integer>();		
		//get connection to 101 repo
		
		//get list of cloneIDs from all projects in a hash map <projectID, cloneIDs>	
		if(previousUserFunctionID == 0)
		{
			previousUserFunctionID = userFunctionID;
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
		}
		else if(userFunctionID != previousUserFunctionID)
		{
			projectCloneIDs = new HashMap<Integer, Set<Integer>>();
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
			previousUserFunctionID = userFunctionID;
		}
		//make a Set of Integers from the userFunctionCloneID
		Set<Integer>  v_user = new HashSet<Integer>();	
		v_user.add(userCloneID);
		
		
		for (int i:contextFeatureIDs) 
		{ 
			v_user.add(i); 
		}
		//System.out.println("The context features of size " + v_user.size() + " are as follows: ");
		//for(int FID: v_user)
		//	System.out.print(FID+",");
		//System.out.println();

		//iterate over hashmap
		for (Map.Entry<Integer, Set<Integer>> entry : projectCloneIDs.entrySet()) {
		    int projectID = entry.getKey();
		    //make a Set of Integers from the cloneIDs of a project
		    Set<Integer>v_cloneIDs = entry.getValue();			
			//make a Set of float containing similarity scores of each project 
		    if(v_cloneIDs.size()>0)
		    {
			    double similarity = RelatedMethods.CustomUtilities.CosineSimilarityCalculator.computeCosineSimilarity(v_user, v_cloneIDs);
				if(similarity > 0)
				{
					//System.out.println("Similarity hai! ");
					//get cosine similarity measures and store in the float Map
					projectSimilarities.put(projectID, similarity);
				}
		    }
		}		
		//get top 20 projects
		HashMap<Integer, Double> sortedProjectSimilarities = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(projectSimilarities);
		
		
		//dbLayer.closeConnector();
		return sortedProjectSimilarities;
	}
	
	
	private static ArrayList<Integer> getSimilarProjectsByThreshold(
			int userFunctionID, int userCloneID, float similarityThreshold) throws Exception {
		
		//get list of cloneIDs from all projects in a hash map <projectID, cloneIDs>	
		if(previousUserFunctionID == 0)
		{
			previousUserFunctionID = userFunctionID;
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
		}
		else if(userFunctionID != previousUserFunctionID)
		{
			projectCloneIDs = new HashMap<Integer, Set<Integer>>();
			projectCloneIDs = dbLayer.getProjectCloneIDs(userFunctionID);
			previousUserFunctionID = userFunctionID;
		}
		//make a Set of Integers from the userFunctionCloneID
		Set<Integer>  v_user = new HashSet<Integer>();	
		v_user.add(userCloneID);
		//iterate over hashmap
		for (Map.Entry<Integer, Set<Integer>> entry : projectCloneIDs.entrySet()) {
		    int projectID = entry.getKey();
		    //make a Set of Integers from the cloneIDs of a project
		    Set<Integer>v_cloneIDs = entry.getValue();			
			//make a Set of float containing similarity scores of each project 
		    if(v_cloneIDs.size()>0)
		    {
			    double similarity = RelatedMethods.CustomUtilities.CosineSimilarityCalculator.computeCosineSimilarity(v_user, v_cloneIDs);
				if(similarity > 0)
				{
					//System.out.println("Similarity hai! ");
					//get cosine similarity measures and store in the float Map
					projectSimilarities.put(projectID, similarity);
				}
		    }
		}		
		//get top 20 projects
		HashMap<Integer, Double> sortedProjectSimilarities = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(projectSimilarities);
		//return ArrayList<Integer> of top 20 projects
		ArrayList<Integer> topProjectIDs = new ArrayList<Integer> ();
		
		for (Map.Entry<Integer, Double> entry : sortedProjectSimilarities.entrySet()) {
			if(entry.getValue()>=similarityThreshold)
				topProjectIDs.add(entry.getKey());
			
		}

		
		//dbLayer.closeConnector();
		return topProjectIDs;
	}
	
	private static Map<Integer, Double> getMCSBasedPredictionScores(
			HashMap<Integer, Set<Integer>> topProjectCloneIDs) throws Exception {

		float sum_of_similarities = findSumOfValues(projectSimilarities);
		HashMap <Integer,Double> predScores= new HashMap <Integer,Double> ();
		Set<Integer> projectIDs = topProjectCloneIDs.keySet();
		//for each cloneID/feature from the top 20 projects,
		for(Map.Entry<Integer, Set<Integer>> entry : topProjectCloneIDs.entrySet())
		{
			int projectID = entry.getKey();
			Set<Integer> cloneIDs = entry.getValue();
			for(int CID: cloneIDs)
			{
				float numerator_simscore = 0f;
				//for each top 20 project,
				for(int PID: projectIDs)
				{			
					//if the feature is in a project then accumulate sim score of project
					if(isFeatureInProject(CID, PID, topProjectCloneIDs))
					{
						numerator_simscore+= projectSimilarities.get(PID);
						
					}
					
				
				}
				if(numerator_simscore != 0)
				{
					//divide accumulated sim scores with summation of all sim scores
					double predScore = (float) numerator_simscore/sum_of_similarities;
					//if it is an MCS member then add 1 to the score
					if(isFeatureMCSMember(CID))
					{
						//boost
						predScore = (predScore + 1)/2;
					}
					else
					{
						//reduce
						predScore = (predScore + 0)/2;
					}
					//put the result in a hash map <cloneID, pred score>
					if(predScore > 0.5)
						predScores.put(CID, predScore);
				}
			}
		}
		//sort the scores map
		HashMap<Integer, Double> sortedPredScores = (HashMap<Integer, Double>) RelatedMethods.CustomUtilities.HashMapSorter.sortHashMapByValuesTest(predScores);
		
		//return sorted scores
		return sortedPredScores;
		
	}

	private static boolean isFeatureMCSMember(int cID) throws Exception {
		boolean result = false;
		//EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		//dbLayer.initializeFoldDatabaseConnector();	
		//get the MCSID of feature if available		
		int MCSID = dbLayer.getMCSID(cID);
		if(MCSID!=-1)
			result = true;
		
		//dbLayer.closeConnector();
		return result;
	}

}
