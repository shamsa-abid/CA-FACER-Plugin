package related_methods.automated_evaluation;

import RelatedMethods.DataObjects.Method;
import RelatedMethods.CustomUtilities.HashMapSorter;
import RelatedMethods.CustomUtilities.Constants;
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

/**
 * Indicate that the connection was lost and notify the UI Activity.
 */

public class Evaluation {

	static int topN = 10;
	static String featureDesc = ""; 
	static boolean userfriendly = false;
	//this main is for making jar file in EMSE paper
	/*public static void main(String args[]) throws Exception
	{
		Utilities.Constants.DATABASE = "jdbc:mysql://localhost/"+ args[0] + "?useSSL=false&user=root";
		if(args[1].contentEquals("true"))
		{
			userfriendly = true;
			//args3 is minsup
			//args4 is MID
			//args2 is topn
			topN = Integer.parseInt(args[2]);
			getRecStats(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));//EMSE paper
			//getRecStatsFACERNoContext(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getRecStatsCAFACERMcMillan(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getRecStatsCAFACER(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getRecStatsCAFACERFromSimProj(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getAggregateRecsCAFACER
			//getBasicFACERHalfGTRecs
			//getBasicFACERHalfGTRecs(0,33, 3);
			getBasicFACERHalfGTRecs(0,33, 3);
			getBasicFACERHalfGTRecs(0,80, 3);
			getBasicFACERHalfGTRecs(0,423, 3);
			
			getBasicFACERHalfGTRecs(0,1161, 3);
			getBasicFACERHalfGTRecs(0,2250, 3);
			getBasicFACERHalfGTRecs(0,2616, 3);
			
			getBasicFACERHalfGTRecs(0,2642, 3);
			getBasicFACERHalfGTRecs(0,2971, 3);
			getBasicFACERHalfGTRecs(0,3017, 3);	
			
			getBasicFACERHalfGTRecs(0,14435, 3);
			getBasicFACERHalfGTRecs(0,14490, 3);
			getBasicFACERHalfGTRecs(0,28669, 3);
			
			getBasicFACERHalfGTRecs(0,29298, 3);
			getBasicFACERHalfGTRecs(0,29947, 3);
			getBasicFACERHalfGTRecs(0,31838, 3);
			getBasicFACERHalfGTRecs(0,33549, 3);
			
			
		}else
		{	
			topN = Integer.parseInt(args[2]);
			userfriendly = false;
			evaluateAllMinSup(Utilities.Constants.DATABASE);
		}
		//evaluateIndividually();	
	}*/

	public static void main(String args[]) throws Exception {

		//basic facer 0,0,0
		//facer+ 1,2,1
		//schafers+ 1,2,3
		//cafacer 1,2,4
		int context = 3;
		int groundtruth = 3;
		int strategy = 4;
		int Nrecs = 10;
		double gamma = 0.7;//for schafers
		int Nprojs = 10; // for schafer and cafacer
		userfriendly = true;
		ArrayList<Metrics> metrics = new ArrayList<Metrics>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();



//		metrics.add(getRecommendations(33, 3, context, groundtruth, strategy, Nrecs, gamma,Nprojs, dbLayer));
//		metrics.add(getRecommendations(80, 3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(423,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(1066,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(1161,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//
//		metrics.add(getRecommendations(2250,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(2616,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(2642,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(2971,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(3017,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//
//		metrics.add(getRecommendations(14435,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(14490,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(15214,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(22968,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(24068,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//
//
//		metrics.add(getRecommendations(28669,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(29298,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(29947,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(31838,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
//		metrics.add(getRecommendations(33549,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));


		//get all projects having files with at least 3 features
		ArrayList<Integer> evalProjectsList = new ArrayList<>();
		evalProjectsList = dbLayer.getEvalProjects();
//
//
	//	for(int pid: evalProjectsList) {
			System.out.println("Evaluating project with Nprojs 10 and Nrecs 10: ");
	//		System.out.println(pid);
			metrics.add(getRecommendationsForIntellij(63, 3, 0, 0, 4, 10, 0.7, 10, dbLayer));
			//metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 30, 0, 20, dbLayer));
//
//
	//	}
		printAverageMetrics(metrics);

		System.out.println("===========================================");


//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 10 and Nrecs 20: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 20, 0, 10, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");
//
//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 10 and Nrecs 30: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 30, 0, 10, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");
//
//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 10 and Nrecs 40: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 40, 0, 10, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");
//
//
//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 20 and Nrecs 10: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 10, 0, 20, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");
//
//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 20 and Nrecs 20: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 20, 0, 20, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");
//
//
//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 20 and Nrecs 30: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 30, 0, 20, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");
//
//
//		metrics = new ArrayList<Metrics>();
//		System.out.println("Evaluating project with Nprojs 20 and Nrecs 40: ");
//		for(int pid: evalProjectsList) {
//			//System.out.println(pid);
//			metrics.add(getRecommendationsForIntellij(pid, 3, 0, 0, 3, 40, 0, 20, dbLayer));
//		}
//		printAverageMetrics(metrics);
//		System.out.println("===========================================");

	}

	private static void printAverageMetrics(ArrayList<Metrics> metrics) {
		float cumulativePrecision = 0;
		float cumulativeRecall = 0;
		float cumulativeSuccessRate = 0;
		float sum = 0;
		int hits = 0;
		int correcthits = 0;
		int rankcount = 0;
		int totalqueries = 0;
		for(Metrics m: metrics)
		{
			totalqueries += 1;
			if(m==null)//no recs for input method
			{

			}
			else
			{
				hits += 1;
				cumulativePrecision += m.precision;
				cumulativeRecall += m.recall;
				if(m.precision>0)
				{
					correcthits+=1;
					rankcount+=1;
					cumulativeSuccessRate += m.success_rate;
					sum = (float) (sum + (1f/m.rank));
				}
			}
		}
		System.out.println();
		System.out.println("Avg. Precision, Avg. Recall, Success Rate, MRR");
		System.out.print(cumulativePrecision/hits);
		System.out.print("," + cumulativeRecall/hits);
		System.out.print("," + cumulativeSuccessRate/ metrics.size());
		System.out.println("," + sum/rankcount);
		System.out.println("Total Query count: " + totalqueries);
		System.out.println("Query with recs count: " + hits);
		System.out.println("Query with at least one correct recs count: " + correcthits);
	}

