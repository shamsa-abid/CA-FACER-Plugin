package related_methods.automated_evaluation;

import java.util.ArrayList;

public class RecommendationStrategy {
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
	public static final int C1_MCMILLAN_STRATEGY = 13;
	public static final int C2_C1Ranked_ByMCS_STRATEGY = 14;
	public static final int C3_PR_Ranking_STRATEGY = 15;
	public static final int C4_FACER_PLUS_MCMILLAN_STRATEGY = 16;
	public static final int C5_STRATEGY = 17;
	public static final int C6_FOCUS_STRATEGY = 17;
	public static final int FACER_ESEC_RETRIEVAL_STRATEGY = 18;
	
	public static int _recommendationStrategy;
	public static float _similarityThreshold;
	
	public static float get_similarityThreshold() {
		return _similarityThreshold;
	}

	public void set_similarityThreshold(float t) {
		RecommendationStrategy._similarityThreshold = t;
	}

	public void setRecommendationStrategy(int strategy)
	{
		_recommendationStrategy = strategy;
	}
	
	public int getRecommendationStrategy()
	{
		return _recommendationStrategy;
	}
	
	public String getRecommendationStrategyName()
	{
		int strategy = this.getRecommendationStrategy();
		String result = "";
		switch(strategy) {
		  case FACER_RETRIEVAL_STRATEGY:
			  result = "FACER MCS ONLY";
		    break;
		  case C1_MCMILLAN_STRATEGY:
			  result = "C1 McMillan Basic";
		    break;
		  case C2_C1Ranked_ByMCS_STRATEGY:
			  result = "C2: C1 ranked by MCS";
			  break;
		  default:
		    return null;
		}
		return result;
	}
	
	public static ArrayList<Integer> executeStrategy(int strategy, InputParameter parameter) throws Exception
	{
		ArrayList<Integer> relatedMethodsList = new ArrayList<Integer>();
		switch(strategy) {
		  case FACER_RETRIEVAL_STRATEGY:
			  relatedMethodsList = AutomatedEvaluation.getRelatedMethodIDsUsingFACERRetrievalLeaveOutProject(parameter.methodID, parameter.projectID);
		    break;
		  case C1_MCMILLAN_STRATEGY:
			  relatedMethodsList = McMillanEvaluation.getMcMillanMethodRecommendations(parameter.methodID, parameter.clusterID, parameter.projectID, false, _similarityThreshold);
		    break;
		  case C2_C1Ranked_ByMCS_STRATEGY:
			  relatedMethodsList = McMillanEvaluation.getMcMillanMethodRecommendations(parameter.methodID, parameter.clusterID, parameter.projectID, true, _similarityThreshold);
			  break;
		  default:
		    return null;
		}
		return relatedMethodsList;
		
	}
	
}
