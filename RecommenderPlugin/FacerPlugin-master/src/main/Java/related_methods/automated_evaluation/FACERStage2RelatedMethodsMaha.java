package related_methods.automated_evaluation;

import RelatedMethods.DataObjects.Method;
import RelatedMethods.CustomUtilities.Constants;
import RelatedMethods.db_access_layer.EvaluationDAL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import related_methods._3_PopulateRelatedFeatures.ViewSampleMethodForClusters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class FACERStage2RelatedMethodsMaha {
	public static HashMap<Integer, Double> projectSimilarities = new HashMap<Integer, Double>();
	public static HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
	public static int previousUserFunctionID = 0;
	public static EvaluationDAL dbLayer;
	
	
	public static void main (String args[]) throws Exception
	{
		//testing Mahas
		//JSONArray ja =  getRelatedMethods(29020 , 3, "jdbc:mysql://localhost/faceremserepopoint5?useSSL=false&user=root");
		JSONArray ja =  getRelatedMethods(31884, 3, "jdbc:mysql://203.135.63.70:3306/faceremserepopoint5?useSSL=false&user=shamsa&password=Mysql123!@#","F:/Maha2021/FACER-AS/FACER-AS");
		
		System.out.print(ja);
		
		
		//JSONObject jo = getFileBody(12,"jdbc:mysql://203.135.63.70:3306/faceremserepopoint5?useSSL=false&user=shamsa&password=Mysql123!@#", "F:/FACER_2020/RawSourceCodeDataset");
//System.out.print(jo);
		
		//JSONArray ja  = getCalledMethods(22,"jdbc:mysql://203.135.63.70:3306/faceremserepopoint5?useSSL=false&user=shamsa&password=Mysql123!@#",  "F:/FACER_2020/RawSourceCodeDataset");
		//System.out.print(ja);
		//testing Saads writing methods to file for making dataset for CodeBERT
		/*
		int[] methodIDs = {63,1721,2484,2728,21386,26658,4431,14191,4427,14564,14625,15155,21258,20916,20979,31754,29947,34032,1966,27345,27451,27297,27492,13713,13643,29688,2819,2810,6009,2830,28667,18455,6315,31765,29901,29900,34220,13311,10520,10514,13313,100,448,10,1417,23622,23274,15267,14310,14862,14813,17414,12856,12855,10068,7279,7105,7783,17318,17373,17374,17377,32178,21700,33581,18169,2807,6005,2875,2864,22643,34411,13912,1250,17173,17168,32323,32223,32206,31741,2242,28556,5478,22204,1239,6883,26667,4433,6174,1946,6165,751,4864,29882,868,176,188,277,226,250,227,251,344,32439,375,6932,1625,435,1147,1281,1217,1231,1597,1600,1994,2032,1995,2033,2018,2017,2036,2052,2537,2538,2552,25054,2623,5684,2767,2768,2791,2795,2809,5939,2822,5952,2826,5956,2882,6012,2883,6013,2885,6015,3252,3256,3303,24904,3355,30404,3487,20580,3506,20599,3508,20601,3518,20611,3519,20612,3520,20613,3568,3573,3688,3687,3930,4003,3931,4004,4140,4173,4144,4177,4147,4180,4162,4195,4213,4412,4382,4383,4730,4544,4565,4821,4566,4823,4873,4887,5313,5359,5632,6626,5634,6628,5637,6631,5647,6641,5666,6660,5695,6688,5697,6690,5716,6709,5725,6718,5789,6777,5790,6778,5792,6780,5797,6785,5798,6786,5802,6790,5806,6794,5807,6795,5816,6804,5817,6805,5818,6806,5820,6808,5823,6811,5825,6813,5841,6829,5844,6832,5845,6833,5850,6838,5386,5388,13939,14897,14891,14939,17809,17908,21705,21629,22389,22396,25483,25475,26542,26559,29022,32382,34226,29897,30366,30365,31403,31402,37191,36932,4142,16062,16061,4175,32371,34259,34260,33915,33919,33917,33918,5869,3034,6164,5167,3558,3561,3559,11470,8690,5907,6570,6572,6575,8368,11148,10244,11273,8493,8492,11272};
		
		for (int m: methodIDs)
			writeMethodToFile(m);
		System.out.println("Done.");*/
		
		/*int[] methodIDs = {28667,	5637,
		18455,	6631,
		2087,	2822,
		344,	5634,
		32439,	6628,
		29900,	33915,
		13313,	1239,
		3506,	3508,
		5634,	5637,
		5844,	5845};
*/
	}
	public static JSONObject getFileBody(int MID, String DB, String pathToFACERAS) throws Exception
	{
		Constants.DATABASE = DB;
		//copy method body getting code here
		//get file path of method from DB
		String filePath = "";
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		filePath = pathToFACERAS + "/Dataset/"+ dbLayer.getFilePathofMethod(MID);
		String fileBody = "";
		//put the file contents in JSON 
		JSONObject methodObject = new JSONObject(); 
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		for (String line: lines ) {
		    //s.add(line);
			fileBody = fileBody.concat(line+"\n");
			
		}
		methodObject.put("FileBody", fileBody);
		return methodObject;
	}
	
	public static JSONArray getCalledMethods(int MID, String DB, String pathToFACERAS) throws Exception
	{
		String methodRecAlgo = "GetCalledMethods";
		Constants.DATABASE = DB;
		JSONArray methodRecsArray= new JSONArray();
		ArrayList<Method> methods = new ArrayList<Method>();

		ArrayList<Integer> calledMethodsList = new ArrayList<Integer>();
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
		
		calledMethodsList = dbLayer.getCalledMethods(MID);
		for(int mID: calledMethodsList)
		{
			Method newMethod = dbLayer.getMethodRelPath(mID);
			methods.add(newMethod);
			
		}
		//this iterate over methods and returns as jsonArray
		
		for(Method m:methods )
		{				
		String s = "";
		
		int from_line = m.from_line_num;
		int to_line = m.to_line_num;
		
		int line_num = from_line;
		BufferedReader reader;
			try {
				String file_path = pathToFACERAS + "/Dataset/" + m.file_name;
				reader = new BufferedReader(new FileReader(file_path));
				for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
				    ;
				String line;
				for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
				    //s.add(line);
				    s = s.concat(line+"\n");
					
					
				}
				
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JSONObject methodObject = new JSONObject(); 
			methodObject.put("ID", m.id);
			methodObject.put("Body", s);	
			methodObject.put("Name", m.name);
			methodObject.put("Algo", methodRecAlgo);
			methodRecsArray.add(methodObject);
		}	
			return methodRecsArray;

	}
	public static void writeMethodToFile(int MID) throws Exception
	{
		////////FileWriter myWriter = new FileWriter("F://SaadArshad//MoreFACERDatasetForCodeBERT//"+MID+".txt");
		FileWriter myWriter = new FileWriter("F://SaadArshad//FACERDatasetForCodeBERT//classmethods//"+MID+".java");
		
		//db connection
		Constants.DATABASE = "jdbc:mysql://localhost/faceremserepopoint5?useSSL=false&user=root";
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeFoldDatabaseConnector();	
			
		//call method to get filepath
		Method m = dbLayer.getMethodById(MID);
		
		//read method body from file and write to another file
		String s = "";
		int from_line = m.from_line_num;
		int to_line = m.to_line_num;		
		int line_num = from_line;
		BufferedReader reader;
		myWriter.write("public class xyz{"+"\n");
			try {
				reader = new BufferedReader(new FileReader(m.file_name));
				for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
				    ;
				String line;
				for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
				    //s.add(line);
				    //s = s.concat(line+"\n");	
				    myWriter.write(line+"\n");
				}
				myWriter.write("}"+"\n");
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		//name of file should be ID of method
		myWriter.close();
		
	}
public static JSONArray getRelatedMethods(int MID, int minSup, String DB, String pathToFACERAS) throws Exception{
	String methodRecAlgo = "none";
	Constants.DATABASE = DB;
	JSONArray methodRecsArray= new JSONArray();
	ArrayList<Method> methods = new ArrayList<Method>();

	EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
	dbLayer.initializeFoldDatabaseConnector();	
	
	ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
	int pid = dbLayer.getProjectID(MID);
	ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
	
	relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);

	if(relatedFeatureIDs.size()!=0)
	{
		methodRecAlgo = "basicFACER";
	}

		Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];  
		int index = 0;
		for(int i:relatedFeatureIDs)
		{
			clusterIDs[index] = i;
			index++;
		}
	
	if(relatedFeatureIDs.size()==0)
	{
		//at least return neighborhood methods whether or not they are MCS or MCG member
		
				int CID = dbLayer.getCID(MID);
				ArrayList<Integer> neighborhoodCIDs = dbLayer.getUniqueNeighborhoodCIDs(MID,CID);
				//copy neighborhood CIDs to clusterIDs variable because of data type mismatch
				clusterIDs = new Integer[neighborhoodCIDs.size()];
				for(int i:neighborhoodCIDs)
				{
					clusterIDs[index] = i;
					index++;
				}
				methodRecAlgo = "neighbors";
	}
	
	//dbLayer.closeConnector();
	//input clusterIDs and get representative methods
	//?????
	ArrayList<Integer> MethodIDs = new ArrayList<Integer>();
	MethodIDs.addAll(ViewSampleMethodForClusters.getMethodsAgainstClusterIDs(clusterIDs, pid));
	
	for(int mID: MethodIDs)
	{
		Method newMethod = dbLayer.getMethodRelPath(mID);
		methods.add(newMethod);
		
	}
	//this iterate over methods and returns as jsonArray
	
	for(Method m:methods )
	{				
	String s = "";
	
	int from_line = m.from_line_num;
	int to_line = m.to_line_num;
	
	int line_num = from_line;
	BufferedReader reader;
		try {
			String file_path = pathToFACERAS + "/Dataset/" + m.file_name;
			reader = new BufferedReader(new FileReader(file_path));
			for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
			    ;
			String line;
			for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
			    //s.add(line);
			    s = s.concat(line+"\n");
				
				
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject methodObject = new JSONObject(); 
		methodObject.put("ID", m.id);
		methodObject.put("Body", s);	
		methodObject.put("Name", m.name);
		methodObject.put("Algo", methodRecAlgo);
		methodRecsArray.add(methodObject);
	}
	

	return methodRecsArray;
	
}

	public static JSONArray getRelatedMethodsWithSupport(int MID, int minSup, String DB, String pathToFACERAS) throws Exception{
		String methodRecAlgo = "none";
		Constants.DATABASE = DB;
		JSONArray methodRecsArray= new JSONArray();
		ArrayList<Method> methods = new ArrayList<Method>();

		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		dbLayer.initializeFoldDatabaseConnector();

		//ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		int pid = dbLayer.getProjectID(MID);
		Map<Integer, Integer> relatedFeatureIDs = new LinkedHashMap<Integer, Integer>();

		relatedFeatureIDs = getRelatedFeaturesFACERWithSupport(MID, minSup, dbLayer);

		if(relatedFeatureIDs.size()!=0)
		{
			methodRecAlgo = "basicFACER";
		}

		methods = ViewSampleMethodForClusters.getMethodsAgainstClusterIDsWithSupport(relatedFeatureIDs, pid);

//		for (Method m : methods) {
//			Method newMethod = dbLayer.getMethodRelPath(m.id);
//			methods.add(newMethod);
//
//		}

//		Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];
//		int index = 0;
//		for(int i:relatedFeatureIDs)
//		{
//			clusterIDs[index] = i;
//			index++;
//		}

		if(relatedFeatureIDs.size()==0) {
			//at least return neighborhood methods whether or not they are MCS or MCG member

			int CID = dbLayer.getCID(MID);
			ArrayList<Integer> neighborhoodCIDs = dbLayer.getUniqueNeighborhoodCIDs(MID, CID);
			//copy neighborhood CIDs to clusterIDs variable because of data type mismatch
			Integer[] clusterIDs = new Integer[neighborhoodCIDs.size()];
			int index = 0;
			for (int i : neighborhoodCIDs) {
				clusterIDs[index] = i;
				index++;
			}
			methodRecAlgo = "neighbors";


			//dbLayer.closeConnector();
			//input clusterIDs and get representative methods
			//?????
			ArrayList<Integer> MethodIDs = new ArrayList<Integer>();
			MethodIDs.addAll(ViewSampleMethodForClusters.getMethodsAgainstClusterIDs(clusterIDs, pid));

			for (int mID : MethodIDs) {
				Method newMethod = dbLayer.getMethodRelPath(mID);
				newMethod.support = -1;
				methods.add(newMethod);

			}
		}
		//this iterate over methods and returns as jsonArray


		for(Method m:methods )
		{
			String s = "";

			int from_line = m.from_line_num;
			int to_line = m.to_line_num;

			int line_num = from_line;
			BufferedReader reader;
			try {

				String file_path = pathToFACERAS + "/Dataset/" + m.file_name;
				reader = new BufferedReader(new FileReader(file_path));
				for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
					;
				String line;
				for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
					//s.add(line);
					s = s.concat(line+"\n");


				}

				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JSONObject methodObject = new JSONObject();
			methodObject.put("ID", m.id);
			methodObject.put("ProjectID", m.projectID);
			methodObject.put("Body", s);
			methodObject.put("Name", m.name);
			methodObject.put("Algo", methodRecAlgo);
			methodObject.put("Support", m.support);
			methodRecsArray.add(methodObject);
		}


		return methodRecsArray;

	}


	public static JSONArray getContextRelatedMethods(int MID, ArrayList<Integer> contextFeatureMappings, ArrayList<Integer> contextProjectsMappings, HashMap<Integer, Set<Integer>> contextProjectCloneIDs, HashMap<Integer, Double> projectIDScores, String DB, String pathToFACERAS) throws Exception{
		String methodRecAlgo = "none";
		RelatedMethods.CustomUtilities.Constants.DATABASE = DB;
		JSONArray methodRecsArray= new JSONArray();
		ArrayList<Method> methods = new ArrayList<Method>();

		EvaluationDAL dbLayer = EvaluationDAL.getInstance();
		if(!EvaluationDAL.initializedFoldDBCon)
			dbLayer.initializeFoldDatabaseConnector();

//		dbLayer.initializeFoldDatabaseConnector();
		int cid = dbLayer.getCID(MID);
		//ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
		int pid = dbLayer.getProjectID(MID);
		//Map<Integer, Integer> relatedFeatureIDs = new LinkedHashMap<Integer, Integer>();

		ArrayList<Integer> relatedFeatureIDsArrayList = new ArrayList<Integer>();
		relatedFeatureIDsArrayList = McMillanEvaluation.getMcMillanFeatureRecommendationsNew(false,10,0.5, MID, cid, contextFeatureMappings, contextProjectsMappings, contextProjectCloneIDs, projectIDScores, pid,false,0);
		relatedFeatureIDsArrayList.removeAll(contextFeatureMappings);

		if(relatedFeatureIDsArrayList.size()!=0)
		{
			methodRecAlgo = "context algo";
		}

		methods = ViewSampleMethodForClusters.getMethodsAgainstClusterIDsArrayList(relatedFeatureIDsArrayList, pid);

//		for (Method m : methods) {
//			Method newMethod = dbLayer.getMethodRelPath(m.id);
//			methods.add(newMethod);
//
//		}

//		Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];
//		int index = 0;
//		for(int i:relatedFeatureIDs)
//		{
//			clusterIDs[index] = i;
//			index++;
//		}

		//this iterate over methods and returns as jsonArray


		for(Method m:methods )
		{
			String s = "";

			int from_line = m.from_line_num;
			int to_line = m.to_line_num;

			int line_num = from_line;
			BufferedReader reader;
			try {

				String file_path = pathToFACERAS + "/Dataset/" + m.file_name;
				reader = new BufferedReader(new FileReader(file_path));
				for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
					;
				String line;
				for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
					//s.add(line);
					s = s.concat(line+"\n");


				}

				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JSONObject methodObject = new JSONObject();
			methodObject.put("ID", m.id);
			methodObject.put("ProjectID", m.projectID);
			methodObject.put("Body", s);
			methodObject.put("Name", m.name);
			methodObject.put("Algo", methodRecAlgo);
			//methodObject.put("Support", m.support);
			methodRecsArray.add(methodObject);
		}


		return methodRecsArray;

	}
