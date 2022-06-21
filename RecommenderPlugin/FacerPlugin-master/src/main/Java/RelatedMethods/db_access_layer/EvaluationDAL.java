package RelatedMethods.db_access_layer;

import DataObjects.CloneGroupSample;
import RelatedMethods.DataObjects.Method;

import java.sql.*;
import java.util.*;

public class EvaluationDAL {

	private EvaluationDAL() {
		try {
		Class.forName("com.mysql.jdbc.Driver");
		this.connector = DriverManager.getConnection(RelatedMethods.CustomUtilities.Constants.DATABASE);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public static final EvaluationDAL SINGLETON = new EvaluationDAL();
	private static Connection connector;

	private static PreparedStatement APIUsagesSelection;
	private static PreparedStatement APIUsagesSelectionFromOtherMethods;
	private static PreparedStatement MethodSelection;
	private static PreparedStatement MethodSelectionByProject;
	private static PreparedStatement MethodSelectionByOtherProject;
	private static PreparedStatement getMethodsFromClusterID;
	private static PreparedStatement getMethodFromClusterID;
	private static PreparedStatement getMCSIDFromClusterID;
	private static PreparedStatement getProjectIDsMcMillan;
	private static PreparedStatement getContextBasedProjectIDsMcMillan;
	private static PreparedStatement getProjectCloneIDsMcMillan;
	private static PreparedStatement getProjectIDs;
	private static PreparedStatement getClusterIDs;
	private static PreparedStatement getFeatureIDs;
	private static PreparedStatement getClusterIDsfromFeatureID;
	private static PreparedStatement getClusterIDsfromFeatureID2;
	private static PreparedStatement getAllFeatureIDs;
	private static PreparedStatement getNonSingletonClusters;
	private static PreparedStatement getFiles;
	private static PreparedStatement getFirstFeatureMethod;
	private static PreparedStatement getClusterIDsFromOtherFiles;
	private static PreparedStatement getCID;
	private static PreparedStatement getClusterIDsFromAncestor;
	private static PreparedStatement getClusterIDsFromDescendant;
	private static PreparedStatement getClusterIDsFromSameFile;
	private static PreparedStatement ProjectIDSelection;
	private static PreparedStatement ProjectNameSelection;
	private static PreparedStatement MethodDetailsSelection;
	private static PreparedStatement getNumUniqueAPICalls;
	private static PreparedStatement ContextCloneIDsSelection;
	private static PreparedStatement getFile;
	private static PreparedStatement getSameFileClusterIDs;
	private static PreparedStatement ps_3;
	private static PreparedStatement getMethodFilePath;
	private static PreparedStatement getCalledMethods;
	private static PreparedStatement ps_getMethodRelPath;
	private static PreparedStatement getProjectIDsHavingFeature;
	private static PreparedStatement getProjectIDsHavingFeature2;
	private static PreparedStatement getProjectFilesWithMinFeatures;
	private static PreparedStatement getClustersOfFile;

	private static PreparedStatement getProjectIDsExceptOne;
	private static PreparedStatement getEvalProjectIDs;
	private static PreparedStatement getProjectIDsWithAPICall;

	public static boolean initializedFoldDBCon = false;


	public static EvaluationDAL getInstance() throws Exception {
		if(!initializedFoldDBCon)
			initializeFoldDatabaseConnector();
		return EvaluationDAL.SINGLETON;
	}

	public void initializeHeldoutDatabaseConnector() throws Exception {
		//if(this.connector==null || this.connector.isClosed()){
		{
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		this.connector = DriverManager
				.getConnection(RelatedMethods.CustomUtilities.Constants.HELDOUT_PROJECT_DATABASE);
		}
		this.APIUsagesSelection = this.connector
				.prepareStatement("select distinct * FROM ( SELECT  api_name, api_usage FROM api_call where api_call.api_name NOT in ('Log','Intent','Toast')) a   ");
		this.connector.setAutoCommit(false);
	}
	
	public static void initializeFoldDatabaseConnector() throws Exception {
		//if(this.connector==null || this.connector.isClosed()){
		{
			initializedFoldDBCon = true;
			System.out.println("I came here");
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connector = DriverManager
					.getConnection(RelatedMethods.CustomUtilities.Constants.DATABASE);
		}
		ContextCloneIDsSelection = connector.prepareStatement("");
		MethodSelection = connector
					.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? ");
		 MethodSelectionByProject = connector
					.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? and project_id = ?");
		 MethodSelectionByOtherProject = connector
					.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? and project_id != ?");
	              
		APIUsagesSelection = connector
				.prepareStatement("select distinct * FROM (SELECT api_name, api_usage FROM api_call where host_method_id = ? and api_call.api_name NOT in ('Log','Intent','Toast')) a ");
			
		APIUsagesSelectionFromOtherMethods = connector
		.prepareStatement("SELECT api_name, api_usage FROM api_call inner join file ON host_method_id=file.id where host_method_id != ? and file.project_id = ? and api_call.api_name NOT in ('Log','Intent','Toast')");
		getMethodsFromClusterID = connector.prepareStatement("SELECT method.id, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? ");
		getMethodFromClusterID = connector.prepareStatement("SELECT method.id, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? ");
		
		getProjectIDsMcMillan =  connector.prepareStatement("select id from project where id NOT IN ( SELECT project.id from method inner join file on file.id= method.file_id inner join project on file.project_id = project.id where method.id =?)");
		getProjectIDsExceptOne = connector.prepareStatement("SELECT project.id from project where project.id != ?");
		getContextBasedProjectIDsMcMillan =  connector.prepareStatement("select distinct (file.project_id) FROM `api_call`" +
				"inner join method on method.id=api_call.host_method_id " +
				"inner join file on file.id = method.file_id " +
				"where api_call_index_id in (SELECT api_call_index_id FROM `api_call` " +
				"where host_method_id = ?);");
		getProjectCloneIDsMcMillan = connector.prepareStatement("select distinct(cluster.clusterID) from cluster " +
				"inner join method on method.id=cluster.methodID " +
				" inner join file on file.id = method.file_id " +
				" where file.project_id = ?");
		getMCSIDFromClusterID = connector.prepareStatement("SELECT feature_id FROM related_features where cluster_id = ?");
		getProjectIDs = connector.prepareStatement("select id from project");
		getClusterIDs = connector.prepareStatement("SELECT file.project_id, method.id, cluster.clusterID from file INNER JOIN (cluster  INNER JOIN method )on (file.id = method.file_id and  method.id = cluster.methodID) where file.project_id=? ORDER BY cluster.clusterID ASC");
		getFeatureIDs = connector.prepareStatement("SELECT distinct(feature_id),support FROM related_features where cluster_id = ? and support >= ? order by support desc");
		getClusterIDsfromFeatureID = connector.prepareStatement("SELECT cluster_id FROM related_features where feature_id = ? and cluster_id != ?");
		getClusterIDsfromFeatureID2 = connector.prepareStatement("SELECT cluster_id FROM related_features where feature_id = ? order by support desc");
		
		getAllFeatureIDs = connector.prepareStatement("SELECT feature_id,support FROM related_features where cluster_id = ?  ");
		getNonSingletonClusters = connector.prepareStatement("select CID,size from (SELECT distinct(clusterID) as CID, count(clusterID) as size FROM `cluster` group by clusterID) as t where size>1");
		getFiles = connector.prepareStatement("select id from file where file.project_id = ?");
		getFirstFeatureMethod = connector.prepareStatement("select method.id from method inner join file on file.id=method.file_id where file.project_id=?");
		getClusterIDsFromOtherFiles = connector.prepareStatement("SELECT file.project_id, file.id as fileID, method.id as methodID, cluster.clusterID from file INNER JOIN (cluster  INNER JOIN method )on (file.id = method.file_id and  method.id = cluster.methodID) where file.project_id=? and file.id != ? ORDER BY cluster.clusterID ASC");
		getCID = connector.prepareStatement("select clusterID from cluster where cluster.methodID = ?");
		getClusterIDsFromAncestor = connector.prepareStatement("select calls.host_method_id, cluster.clusterID from calls inner join cluster on cluster.methodID = calls.host_method_id where calls.target_method_id=?");
		getClusterIDsFromDescendant = connector.prepareStatement("select calls.target_method_id, cluster.clusterID from calls inner join cluster on cluster.methodID = calls.target_method_id where calls.host_method_id=?");
		getClusterIDsFromSameFile = connector.prepareStatement("select cluster.clusterID from cluster inner join method on cluster.methodID = method.id where file_id IN (select file_id from method where method.id = ? and cluster.clusterID!=?)");
		ProjectIDSelection =  connector.prepareStatement("SELECT file.project_id from file inner join method on method.file_id=file.id where method.id =?");
        MethodDetailsSelection = connector.prepareStatement("SELECT method.name,method.from_line_num,method.to_line_num , file.file_name from method inner join file on file.id=method.file_id  where method.id = ? ");
		ProjectNameSelection = connector.prepareStatement("select project.name from project where id=?");
		
		getFile = connector.prepareStatement("SELECT method.file_id from method where id = ?") ;
		getSameFileClusterIDs = connector.prepareStatement("SELECT cluster.clusterID FROM method inner join cluster on cluster.methodID = method.id where file_id = ?") ;
		ps_3 = connector.prepareStatement(
				"SELECT method.id, name, from_line_num, to_line_num, file_name,file.project_id from method INNER JOIN file on file.id = method.file_id where method.id = ?");
	getMethodFilePath = connector.prepareStatement(
			"select file.relative_path from file inner join method on method.file_id= file.id where method.id = ?");
	getCalledMethods	= connector.prepareStatement(
			"select distinct calls.target_method_id from calls where calls.host_method_id = ? and calls.target_method_id!=0");
	ps_getMethodRelPath = connector.prepareStatement(
			"SELECT method.id, name, from_line_num, to_line_num, file.relative_path,file.project_id from method INNER JOIN file on file.id = method.file_id where method.id = ?");

	getProjectIDsHavingFeature = connector.prepareStatement("select distinct file.project_id from cluster " +
			"inner join method on cluster.methodID=method.id " +
			"INNER JOIN file on file.id=method.file_id " +
			"where cluster.clusterID = ?");

		getProjectIDsHavingFeature2 = connector.prepareStatement("select distinct file.project_id from cluster " +
				"inner join method on cluster.methodID=method.id " +
				"INNER JOIN file on file.id=method.file_id " +
				"where cluster.clusterID = ? and file.project_id!= ?");

		getEvalProjectIDs =  connector.prepareStatement("select file.project_id, count(file.id) as numfiles " +
		"from file " +
		"group by file.project_id " +
		"having numfiles > 10 ");

		getProjectIDsWithAPICall = connector.prepareStatement("SELECT distinct file.project_id " +
				"FROM file inner join method on method.file_id=file.id " +
				"inner join api_call on api_call.host_method_id=method.id " +
				"INNER JOIN api_call_index on api_call_index.id=api_call.api_call_index_id " +
				"where api_call_index.api_call like ? or ?");

//		getEvalProjectIDs = connector.prepareStatement("SELECT distinct project.id FROM `method` " +
//				"inner join cluster on cluster.methodID=method.id  " +
//				"inner join file on file.id=method.file_id " +
//				"inner join project on project.id=file.project_id " +
//				"group by file_id  " +
//				"having count(cluster.clusterID) > 5 " +
//				"ORDER BY `method`.`file_id` ASC");
	//connector.setAutoCommit(false);

		getProjectFilesWithMinFeatures = connector.prepareStatement("SELECT file.id, project.id FROM `method` " +
				"inner join cluster on cluster.methodID=method.id  " +
				"inner join file on file.id=method.file_id " +
				"inner join project on project.id=file.project_id " +
				"group by file_id   " +
				"having count(distinct(cluster.clusterID)) > 3 and project.id = ? " +
				"ORDER BY method.file_id ASC");

		getClustersOfFile = connector.prepareStatement("SELECT file.id, cluster.clusterID FROM `method` " +
				"inner join cluster on cluster.methodID=method.id  " +
				"inner join file on file.id=method.file_id " +
				"inner join project on project.id=file.project_id " +
				"where file.id = ?");
	}
	
	 public void closeConnector() throws SQLException{
	    	connector.close();
	 }
	 
	 public ArrayList<String> getAPIUsages() throws SQLException {
		 //selects distinct API usages in query
		 ArrayList<String> apiUsagesList = new ArrayList<String>();
		 ResultSet resultSet = APIUsagesSelection.executeQuery();
		 while(resultSet.next())
		 { 	
			 String name = resultSet.getString(1);			
			 String usage = resultSet.getString(2);
			 apiUsagesList.add(name + "." + usage);
		 }		
		 return apiUsagesList;
	 }
	 
	 public Set<String> getHashedAPIUsages() throws SQLException {
		 //selects distinct API usages in query
		 Set<String> apiUsagesList = new HashSet<>();
		 //ArrayList<String> apiUsagesList = new ArrayList<String>();
		 ResultSet resultSet = APIUsagesSelection.executeQuery();
		 while(resultSet.next())
		 { 	

			 String name = resultSet.getString(1);			
			 String usage = resultSet.getString(2);
			 apiUsagesList.add(name + "." + usage);
		 }		
		 
		 return apiUsagesList;
	 }
	 
	 public Method getFirstMethod(int clusterID) throws SQLException {
			Method m = new Method();
			
			MethodSelection.setInt(1,clusterID);
			ResultSet resultSet = MethodSelection.executeQuery();
			if(resultSet.first())
	    	{ 	
				
				m.id = resultSet.getInt(2);			
				m.name = resultSet.getString(3);
				m.from_line_num = resultSet.getInt(4);
				m.to_line_num = resultSet.getInt(5);
				m.file_name = resultSet.getString(6);
				m.projectID = resultSet.getInt(7);
				
				
	    	}		
			//MethodSelection.close();
			return m;
		}
	 
	 public Method getMethodFromProject(Integer clusterID, int projectID) throws SQLException {
			
			Method m = new Method();
			
			MethodSelectionByProject.setInt(1,clusterID);
			MethodSelectionByProject.setInt(2,projectID);
			ResultSet resultSet = MethodSelectionByProject.executeQuery();
			if(resultSet.first())
	    	{ 	
				
				m.id = resultSet.getInt(2);			
				m.name = resultSet.getString(3);
				m.from_line_num = resultSet.getInt(4);
				m.to_line_num = resultSet.getInt(5);
				m.file_name = resultSet.getString(6);
				m.projectID = resultSet.getInt(7);
				m.clusterID = clusterID;
				
				
				
	    	}		
			//MethodSelection.close();
			return m;
		}
	 
	 public ArrayList<String> getMethodAPICalls(Method m) throws SQLException {
			ArrayList<String> apiCallsList = new ArrayList<String>();
			
			APIUsagesSelection.setInt(1,m.id);
			ResultSet resultSet = APIUsagesSelection.executeQuery();
			while(resultSet.next())
	    	{ 	
				
				String name = resultSet.getString(1);			
				String usage = resultSet.getString(2);
				
				
				apiCallsList.add(name + "." + usage);
	    	}		
			//APICallsSelection.close();
			return apiCallsList;
		}
	 
	 public ArrayList<String> getMethodAPICalls(int methodID) throws SQLException {
			ArrayList<String> apiCallsList = new ArrayList<String>();
			
			APIUsagesSelection.setInt(1,methodID);
			ResultSet resultSet = APIUsagesSelection.executeQuery();
			while(resultSet.next())
	    	{ 	
				
				String name = resultSet.getString(1);			
				String usage = resultSet.getString(2);
				
				
				apiCallsList.add(name + "." + usage);
	    	}		
			//APICallsSelection.close();
			return apiCallsList;
		}


	 public ArrayList<String> getUniqueMethodAPICalls(int methodID) throws SQLException {
			ArrayList<String> apiCallsList = new ArrayList<String>();
			
			APIUsagesSelection.setInt(1,methodID);
			ResultSet resultSet = APIUsagesSelection.executeQuery();
			while(resultSet.next())
	    	{ 	
				
				String name = resultSet.getString(1);			
				String usage = resultSet.getString(2);
				
				if(!apiCallsList.contains(name + "." + usage))
					apiCallsList.add(name + "." + usage);
	    	}		
			//APICallsSelection.close();
			return apiCallsList;
		}
	 
	 public Set<String> getHashedMethodAPICalls(int methodID) throws SQLException {
			
			Set<String> apiCallsList = new HashSet<>();
			APIUsagesSelection.setInt(1,methodID);
			ResultSet resultSet = APIUsagesSelection.executeQuery();
			while(resultSet.next())
	    	{ 	
				
				String name = resultSet.getString(1);			
				String usage = resultSet.getString(2);
				
				if(!apiCallsList.contains(name + "." + usage))
					apiCallsList.add(name + "." + usage);
	    	}		
			//APICallsSelection.close();
			return apiCallsList;
		}
	 
	 public Set<String> getHashedMethodAPICallsFromOtherMethods(int methodID, int projectID) throws SQLException {
			
			Set<String> apiCallsList = new HashSet<>();
			APIUsagesSelectionFromOtherMethods.setInt(1,methodID);
			APIUsagesSelectionFromOtherMethods.setInt(2,projectID);
			ResultSet resultSet = APIUsagesSelectionFromOtherMethods.executeQuery();
			while(resultSet.next())
	    	{ 	
				
				String name = resultSet.getString(1);			
				String usage = resultSet.getString(2);
				
				if(!apiCallsList.contains(name + "." + usage))
					apiCallsList.add(name + "." + usage);
	    	}		
			//APICallsSelection.close();
			return apiCallsList;
		}
	 
	public ArrayList<Method> getMethodsByClusterID(int clusterID) throws SQLException {
		ArrayList<Method> methodsList = new ArrayList<Method>();
		
		
		getMethodsFromClusterID.setInt(1,clusterID);
		ResultSet resultSet = getMethodsFromClusterID.executeQuery();
		while(resultSet.next())
    	{ 	
			Method m = new Method();
			m.id = resultSet.getInt(1);			
			m.projectID = resultSet.getInt(2);
			methodsList.add(m);
			
    	}		
		//MethodSelection.close();
		return methodsList;
	}

	public HashMap<Integer, Set<Integer>> getProjectCloneIDs(
			int userFunctionID) throws Exception {
		
		HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		ArrayList<Integer> projectIDs = new ArrayList<Integer>();
		
		getProjectIDsMcMillan.setInt(1,userFunctionID);
		ResultSet resultSet = getProjectIDsMcMillan.executeQuery();
		while(resultSet.next())
    	{ 	
			projectIDs.add(resultSet.getInt(1));
			
    	}	
		
		for(int pID: projectIDs)
		{
			getProjectCloneIDsMcMillan.setInt(1,pID);
			ResultSet resultSet2 = getProjectCloneIDsMcMillan.executeQuery();
			Set<Integer> cloneIDs = new HashSet<Integer>();
			while(resultSet2.next())
	    	{ 	
				
				cloneIDs.add(resultSet2.getInt(1));
				
	    	}	
			projectCloneIDs.put(pID, cloneIDs);		
		}

		
		return projectCloneIDs;
	}



	//This method uses only the projects that have at least one of the featureIDs from the user context
	//to reduce the size of the hashmap of all possible projects
	public HashMap<Integer, Set<Integer>> getContextBasedProjectCloneIDs(
			ArrayList<Integer> contextProjectsMappings) throws Exception {

		HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		//ArrayList<Integer> projectIDs = new ArrayList<Integer>();

		//getContextBasedProjectIDsMcMillan.setInt(1,userFunctionID);
		//ResultSet resultSet = getContextBasedProjectIDsMcMillan.executeQuery();

		//while(resultSet.next())
		//{
		//	projectIDs.add(resultSet.getInt(1));

		//}


		for(int pID: contextProjectsMappings)
		{
			getProjectCloneIDsMcMillan.setInt(1,pID);
			ResultSet resultSet2 = getProjectCloneIDsMcMillan.executeQuery();
			Set<Integer> cloneIDs = new HashSet<Integer>();
			while(resultSet2.next())
			{

				cloneIDs.add(resultSet2.getInt(1));

			}
			//check if projects cloneIDs include a cloneID from the user context
			/*for(int fID: contextFeatureIDs) {
				if (cloneIDs.contains(fID)) {
					projectCloneIDs.put(pID, cloneIDs);
					break;
				}
			}*/
			projectCloneIDs.put(pID, cloneIDs);
		}


		return projectCloneIDs;
	}




	public HashMap<Integer, Set<Integer>> getProjectCloneIDs(
			ArrayList<Integer> projectIDs) throws Exception {
		
		HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
			
		for(int pID: projectIDs)
		{
			getProjectCloneIDsMcMillan.setInt(1,pID);
			ResultSet resultSet2 = getProjectCloneIDsMcMillan.executeQuery();
			Set<Integer> cloneIDs = new HashSet<Integer>();
			while(resultSet2.next())
	    	{ 	
				
				cloneIDs.add(resultSet2.getInt(1));
				
	    	}	
			projectCloneIDs.put(pID, cloneIDs);		
		}
		
			
		
		return projectCloneIDs;
	}
	
	public ArrayList<Integer> getProjectsCloneIDs(
			ArrayList<Integer> projectIDs) throws Exception {
		
		ArrayList<Integer> projectCloneIDs = new ArrayList<Integer>();
			
		for(int pID: projectIDs)
		{
			getProjectCloneIDsMcMillan.setInt(1,pID);
			ResultSet resultSet2 = getProjectCloneIDsMcMillan.executeQuery();
			
			while(resultSet2.next())
	    	{ 	
				
				projectCloneIDs.add(resultSet2.getInt(1));
				
	    	}	
				
		}
		
			
		
		return projectCloneIDs;
	}
	
	public HashMap<Integer, ArrayList<Integer>> getProjectsAndCloneIDs(
			ArrayList<Integer> projectIDs) throws Exception {
		
		HashMap<Integer, ArrayList<Integer>> projectCloneIDs = new HashMap<Integer, ArrayList<Integer>>();
			
		for(int pID: projectIDs)
		{
			getProjectCloneIDsMcMillan.setInt(1,pID);
			ResultSet resultSet2 = getProjectCloneIDsMcMillan.executeQuery();
			ArrayList<Integer> cloneIDs = new ArrayList<Integer>();
			while(resultSet2.next())
	    	{ 	
				cloneIDs.add(resultSet2.getInt(1));
				
				
	    	}	
			projectCloneIDs.put(pID,cloneIDs);
				
		}
		
			
		
		return projectCloneIDs;
	}
	
	

	public Integer getMethodByClusterID(int cID, Set<Integer> projectIDs) throws SQLException {

		int mid = 0;
		
		getMethodFromClusterID.setInt(1,cID);
		ResultSet resultSet = getMethodFromClusterID.executeQuery();
		while(resultSet.next())
    	{ 	
			Method m = new Method();
			m.id = resultSet.getInt(1);	
			m.projectID = resultSet.getInt(2);	
			if(projectIDs.contains(m.projectID))
				mid = m.id;
			
    	}
		return mid;		
		
	}

	public int getMCSID(int cID) throws SQLException {
		
		int mcsid = -1;
		
		getMCSIDFromClusterID.setInt(1,cID);
		ResultSet resultSet = getMCSIDFromClusterID.executeQuery();
		while(resultSet.next())
    	{ 	
			
			mcsid = resultSet.getInt(1);	
			break;
			
    	}
		return mcsid;		
	}

	public Method getMethodFromOtherProject(Integer clusterID, int projectID) throws SQLException {
		Method m = new Method();
		
		MethodSelectionByOtherProject.setInt(1,clusterID);
		MethodSelectionByOtherProject.setInt(2,projectID);
		ResultSet resultSet = MethodSelectionByOtherProject.executeQuery();
		if(resultSet.first())
    	{ 	
			
			m.id = resultSet.getInt(2);			
			m.name = resultSet.getString(3);
			m.from_line_num = resultSet.getInt(4);
			m.to_line_num = resultSet.getInt(5);
			m.file_name = resultSet.getString(6);
			m.projectID = resultSet.getInt(7);
			m.clusterID = clusterID;
			
			
			
    	}		
		//MethodSelection.close();
		return m;
	}

	public ArrayList<Integer> getProjectIDs() throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ResultSet resultSet = getProjectIDs.executeQuery();
		while(resultSet.next())
    	{ 	
			
			result.add(resultSet.getInt(1));	
			
			
    	}
		return result;	
	}

	public ArrayList<Integer> getClusterIDs(Integer pid) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getClusterIDs.setInt(1, pid);
		ResultSet resultSet = getClusterIDs.executeQuery();
		while(resultSet.next())
    	{
			int cid = resultSet.getInt(3);
			if(!result.contains(cid))
				result.add(cid);	
			
			
    	}
		return result;	
	}

