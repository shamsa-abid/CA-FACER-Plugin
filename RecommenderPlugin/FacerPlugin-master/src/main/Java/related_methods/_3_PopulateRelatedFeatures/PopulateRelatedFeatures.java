package related_methods._3_PopulateRelatedFeatures;

import RelatedMethods.CustomUtilities.Constants;
import RelatedMethods.db_access_layer.DatabaseAccessLayer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PopulateRelatedFeatures {



	public static LinkedHashMap<Integer, Integer> featuresSupport = new LinkedHashMap<Integer, Integer>();
	
	public static void main(String args[]) throws Exception
	{
		//read the file output by spmf fpmax miner
		//detect #SUP:
		//the output is clusterIDs that appear together across projects
		
		//read file line by line and add to an arraylist of strings
		//String fileName = "output_combined_fpclose.txt";
		//String fileName = "output_chat_fpclose_2.txt";
		//String fileName = "output_56plus_fpmax_0point1.txt";
		//String fileName = "output_101repo_fpclose_point03.txt";
		//String fileName = "output_101reponew_point03.txt";
		//String fileName = "output_120repo_point9simthresh_point03minsup.txt";
		//String fileName = "output_faceremserepo_simthreshpoint9_minsuppoint02_3api.txt";
		//String fileName = "output_simplefaceremserepo_simthreshpoint9_minsuppoint02_3api.txt";
		
		//String fileName = "D:\\JetBrainsInternship\\Code\\RecommenderPlugin\\FacerPlugin-master\\src\\main\\Java\\related_methods\\_3_PopulateRelatedFeatures\\output_dungeon.txt";
		//String fileName = "G:\\Downloads\\outputplugins.txt";
		String fileName = "output\\FBpatterns.txt";
		int minSup = 2;
		int minDepth = 2;
		ArrayList<String> relatedFeaturesList = getCoOccurringFeatures(fileName, minSup);
		
		//save the clusterIDs in a related_features table (id, feature_id, cluster_id)
		//each itemset that has a min support of 2 a min depth of two should be stored 
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToPopulateRelatedFeatures();				
		dbLayer.populateRelatedFeaturesTable(relatedFeaturesList, minDepth, featuresSupport);
		//dbLayer.populateFeaturesSupportTable(relatedFeaturesList, minDepth, featuresSupport);
		dbLayer.closeConnector();		
	}

	public static void populateRelatedFeatures(String database) throws Exception
	{

		Constants.DATABASE = database;
		String fileName = "Resources\\output\\FBpatterns.txt";
		int minSup = 2;
		int minDepth = 2;
		ArrayList<String> relatedFeaturesList = getCoOccurringFeatures(fileName, minSup);

		//save the clusterIDs in a related_features table (id, feature_id, cluster_id)
		//each itemset that has a min support of 2 a min depth of two should be stored
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToPopulateRelatedFeatures();
		dbLayer.populateRelatedFeaturesTable(relatedFeaturesList, minDepth, featuresSupport);
		//dbLayer.populateFeaturesSupportTable(relatedFeaturesList, minDepth, featuresSupport);
		dbLayer.closeConnector();
	}

	private static ArrayList<String> getCoOccurringFeatures(String fileName, int minSup) throws IOException {
		
		ArrayList<String> relatedFeaturesList = new ArrayList<String> ();
		File file = new File(fileName);
		 
		BufferedReader br = new BufferedReader(new FileReader(file));
		 
		String st;
		int feature_id = 0;
		while ((st = br.readLine()) != null)
		{
			int support = Integer.parseInt(st.substring(st.indexOf(":")+2));
			if(support >= minSup)
			{
			st = st.substring(0, st.indexOf("#")-1);			
			System.out.println(st);
			relatedFeaturesList.add(st);
			feature_id+=1;
			featuresSupport.put(feature_id, support);
			}
		}
		 		
		return relatedFeaturesList;
	}
}