//the following method uses schafer if no methods from MCS
public static JSONArray getRelatedMethodsStage2(int MID, int minSup, String DB) throws Exception{
	
	Constants.DATABASE = DB;
	JSONArray methodRecsArray= new JSONArray();
	ArrayList<Method> methods = new ArrayList<Method>();

	dbLayer = EvaluationDAL.getInstance();		
	dbLayer.initializeFoldDatabaseConnector();	
	
	ArrayList<Integer> nonSingletonClustersList = dbLayer.getNonSingletonClusters();
	int pid = dbLayer.getProjectID(MID);
	ArrayList<Integer> relatedFeatureIDs = new ArrayList<>();
	
	//relatedFeatureIDs = getRelatedFeaturesFACER(MID, minSup, dbLayer);

	if(relatedFeatureIDs.size()==0)//if FACER doesnt give recs fall back to schafer
	{
	ArrayList<Integer> relatedFeatureIDs2 = new ArrayList<>();
	ArrayList<Integer> contextFeatureIDs = new ArrayList<>();//this will be empty for now
	
	int CID = dbLayer.getCID(MID);
	
	relatedFeatureIDs2 = getMcMillanFeatureRecommendations(dbLayer, true,10,0.5f, MID, CID, contextFeatureIDs, pid, false, 0);
	
	//ArrayList<Integer> relatedFeatureIDs3 = new ArrayList<>();
	//relatedFeatureIDs3 = McMillanEvaluation.getMcMillanFeatureRecommendations(false, MID, CID, contextFeatureIDs, pid, false, 0);
	relatedFeatureIDs = relatedFeatureIDs2;
	}
	if(relatedFeatureIDs.size()!=0)
	{
	Integer[] clusterIDs = new Integer[relatedFeatureIDs.size()];  
	int index = 0;
	for(int i:relatedFeatureIDs)
	{
		clusterIDs[index] = i;
		index++;
	}
	//dbLayer.closeConnector();
	//input clusterIDs and get representative methods
	//?????
	ArrayList<Integer> MethodIDs = new ArrayList<Integer>();
	MethodIDs.addAll(ViewSampleMethodForClusters.viewMethodsAgainstClusterIDs(clusterIDs, pid));
	
	for(int mID: MethodIDs)
	{
		Method newMethod = dbLayer.getMethodById(mID);
		methods.add(newMethod);
		
	}
	//this iterate over methods and returns as jsonArray
	
	for(Method m:methods )
	{				
	String s = "";
	
	int from_line = m.from_line_num;
	int to_line = m.to_line_num;
	
	int line_num = from_line;
	BufferedReader reader;
		try {
			
			reader = new BufferedReader(new FileReader(m.file_name));
			for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
			    ;
			String line;
			for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
			    //s.add(line);
			    s = s.concat(line+"\n");
				
				
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject methodObject = new JSONObject(); 
		methodObject.put("ID", m.id);
		methodObject.put("Body", s.toString());	
		methodObject.put("Name", m.name);
		methodRecsArray.add(methodObject);
	}
	}

	return methodRecsArray;
	
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

	private static Map<Integer, Integer> getRelatedFeaturesFACERWithSupport(int mID,
															  int minSup, EvaluationDAL dbLayer) throws SQLException {
		Map<Integer, Integer> relatedFeatures = new LinkedHashMap<Integer, Integer>();
		//get CID
		int CID = dbLayer.getCID(mID);
		//if has CID
		if(CID!=0)
		{
			//getRelatedFeatures normal way
			relatedFeatures = getRelatedFeaturesWithSupport(CID, minSup, dbLayer);

			//if no features, get from NeighborhoodMethods
			if(relatedFeatures.size()==0)
			{
				relatedFeatures = getRelatedFeaturesFromNeighborhoodWithSupport(mID,CID, minSup, dbLayer);

			}
			else
			{
				//System.out.println("Rec from MCS");
			}
		}
		else
		{
			//if no CID
			relatedFeatures = getRelatedFeaturesFromNeighborhoodWithSupport(mID,CID, minSup, dbLayer);

		}


		return relatedFeatures;
	}
	private static Map<Integer, Integer> getRelatedFeaturesFromNeighborhoodWithSupport(
			int mID, int CIDofMID, int minSup, EvaluationDAL dbLayer) throws SQLException {

		Map<Integer, Integer> relatedFeatures = new LinkedHashMap<Integer, Integer>();
		//getNeighborhoodMethods CIDs

		ArrayList<Integer> neighborhoodCIDs = dbLayer.getUniqueNeighborhoodCIDs(mID,CIDofMID);

		Map<Integer,Integer> featureIDs  = new LinkedHashMap<Integer,Integer>();
		for(int CID: neighborhoodCIDs)
		{
			//<featureID,support>
			featureIDs.putAll(dbLayer.getFeatureIDs(CID, minSup));//higher support features first

		}
		if(featureIDs.size()!=0)//belongs to a clone structure
		{
			//sort the featureIDs map
			//featureIDs = HashMapSorter.sortHashMapByValuesTest2(featureIDs);
			//iterate over feature ids list to get the clusterIDs
			Set set = featureIDs.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs2(Integer.parseInt(me.getKey().toString()));
				for(int CID:someRelatedFeatures)
				{
					if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.containsKey(CID))
						relatedFeatures.put(CID,Integer.parseInt(me.getValue().toString()));
				}
			}
			//System.out.println("Rec from call heirarchy neighborhood");

		}

	/*else
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
				System.out.println("Rec from same file neighborhood");

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
		        	 if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.contains(CID))
		        		 relatedFeatures.add(CID);
		         }
		      }



	}}*/

		return relatedFeatures;
	}