	public static void oldmain(String args[]) throws Exception
	{
		//Constants.DATABASE = "jdbc:mysql://localhost/"+ args[0] + "?useSSL=false&user=root";
		//if(args[1].contentEquals("true"))
		if(true)
		{
			userfriendly = true;
			//args3 is minsup
			//args4 is MID
			//args2 is topn
			//topN = Integer.parseInt(args[2]);
			topN = 20;
			//getRecStats(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));//EMSE paper
			//getRecStatsFACERNoContext(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getRecStatsCAFACERMcMillan(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getRecStatsCAFACER(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getRecStatsCAFACERFromSimProj(0, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
			//getAggregateRecsCAFACER
			//getBasicFACERHalfGTRecs
			//getBasicFACERHalfGTRecs(0,33, 3);
			
			//int context-> 0=no, 1=same file features, 2=first half features, 3= second half features 
			//int groundtruth-> 0=all features, 1=all except same file features, 2= first half features, 3=second half features
			//strategy-> 0=basic facer, 1=facer with post filtering, 2= schafers, 3= schafers with postfiltering, 4= proposed CAFACER
			ArrayList<Metrics> metrics = new ArrayList<Metrics>(); 
			//getRecommendations(int MID, int minSup, int context, int groundtruth, int strategy, int Nrecs, double gamma)
			//basic facer 0,0,0
			//facer+ 1,2,1
			//schafers+ 1,2,3
			//cafacer 1,2,4 
			int context = 1;
			int groundtruth = 1;
			int strategy = 3;			
			int Nrecs = 10;
			double gamma = 0.9;//for schafers
			int Nprojs = 5; // for schafer and cafacer
			System.out.println("Experiment on faceremserepopoint5 beta=3");
			
			/*calculateAveragePrecisionOver20Queries(1,1,4,5,0.3,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.9,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.3,20);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.5,20);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.7,20);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.9,20);
			
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.3,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.9,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.3,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.5,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.7,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.9,20);
			
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.3,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.9,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.3,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.5,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.7,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.9,20);*/
			
			/*calculateAveragePrecisionOver20Queries(2,2,4,5,0.5,10);
			calculateAveragePrecisionOver20Queries(2,2,4,10,0.5,10);
			calculateAveragePrecisionOver20Queries(2,2,4,15,0.5,10);
			calculateAveragePrecisionOver20Queries(2,2,4,5,0.7,10);
			calculateAveragePrecisionOver20Queries(2,2,4,10,0.7,10);
			calculateAveragePrecisionOver20Queries(2,2,4,15,0.7,10);*/

//			calculateAveragePrecisionOver20Queries(1,1,6,5,0.5,10);			
//			calculateAveragePrecisionOver20Queries(1,1,6,10,0.5,10);
//			calculateAveragePrecisionOver20Queries(1,1,6,15,0.5,10);
//			
//			calculateAveragePrecisionOver20Queries(2,2,6,5,0.5,10);
//			calculateAveragePrecisionOver20Queries(2,2,6,10,0.5,10);
//			calculateAveragePrecisionOver20Queries(2,2,6,15,0.5,10);
		
			//calculateAveragePrecisionOver20Queries(1,2,6,5,0.5,10);			
			//calculateAveragePrecisionOver20Queries(1,2,6,10,0.5,10);
			//calculateAveragePrecisionOver20Queries(1,2,6,15,0.5,10);
//			
			//calculating for c3 and c4 for facer+
			/*calculateAveragePrecisionOver20Queries(3,3,1,10,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,1,10,0.5,10);
			
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.5,10);
			
			calculateAveragePrecisionOver20Queries(3,3,6,10,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,6,10,0.5,10);*/
			
			//calculateAveragePrecisionOver20Queries(0,0,2,10,0.5,10);
			//basic facer
		/*	calculateAveragePrecisionOver20Queries(0,0,0,5,0,0);
			calculateAveragePrecisionOver20Queries(0,0,0,10,0,0);
			calculateAveragePrecisionOver20Queries(0,0,0,15,0,0);
			calculateAveragePrecisionOver20Queries(0,1,0,5,0,0);
			calculateAveragePrecisionOver20Queries(0,1,0,10,0,0);
			calculateAveragePrecisionOver20Queries(0,1,0,15,0,0);
			calculateAveragePrecisionOver20Queries(0,3,0,5,0,0);
			calculateAveragePrecisionOver20Queries(0,3,0,10,0,0);
			calculateAveragePrecisionOver20Queries(0,3,0,15,0,0);
			calculateAveragePrecisionOver20Queries(0,4,0,5,0,0);
			calculateAveragePrecisionOver20Queries(0,4,0,10,0,0);
			calculateAveragePrecisionOver20Queries(0,4,0,15,0,0);
			*/
			//facer+
//			calculateAveragePrecisionOver20Queries(1,1,1,5,0,0);
//			calculateAveragePrecisionOver20Queries(1,1,1,10,0,0);
//			calculateAveragePrecisionOver20Queries(1,1,1,15,0,0);
//			calculateAveragePrecisionOver20Queries(3,3,1,5,0,0);
//			calculateAveragePrecisionOver20Queries(3,3,1,10,0,0);
//			calculateAveragePrecisionOver20Queries(3,3,1,15,0,0);
//			calculateAveragePrecisionOver20Queries(4,4,1,5,0,0);
//			calculateAveragePrecisionOver20Queries(4,4,1,10,0,0);
//			calculateAveragePrecisionOver20Queries(4,4,1,15,0,0);
			/*
			//schafers
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.3,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.3,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.3,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.9,10);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.9,10);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.9,10);
			
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.3,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.3,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.3,20);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.5,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.5,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.5,20);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.7,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.7,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.7,20);
			calculateAveragePrecisionOver20Queries(1,1,4,5,0.9,20);
			calculateAveragePrecisionOver20Queries(1,1,4,10,0.9,20);
			calculateAveragePrecisionOver20Queries(1,1,4,15,0.9,20);
			
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.3,10);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.3,10);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.3,10);
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.5,10);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.5,10);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.5,10);
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.7,10);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.7,10);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.7,10);
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.9,10);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.9,10);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.9,10);
			
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.3,20);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.3,20);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.3,20);
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.5,20);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.5,20);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.5,20);
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.7,20);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.7,20);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.7,20);
			calculateAveragePrecisionOver20Queries(3,3,4,5,0.9,20);
			calculateAveragePrecisionOver20Queries(3,3,4,10,0.9,20);
			calculateAveragePrecisionOver20Queries(3,3,4,15,0.9,20);
			
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.3,10);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.3,10);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.3,10);
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.7,10);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.7,10);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.7,10);
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.9,10);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.9,10);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.9,10);
			
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.3,20);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.3,20);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.3,20);
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.5,20);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.5,20);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.5,20);
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.7,20);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.7,20);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.7,20);
			calculateAveragePrecisionOver20Queries(4,4,4,5,0.9,20);
			calculateAveragePrecisionOver20Queries(4,4,4,10,0.9,20);
			calculateAveragePrecisionOver20Queries(4,4,4,15,0.9,20);
			
			//cafacer
			calculateAveragePrecisionOver20Queries(1,1,6,5,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,6,10,0.5,10);
			calculateAveragePrecisionOver20Queries(1,1,6,15,0.5,10);
			calculateAveragePrecisionOver20Queries(3,3,6,5,0.5,10);
			calculateAveragePrecisionOver20Queries(3,3,6,10,0.5,10);
			calculateAveragePrecisionOver20Queries(3,3,6,15,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,6,5,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,6,10,0.5,10);
			calculateAveragePrecisionOver20Queries(4,4,6,15,0.5,10);
			//rq3
			calculateAveragePrecisionOver20Queries(1,3,6,10,0.5,10);
			calculateAveragePrecisionOver20Queries(1,4,6,10,0.5,10);
			*/
			
			//cafacer
			calculateAveragePrecisionOver20Queries(1,1,6,5,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,6,10,0.7,10);
			calculateAveragePrecisionOver20Queries(1,1,6,15,0.7,10);
			/*
			calculateAveragePrecisionOver20Queries(3,3,6,5,0.7,10);
			calculateAveragePrecisionOver20Queries(3,3,6,10,0.7,10);
			calculateAveragePrecisionOver20Queries(3,3,6,15,0.7,10);
			calculateAveragePrecisionOver20Queries(4,4,6,5,0.7,10);
			calculateAveragePrecisionOver20Queries(4,4,6,10,0.7,10);
			calculateAveragePrecisionOver20Queries(4,4,6,15,0.7,10);
			//rq3
			calculateAveragePrecisionOver20Queries(1,3,6,10,0.7,10);
			calculateAveragePrecisionOver20Queries(1,4,6,10,0.7,10);*/
			
			//calculateAveragePrecisionOver20Queries(0,0,6,5,0.7,10);
			//calculateAveragePrecisionOver20Queries(0,0,4,5,0.7,10);
			
		}else
		{	
			topN = Integer.parseInt(args[2]);
			userfriendly = false;
			evaluateAllMinSup(Constants.DATABASE);
		}
		//evaluateIndividually();	
	}

