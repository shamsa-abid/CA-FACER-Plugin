package RelatedMethods.db_access_layer;

import RelatedMethods.DataObjects.Method;
import RelatedMethods.CustomUtilities.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;





public class DatabaseAccessLayer {
	
	public static final DatabaseAccessLayer SINGLETON = new DatabaseAccessLayer();
	private Connection connector;
	
	public boolean relatedFeaturesInitialized = false;
	

	private PreparedStatement ClusterIDSelection;
	private PreparedStatement RelatedFeaturesInsertion;
	private PreparedStatement FeaturesSupportInsertion;
	private PreparedStatement MethodSelection;
	private PreparedStatement MethodSelectionRelPath;
	private PreparedStatement MethodData;
	private PreparedStatement MethodSelectionByProject;
	private PreparedStatement MethodSelectionByProjectRelPath;
	private PreparedStatement MethodSelectionByOtherProject;
	private PreparedStatement APICallsSelection;
	private PreparedStatement UniqueAPICallsSelection;
	private PreparedStatement RelatedFeaturesSelection;
	private PreparedStatement ClustersBelongingToFeaturesSelection;
	private PreparedStatement ClusterIDAgainstMethod;
	private PreparedStatement  SameFileMethodsSelection;
	private PreparedStatement SameProjectMethodsSelection;
	private PreparedStatement UniqueAPICallCount;
	//private PreparedStatement  SameFileMethodsSelection;
	private PreparedStatement DensitySelection;
	private PreparedStatement AllMethodSelection;
	private PreparedStatement ClusterInsertion;
	private PreparedStatement MethodSelectionMoreThan3APICalls;
	private PreparedStatement ProjectIDSelection;
	private PreparedStatement HighDensityMethodSelection;
	private PreparedStatement HDMIDSelection;
	private PreparedStatement LDMIDSelection;
	private PreparedStatement MethodIDClusterIDSelection;

	
	public static DatabaseAccessLayer getInstance() { 
	        return DatabaseAccessLayer.SINGLETON;
	} 
	