private static ArrayList<Integer> getRelatedFeaturesFromNeighborhood(
		int mID, int CIDofMID, int minSup, EvaluationDAL dbLayer) throws SQLException {
	
	ArrayList<Integer> relatedFeatures = new ArrayList<Integer>();
	//getNeighborhoodMethods CIDs
	
	ArrayList<Integer> neighborhoodCIDs = dbLayer.getUniqueNeighborhoodCIDs(mID,CIDofMID);
	
	Map<Integer,Integer> featureIDs  = new LinkedHashMap<Integer,Integer>();
	for(int CID: neighborhoodCIDs)
	{
		//<featureID,support>
		featureIDs.putAll(dbLayer.getFeatureIDs(CID, minSup));//higher support features first		
		
	}
	if(featureIDs.size()!=0)//belongs to a clone structure
	{
		//sort the featureIDs map
		//featureIDs = HashMapSorter.sortHashMapByValuesTest2(featureIDs);
		//iterate over feature ids list to get the clusterIDs
		 Set set = featureIDs.entrySet();
	     Iterator i = set.iterator();
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs2(Integer.parseInt(me.getKey().toString()));	
	         for(int CID:someRelatedFeatures)
	         {
	        	 if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.contains(CID))
	        		 relatedFeatures.add(CID);
	         }
	      }
	      //System.out.println("Rec from call heirarchy neighborhood");		
	     
	}
	
	/*else
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
				System.out.println("Rec from same file neighborhood");
			
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
		        	 if(!neighborhoodCIDs.contains(CID) && !relatedFeatures.contains(CID))
		        		 relatedFeatures.add(CID);
		         }
		      }
					
		     
		
	}}*/
	
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
		      while(i.hasNext() && relatedFeatures.size() < 20) {//added the condition to limit top 20 related recs
		         Map.Entry me = (Map.Entry)i.next();
		        ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs(Integer.parseInt(me.getKey().toString()),cid);
		        for(int CID:someRelatedFeatures)
		        {
		        	
		        	if(!relatedFeatures.contains(CID) )
		        		relatedFeatures.add(CID);
		        }
		      }
					
		
		}
	
	return relatedFeatures;
}

	private static Map<Integer,Integer> getRelatedFeaturesWithSupport(Integer cid, int minSup, EvaluationDAL dbLayer) throws SQLException {

		Map<Integer,Integer> relatedFeatures = new LinkedHashMap<Integer, Integer>();
		//<featureID,support>
		Map<Integer,Integer> featureIDs = dbLayer.getFeatureIDs(cid, minSup);//higher support features first
		if(featureIDs.size()!=0)//method belongs to a clone structure
		{
			//iterate over feature ids list to get the clusterIDs
			Set set = featureIDs.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext() && relatedFeatures.size() < 20) {//added the condition to limit top 20 related recs
				Map.Entry me = (Map.Entry)i.next();
				ArrayList<Integer> someRelatedFeatures = dbLayer.retrieveRelatedClusterIDs(Integer.parseInt(me.getKey().toString()),cid);
				for(int CID:someRelatedFeatures)
				{

					if(!relatedFeatures.containsKey(CID) )
						relatedFeatures.put(CID, Integer.parseInt(me.getValue().toString()));
				}
			}


		}

		return relatedFeatures;
	}

