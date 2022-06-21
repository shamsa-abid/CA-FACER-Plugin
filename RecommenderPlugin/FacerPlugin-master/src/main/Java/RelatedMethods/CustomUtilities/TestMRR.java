package Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestMRR {
	public static void main(String args[])  { 
		ArrayList<Integer> nonSingletonClustersList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
		
		
		ArrayList<Float> avgPrecisionList = new  ArrayList<Float>();
		ArrayList<Float> avgRecallList = new  ArrayList<Float>();
		ArrayList<Float> avgSuccessRateList = new  ArrayList<Float>();
		ArrayList<Float> avgReciprocalRankList = new  ArrayList<Float>();
		
		ArrayList<Integer> projectIDsList = new  ArrayList<Integer>(Arrays.asList(1,2));
		
		for(Integer pid: projectIDsList)
		{
			//System.out.println("getting cluster IDs of project:" + pid);
			ArrayList<Integer> clusterIDsList = new  ArrayList<Integer>(Arrays.asList(1,2,3,15,16));
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
					
				
				relatedFeatureIDs = getRelatedFeatures(cid);
				//System.out.println("relatedFeatureIDs:"+relatedFeatureIDs.size());
				if(relatedFeatureIDs.size()>0 && testProjectFeatureIDs.size()>0)
				{
					hits+=1;
					Set<Integer> intersection = new HashSet<Integer>(testProjectFeatureIDs);
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
		System.out.println(averagePrecision + "," + averageRecall + "," + averageSuccessRate + "," + averageMRR);
		
	}

	private static ArrayList<Integer> getRelatedFeatures(Integer cid) {
		if (cid == 1)
		{
			return new ArrayList<Integer>(Arrays.asList(20));
		}
		if (cid == 2)
		{
			return new ArrayList<Integer>(Arrays.asList(1,3));
		}
		if (cid == 3)
		{
			return new ArrayList<Integer>();
		}
		return null;
	}
}