	public Map<Integer, Integer> getFeatureIDs(Integer cid, Integer minSup) throws SQLException {
		Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
		getFeatureIDs.setInt(1, cid);
		getFeatureIDs.setInt(2, minSup);
		ResultSet resultSet = getFeatureIDs.executeQuery();
		while(resultSet.next())
    	{ 				
			result.put(resultSet.getInt(1),resultSet.getInt(2));	
			
    	}
		return result;	
	}

	public ArrayList<Integer> retrieveRelatedClusterIDs(Integer featureID, Integer cid) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getClusterIDsfromFeatureID.setInt(1, featureID);
		getClusterIDsfromFeatureID.setInt(2, cid);
		ResultSet resultSet = getClusterIDsfromFeatureID.executeQuery();
		while(resultSet.next())
    	{ 	
			int CID = resultSet.getInt(1);
			if(!result.contains(CID))
			result.add(CID);	
			
			
    	}
		return result;	
	}
	
	public ArrayList<Integer> retrieveRelatedClusterIDs2(Integer featureID) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getClusterIDsfromFeatureID2.setInt(1, featureID);
	
		ResultSet resultSet = getClusterIDsfromFeatureID2.executeQuery();
		while(resultSet.next())
    	{ 	
			int CID = resultSet.getInt(1);
			if(!result.contains(CID))
			result.add(CID);	
			
			
    	}
		return result;	
	}

	public Map<Integer, Integer> getFeatureIDs(Integer cid) throws SQLException {
		Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
		getAllFeatureIDs.setInt(1, cid);
		
		ResultSet resultSet = getAllFeatureIDs.executeQuery();
		while(resultSet.next())
    	{ 				
			result.put(resultSet.getInt(1),resultSet.getInt(2));	
			
    	}
		return result;	
	}

	public ArrayList<Integer> getNonSingletonClusters() throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		ResultSet resultSet = getNonSingletonClusters.executeQuery();
		while(resultSet.next())
    	{ 	
			
			result.add(resultSet.getInt(1));	
			
			
    	}
		return result;	
	}

	public ArrayList<Integer> getFiles(Integer pid) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getFiles.setInt(1, pid);
		ResultSet resultSet = getFiles.executeQuery();
		while(resultSet.next())
    	{ 	
			
			result.add(resultSet.getInt(1));	
			
			
    	}
		return result;	
	}

	public int getFirstFeatureMethod(Integer pid) throws SQLException {
		int methodID = 0;
		getFirstFeatureMethod.setInt(1, pid);
		ResultSet resultSet = getFirstFeatureMethod.executeQuery();
		while(resultSet.next())
    	{ 	
			
			methodID = resultSet.getInt(1);	
			break;
			
    	}
		return methodID;
	}

	public ArrayList<Integer> getClusterIDsFromOtherFiles(Integer pid,
			Integer fileID) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getClusterIDsFromOtherFiles.setInt(1, pid);
		getClusterIDsFromOtherFiles.setInt(2, fileID);
		ResultSet resultSet = getClusterIDsFromOtherFiles.executeQuery();
		while(resultSet.next())
    	{ 	
			
			result.add(resultSet.getInt(4));	
			
			
    	}
		return result;	
	}

	public int getCID(int mID) throws SQLException {
		int CID = 0;
		getCID.setInt(1, mID);
		ResultSet resultSet = getCID.executeQuery();
		while(resultSet.next())
    	{ 	
			
			CID = resultSet.getInt(1);	
			break;
			
    	}
		return CID;
	}

	public ArrayList<Integer> getNeighborhoodCIDs(int mID, int CID) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getClusterIDsFromAncestor.setInt(1, mID);
		getClusterIDsFromDescendant.setInt(1, mID);
		
		ResultSet resultSet1 = getClusterIDsFromAncestor.executeQuery();
		ResultSet resultSet2 = getClusterIDsFromDescendant.executeQuery();
		while(resultSet1.next())
    	{ 	
			
			result.add(resultSet1.getInt(2));	
			
			
    	}
		while(resultSet2.next())
		{		 	
			
			result.add(resultSet2.getInt(2));	
			
			
    	}
		//check if no call hierarchy based neighbors, then use the methods of that file
		/*if(result.size()==0)
		{
			getClusterIDsFromSameFile.setInt(1, mID);		
			getClusterIDsFromSameFile.setInt(2, CID);		
			ResultSet resultSet3 = getClusterIDsFromSameFile.executeQuery();
			while(resultSet3.next())
			{
				result.add(resultSet3.getInt(1));
			}
			System.out.println("Rec from same file neighborhood");
			
		}*/
		return result;	
	}
	
	public ArrayList<Integer> getUniqueNeighborhoodCIDs(int mID, int CID) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		getClusterIDsFromAncestor.setInt(1, mID);
		getClusterIDsFromDescendant.setInt(1, mID);
		
		ResultSet resultSet1 = getClusterIDsFromAncestor.executeQuery();
		ResultSet resultSet2 = getClusterIDsFromDescendant.executeQuery();
		while(resultSet1.next())
    	{ 	
			if(!result.contains(resultSet1.getInt(2)))
				result.add(resultSet1.getInt(2));	
			
			
    	}
		while(resultSet2.next())
		{		 	
			if(!result.contains(resultSet2.getInt(2)))
			result.add(resultSet2.getInt(2));	
			
			
    	}
		//check if no call hierarchy based neighbors, then use the methods of that file
		/*if(result.size()==0)
		{
			getClusterIDsFromSameFile.setInt(1, mID);		
			getClusterIDsFromSameFile.setInt(2, CID);		
			ResultSet resultSet3 = getClusterIDsFromSameFile.executeQuery();
			while(resultSet3.next())
			{
				result.add(resultSet3.getInt(1));
			}
			System.out.println("Rec from same file neighborhood");
			
		}*/
		return result;	
	}

	public ArrayList<Integer> getsameFielNeighborhoodCIDs(int mID, int CID) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		//check if no call hierarchy based neighbors, then use the methods of that file
		
			getClusterIDsFromSameFile.setInt(1, mID);		
			getClusterIDsFromSameFile.setInt(2, CID);		
			ResultSet resultSet3 = getClusterIDsFromSameFile.executeQuery();
			while(resultSet3.next())
			{
				result.add(resultSet3.getInt(1));
			}
			
		
		return result;	
	}

	public int getProjectID(int methodID) throws SQLException {
		int result= 0;
		ProjectIDSelection.setInt(1, methodID);
		ResultSet resultSet = ProjectIDSelection.executeQuery();
		while(resultSet.next())
		{
			result = resultSet.getInt(1);
		}
		return result;
	}

	public Method getMethodDetails(int mID) throws SQLException {
			Method m = new Method();
			
			MethodDetailsSelection.setInt(1,mID);
			ResultSet resultSet = MethodDetailsSelection.executeQuery();
			if(resultSet.first())
	    	{ 	
				m.id = mID;
				
				m.name = resultSet.getString(1);
				m.from_line_num = resultSet.getInt(2);
				m.to_line_num = resultSet.getInt(3);
				m.file_name = resultSet.getString(4);
			
				
				
				
				
	    	}		
			//MethodSelection.close();
			return m;
		}
	
	 public Method getMethodById(int methodID){
	    	Method method = new Method();
	    	try
	    	{    		
	    		PreparedStatement ps_getMethodByID_;
	    	
	    		ps_getMethodByID_ = ps_3;
	    		
	    
	    		ps_getMethodByID_.setInt(1, methodID);
	            ResultSet resultSet = ps_getMethodByID_.executeQuery();
	            
	            if(resultSet.next())
	            {              	
	                    
		            	method.id = methodID;
		            	//method.clusterID = resultSet.getInt(2);
		            	method.name = resultSet.getString(2);
		            	method.from_line_num = resultSet.getInt(3);
		            	method.to_line_num = resultSet.getInt(4);
		            	method.file_name = resultSet.getString(5);
		            	method.projectID = resultSet.getInt(6);
		            	
		            
		            
	            }            
	            resultSet.close();
	            //ps_getMethodByID_.close();
	            return method;
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    		return method;
	    	}
	    }
	 
	 public Method getMethodRelPath(int methodID){
	    	Method method = new Method();
	    	try
	    	{    		
	    		//PreparedStatement ps_getMethodRelPath;
	    		ps_getMethodRelPath.setInt(1, methodID);
	            ResultSet resultSet = ps_getMethodRelPath.executeQuery();
	            
	            if(resultSet.next())
	            {              	
	                    
		            	method.id = methodID;
		            	//method.clusterID = resultSet.getInt(2);
		            	method.name = resultSet.getString(2);
		            	method.from_line_num = resultSet.getInt(3);
		            	method.to_line_num = resultSet.getInt(4);
		            	method.file_name = resultSet.getString(5);
		            	method.projectID = resultSet.getInt(6);
		            	
		            
		            
	            }            
	            resultSet.close();
	            //ps_getMethodByID_.close();
	            return method;
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    		return method;
	    	}
	    }

	 public String getFilePathofMethod(int methodID){
	    	String filePath = "";
	    	try
	    	{    		
	    		//PreparedStatement ps_getMethodByID_;
	    
	    		getMethodFilePath.setInt(1, methodID);
	            ResultSet resultSet = getMethodFilePath.executeQuery();
	            
	            if(resultSet.next())
	            {              	
	            	filePath = resultSet.getString(1);
	            }            
	            resultSet.close();
	            //ps_getMethodByID_.close();
	            
	    	}
	    	catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    		
	    	}
	    	return filePath;
	    }

	public String getProjectName(int pid) throws SQLException {
		String result= "";
		ProjectNameSelection.setInt(1, pid);
		ResultSet resultSet = ProjectNameSelection.executeQuery();
		while(resultSet.next())
		{
			result = resultSet.getString(1);
		}
		return result;
	}

	public void initializeCloneGroupEvaluationConnector() throws Exception {
		
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		connector = DriverManager
				.getConnection(RelatedMethods.CustomUtilities.Constants.DATABASE);
		
		getNumUniqueAPICalls = connector
				.prepareStatement("select api_call.host_method_id as methodID, count(distinct(api_call.api_call_index_id)) as numUniqueAPICalls "+
						"FROM `api_call` inner join method on method.id = api_call.host_method_id " +
						"where api_call.api_call_index_id!=0 and api_call.host_method_id=?");
		getMethodsFromClusterID = connector.prepareStatement("SELECT method.id, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? ");
		getNonSingletonClusters = connector.prepareStatement("select CID,size from (SELECT distinct(clusterID) as CID, count(clusterID) as size FROM `cluster` group by clusterID) as t where size>1");
		
connector.setAutoCommit(false);
	}

	public ArrayList<CloneGroupSample> getClusterDiversity() throws SQLException {
		//an arraylist of clone group samples containing CID and diversity
		ArrayList<CloneGroupSample> list = new ArrayList<CloneGroupSample>();
		
		//get CIDs
		
		ResultSet resultSet = getNonSingletonClusters.executeQuery();
		while(resultSet.next())
    	{ 	
			CloneGroupSample rowsample = new CloneGroupSample();
			rowsample.CID = resultSet.getInt(1);	
			rowsample.size = resultSet.getInt(2);	
			//for each CID get its methods
			getMethodsFromClusterID.setInt(1,rowsample.CID);
			ResultSet methods = getMethodsFromClusterID.executeQuery();
			int max = 0;
			int min = 0;
			while(methods.next())
	    	{//for each method get its num of unique api calls
				
				getNumUniqueAPICalls.setInt(1,methods.getInt(1));
				ResultSet numcalls = getNumUniqueAPICalls.executeQuery();
				int num = 0;
				if(numcalls.first())
				{
					num = numcalls.getInt(2);
					
				
				}
				if(max<num)
					max = num;
				if(min>num || min==0)
					min = num;
			
				
				
	    	}
			rowsample.diversity = max-min;
			list.add(rowsample);
			
    	}
		
		
		return list;
	}

	public HashSet<Integer> getContextCloneIDs(int userFunctionID, int pid) throws SQLException {
		HashSet<Integer> contextCloneIDsList = new HashSet<>();
		getClusterIDs.setInt(1,pid);
		
		ResultSet resultSet = getClusterIDs.executeQuery();
		while(resultSet.next())
    	{ 	
			
			int cloneID = resultSet.getInt(3);			
			contextCloneIDsList.add(cloneID);
    	}		
		//APICallsSelection.close();
		return contextCloneIDsList;
	}

	public ArrayList<Integer> getFileCIDs(int mID) throws SQLException {
		ArrayList<Integer> sameFileCloneIDsList = new ArrayList<>();
		getFile.setInt(1, mID);
		
		
		ResultSet resultSet = getFile.executeQuery();
		int fileID = 0;
		while(resultSet.next())
    	{ 	
			
			fileID = resultSet.getInt(1);			
			
    	}	
		getSameFileClusterIDs.setInt(1, fileID);
		ResultSet resultSet2 = getSameFileClusterIDs.executeQuery();
		while(resultSet2.next())
    	{ 	
			
			int cloneID = resultSet2.getInt(1);			
			sameFileCloneIDsList.add(cloneID);
    	}		
		//APICallsSelection.close();
		return sameFileCloneIDsList;
	}

	public ArrayList<Integer> getCalledMethods(int mID) throws SQLException {
		ArrayList<Integer> calledMethodsList = new ArrayList<>();
		getCalledMethods.setInt(1, mID);
		
		
		ResultSet resultSet = getCalledMethods.executeQuery();
		
		while(resultSet.next())
    	{ 	
			
			calledMethodsList.add(resultSet.getInt(1));			
			
    	}	
		return calledMethodsList;
	}


	public ArrayList<Integer> getProjectsContainingFeature(int featureID, int pid) throws SQLException {

		ArrayList<Integer> results = new ArrayList<>();

		getProjectIDsHavingFeature2.setInt(1, featureID);
		getProjectIDsHavingFeature2.setInt(2, pid);


		ResultSet resultSet = getProjectIDsHavingFeature2.executeQuery();

		while(resultSet.next())
		{

			results.add(resultSet.getInt(1));

		}
		return results;
	}

	public ArrayList<Integer> getFileFeatures(int pid) throws SQLException{
		ArrayList<Integer> results = new ArrayList<>();
		getProjectFilesWithMinFeatures.setInt(1, pid);
		ResultSet resultSet = getProjectFilesWithMinFeatures.executeQuery();
		int fileID = 0;
		if(resultSet.first()) {
			//get the first file
			fileID = (resultSet.getInt(1));


			getClustersOfFile.setInt(1, fileID);
			resultSet = getClustersOfFile.executeQuery();
			while (resultSet.next()) {
				//get the clusterID
				results.add(resultSet.getInt(2));

			}
		}
		return results;
	}

	public HashMap<Integer, ArrayList<Integer>> getFileFeaturesHashMap(int pid) throws SQLException{
		HashMap<Integer, ArrayList<Integer>> results = new HashMap<Integer, ArrayList<Integer>>();
		getProjectFilesWithMinFeatures.setInt(1, pid);
		ResultSet resultSet = getProjectFilesWithMinFeatures.executeQuery();
		int fileID = 0;
		while(resultSet.next()) {
			//get the first file
			fileID = (resultSet.getInt(1));


			getClustersOfFile.setInt(1, fileID);
			resultSet = getClustersOfFile.executeQuery();

			ArrayList<Integer> featureIDs = new ArrayList<>();
			while (resultSet.next()) {
				//get the clusterID
				featureIDs.add(resultSet.getInt(2));

			}
			results.put(fileID, featureIDs );
		}
		return results;
	}

	public ArrayList<Integer> getProjectsContainingFeature(int featureID) throws SQLException {
		ArrayList<Integer> results = new ArrayList<>();

		getProjectIDsHavingFeature.setInt(1, featureID);


		ResultSet resultSet = getProjectIDsHavingFeature.executeQuery();

		while(resultSet.next())
		{

			results.add(resultSet.getInt(1));

		}
		return results;
	}

	public ArrayList<Integer> getEvalProjects() throws SQLException {
		ArrayList<Integer> results = new ArrayList<>();




		ResultSet resultSet = getEvalProjectIDs.executeQuery();

		while(resultSet.next())
		{

			results.add(resultSet.getInt(1));

		}
		return results;
	}

	public HashMap<Integer, Set<Integer>> getProjectCloneIDsMinusOne(int pid) throws SQLException {
		HashMap<Integer, Set<Integer>> projectCloneIDs = new HashMap<Integer, Set<Integer>>();
		ArrayList<Integer> projectIDs = new ArrayList<Integer>();

		getProjectIDsExceptOne.setInt(1,pid);
		ResultSet resultSet = getProjectIDsExceptOne.executeQuery();
		while(resultSet.next())
		{
			projectIDs.add(resultSet.getInt(1));

		}

		for(int pID: projectIDs)
		{
			getProjectCloneIDsMcMillan.setInt(1,pID);
			ResultSet resultSet2 = getProjectCloneIDsMcMillan.executeQuery();
			Set<Integer> cloneIDs = new HashSet<Integer>();
			while(resultSet2.next())
			{

				cloneIDs.add(resultSet2.getInt(1));

			}
			projectCloneIDs.put(pID, cloneIDs);
		}


		return projectCloneIDs;
	}

    public HashMap<Integer, Double> getProjectsContainingAPICall(String s, HashMap<Integer, Double>  projectIDScores) throws SQLException {
		//ArrayList<Integer> projectIDs = new ArrayList<Integer>();
		//HashMap<Integer, Integer> projectIDScores = new HashMap<Integer, Integer>();

		String apiname = s.substring(0,s.indexOf("."))+"%";
		String apiusage = "%" + s.substring(s.indexOf("."));
		getProjectIDsWithAPICall.setString(1,apiname);
		getProjectIDsWithAPICall.setString(2,apiusage);
		ResultSet resultSet = getProjectIDsWithAPICall.executeQuery();
		while(resultSet.next())
		{
			int pid = resultSet.getInt(1);
			if(projectIDScores.containsKey(pid))
			{
				double oldvalue = projectIDScores.get(pid);
				projectIDScores.replace(pid,oldvalue+1);
			}
			else

			projectIDScores.put(pid, 1.0);

		}

		return projectIDScores;
    }
}