public static ArrayList<Integer> getMcMillanFeatureRecommendations(EvaluationDAL dbLayer, boolean tuned, int Nprojs, double gamma, int MID, int CID, ArrayList<Integer> contextFeatureIDs, int PID, boolean isWeightedByMCS, float similarityThreshold) throws Exception
{
	//dbLayer = EvaluationDAL.getInstance();		
	//dbLayer.initializeFoldDatabaseConnector();	
	
	projectSimilarities = new HashMap<Integer, Double>();
	projectCloneIDs = new HashMap<Integer, Set<Integer>>();
	previousUserFunctionID = 0;
	
	ArrayList<Integer> topMethodsList = new ArrayList<Integer>();
	ArrayList<Integer> projectIDs = new ArrayList<Integer>();
	if(similarityThreshold > 0 && similarityThreshold <= 1)
		projectIDs = getSimilarProjectsByThreshold(MID, CID, similarityThreshold);
	else
	{
		//projectIDs = getTop20SimilarProjects(MID, CID);
		projectIDs = getTop20SimilarProjectsByContext(tuned, Nprojs, MID, CID, contextFeatureIDs, PID);
	}
	//get prediction score for each function of those projects which is a clone class member (a feature in other words)
	HashMap<Integer, Set<Integer>> projectCloneIDs = getFeaturesFromProjects(projectIDs);
	Map<Integer, Double> clonePredictionScores = new HashMap<Integer, Double>();
	if(isWeightedByMCS)
		clonePredictionScores = getMCSBasedPredictionScores(projectCloneIDs);
	else
		clonePredictionScores = getPredictionScores(projectCloneIDs);
	
	
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

public static <T> ArrayList<T> convertSetToList(Set<T> set) 
{ 
    // create an empty list 
    ArrayList<T> list = new ArrayList<>(); 

    // push each element in the set into the list //upto 10 only 
    for (T t : set) {
    	//if(list.size()<10)
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