    public void initializeConnector() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);
        this.ClusterIDSelection = this.connector
				.prepareStatement("SELECT file.project_id, method.id, cluster.clusterID from file  INNER JOIN (cluster  INNER JOIN method )on (file.id = method.file_id and  method.id = cluster.methodID) ORDER BY file.project_id,cluster.clusterID ASC ");
        this.APICallsSelection = this.connector
				.prepareStatement("SELECT file.project_id, api_call.api_call_index_id FROM api_call inner join method on api_call.host_method_id = method.id inner join file on method.file_id = file.id where api_call.api_name NOT in ('Log','Intent','Toast') ");
        
        this.MethodSelectionMoreThan3APICalls = this.connector.prepareStatement("SELECT distinct(host_method_id),count(api_call.api_call_index_id) as count1 "+
        						"FROM `api_call` group by host_method_id having count1>=3");
		this.connector.setAutoCommit(false);
    }
    public void initializeConnectorForDensity() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);
        this.AllMethodSelection = this.connector.prepareStatement("select distinct(api_call.host_method_id) as methodID,method.api_calls_density from api_call "+
"inner join method on method.id=api_call.host_method_id "+
"where api_call.api_call_index_id!=0 and api_call.host_method_id in(select api_call.host_method_id as MID "+
"from api_call GROUP by api_call.host_method_id HAVING count(distinct(api_call_index_id))>=3)"
);
        this.DensitySelection = this.connector
				.prepareStatement("SELECT method.api_calls_density from method where method.id = ? ");
        this.ClusterInsertion = this.connector.prepareStatement("INSERT INTO cluster VALUES(0,?,0,?)");  

        this.connector.setAutoCommit(false);
    }
    
    public void initializeConnectorForMCSMining(String DB) throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(DB);
        this.AllMethodSelection = this.connector.prepareStatement("select distinct(api_call.host_method_id) as methodID,method.api_calls_density from api_call "+
"inner join method on method.id=api_call.host_method_id "+
"where api_call.api_call_index_id!=0 and api_call.host_method_id in(select api_call.host_method_id as MID "+
"from api_call GROUP by api_call.host_method_id HAVING count(distinct(api_call_index_id))>=3)"
);
        this.ClusterInsertion = this.connector.prepareStatement("INSERT INTO cluster VALUES(0,?,0,?)");  
        this.ClusterIDSelection = this.connector
				.prepareStatement("SELECT file.project_id, method.id, cluster.clusterID from file  INNER JOIN (cluster  INNER JOIN method )on (file.id = method.file_id and  method.id = cluster.methodID) ORDER BY file.project_id,cluster.clusterID ASC ");
        this.RelatedFeaturesInsertion = this.connector
				.prepareStatement("INSERT INTO related_features VALUES (0,?,?,?)");
        this.FeaturesSupportInsertion = this.connector
				.prepareStatement("INSERT INTO feature_support VALUES (?,?)");
        this.MethodSelectionMoreThan3APICalls = this.connector.prepareStatement("SELECT distinct(host_method_id),count(api_call.api_call_index_id) as count1 "+
				"FROM `api_call` group by host_method_id having count1>=3");
		this.MethodIDClusterIDSelection = this.connector.prepareStatement("SELECT SUBSTRING_INDEX(SUBSTRING(file.file_name,?,10),'.',1) as methodID,cluster.clusterID FROM cluster "+
				"inner join method on method.id=cluster.methodID "+
				"inner join file on method.file_id=file.id  "+
				"ORDER BY cluster.clusterID ASC");
        this.connector.setAutoCommit(false);
    }
   
    
    public void initializeConnectorToPopulateRelatedFeatures() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

        this.RelatedFeaturesInsertion = this.connector
				.prepareStatement("INSERT INTO related_features VALUES (0,?,?,?)");
        this.FeaturesSupportInsertion = this.connector
				.prepareStatement("INSERT INTO feature_support VALUES (?,?)");
        this.connector.setAutoCommit(false);
    }
    
   
    public void closeConnector() throws SQLException{
    	this.connector.close();
    }
 
	
	public ArrayList<String> getClusterIDsPerProject() throws SQLException {
		ResultSet resultSet1 = MethodSelectionMoreThan3APICalls.executeQuery();
		//ArrayList<Integer> methodIDsWithAPICalls3 = new ArrayList<Integer>();
		//while(resultSet1.next())
		{
			//methodIDsWithAPICalls3.add(resultSet1.getInt(1));
		}
		ArrayList<String> clusterIDList = new ArrayList<String>();	
		ResultSet resultSet = ClusterIDSelection.executeQuery();
		int previousProjectID = -1;
		String s = "";
		ArrayList<Integer> clusterIDsForProject = new ArrayList<Integer>();
		while(resultSet.next())
		{
			int projectID = resultSet.getInt(1);
			int methodID = resultSet.getInt(2);
			int clusterID = resultSet.getInt(3);			
			
			if(projectID == previousProjectID || previousProjectID == -1)
			{
				if(!clusterIDsForProject.contains(clusterID))
				{
					//if(methodIDsWithAPICalls3.contains(methodID))
					{
						clusterIDsForProject.add(clusterID);
					}
				}
				//s = s.concat(clusterID + " ");
			}			
			else
			{		
				String clusterTransaction = createString(clusterIDsForProject);
				clusterIDList.add(clusterTransaction);
				clusterIDsForProject.clear();
				//if(methodIDsWithAPICalls3.contains(methodID))
				{
				clusterIDsForProject.add(clusterID);
				}
				//s = "";
				//s = s.concat(clusterID + " ");
				
			}
			
			previousProjectID = projectID;
		}
		
		
		String clusterTransaction = createString(clusterIDsForProject);
		clusterIDList.add(clusterTransaction);
		ClusterIDSelection.close();
		return clusterIDList;
	}
	private String createString(ArrayList<Integer> clusterIDsForProject) {
		String result = "";
		for(int i:clusterIDsForProject)
		{
			result = result.concat(i + " ");
		}
		
		return result.trim();
	}
	public void populateRelatedFeaturesTable(ArrayList<String> relatedFeaturesList, int minDepth, HashMap<Integer, Integer> featuresSupport) throws NumberFormatException, SQLException {
		// Getting an iterator 
        Iterator hmIterator = featuresSupport.entrySet().iterator(); 
  
        
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
  
           
        } 
        
        
		int featureID = 0;
		int prevFeatureID = 0;
		for(String relatedClusterIDs: relatedFeaturesList)
		{

			featureID += 1;

			String[] clusterIDs = relatedClusterIDs.split("\\s+");
			//if(!(clusterIDs.length < 2)&&!(clusterIDs.length > 6))
			if(!(clusterIDs.length < 2))
			{
				//featureID += 1;
				for(int i = 0; i < clusterIDs.length; i++)
				{
					RelatedFeaturesInsertion.setInt(1, featureID);   
					RelatedFeaturesInsertion.setInt(2, Integer.parseInt(clusterIDs[i]));  	  
					RelatedFeaturesInsertion.setInt(3, featuresSupport.get(featureID)); 
					RelatedFeaturesInsertion.addBatch();
					if(featureID!=prevFeatureID)
					{
						FeaturesSupportInsertion.setInt(1, featureID );   
						FeaturesSupportInsertion.setInt(2, featuresSupport.get(featureID)); 
						FeaturesSupportInsertion.addBatch();					
						prevFeatureID=featureID;
					}
				}
				
			}
			
		}
		
		int[] inserted = RelatedFeaturesInsertion.executeBatch();
		RelatedFeaturesInsertion.close();
		FeaturesSupportInsertion.executeBatch();
		FeaturesSupportInsertion.close();
		connector.commit();
		
	}
	public void initializeConnectorToDisplayMethodBodies() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

        this.MethodSelection = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? order by method.api_calls_density DESC");
		this.MethodSelectionRelPath = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.relative_path, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? order by method.api_calls_density DESC");

		this.HighDensityMethodSelection = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? ");
        this.HDMIDSelection = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, max(api_calls_density) FROM `method` inner join cluster on cluster.methodID=method.id group by cluster.clusterID having cluster.clusterID = ?");
        this.LDMIDSelection = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, min(api_calls_density) FROM `method` inner join cluster on cluster.methodID=method.id group by cluster.clusterID having cluster.clusterID = ?");
        
        this.MethodData= this.connector
				.prepareStatement("SELECT method.id, name, from_line_num, to_line_num, file_name FROM `method` inner join file on file.id = method.file_id where method.id = ?  ");
        
        this.MethodSelectionByProject = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? and project_id = ? order by method.api_calls_density DESC");
		this.MethodSelectionByProjectRelPath = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.relative_path, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? and project_id = ? order by method.api_calls_density DESC");

		this.MethodSelectionByOtherProject = this.connector
				.prepareStatement("SELECT cluster.clusterID, method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM `cluster` inner join method on method.id = cluster.methodID inner join file on file.id=method.file_id where clusterID = ? and project_id != ?");
        
        this.APICallsSelection = this.connector
				.prepareStatement("SELECT api_name, api_usage FROM api_call where host_method_id = ?");
		this.UniqueAPICallsSelection = this.connector
				.prepareStatement("SELECT DISTINCT(CONCAT(api_name , '.', api_usage )) FROM api_call where host_method_id = ? and  api_call.api_name NOT in ('Log','Intent','Toast') ");
		this.UniqueAPICallCount = this.connector
				.prepareStatement("select method.id, count(distinct(concat(api_call.api_name,'.',api_call.api_usage))) from api_call inner join method on method.id = api_call.host_method_id where method.id = ? and api_call.api_name NOT in ('Log','Intent','Toast') ");
		
        this.connector.setAutoCommit(false);
		
	}
	
	
	public int getMethodUniqueAPICount(int mID) throws SQLException {
		int APICallCount = 0;
		
		UniqueAPICallCount.setInt(1,mID);
		ResultSet resultSet = UniqueAPICallCount.executeQuery();
		while(resultSet.next())
    	{ 	
			
			APICallCount = resultSet.getInt(2);		
			
			
    	}		
		//UniqueAPICallCount.close();
		return APICallCount;
	}
	
	public ArrayList<Method> getMethods(int clusterID) throws SQLException {
		ArrayList<Method> methodIDsList = new ArrayList<Method>();
		
		MethodSelection.setInt(1,clusterID);
		ResultSet resultSet = MethodSelection.executeQuery();
		while(resultSet.next())
    	{ 	
			Method m = new Method();
			m.id = resultSet.getInt(2);			
			m.name = resultSet.getString(3);
			m.from_line_num = resultSet.getInt(4);
			m.to_line_num = resultSet.getInt(5);
			m.file_name = resultSet.getString(6);
			m.projectID = resultSet.getInt(7);
			
			methodIDsList.add(m);
    	}		
		MethodSelection.close();
		return methodIDsList;
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
			m.clusterID = clusterID;
			
			
			
    	}		
		//MethodSelection.close();
		return m;
	}


	public ArrayList<String> getMethodAPICalls(Method m) throws SQLException {
		ArrayList<String> apiCallsList = new ArrayList<String>();
		
		this.UniqueAPICallsSelection.setInt(1,m.id);
		ResultSet resultSet = UniqueAPICallsSelection.executeQuery();
		while(resultSet.next())
    	{ 	
			
			String call = resultSet.getString(1);			
			//String usage = resultSet.getString(2);
			
			
			apiCallsList.add(call);
    	}		
		//APICallsSelection.close();
		return apiCallsList;
	}
	
	public ArrayList<String> getMethodAPICalls(int mid) throws SQLException {
		ArrayList<String> apiCallsList = new ArrayList<String>();
		
		this.UniqueAPICallsSelection.setInt(1,mid);
		ResultSet resultSet = UniqueAPICallsSelection.executeQuery();
		while(resultSet.next())
    	{ 	
			
			String call = resultSet.getString(1);			
			//String usage = resultSet.getString(2);
			
			
			apiCallsList.add(call);
    	}		
		//APICallsSelection.close();
		return apiCallsList;
	}
	
	public ArrayList<String> getAPICallsPerProject() throws SQLException {
		ArrayList<String> apiCallIndexIDList = new ArrayList<String>();	
		ResultSet resultSet = APICallsSelection.executeQuery();
		int previousProjectID = -1;
		String s = "";
		ArrayList<Integer> apiCallIndexIDsForProject = new ArrayList<Integer>();
		while(resultSet.next())
		{
			int projectID = resultSet.getInt(1);
			//int methodID = resultSet.getInt(2);
			int apiIndexID = resultSet.getInt(2);			
			
			if(projectID == previousProjectID || previousProjectID == -1)
			{
				if(!apiCallIndexIDsForProject.contains(apiIndexID))
				{
					apiCallIndexIDsForProject.add(apiIndexID);
				}
				//s = s.concat(clusterID + " ");
			}			
			else
			{		
				String apiCallTransaction = createString(apiCallIndexIDsForProject);
				apiCallIndexIDList.add(apiCallTransaction);
				apiCallIndexIDsForProject.clear();
				apiCallIndexIDsForProject.add(apiIndexID);
				//s = "";
				//s = s.concat(clusterID + " ");
				
			}
			previousProjectID = projectID;
		}
		String clusterTransaction = createString(apiCallIndexIDsForProject);
		apiCallIndexIDList.add(clusterTransaction);
		APICallsSelection.close();
		return apiCallIndexIDList;
	}
	public void initializeConnectorToRetrieveRelatedFeatures() throws ClassNotFoundException, SQLException {
		System.out.println("Im HERE ====================");
		relatedFeaturesInitialized = true;
		Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);
       
        this.SameFileMethodsSelection =  this.connector
				.prepareStatement("select id from method where file_id IN (select file_id from method where method.id = ?)");
        this.SameProjectMethodsSelection = this.connector
				.prepareStatement("SELECT method.id, method.name,method.from_line_num, method.to_line_num, file.file_name, file.project_id FROM method inner join file on file.id=method.file_id where project_id = ?");
        

        this.RelatedFeaturesSelection = this.connector
				.prepareStatement("SELECT id, feature_id FROM related_features where cluster_id = ? ");
        this.ClusterIDAgainstMethod = this.connector
				.prepareStatement("SELECT clusterID FROM cluster where methodID = ? ");
        this.ClustersBelongingToFeaturesSelection = this.connector
				.prepareStatement("SELECT id, feature_id, cluster_id FROM related_features where feature_id = ? and cluster_id != ?");
        this.ProjectIDSelection = this.connector.prepareStatement("SELECT file.project_id from file inner join method on method.file_id=file.id where method.id =?");
        
		this.connector.setAutoCommit(false);
		
	}
	public ArrayList<Integer> getFeatureIDs(int clusterID) throws SQLException {
		
		ArrayList<Integer> featureIDsList = new ArrayList<Integer>();
		
		this.RelatedFeaturesSelection.setInt(1,clusterID);
		ResultSet resultSet = RelatedFeaturesSelection.executeQuery();
		while(resultSet.next())
    	{ 		
			int fID = resultSet.getInt(2);
			featureIDsList.add(fID);
    	}		
		
		return featureIDsList;
	}
	public ArrayList<Integer> getclusterIDs(int clusterID, Integer fID) throws SQLException {
		
		ArrayList<Integer> clusterIDsList = new ArrayList<Integer>();
		
		this.ClustersBelongingToFeaturesSelection.setInt(1,fID);
		this.ClustersBelongingToFeaturesSelection.setInt(2,clusterID);
		ResultSet resultSet = ClustersBelongingToFeaturesSelection.executeQuery();
		while(resultSet.next())
    	{ 		
			int cID = resultSet.getInt(3);
			if(!clusterIDsList.contains(cID))
				clusterIDsList.add(cID);
    	}		
		
		return clusterIDsList;
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

	public int getClusterID(int mID) throws SQLException {
		
		int CID = -1;
		this.ClusterIDAgainstMethod.setInt(1, mID);
		ResultSet resultSet = ClusterIDAgainstMethod.executeQuery();
		while(resultSet.next())
    	{ 		
			CID = resultSet.getInt(1);
			
    	}		
		
		return CID;
	}

	public ArrayList<Integer> getSameFileMethods(int methodID) throws SQLException {
		
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();		
		this.SameFileMethodsSelection.setInt(1,methodID);
		ResultSet resultSet = SameFileMethodsSelection.executeQuery();
		while(resultSet.next())
    	{ 		
			int mID = resultSet.getInt(1);
			methodIDsList.add(mID);
    	}		
		
		return methodIDsList;
	}
	
	public ArrayList<Integer> getSameProjectMethods(int projectID) throws SQLException {
		
		ArrayList<Integer> methodIDsList = new ArrayList<Integer>();		
		this.SameProjectMethodsSelection.setInt(1,projectID);
		ResultSet resultSet = SameProjectMethodsSelection.executeQuery();
		while(resultSet.next())
    	{ 		
			int mID = resultSet.getInt(1);
			methodIDsList.add(mID);
    	}		
		
		return methodIDsList;
	}

	public Method getMethod(Integer methodID) throws SQLException {
	
			Method m = new Method();
			
			MethodData.setInt(1,methodID);
			ResultSet resultSet = MethodData.executeQuery();
			if(resultSet.first())
	    	{ 	
				
				m.id = resultSet.getInt(1);			
				m.name = resultSet.getString(2);
				m.from_line_num = resultSet.getInt(3);
				m.to_line_num = resultSet.getInt(4);
				m.file_name = resultSet.getString(5);
				
	    	}		
	
			return m;
	}

	public float getDensity(int mID) throws SQLException {
		float result = 0.0f;
		DensitySelection.setInt(1, mID);
		ResultSet resultSet = DensitySelection.executeQuery();
		if(resultSet.first())
    	{ 	
    	 result = resultSet.getFloat(1);
    	}
		return result;
	}

	public ArrayList<String> getMethods() throws SQLException {
		ArrayList<String> result= new ArrayList<String>();
		
		ResultSet resultSet = AllMethodSelection.executeQuery();
		while(resultSet.next())
		{
			result.add(resultSet.getInt(1)+","+resultSet.getFloat(2));
		}
		return result;
	}

	public ArrayList<Integer> getMethodIDs() throws SQLException {
		ArrayList<Integer> result= new ArrayList<Integer>();
		
		ResultSet resultSet = AllMethodSelection.executeQuery();
		while(resultSet.next())
		{
			result.add(resultSet.getInt(1));
		}
		return result;
	}



	public void insertClusterID(File file, ArrayList<Integer> methods) throws NumberFormatException, IOException, SQLException {
		
			
		BufferedReader br = new BufferedReader(new FileReader(file));
		int counter = 0;
		String st;
		while ((st = br.readLine()) != null)
		{
			if(counter==0)
			{
				counter++;
				continue;
			}
			String[] line = st.split(",");
			String num = line[0].replace("\"", "");
			String CID = line[1].replace("\"", "");
			int MID = methods.get(Integer.parseInt(num)-1);
			int ClusterID = Integer.parseInt(CID);
			//save in cluster table
			
			ClusterInsertion.setInt(1, ClusterID);   
			ClusterInsertion.setInt(2, MID);    
		
			ClusterInsertion.addBatch();  
			
		}
		int[] inserted = ClusterInsertion.executeBatch();
		connector.commit();
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

	public int getHighDensityMethod(int clusterID) throws SQLException {
		int result= 0;
		HDMIDSelection.setInt(1, clusterID);
		ResultSet resultSet = HDMIDSelection.executeQuery();
		while(resultSet.next())
		{
			result = resultSet.getInt(2);
		}
		return result;
	}

	public int getLowDensityMethod(int clusterID) throws SQLException {
		int result= 0;
		LDMIDSelection.setInt(1, clusterID);
		ResultSet resultSet = LDMIDSelection.executeQuery();
		while(resultSet.next())
		{
			result = resultSet.getInt(2);
		}
		return result;
	}


	public Method getMethodFromProjectRelativeFilePath(int clusterID, int projectID) throws SQLException {

		Method m = new Method();

		MethodSelectionByProjectRelPath.setInt(1,clusterID);
		MethodSelectionByProjectRelPath.setInt(2,projectID);
		ResultSet resultSet = MethodSelectionByProjectRelPath.executeQuery();
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

	public Method getFirstMethodRelativePath(int clusterID)  throws SQLException {
		Method m = new Method();

		MethodSelectionRelPath.setInt(1,clusterID);
		ResultSet resultSet = MethodSelectionRelPath.executeQuery();
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

	public ArrayList<String> getClusteringResultsForPythonScript(int length) throws SQLException {
		ArrayList<String> result= new ArrayList<String>();
		MethodIDClusterIDSelection.setInt(1, length);
		ResultSet resultSet = MethodIDClusterIDSelection.executeQuery();
		result.add("methodID,clusterID");
		while(resultSet.next())
		{
			result.add(resultSet.getInt(1)+","+resultSet.getInt(2));
		}
		return result;
	}
}