	private static void calculateAveragePrecisionOver20Queries(int context, int groundtruth,
			int strategy, int Nrecs, double gamma, int Nprojs) throws Exception {
		ArrayList<Metrics> metrics = new ArrayList<Metrics>();
		System.out.print(context);
		System.out.print("_"+ groundtruth);
		System.out.print("_"+ strategy);
		System.out.print("_"+ Nrecs);
		System.out.print("_"+ gamma);
		System.out.println("_"+ Nprojs);
		
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		
		metrics.add(getRecommendations(33, 3, context, groundtruth, strategy, Nrecs, gamma,Nprojs, dbLayer));
		metrics.add(getRecommendations(80, 3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(423,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(1066,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(1161,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		
		metrics.add(getRecommendations(2250,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(2616,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(2642,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(2971,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));			
		metrics.add(getRecommendations(3017,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		
		metrics.add(getRecommendations(14435,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(14490,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(15214,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(22968,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(24068,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		
		
		metrics.add(getRecommendations(28669,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));			
		metrics.add(getRecommendations(29298,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(29947,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(31838,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		metrics.add(getRecommendations(33549,3, context, groundtruth, strategy, Nrecs, gamma, Nprojs, dbLayer));
		/*getBasicFACERHalfGTRecs(0,33, 3);
		getBasicFACERHalfGTRecs(0,80, 3);
		getBasicFACERHalfGTRecs(0,423, 3);
		
		getBasicFACERHalfGTRecs(0,1161, 3);
		getBasicFACERHalfGTRecs(0,2250, 3);
		getBasicFACERHalfGTRecs(0,2616, 3);
		
		getBasicFACERHalfGTRecs(0,2642, 3);
		getBasicFACERHalfGTRecs(0,2971, 3);
		getBasicFACERHalfGTRecs(0,3017, 3);	
		
		getBasicFACERHalfGTRecs(0,14435, 3);
		getBasicFACERHalfGTRecs(0,14490, 3);
		getBasicFACERHalfGTRecs(0,28669, 3);
		
		getBasicFACERHalfGTRecs(0,29298, 3);
		getBasicFACERHalfGTRecs(0,29947, 3);
		getBasicFACERHalfGTRecs(0,31838, 3);
		getBasicFACERHalfGTRecs(0,33549, 3);*/
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
		System.out.println();
		System.out.println("Avg. Precision, Success Rate, MRR");
		System.out.print(cumulativePrecision/hits);
		System.out.print("," + cumulativeSuccessRate/metrics.size());
		System.out.println("," + sum/rankcount);
	}

	private static void evaluateIndividually() throws Exception {
		
		ArrayList<Metrics> metrics = new ArrayList<Metrics>(); 
		//getRecStats(pid,mid,minsup)
		int minSup = 3;	
		
				//1
				//checkPermissionsSettingsAndShowAlert
				//yes
				//featureDesc="Check and add permissions for location access";
				metrics.add(getRecStats(0, 31838, minSup));
				
				//2
				//creates a new memory cache to store the weather icons
				//yes
				//featureDesc = "creates a new memory cache to store the weather icons";
				metrics.add(getRecStats(0, 33549, minSup));
				
				//3
				//move file
				//yes
				//featureDesc = "move file";
				metrics.add(getRecStats(0, 2642, minSup));
				
				//4
				//save forecast
				//yes
				//featureDesc = "save forecast in database";
				metrics.add(getRecStats(0, 28669, minSup));
				
				//5
				//isNetworkAvailable
				//yes
				//featureDesc = "check if network connection available";
				metrics.add(getRecStats(0, 29947, minSup));
				
				//6
				//set path to prepare media player
				//yes
				//featureDesc = "set data source for media player";
				metrics.add(getRecStats(0, 14435, minSup));

				//7
				//get Blueooth paired device name and address
				//yes
				//featureDesc = "receive paired device name and address";
				metrics.add(getRecStats(0, 33, minSup));
			
				//8
				//sendHttpRequest to get weather
				//yes
				//featureDesc = "send Http Request to get weather";
				metrics.add(getRecStats(0, 29298, minSup));
				
				//9
				//create new folder
				//yes
				//featureDesc = "create new folder";
				metrics.add(getRecStats(0, 2250, minSup));

				//10
				//browse to file /directory
				//yes
				//featureDesc = "browse to file /directory";
				metrics.add(getRecStats(0, 2616, minSup));
				
				//11
//				//get paired Bluetooth devices
				//yes
				//featureDesc = "update list of paired Bluetooth devices";
				metrics.add(getRecStats(0, 80, minSup));			
//				
				//12
				//connect to a Bluetooth device
				//yes
				//featureDesc = "connect to a Bluetooth device";
				metrics.add(getRecStats(0, 1161, minSup));		
				
				//13
//				//scanning for other Bluetooth devices
				//yes
				//featureDesc = "do discovery of Bluetooth devices";
				metrics.add(getRecStats(0, 423, minSup));		
		
				//14
				//handles key presses to start stop pause media
				//featureDesc = "receive key press to start stop pause media";
				metrics.add(getRecStats(0, 14490, minSup));		
				
				//15
				//write to file
				//featureDesc = "put file to cache";
				metrics.add(getRecStats(0, 2971, minSup));	
				
				//16
				//draw bitmap
				featureDesc = "draw bitmap";
				metrics.add(getRecStats(0, 3017, minSup));	
		
		
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

	private static void evaluateAllMinSup(String DB) throws Exception {
		
		//evaluationLOOCVallMethods();
		//getRecStats(pid,mid,minsup)
		//int minSup = 5;
		
		System.out.println("Evaluating " + DB );
		System.out.println("Evaluating Top: " + topN );
		System.out.println("Min Support, Avg. Precision, Success Rate, MRR");
		//System.out.println("Evaluating Top: " + topN );
		for(int minSup = 0; minSup <= 15; minSup += 5)
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
		
		System.out.print((minSup==0?3:minSup)+"," + cumulativePrecision/hits);
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
		System.out.println("Evaluating " + Constants.DATABASE + " with minSup ");
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
		
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(MID, CID, pid, false, 0);
		
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
			if(userfriendly)
			System.out.println("Precision: "+precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	private static Metrics getRecStatsFACERNoContext(int pid, int MID, int minSup) throws Exception
	{
		userfriendly = false;
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

		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);
		groundTruthFeatureIDs = new ArrayList<>();
		int clusterID = dbLayer.getCID(MID);
		if(userfriendly)
		{
		System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
		for(int FID: featuresList)
			System.out.print(FID+",");
		System.out.println();
		}
		featuresList.remove(clusterID);
		//divide the cluster IDs obtained into half and use the second half as ground truth while the first half will be for the
		//context in other context aware approaches		
		for(int FID: featuresList){
			groundTruthFeatureIDs.add(FID);
		}
				
		//groundTruthFeatureIDs.subList(0, (groundTruthFeatureIDs.size()/2)).clear();	
		
		
		//groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		if(userfriendly)
		{
		System.out.println("The Ground Truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
		for(int FID: groundTruthFeatureIDs)
			System.out.print(FID+",");
		System.out.println();
		}
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
			System.out.println("Your Method's Feature Description: " + featureDesc);
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
			//if(userfriendly)
			//System.out.println("Precision: ");
			System.out.println(precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	private static Metrics getRecStatsCAFACERMcMillan(int pid, int MID, int minSup) throws Exception
	{
		userfriendly = false;
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
		ArrayList<Integer> contextFeatureIDs = new ArrayList<>();
		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);
		groundTruthFeatureIDs = new ArrayList<>();
		int clusterID = dbLayer.getCID(MID);
		//get file against MID and get cluster IDs in that file to make part of context
		ArrayList<Integer> sameFileFeatureIDs = dbLayer.getFileCIDs(MID);
		if(userfriendly)
		{
		System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
		for(int FID: featuresList)
			System.out.print(FID + ",");
		System.out.println();
		}
		featuresList.remove(clusterID);
		//divide the cluster IDs obtained into half and use the second half as ground truth while the first half will be for the
		//context in other context aware approaches		
		for(int FID: featuresList){
			contextFeatureIDs.add(FID);
			groundTruthFeatureIDs.add(FID);
		}
		int startIndex = contextFeatureIDs.size()/2;
		if(startIndex < 1)
			startIndex = 0;
//		int startIndex = 3;
		contextFeatureIDs.subList(startIndex, contextFeatureIDs.size()).clear();	
		//contextFeatureIDs.addAll(sameFileFeatureIDs);
		for(int sfid: sameFileFeatureIDs)
		{
			if(!contextFeatureIDs.contains(sfid))
			{
				contextFeatureIDs.add(sfid);
			}
		}
		//groundTruthFeatureIDs.subList(0, (groundTruthFeatureIDs.size()/2)).clear();	
		
		//groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		if(userfriendly)
		{
			System.out.println("The Ground Truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
		
		for(int FID: groundTruthFeatureIDs)
			System.out.print(FID+",");
		System.out.println();
		}

		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		
		//relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
		
		int CID = dbLayer.getCID(MID);
		relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(true,10, 0.5f, MID, CID, contextFeatureIDs, pid, false, 0);
		
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
			//if(userfriendly)
			System.out.println("Precision: "+precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	private static Metrics getRecStatsCAFACER(int pid, int MID, int minSup) throws Exception
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
		ArrayList<Integer> contextFeatureIDs = dbLayer.getClusterIDs(pid);
		//divide the cluster IDs obtained into half and use the second half as ground truth while the first half will be for the
		//context in other context aware approaches
		for(int i=0;i<=groundTruthFeatureIDs.size()/2;i++)
			groundTruthFeatureIDs.remove(i);
		//get context features
		for(int i=contextFeatureIDs.size()/2+1;i<contextFeatureIDs.size();i++)
			contextFeatureIDs.remove(i);
		
		
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		
		//get related features against each method of context and sort them by the MCS support from which they originate
		relatedFeatureIDs = getRelatedFeaturesFACER(MID,contextFeatureIDs, minSup, dbLayer);
		
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(MID, CID, pid, false, 0);
		
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
			if(userfriendly)
			System.out.println("Precision: "+precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	private static Metrics getRecStatsCAFACERFromSimProj(int pid, int MID, int minSup) throws Exception
	{
		userfriendly = false;
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
		ArrayList<Integer> contextFeatureIDs = new ArrayList<>();
		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);
		groundTruthFeatureIDs = new ArrayList<>();
		int clusterID = dbLayer.getCID(MID);
		//get file against MID and get cluster IDs in that file to make part of context
		ArrayList<Integer> sameFileFeatureIDs = dbLayer.getFileCIDs(MID);
		if(userfriendly)
		{
			System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
			for(int FID: featuresList)
				System.out.print(FID+",");
			System.out.println();
		}
		featuresList.remove(clusterID);
		//divide the cluster IDs obtained into half and use the second half as ground truth while the first half will be for the
		//context in other context aware approaches		
		for(int FID: featuresList){
			contextFeatureIDs.add(FID);
			groundTruthFeatureIDs.add(FID);
		}
		int startIndex = contextFeatureIDs.size()/4;
		if(startIndex < 1)
			startIndex = 0;
		//int startIndex = 3;
		contextFeatureIDs.subList(startIndex, contextFeatureIDs.size()).clear();	
		for(int sfid: sameFileFeatureIDs)
		{
			if(!contextFeatureIDs.contains(sfid))
			{
				contextFeatureIDs.add(sfid);
			}
		}
		//contextFeatureIDs.subList(contextFeatureIDs.size()/2, contextFeatureIDs.size()).clear();		
		//groundTruthFeatureIDs.subList(0, (groundTruthFeatureIDs.size()/2)).clear();		
		//groundTruthFeatureIDs.removeAll(contextFeatureIDs);
		
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		//groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		if(userfriendly)
		{
		System.out.println("The Ground truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
		for(int FID: groundTruthFeatureIDs)
			System.out.print(FID+",");
		System.out.println();
		}
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		
		//get related features against each method of context and sort them by the MCS support from which they originate
		//relatedFeatureIDs = getRelatedFeaturesFACERFromSimProj(MID,contextFeatureIDs, minSup, dbLayer);
		relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
		//below line adds the additional step of post retrieval re-ordering		
		relatedFeatureIDs = postRetrievalContextualReordering(relatedFeatureIDs, MID, clusterID, contextFeatureIDs, minSup, 10, dbLayer);
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(MID, CID, pid, false, 0);
		
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
			//if(userfriendly)
			System.out.println("Precision: "+precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	private static Metrics getAggregateRecsCAFACER(int pid, int MID, int minSup) throws Exception
	{
		userfriendly = false;
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
		ArrayList<Integer> contextFeatureIDs = new ArrayList<>();
		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);
		groundTruthFeatureIDs = new ArrayList<>();
		int clusterID = dbLayer.getCID(MID);
		//get file against MID and get cluster IDs in that file to make part of context
		ArrayList<Integer> sameFileFeatureIDs = dbLayer.getFileCIDs(MID);
		if(userfriendly)
		{
			System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
			for(int FID: featuresList)
				System.out.print(FID+",");
			System.out.println();
		}
		featuresList.remove(clusterID);
		//divide the cluster IDs obtained into half and use the second half as ground truth while the first half will be for the
		//context in other context aware approaches		
		for(int FID: featuresList){
			
			//contextFeatureIDs.add(FID);
			//groundTruthFeatureIDs.add(FID);
			
		}
//		int startIndex = contextFeatureIDs.size()/4;
//		if(startIndex < 1)
//			startIndex = 0;
//		//int startIndex = 3;
//		contextFeatureIDs.subList(startIndex, contextFeatureIDs.size()).clear();	
		for(int sfid: sameFileFeatureIDs)
		{
			if(!contextFeatureIDs.contains(sfid))
			{
				contextFeatureIDs.add(sfid);
			}
		}
		int halfsize = featuresList.size()/2;
		featuresList.removeAll(contextFeatureIDs);
		for(int FID: featuresList){
				
			groundTruthFeatureIDs.add(FID);
			if(groundTruthFeatureIDs.size()==halfsize)
				break;
		}
		//for half context and half GT
//		featuresList.removeAll(groundTruthFeatureIDs);
//		if(featuresList.size()>0)
//		{
//			for(int FID: featuresList)
//			{
//				if(!contextFeatureIDs.contains(FID))
//				{
//					contextFeatureIDs.add(FID);
//				}
//			}
//			
//		}
		//ends here for half context and half GT
		
		//contextFeatureIDs.subList(contextFeatureIDs.size()/2, contextFeatureIDs.size()).clear();		
		//groundTruthFeatureIDs.subList(0, (groundTruthFeatureIDs.size()/2)).clear();		
		groundTruthFeatureIDs.removeAll(contextFeatureIDs);
		
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		//groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		if(true)
		{
		System.out.println("The Ground truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
		for(int FID: groundTruthFeatureIDs)
			System.out.print(FID+",");
		System.out.println();
		}
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		
		//get related features against each method of context and sort them by the MCS support from which they originate
		//relatedFeatureIDs = getRelatedFeaturesFACERFromSimProj(MID,contextFeatureIDs, minSup, dbLayer);
		relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
		//below line adds the additional step of post retrieval re-ordering		
		relatedFeatureIDs = postRetrievalContextualReordering(relatedFeatureIDs, MID, clusterID, contextFeatureIDs, minSup, 10, dbLayer);
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(MID, CID, pid, false, 0);
		
		//System.out.print("No. of recommendations:"+relatedFeatureIDs.size()+",");
		
		ArrayList<Integer> relatedFeatureIDs2 = new ArrayList<>();
		int CID = dbLayer.getCID(MID);
		relatedFeatureIDs2 = McMillanEvaluation.getMcMillanFeatureRecommendations(true,10,0.5f, MID, CID, contextFeatureIDs, pid, false, 0);
		
		//ArrayList<Integer> relatedFeatureIDs3 = new ArrayList<>();
		//relatedFeatureIDs3 = McMillanEvaluation.getMcMillanFeatureRecommendations(false, MID, CID, contextFeatureIDs, pid, false, 0);
		
		//approach with only two strategies intersection
		ArrayList<Integer> finalset = new ArrayList<>();		
		for(int i: relatedFeatureIDs)
		{
			finalset.add(i);
		}
		
		finalset.retainAll(relatedFeatureIDs2);
		System.out.println("Intersection of features: " + finalset.toString());
		finalset.removeAll(contextFeatureIDs);
		if(finalset.size()==0)
		{
			
			//remove features in GT
			relatedFeatureIDs.removeAll(contextFeatureIDs);
			if(relatedFeatureIDs.size()==0)
			{
				
				relatedFeatureIDs2.removeAll(contextFeatureIDs);
				relatedFeatureIDs = relatedFeatureIDs2;
			}
			
		}
		else
		{
			relatedFeatureIDs = finalset;
		}
		
		System.out.println("Recommendation set size is: " + relatedFeatureIDs.size());
		//ends here
		
//		ArrayList<Integer> finalset = new ArrayList<>();		
//	
//		ArrayList<Integer> common3 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			common3.add(i);
//		}
//		
//		common3.retainAll(relatedFeatureIDs2);
//		common3.retainAll(relatedFeatureIDs3);
		
//		ArrayList<Integer> common12 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			common12.add(i);
//		}		
//		common12.retainAll(relatedFeatureIDs2);
//		
//		ArrayList<Integer> common23 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs2)
//		{
//			common23.add(i);
//		}		
//		common23.retainAll(relatedFeatureIDs3);
//		
//		ArrayList<Integer> common13 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			common13.add(i);
//		}		
//		common13.retainAll(relatedFeatureIDs3);
		
//		Set<Integer> finalrecs = new HashSet<>();
//		finalrecs.addAll(common3);
//		finalrecs.addAll(common12);
//		finalrecs.addAll(common23);
//		finalrecs.addAll(common13);
		
//		for(int i: finalrecs)
//		{
//			finalset.add(i);
//		}
//		
//		
//		System.out.println("Intersection of features: " + finalset.toString());
//		relatedFeatureIDs = finalset;
		
		
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
			//if(userfriendly)
			System.out.println("Precision: "+precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	//int context-> 0=no, 1=same file features, 2=first half features, 3= second half features 
	//int groundtruth-> 0=all features, 1=all except same file features, 2= first half features, 3=second half features
	//strategy-> 0=basic facer, 1=facer with post filtering, 2= schafers, 3= schafers with postfiltering, 4= proposed CAFACER
	//int Nrecs -> the top N recommendations from a strategy
	//int gamma-> the prediction score threshold used in schafers 
	private static Metrics getRecommendations(int MID, int minSup, int context, int groundtruth, int strategy, int Nrecs, double gamma, int Nprojs, EvaluationDAL dbLayer) throws Exception
	{		
		userfriendly = false;
		Metrics metrics = new Metrics();
		//System.out.println("Recs for method: "+ MID);
		
		int pid = dbLayer.getProjectID(MID);
		if(userfriendly)
		{
		String projectCategory = dbLayer.getProjectName(pid);
		System.out.println("Evaluation ID:" + MID);
		System.out.println("Application Category:"+ projectCategory.substring(0,projectCategory.indexOf("_")));
		}
		
		ArrayList<Integer> groundTruthFeatureIDs = dbLayer.getClusterIDs(pid);
		ArrayList<Integer> contextFeatureIDs = new ArrayList<>();
		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);
		groundTruthFeatureIDs = new ArrayList<>();
		int clusterID = dbLayer.getCID(MID);
		//get file against MID and get cluster IDs in that file to make part of context
		ArrayList<Integer> sameFileFeatureIDs = dbLayer.getFileCIDs(MID);
		if(userfriendly)
		{
			System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
			for(int FID: featuresList)
				System.out.print(FID+",");
			System.out.println();
		}
		featuresList.remove(clusterID);		
			
		
		if(groundtruth == 0)
		{
			for(int FID: featuresList){
				
				groundTruthFeatureIDs.add(FID);
				
			}
		}
		
		if(groundtruth == 2)
		{
			int halfsize = featuresList.size()/2;
			featuresList.removeAll(sameFileFeatureIDs);
			for(int FID: featuresList){
					
				groundTruthFeatureIDs.add(FID);
				if(groundTruthFeatureIDs.size()==halfsize)
					break;
			}
		}
		
		else if(groundtruth == 1)
		{			
			featuresList.removeAll(sameFileFeatureIDs);
			for(int FID: featuresList){
				
				groundTruthFeatureIDs.add(FID);
				
			}
			//now remove same file features
			//groundTruthFeatureIDs.removeAll(sameFileFeatureIDs);
			
		}
		else if(groundtruth == 3)
		{			
			int halfsize = featuresList.size()/2;			
			for(int FID: featuresList){
					
				groundTruthFeatureIDs.add(FID);
				if(groundTruthFeatureIDs.size()==halfsize)
					break;
			}
			featuresList.removeAll(groundTruthFeatureIDs);
			if(featuresList.size()>0)
			{
				for(int FID: featuresList)
				{
					if(!contextFeatureIDs.contains(FID))
					{
						contextFeatureIDs.add(FID);
					}
				}
				
			}
		}
		else if(groundtruth == 4)
		{			
			int halfsize = featuresList.size()/2;			
			for(int FID: featuresList){
					
				contextFeatureIDs.add(FID);
				if(contextFeatureIDs.size()==halfsize)
					break;
			}
			featuresList.removeAll(contextFeatureIDs);
			if(featuresList.size()>0)
			{
				for(int FID: featuresList)
				{
					if(!groundTruthFeatureIDs.contains(FID))
					{
						groundTruthFeatureIDs.add(FID);
					}
				}
				
			}
			
		}
		if(context==1||context==2)
		{
			for(int sfid: sameFileFeatureIDs)
			{
				if(!contextFeatureIDs.contains(sfid))
				{
					contextFeatureIDs.add(sfid);
				}
			}
		}
		//for half context we assume that groundtruthfeatureIDs have half features from groundtruth=2 branch execution
		if(context==2)
		{
			featuresList.removeAll(groundTruthFeatureIDs);
			if(featuresList.size()>0)
			{
				for(int FID: featuresList)
				{
					if(!contextFeatureIDs.contains(FID))
					{
						contextFeatureIDs.add(FID);
					}
				}
				
			}
		}
		else if(context==0)
		{
			contextFeatureIDs.add(clusterID);
		}
		//ends here for half context and half GT
		
		//contextFeatureIDs.subList(contextFeatureIDs.size()/2, contextFeatureIDs.size()).clear();		
		//groundTruthFeatureIDs.subList(0, (groundTruthFeatureIDs.size()/2)).clear();	
		//groundTruthFeatureIDs.removeAll(contextFeatureIDs);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		//groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		if(userfriendly)
		{
		System.out.println("The Ground truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
		for(int FID: groundTruthFeatureIDs)
			System.out.print(FID+",");
		System.out.println();
		}
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		if(strategy == 0)
			relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
		else if(strategy == 1)
		{
			relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
			relatedFeatureIDs.removeAll(contextFeatureIDs);
			
		}
		else if(strategy == 2)
		{
			int CID = dbLayer.getCID(MID);
			relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(false, Nprojs, gamma, MID, CID, contextFeatureIDs, pid, false, 0);
			
		}
		else if(strategy == 3)
		{
			int CID = dbLayer.getCID(MID);
			relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(false,Nprojs,gamma, MID, CID, contextFeatureIDs, pid, false, 0);
			relatedFeatureIDs.removeAll(contextFeatureIDs);
			
		}
		else if(strategy == 4)
		{
			int CID = dbLayer.getCID(MID);
			relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(true,Nprojs,gamma, MID, CID, contextFeatureIDs, pid, false, 0);
			relatedFeatureIDs.removeAll(contextFeatureIDs);
			
		}
		else if(strategy == 5)
		{
			relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
			//i was only taking top ten recs from here
			relatedFeatureIDs = postRetrievalContextualReordering(relatedFeatureIDs, MID, clusterID, contextFeatureIDs, minSup, Nprojs, dbLayer);

			//contextual post filtering step
			relatedFeatureIDs.removeAll(contextFeatureIDs);
		}
		else if(strategy == 6)
		{
			relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
			//i was only taking top ten recs from here
			//relatedFeatureIDs = postRetrievalContextualReordering(relatedFeatureIDs, MID, clusterID, contextFeatureIDs, minSup, Nprojs, dbLayer);
			ArrayList<Integer> relatedFeatureIDs2 = new ArrayList<>();
			int CID = dbLayer.getCID(MID);
			relatedFeatureIDs2 = McMillanEvaluation.getMcMillanFeatureRecommendations(true,Nprojs,gamma, MID, CID, contextFeatureIDs, pid, false, 0);
			ArrayList<Integer> finalset = new ArrayList<>();		
			for(int i: relatedFeatureIDs)
			{
				finalset.add(i);
			}
			
			finalset.retainAll(relatedFeatureIDs2);
			//System.out.println("Intersection of features: " + finalset.toString());
			finalset.removeAll(contextFeatureIDs);
			if(finalset.size()==0)
			{
				
				//remove features in GT
				relatedFeatureIDs.removeAll(contextFeatureIDs);
				if(relatedFeatureIDs.size()==0)
				{
					
					relatedFeatureIDs2.removeAll(contextFeatureIDs);
					relatedFeatureIDs = relatedFeatureIDs2;
				}
				
			}
			else
			{
				relatedFeatureIDs = finalset;
			}
			//contextual post filtering step
			relatedFeatureIDs.removeAll(contextFeatureIDs);
		}
		
		
		//System.out.println("Recommendation set size is: " + relatedFeatureIDs.size());
		//consider only top N as specified in Nrecs parameter
		//while (relatedFeatureIDs.size() > Nrecs) { // 
			// relatedFeatureIDs.remove(relatedFeatureIDs.size() - 1); // Remove the last entry of the list
		   
		 //}
		int hits = 0;
		float precision = 0;
		float successrate = 0;
		int rank = 0;
		//System.out.println();
		
		if(relatedFeatureIDs.size()>0 && groundTruthFeatureIDs.size()>0)
		{
			if(relatedFeatureIDs.size()>Nrecs)
				relatedFeatureIDs.subList(Nrecs, relatedFeatureIDs.size()).clear();
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
			//if(userfriendly)
				System.out.print(precision+",");
			
		}
		else
		{
			metrics = null;
			System.out.print("-");
		}
		
		//System.out.println();
		
		return metrics;
		
	}


	private static Metrics getRecommendationsForIntellij(int PID, int minSup, int context, int groundtruth, int strategy, int Nrecs, double gamma, int Nprojs, EvaluationDAL dbLayer) throws Exception
	{

		//ArrayList<Metrics> metricsArray = new ArrayList<>();
		Metrics metrics = new Metrics();
		//System.out.println("Recs for method: "+ MID);

		//get project ID from a for loop where you get the 505 plugin projects with at least three features in a fiel
		int pid = PID;
		if(userfriendly)
		{
			//String projectCategory = dbLayer.getProjectName(pid);
			System.out.println("Evaluation ID:" + pid);
			//System.out.println("Application Category:"+ projectCategory.substring(0,projectCategory.indexOf("_")));
		}
//get all the projects features, only those in file file are context rest are GT
		ArrayList<Integer> groundTruthFeatureIDs = dbLayer.getClusterIDs(pid);
		ArrayList<Integer> contextFeatureIDs = new ArrayList<>();
		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);

		//int clusterID = dbLayer.getCID(MID);
		//get file with min 3 features against PID and get cluster IDs in that file to make part of context
		//HashMap<Integer, ArrayList<Integer>> FileFeatureIDs = dbLayer.getFileFeaturesHashMap(pid);

		//Iterator it = FileFeatureIDs.entrySet().iterator();
		//while (it.hasNext()) {

			//Map.Entry pair = (Map.Entry) it.next();
			//ArrayList<Integer> sameFileFeatureIDs = (ArrayList<Integer>) pair.getValue();


		//ArrayList<Integer> sameFileFeatureIDs = dbLayer.getFileFeatures(pid);
		//just get half features for half context evaluation
		ArrayList<Integer> sameFileFeatureIDs = new ArrayList<>();
		for(int i=0; i< groundTruthFeatureIDs.size()/2; i++)
			sameFileFeatureIDs.add(groundTruthFeatureIDs.get(i));

		groundTruthFeatureIDs = new ArrayList<>();

			if (userfriendly) {
				System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
				for (int FID : featuresList)
					System.out.print(FID + ",");
				System.out.println();
			}

			//remove single file features of a project from featureslist
			featuresList.removeAll(sameFileFeatureIDs);
			for (int FID : featuresList) {

				groundTruthFeatureIDs.add(FID);

			}


			//copy same file features to context features
			for (int sfid : sameFileFeatureIDs) {
				if (!contextFeatureIDs.contains(sfid)) {
					contextFeatureIDs.add(sfid);
				}
			}

			if (userfriendly) {
				System.out.println("The Ground truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
				for (int FID : groundTruthFeatureIDs)
					System.out.print(FID + ",");
				System.out.println();
			}
			ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
			ArrayList<Integer> contextProjectsMappings = new ArrayList<Integer>();
			if (strategy == 4) {
				//ArrayList<Integer> contextProjectsMappings = new ArrayList<Integer>();
				//int CID = dbLayer.getCID(MID);
				for (int fid : contextFeatureIDs) {
					ArrayList<Integer> projectIDs = dbLayer.getProjectsContainingFeature(fid, pid);

					//FACERSearchService.contextFeatureMappings.add(featureID);
					for (int aPID : projectIDs) {
						if (!contextProjectsMappings.contains(aPID))
							contextProjectsMappings.add(aPID);
					}

				}


				HashMap<Integer, Set<Integer>> contextProjectCloneIDs = dbLayer.getContextBasedProjectCloneIDs(contextProjectsMappings);
				//
				//contextProjectCloneIDs
				//if (contextProjectsMappings.size() > 1)
				// {
					//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendationsNew(false, Nprojs,gamma, 0, 0, contextFeatureIDs, contextProjectsMappings, contextProjectCloneIDs, pid, false, 0);
					relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendationsB(false, Nprojs,gamma, contextFeatureIDs, pid, false, 0);
					relatedFeatureIDs.removeAll(contextFeatureIDs);
				//}
			}


			//System.out.println("Recommendation set size is: " + relatedFeatureIDs.size());
			//consider only top N as specified in Nrecs parameter
			//while (relatedFeatureIDs.size() > Nrecs) { //
			// relatedFeatureIDs.remove(relatedFeatureIDs.size() - 1); // Remove the last entry of the list

			//}
			int hits = 0;
			float precision = 0;
			float recall = 0;
			float successrate = 0;
			int rank = 0;
			//System.out.println();

			if (relatedFeatureIDs.size() > 0 && groundTruthFeatureIDs.size() > 0 ) {
				if (relatedFeatureIDs.size() > Nrecs)
					relatedFeatureIDs.subList(Nrecs, relatedFeatureIDs.size()).clear();
				if (userfriendly) {
					//comment till // for making method bodies disappear in execution output
					Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];
					int index = 0;
					for (int i : relatedFeatureIDs) {
						clusterIDs[index] = i;
						index++;
					}
					//Method m = dbLayer.getMethodDetails(MID);
					System.out.println("Your Method's Feature Description: " + featureDesc);
					System.out.println("-------------------------------------------------------");
					System.out.println("Your Method's Body: ");
					//System.out.println("======================================");
					//ArrayList<String> body = ViewSampleMethodForClusters.getMethodBody(m);
					//ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);

//				for(String s: body)
//				{
//					System.out.println(s);
//				}
//				System.out.println("-------------------------------------------------------");
//				System.out.println("Your Method's API calls:");
//				for(String s: api_calls)
//				{
//					System.out.println(s);
//				}
					System.out.println("======================================");
					System.out.println("Related Methods:");
					System.out.println("======================================");

					ViewSampleMethodForClusters.viewMethodsAgainstClusterIDsNew(clusterIDs, pid);
					//ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(clusterIDs);
				}
				//
				hits += 1;
				ArrayList<Integer> intersection = new ArrayList<Integer>(groundTruthFeatureIDs);
				intersection.retainAll(relatedFeatureIDs);
				//System.out.println("relatedFeatureIDs size: " + relatedFeatureIDs.size());
				//System.out.println("groundtruthIDs size: " + groundTruthFeatureIDs.size());
				//System.out.println("context size: " + contextFeatureIDs.size());
				precision += (float) intersection.size() / relatedFeatureIDs.size();//keep accumulating for every project
				recall += (float) intersection.size() / groundTruthFeatureIDs.size();//keep accumulating for every project
				float pr = (float) intersection.size() / relatedFeatureIDs.size();
				float re = (float) intersection.size() / groundTruthFeatureIDs.size();
				//System.out.println("Intersection: "+ (float) intersection.size());
				if (pr > 0f) {
					successrate += 1;

					for (int FID : relatedFeatureIDs) {
						rank += 1;
						if (groundTruthFeatureIDs.contains(FID)) {
							break;
						}

					}

				}
				//System.out.println(minSup + "," + precision);
				metrics.precision = precision;
				metrics.recall = recall;
				metrics.success_rate = successrate;
				metrics.rank = rank;
				//if(userfriendly)
				System.out.println(pr + "," + re + "," + successrate + "," + rank);

			} else {
				metrics = null;
				System.out.println("-");
			}
			//metricsArray.add(metrics);
			//System.out.println();
		//}
		return metrics;

	}
	
	private static Metrics getBasicFACERHalfGTRecs(int pid, int MID, int minSup) throws Exception
	{
		userfriendly = false;
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
		ArrayList<Integer> contextFeatureIDs = new ArrayList<>();
		Set<Integer> featuresList = new HashSet<>();
		//copy all features to hashset
		for(int i:groundTruthFeatureIDs)
			featuresList.add(i);
		groundTruthFeatureIDs = new ArrayList<>();
		int clusterID = dbLayer.getCID(MID);
		//get file against MID and get cluster IDs in that file to make part of context
		ArrayList<Integer> sameFileFeatureIDs = dbLayer.getFileCIDs(MID);
		if(userfriendly)
		{
			System.out.println("The full set of features of size " + featuresList.size() + " are as follows: ");
			for(int FID: featuresList)
				System.out.print(FID+",");
			System.out.println();
		}
		featuresList.remove(clusterID);
		//divide the cluster IDs obtained into half and use the second half as ground truth while the first half will be for the
		//context in other context aware approaches		
		/*for(int FID: featuresList){
			
			contextFeatureIDs.add(FID);
			groundTruthFeatureIDs.add(FID);
			
		}
		int startIndex = contextFeatureIDs.size()/4;
		if(startIndex < 1)
			startIndex = 0;
		//int startIndex = 3;
		contextFeatureIDs.subList(startIndex, contextFeatureIDs.size()).clear();*/	
//context setting ends here
			
		for(int sfid: sameFileFeatureIDs)
		{
			if(!contextFeatureIDs.contains(sfid))
			{
				contextFeatureIDs.add(sfid);
			}
		}
		int halfsize = featuresList.size()/2;
		featuresList.removeAll(contextFeatureIDs);
		for(int FID: featuresList){
				
			groundTruthFeatureIDs.add(FID);
			if(groundTruthFeatureIDs.size()==halfsize)
				break;
		}
		//for half context 
		/*featuresList.removeAll(groundTruthFeatureIDs);
		if(featuresList.size()>0)
		{
			for(int FID: featuresList)
			{
				if(!contextFeatureIDs.contains(FID))
				{
					contextFeatureIDs.add(FID);
				}
			}
			
		}*/
		//ends here for half context and half GT
		
		//contextFeatureIDs.subList(contextFeatureIDs.size()/2, contextFeatureIDs.size()).clear();		
		//groundTruthFeatureIDs.subList(0, (groundTruthFeatureIDs.size()/2)).clear();		
		
		
		//groundTruthFeatureIDs.removeAll(contextFeatureIDs);
		
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		//groundTruthFeatureIDs.retainAll(nonSingletonClustersList);
		//System.out.println("clusterIDsList:"+clusterIDsList.size());
		if(true)
		{
		System.out.println("The Ground truth features of size " + groundTruthFeatureIDs.size() + " are as follows: ");
		for(int FID: groundTruthFeatureIDs)
			System.out.print(FID+",");
		System.out.println();
		}
		ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
		
		//get related features against each method of context and sort them by the MCS support from which they originate
		//relatedFeatureIDs = getRelatedFeaturesFACERFromSimProj(MID,contextFeatureIDs, minSup, dbLayer);
		relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);
		
		
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(false,0.5f, MID, CID, contextFeatureIDs, pid, false, 0);
		
		
		//below line adds the additional step of post retrieval re-ordering		
		//relatedFeatureIDs = postRetrievalContextualReordering(relatedFeatureIDs, MID, clusterID, contextFeatureIDs, minSup, dbLayer);
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs = McMillanEvaluation.getMcMillanFeatureRecommendations(MID, CID, pid, false, 0);
		
		//System.out.print("No. of recommendations:"+relatedFeatureIDs.size()+",");
		
		//ArrayList<Integer> relatedFeatureIDs2 = new ArrayList<>();
		//int CID = dbLayer.getCID(MID);
		//relatedFeatureIDs2 = McMillanEvaluation.getMcMillanFeatureRecommendations(true, MID, CID, contextFeatureIDs, pid, false, 0);
		
		//ArrayList<Integer> relatedFeatureIDs3 = new ArrayList<>();
		//relatedFeatureIDs3 = McMillanEvaluation.getMcMillanFeatureRecommendations(false, MID, CID, contextFeatureIDs, pid, false, 0);
		
		//approach with only two strategies intersection
//		ArrayList<Integer> finalset = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			finalset.add(i);
//		}
//		
//		finalset.retainAll(relatedFeatureIDs2);
//		System.out.println("Intersection of features: " + finalset.toString());
//		finalset.removeAll(contextFeatureIDs);
//		if(finalset.size()==0)
//		{
//			
//			//remove features in GT
//			relatedFeatureIDs.removeAll(contextFeatureIDs);
//			if(relatedFeatureIDs.size()==0)
//			{
//				
//				relatedFeatureIDs2.removeAll(contextFeatureIDs);
//				relatedFeatureIDs = relatedFeatureIDs2;
//			}
//			
//		}
//		else
//		{
//			relatedFeatureIDs = finalset;
//		}
		
		//contextual post filtering step
		relatedFeatureIDs.removeAll(contextFeatureIDs);
		System.out.println("Recommendation set size is: " + relatedFeatureIDs.size());
		//ends here
		
//		ArrayList<Integer> finalset = new ArrayList<>();		
//	
//		ArrayList<Integer> common3 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			common3.add(i);
//		}
//		
//		common3.retainAll(relatedFeatureIDs2);
//		common3.retainAll(relatedFeatureIDs3);
		
//		ArrayList<Integer> common12 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			common12.add(i);
//		}		
//		common12.retainAll(relatedFeatureIDs2);
//		
//		ArrayList<Integer> common23 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs2)
//		{
//			common23.add(i);
//		}		
//		common23.retainAll(relatedFeatureIDs3);
//		
//		ArrayList<Integer> common13 = new ArrayList<>();		
//		for(int i: relatedFeatureIDs)
//		{
//			common13.add(i);
//		}		
//		common13.retainAll(relatedFeatureIDs3);
		
//		Set<Integer> finalrecs = new HashSet<>();
//		finalrecs.addAll(common3);
//		finalrecs.addAll(common12);
//		finalrecs.addAll(common23);
//		finalrecs.addAll(common13);
		
//		for(int i: finalrecs)
//		{
//			finalset.add(i);
//		}
//		
//		
//		System.out.println("Intersection of features: " + finalset.toString());
//		relatedFeatureIDs = finalset;
		
		
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
			//if(userfriendly)
			System.out.println("Precision: "+precision);
			
		}
		else
		{
			metrics = null;
			//System.out.println("-");
		}
		
		
		
		return metrics;
		
	}
	
	private static ArrayList<Integer> postRetrievalContextualReordering(
			ArrayList<Integer> relatedFeatures, int MID, int CID, ArrayList<Integer> contextFeatureIDs,
			int minSup,int Nprojs, EvaluationDAL dbLayer) throws Exception {
		//get top 10 similar projects
				HashMap<Integer, Double> sortedProjectSimilarities = McMillanEvaluation.getTop10SimilarProjectsByContext(MID, CID, contextFeatureIDs);
				//return ArrayList<Integer> of top 20 projects
						ArrayList<Integer> projectIDs = new ArrayList<Integer> ();
						for(int i: sortedProjectSimilarities.keySet())
						{
							projectIDs.add(i);
							if(projectIDs.size()==Nprojs)
								break;
						}

		HashMap<Integer, ArrayList<Integer>> projectsCloneIDs = new HashMap<Integer, ArrayList<Integer>>();
				projectsCloneIDs = dbLayer.getProjectsAndCloneIDs(projectIDs);

		HashMap<Integer, Double> cloneIDScores = new HashMap<Integer, Double>();
				
				for(int rf: relatedFeatures)
				{
					//is feature/cloneID in similar project?
					//iterate over hashmap values which are arraylists of integers and check if list contains the feature
					//then add the project in projectsWithFeature list
					//and add its score in projectsWithFeatureSimScore list
					ArrayList<Integer> projectsWithFeature = new ArrayList<Integer>();
					ArrayList<Double> projectsWithFeatureSimScore = new ArrayList<Double>();
					Iterator it = projectsCloneIDs.entrySet().iterator();
				    while (it.hasNext()) {
				    	
				        Map.Entry pair = (Map.Entry)it.next();
				        ArrayList<Integer> projectscloneIDsList = (ArrayList<Integer>) pair.getValue(); 
				        if(projectscloneIDsList.contains(rf))
				        {
				        	int PID = (Integer) pair.getKey();
				        	projectsWithFeature.add(PID);
				        	projectsWithFeatureSimScore.add(sortedProjectSimilarities.get(PID));
				        }
				        		
				    }
				    //get the maximum score of project and use it in hashmap
				    double score = 0;
				    if(projectsWithFeatureSimScore.size() != 0)
				    	score = Collections.max(projectsWithFeatureSimScore);
					//build hashmap of <cloneID,score>
				    cloneIDScores.put(rf, score);
					
				}
				//to do
				//reorder hashmap of <cloneID,score>
				cloneIDScores = (HashMap<Integer, Double>) HashMapSorter.sortHashMapByValuesTest(cloneIDScores);
				//to do
				//copy top ten clone IDs to arraylist
				ArrayList<Integer> recs = new ArrayList<Integer>();
				Iterator it = cloneIDScores.entrySet().iterator();
			    while (it.hasNext()){//&& recs.size()<15) {
			        Map.Entry pair = (Map.Entry)it.next();
			        recs.add((Integer) pair.getKey());
			        		
			    }
				//relatedFeatures.retainAll(projectCloneIDs);
				//if(relatedFeatures.size()==0)
					//return relatedFeaturesCopy;
				return recs;
				

	}

	private static ArrayList<Integer> getRelatedFeaturesFACER(int mID,
			ArrayList<Integer> contextFeatureIDs, int minSup,
			EvaluationDAL dbLayer) throws SQLException {
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//get CID
		int CID = dbLayer.getCID(mID);
		if(!contextFeatureIDs.contains(CID))
			contextFeatureIDs.add(CID);
		//getRelatedFeatures normal way
		relatedFeatures = getRelatedFeatures(contextFeatureIDs, minSup, dbLayer);
		
		
		return relatedFeatures;

	}
	
	private static ArrayList<Integer> getRelatedFeaturesFACERFromSimProj(int mID,
			ArrayList<Integer> contextFeatureIDs, int minSup,
			EvaluationDAL dbLayer) throws Exception {
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//get CID
		int CID = dbLayer.getCID(mID);
		if(!contextFeatureIDs.contains(CID))
			contextFeatureIDs.add(CID);
		//getRelatedFeatures normal way
		relatedFeatures = getRelatedFeaturesFromSimProj(mID, CID, contextFeatureIDs, minSup, dbLayer);
		
		
		return relatedFeatures;

	}

	private static ArrayList<Integer> getRelatedFeatures(
			ArrayList<Integer> contextFeatureIDs, int minSup,
			EvaluationDAL dbLayer) throws SQLException {
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//get MCS against each CID
		Map<Integer,Integer> featureIDs = new LinkedHashMap<Integer, Integer>();//<featureID,support>
		for(int CID : contextFeatureIDs){		
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
		        	 if(!relatedFeatures.contains(CID))//&& !contextFeatureIDs.contains(CID))
		        		 relatedFeatures.add(CID);
		         }
		      }
		      //System.out.println("Rec from call heirarchy neighborhood");		
		     
		}
		
		return relatedFeatures;
	}
	
	private static ArrayList<Integer> getRelatedFeaturesFromSimProj(
			int MID, int CID, ArrayList<Integer> contextFeatureIDs, int minSup,
			EvaluationDAL dbLayer) throws Exception {
		
		//get top 10 similar projects
		HashMap<Integer, Double> sortedProjectSimilarities = McMillanEvaluation.getTop10SimilarProjectsByContext(MID, CID, contextFeatureIDs);
		//return ArrayList<Integer> of top 20 projects
				ArrayList<Integer> projectIDs = new ArrayList<Integer> ();
				for(int i: sortedProjectSimilarities.keySet())
				{
					projectIDs.add(i);
					if(projectIDs.size()==10)
						break;
				}
		//get features of top 10 similar projects
		//ArrayList<Integer> projectCloneIDs = new ArrayList<Integer>();
		//projectCloneIDs = dbLayer.getProjectsCloneIDs(projectIDs);
		
		HashMap<Integer, ArrayList<Integer>> projectsCloneIDs = new HashMap<Integer, ArrayList<Integer>>();
		projectsCloneIDs = dbLayer.getProjectsAndCloneIDs(projectIDs);
		
		ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
		//get MCS against each CID
		Map<Integer,Integer> featureIDs = new LinkedHashMap<Integer, Integer>();//<featureID,support>
		for(int cid : contextFeatureIDs){		
			featureIDs.putAll(dbLayer.getFeatureIDs(cid, minSup));//higher support features first	
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
		         for(int cid:someRelatedFeatures)
		         {
		        	//this check of not equal to neighborhood CIDs shouldnt be here
		        	 //if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.contains(CID))
		        	 if(!relatedFeatures.contains(cid))//&& !contextFeatureIDs.contains(cid))
		        		 relatedFeatures.add(cid);
		         }
		      }
		      //System.out.println("Rec from call heirarchy neighborhood");		
		     
		}
		ArrayList<Integer> relatedFeaturesCopy = relatedFeatures;
		//get only those features that are part of contextually similar projects
		
		HashMap<Integer, Double> cloneIDScores = new HashMap<Integer, Double>();
		
		for(int rf: relatedFeatures)
		{
			//is feature/cloneID in similar project?
			//iterate over hashmap values which are arraylists of integers and check if list contains the feature
			//then add the project in projectsWithFeature list
			//and add its score in projectsWithFeatureSimScore list
			ArrayList<Integer> projectsWithFeature = new ArrayList<Integer>();
			ArrayList<Double> projectsWithFeatureSimScore = new ArrayList<Double>();
			Iterator it = projectsCloneIDs.entrySet().iterator();
		    while (it.hasNext()) {
		    	
		        Map.Entry pair = (Map.Entry)it.next();
		        ArrayList<Integer> projectscloneIDsList = (ArrayList<Integer>) pair.getValue(); 
		        if(projectscloneIDsList.contains(rf))
		        {
		        	int PID = (Integer) pair.getKey();
		        	projectsWithFeature.add(PID);
		        	projectsWithFeatureSimScore.add(sortedProjectSimilarities.get(PID));
		        }
		        		
		    }
		    //get the maximum score of project and use it in hashmap
		    double score = 0;
		    if(projectsWithFeatureSimScore.size() != 0)
		    	score = Collections.max(projectsWithFeatureSimScore);
			//build hashmap of <cloneID,score>
		    cloneIDScores.put(rf, score);
			
		}
		//to do
		//reorder hashmap of <cloneID,score>
		cloneIDScores = (HashMap<Integer, Double>) HashMapSorter.sortHashMapByValuesTest(cloneIDScores);
		//to do
		//copy top ten clone IDs to arraylist
		ArrayList<Integer> recs = new ArrayList<Integer>();
		Iterator it = cloneIDScores.entrySet().iterator();
	    while (it.hasNext() && recs.size()<10 ) {
	        Map.Entry pair = (Map.Entry)it.next();
	        recs.add((Integer) pair.getKey());
	        		
	    }
		//relatedFeatures.retainAll(projectCloneIDs);
		//if(relatedFeatures.size()==0)
			//return relatedFeaturesCopy;
		return recs;
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
