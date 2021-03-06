package clustering;

import support.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

public class DatabaseAccessLayer {
	
	public static final DatabaseAccessLayer SINGLETON = new DatabaseAccessLayer();
	private Connection connector;
	
	private PreparedStatement APICallSelection;
	private PreparedStatement APIDataSelection;
	private PreparedStatement APICallIndexSelection;
	private PreparedStatement APICallIndexInsertion;
	private PreparedStatement APICallUpdation;
	private PreparedStatement SequenceInsertion;
	private PreparedStatement SequenceSelection1;
	private PreparedStatement SequenceSelection2;
	private PreparedStatement ScoreInsertion;
	private PreparedStatement SimScoreSelection;
	private PreparedStatement DistinctMethodIDSelection;
	private PreparedStatement ClusterInsertion;
	//for R
	private PreparedStatement SequenceSelection;
	private PreparedStatement ClustersInsertion;
	private PreparedStatement EdgeSelection;

	private PreparedStatement ClusterIDSelection;
	private PreparedStatement FileBasedClusterIDSelection;
	private PreparedStatement MethodSelectionMoreThan3APICalls;
		
	 public static DatabaseAccessLayer getInstance() { 
	        return DatabaseAccessLayer.SINGLETON;
	} 
    public void initializeConnector() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

        this.APICallSelection = this.connector.prepareStatement(
   				"SELECT id, api_name, api_usage from api_call where api_name NOT IN ('Toast', 'Log', 'Intent', 'EditText', 'String') ");
        this.APICallIndexSelection = this.connector
				.prepareStatement("SELECT id FROM api_call_index WHERE INSTR(api_call, ?) > 0 ");
        this.APICallIndexInsertion = this.connector
				.prepareStatement("INSERT INTO api_call_index VALUES(0,?)", Statement.RETURN_GENERATED_KEYS);
        this.APICallUpdation = this.connector
				.prepareStatement("UPDATE api_call set api_call_index_id = ? where id = ?");
        
		this.connector.setAutoCommit(false);
    }
    
    public void initializeConnectorForSimSeq() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

        this.APICallSelection = this.connector.prepareStatement("select api_call.host_method_id,api_call.api_call_index_id "+
				"from api_call inner join method on method.id=api_call.host_method_id "+
						"where api_call.api_call_index_id!=0 and api_call.host_method_id in "+
						"(select api_call.host_method_id as MID from api_call GROUP by api_call.host_method_id "+
						"HAVING count(distinct(api_call_index_id))>=3)");

		this.APIDataSelection = this.connector.prepareStatement(
   				"SELECT id, host_method_id, api_name, api_usage from api_call where api_name NOT IN ('Toast', 'Log','Intent','EditText', 'String') and host_method_id != 0 order by host_method_id ASC");
        this.SequenceInsertion = this.connector
				.prepareStatement("INSERT INTO sequence VALUES(0,?,?)");//, Statement.RETURN_GENERATED_KEYS); 
        this.SequenceSelection1 = this.connector.prepareStatement(
   				"SELECT id, method_ID, sequence from sequence");
        this.SequenceSelection2 = this.connector.prepareStatement(
   				"SELECT id, method_ID, sequence from sequence");
        this.ScoreInsertion = this.connector
				.prepareStatement("INSERT INTO sim_score VALUES(0,?,?,?)");//, Statement.RETURN_GENERATED_KEYS); 
        
		this.connector.setAutoCommit(false);
    }
    
    public void initializeConnectorForGettingDistanceMatrix() throws ClassNotFoundException, SQLException {
		
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

        this.SimScoreSelection = this.connector.prepareStatement(
   				"SELECT id, method_ID_1, method_ID_2, score from sim_score");
       
        
		this.connector.setAutoCommit(false);
		
	}
    
    public void initializeConnectorToWriteClustersFromR() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

		this.EdgeSelection = this.connector.prepareStatement("SELECT method_ID_1, method_ID_2, score FROM `sim_score` ORDER BY score DESC");
        this.SequenceSelection = this.connector.prepareStatement(
   				"SELECT id, method_ID from sequence");
        this.ClusterInsertion = this.connector.prepareStatement("INSERT INTO cluster VALUES(0,?,?,?)");     
		this.connector.setAutoCommit(false);
    }

	public void initializeConnectorToWriteTransaction() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		this.connector = DriverManager.getConnection(Constants.DATABASE);


		this.ClusterIDSelection = this.connector
				.prepareStatement("SELECT file.project_id, method.id, cluster.clusterID from file  INNER JOIN (cluster  INNER JOIN method )on (file.id = method.file_id and  method.id = cluster.methodID) ORDER BY file.project_id,cluster.clusterID ASC ");
		this.FileBasedClusterIDSelection = this.connector
				.prepareStatement("SELECT file.id, method.id, cluster.clusterID from file  INNER JOIN (cluster  INNER JOIN method )on (file.id = method.file_id and  method.id = cluster.methodID) ORDER BY file.id,cluster.clusterID ASC ");

		this.MethodSelectionMoreThan3APICalls = this.connector.prepareStatement("SELECT distinct(host_method_id),count(api_call.api_call_index_id) as count1 " +
				"FROM `api_call` group by host_method_id having count1>=3");
	}
    public void closeConnector() throws SQLException{
    	this.connector.close();
    }

    public void populateAPICallIndex() throws SQLException {
    	int count = 0;
    	
    	APICall apiCall = new APICall();
    	ResultSet apiCallsResultSet = APICallSelection.executeQuery();

    	//iterate on every api call
    	while(apiCallsResultSet.next())
    	{            	
    		count += 1;
    		System.out.println("Currently on: " + count);
    		
    		apiCall.id = apiCallsResultSet.getInt(1);
    		apiCall.api_name = apiCallsResultSet.getString(2);
    		apiCall.api_usage = apiCallsResultSet.getString(3);
    		apiCall.fullAPIcall = apiCall.api_name + "." + apiCall.api_usage;
    		
    		//check if not exists in api_call_index table
    		APICallIndexSelection.setString(1,apiCall.fullAPIcall);
    		ResultSet apiCallIndexResultSet = APICallIndexSelection.executeQuery();
    		if(apiCallIndexResultSet.next())//exists
        	{ 
    			//update the api_call_index column in api_call table with the id
    			int index_id = apiCallIndexResultSet.getInt(1);
    			APICallUpdation.setInt(1, index_id);
    			APICallUpdation.setInt(2, apiCall.id);
    			APICallUpdation.addBatch();
        	}
    		else
    		{
    			//add the api call to the api_call_index table    			
    			APICallIndexInsertion.setString(1, apiCall.fullAPIcall);        		
        		APICallIndexInsertion.executeUpdate();    
        		ResultSet rs = APICallIndexInsertion.getGeneratedKeys();                 
                if(rs.next()){
                	int index_id = rs.getInt(1);
                	//update the api_call_index column in api_call table with the id
        			APICallUpdation.setInt(1, index_id);
        			APICallUpdation.setInt(2, apiCall.id);
        			APICallUpdation.addBatch();
                }
                rs.close();
        		System.out.println("Added");
    		}
    		
    		apiCallIndexResultSet.close();

    	}
    	APICallUpdation.executeBatch();
    	apiCallsResultSet.close();
    	APICallSelection.close();
    	APICallIndexSelection.close();
    	APICallIndexInsertion.close();
    	APICallUpdation.close();
    	connector.commit();
    	

    }
	public void populateSequenceTable() throws SQLException {

		ResultSet apiCallsResultSet = APICallSelection.executeQuery();
		String sequence = "";
		int prev_methodID = -1;
		ArrayList<Integer> api_indexes = new ArrayList<Integer>();

    	//iterate on every api call
    	while(apiCallsResultSet.next())
    	{
    		int method_id = apiCallsResultSet.getInt(1);
    		int index_id = apiCallsResultSet.getInt(2);

    		if(method_id == prev_methodID || prev_methodID == -1){
    			if(!api_indexes.contains(index_id)){
    				api_indexes.add(index_id);
    				sequence = sequence.concat(Integer.toString(index_id)+ " ");
    			}
    		}
    		else
    		{
    			//insert sequence string in sequence table
    			//remove the last space
    			sequence = sequence.substring(0, sequence.length()-1);
    			SequenceInsertion.setInt(1, prev_methodID);
    			SequenceInsertion.setString(2, sequence);
    			SequenceInsertion.addBatch();
    			sequence = "";
    			sequence = sequence.concat(Integer.toString(index_id)+ " ");
    			api_indexes.clear();
    			api_indexes.add(index_id);
    		}
    		prev_methodID = method_id;

    	}
    	sequence = sequence.substring(0, sequence.length()-1);
		SequenceInsertion.setInt(1, prev_methodID);
		SequenceInsertion.setString(2, sequence);
		SequenceInsertion.addBatch();

    	SequenceInsertion.executeBatch();
    	apiCallsResultSet.close();
    	APICallSelection.close();
    	SequenceInsertion.close();
    	connector.commit();
    	System.out.println("done");

	}
	public void populateSimScoreTable() throws SQLException {
		//iterate over the sequence table in two for loops for pairwise comparison
		//for every comparison call SeqSimCalculation.seqSim function to get scores and save in sim_score
		//String seq1 = "a b c";
		//String seq2 = "c a b";
		ArrayList<Method> methodList= new ArrayList();
		ResultSet seqResultSet1 = SequenceSelection1.executeQuery();


		while(seqResultSet1.next()) {
			int method_ID_1 = seqResultSet1.getInt(2);
			String sequence1 = seqResultSet1.getString(3);
			methodList.add(new Method(method_ID_1,sequence1));
		}
		int batchsize = methodList.size()/8;
		int start = 0;
		int end = batchsize;
		while(end< methodList.size()) {
			System.out.println("processing batch");
			storeSimScore(methodList, start, end);
			start = end + 1;
			end = end + batchsize;

		}
		if(end-batchsize>0)
		{
			//storeSimScore(methodList, start, start + (methodList.size())-(end-batchsize));
			end = methodList.size()-1;
			if(start!=end)
				storeSimScore(methodList, start, end);
		}
	}

	private void storeSimScore(ArrayList<Method> methodList, int start, int end) throws SQLException {
		int comparison_num = 0;
		 for(int i = start; i<= end; i++)
		{
			for(int j = i+1; j< methodList.size(); j++)
			{
				int method_ID_1 = methodList.get(i).getMethodID();
				int method_ID_2 = methodList.get(j).getMethodID();
				String sequence1 = methodList.get(i).getSequence();
				String sequence2 = methodList.get(j).getSequence();
				//tokenize sequence in integer arraylist
				List<String> arrayList = new ArrayList<String>    (Arrays.asList(sequence1.split(" ")));
				List<Integer> intSeq1 = new ArrayList<Integer>();
				for(String fav:arrayList){
					intSeq1.add(Integer.parseInt(fav.trim()));
				}
				arrayList = new ArrayList<String>    (Arrays.asList(sequence2.split(" ")));
				List<Integer> intSeq2 = new ArrayList<Integer>();
				for(String fav:arrayList){
					intSeq2.add(Integer.parseInt(fav.trim()));
				}
				double score = JaccardSimilarity.calculateJaccardSimilarityIntegers(intSeq1, intSeq2);
				//System.out.println(comparison_num);
				//comparison_num += 1;
				//insert in sim_score
				if(score>0.5) {
					ScoreInsertion.setInt(1, method_ID_1);
					ScoreInsertion.setInt(2, method_ID_2);
					DecimalFormat df = new DecimalFormat("#.###");
					ScoreInsertion.setDouble(3, Double.parseDouble(df.format(score)));
					ScoreInsertion.addBatch();
				}
			}
		}
//		while(seqResultSet1.next())
//		{
//			int method_ID_1 = seqResultSet1.getInt(2);
//			String sequence1 = seqResultSet1.getString(3);
//			//just a temporary check to only process strings with length <= 100
//			if(sequence1.length() > 100)
//				continue;
//
//			ResultSet seqResultSet2 = SequenceSelection2.executeQuery();
//			while(seqResultSet2.next())
//			{
//				int method_ID_2 = seqResultSet2.getInt(2);
//				String sequence2 = seqResultSet2.getString(3);
//				if(sequence2.length() > 100)
//					continue;
//				//double score = Utilities.SeqSimCalculation.seqSim(sequence1, sequence2);
//				double score = JaccardSimilarity.computeJaccardsCoefficient(sequence1, sequence2);
//				System.out.println(comparison_num);
//				comparison_num += 1;
//				//insert in sim_score
//				ScoreInsertion.setInt(1, method_ID_1);
//				ScoreInsertion.setInt(2, method_ID_2);
//				DecimalFormat df = new DecimalFormat("#.###");
//
//    			ScoreInsertion.setDouble(3, Double.parseDouble(df.format(score)));
//    			ScoreInsertion.addBatch();
//			}
//			seqResultSet2.close();
//		}

		System.out.println("reached here");
		//seqResultSet1.close();
		ScoreInsertion.executeBatch();
		//SequenceSelection1.close();
		//SequenceSelection2.close();
		//SequenceInsertion.close();
		connector.commit();
		ScoreInsertion.clearBatch();
	}

	public double[][] getDistanceMatrix() throws SQLException {
		ArrayList<ArrayList<Double>> my_distances = new ArrayList<ArrayList<Double>>();
    	ArrayList<Double> distance = new ArrayList<Double>();   
    	
		ResultSet resultSet = SimScoreSelection.executeQuery();	
		
		int prev_method_ID = -1;
		
		while(resultSet.next())
		{
			int method_ID_1 = resultSet.getInt(2);
			int method_ID_2 = resultSet.getInt(3);
			double score = resultSet.getDouble(4);
			
			
			//put the first entry in the first position
			//keep track of index1 and index2, the change in method id should
			//result in increment of index1 and reset index2 to 0
			
			
			if((prev_method_ID == -1) || prev_method_ID == method_ID_1)
			{
				
					//index2 += 1;
					distance.add(1-score);
			}
			else
			{
					my_distances.add(distance);
					distance = new ArrayList<Double>();
					distance.add(1-score);
					//index1 += 1;
					//index2 = 0;
			}						
			
			prev_method_ID = method_ID_1;	
				    	
			//my_distancematrix[index1][index2] = score;
		}
		my_distances.add(distance);//for the last row
		
		SimScoreSelection.close();
		double[][] my_distancematrix = new double[my_distances.size()][];    	
    	for (int i = 0; i < my_distances.size(); i++) {    		
    	    ArrayList<Double> row = my_distances.get(i);
    	    Double[] array1 = row.toArray(new Double[row.size()]);    	    
    	    my_distancematrix[i] = Stream.of(array1).mapToDouble(Double::doubleValue).toArray();    	    
    	}		
		return my_distancematrix;
		
		
	}
	public void initializeConnectorForGettingDistanceMatrixLabels() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);

        // this.DistinctMethodIDSelection = this.connector.prepareStatement(
   		//		"SELECT DISTINCT method_ID_1 from sim_score");
        this.DistinctMethodIDSelection = this.connector.prepareStatement(
   				"SELECT DISTINCT method_ID_1, sequence.id FROM sim_score INNER JOIN sequence ON sim_score.method_ID_1 = sequence.method_ID");
        this.ClusterInsertion = this.connector.prepareStatement("INSERT INTO cluster VALUES(0,?,?,?)");   
       
		this.connector.setAutoCommit(false);
		
	}
	public String[] getDistanceMatrixLabels() throws SQLException {
		
		ArrayList<Integer> labels = new ArrayList<Integer>();
		ResultSet resultSet = DistinctMethodIDSelection.executeQuery();
				
		while(resultSet.next())
		{
			int method_ID = resultSet.getInt(1);
			int seq_ID = resultSet.getInt(2);
			labels.add(method_ID);
			
		}
		String[] my_labels = new String[labels.size()];
		for(int i=0; i<labels.size(); i++)
		{
			my_labels[i] = labels.get(i).toString();
		}
		//DistinctMethodIDSelection.close();
		return my_labels;
	}
	
	public LinkedHashMap<Integer, Integer> getMethodSequenceMapping() throws SQLException {
		
		LinkedHashMap<Integer, Integer> methodSeqMap = new LinkedHashMap<Integer, Integer>();		
		ResultSet resultSet = DistinctMethodIDSelection.executeQuery();
		
		while(resultSet.next())
		{
			int method_ID = resultSet.getInt(1);
			int seq_ID = resultSet.getInt(2);
			methodSeqMap.put(method_ID, seq_ID);
			
		}
		DistinctMethodIDSelection.close();
		return methodSeqMap;
	}
	
public LinkedHashMap<Integer, Integer> getSeqIDMethodIDMapping() throws SQLException {
		
		LinkedHashMap<Integer, Integer> sMap = new LinkedHashMap<Integer, Integer>();		
		ResultSet resultSet = SequenceSelection.executeQuery();
		
		while(resultSet.next())
		{		
			int seq_ID = resultSet.getInt(1);
			int method_ID = resultSet.getInt(2);
			sMap.put(seq_ID, method_ID);
			
		}
		SequenceSelection.close();
		return sMap;
	}

	public void insertClusters(ArrayList<ClusterDTO> clusterDTOsList) throws SQLException {
		for(ClusterDTO cluster: clusterDTOsList)
		{
			ClusterInsertion.setInt(1, cluster.clusterID);   
			ClusterInsertion.setInt(2, cluster.seqID);
			ClusterInsertion.setInt(3, cluster.methodID);  
			ClusterInsertion.addBatch();  
		}
		
		int[] inserted = ClusterInsertion.executeBatch();
		ClusterInsertion.close();
		connector.commit();
	}
	public void writeSequencesToCSV(String filename) throws SQLException, IOException {
		ResultSet apiCallsResultSet = APIDataSelection.executeQuery();
		String sequence = "";
		int prev_methodID = -1;
		
		
		File file = new File(filename);
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);
		

		
	    

    	//iterate on every api call
    	while(apiCallsResultSet.next())
    	{       
    		int method_id = apiCallsResultSet.getInt(2);
    		String api_name = apiCallsResultSet.getString(3);
    		String api_usage = apiCallsResultSet.getString(4);
    		
    		if(method_id == prev_methodID || prev_methodID == -1)
    			sequence = sequence.concat(api_name+ "." + api_usage + " ");
    		else
    		{
    			//insert sequence string in sequence table
    			//remove the last space
    			sequence = sequence.substring(0, sequence.length()-1);
    			sequence = sequence.concat("\n");
    			//write the sequence to file    			
    		    br.write(sequence);
    			//start building new sequence for new method
    			sequence = "";
    			sequence = sequence.concat(api_name+ "." + api_usage + " ");
    			
    		}
    		prev_methodID = method_id;
    		
    	}
    	sequence = sequence.substring(0, sequence.length()-1);
    	//write the last sequence to file
    	br.write(sequence);
    	
    	apiCallsResultSet.close();
    	APIDataSelection.close();
    	br.close();
		fr.close();
    	connector.commit();
		
	}


	public ArrayList<Edge> getEdges() throws SQLException {

			ArrayList<Edge> edgesList = new ArrayList<>();
			ResultSet resultSet = EdgeSelection.executeQuery();

			while(resultSet.next())
			{
				int mID1 = resultSet.getInt(1);
				int mID2 = resultSet.getInt(2);
				double score = resultSet.getDouble(3);

				edgesList.add(new Edge(mID1,mID2,score));

			}
			EdgeSelection.close();
			return edgesList;

	}

	public void populateClusterTable(ArrayList<ClusterDTO> clusterList) throws SQLException {

		for(ClusterDTO cluster: clusterList)
		{
			for(Integer method:cluster.getMethodsList()) {
				ClusterInsertion.setInt(1, cluster.clusterID);
				ClusterInsertion.setInt(2, 0);
				ClusterInsertion.setInt(3, method);
				ClusterInsertion.addBatch();
			}
		}

		int[] inserted = ClusterInsertion.executeBatch();
		ClusterInsertion.close();
		connector.commit();
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


	public ArrayList<String> getClusterIDsPerFile() throws SQLException {
		//ResultSet resultSet1 = MethodSelectionMoreThan3APICalls.executeQuery();
//ArrayList<Integer> methodIDsWithAPICalls3 = new ArrayList<Integer>();
//while(resultSet1.next())
		{
//methodIDsWithAPICalls3.add(resultSet1.getInt(1));
		}
		ArrayList<String> clusterIDList = new ArrayList<String>();
		ResultSet resultSet = FileBasedClusterIDSelection.executeQuery();
		int previousFileID = -1;
		String s = "";
		ArrayList<Integer> clusterIDsForFile = new ArrayList<Integer>();
		while(resultSet.next())
		{
			int fileID = resultSet.getInt(1);
			int methodID = resultSet.getInt(2);
			int clusterID = resultSet.getInt(3);
			if(fileID == previousFileID || previousFileID == -1)
			{
				if(!clusterIDsForFile.contains(clusterID))
				{
//if(methodIDsWithAPICalls3.contains(methodID))
					{
						clusterIDsForFile.add(clusterID);
					}
				}
//s = s.concat(clusterID + " ");
			}
			else
			{
				String clusterTransaction = createString(clusterIDsForFile);
				clusterIDList.add(clusterTransaction);
				clusterIDsForFile.clear();
//if(methodIDsWithAPICalls3.contains(methodID))
				{
					clusterIDsForFile.add(clusterID);
				}
//s = "";
//s = s.concat(clusterID + " ");
			}
			previousFileID = fileID;
		}
		String clusterTransaction = createString(clusterIDsForFile);
		clusterIDList.add(clusterTransaction);
		ClusterIDSelection.close();
		return clusterIDList;
	}

	private String createString(ArrayList<Integer> clusterIDsForProject) {
		String result = "";
		for(int i:clusterIDsForProject) {
			result = result.concat(i + " ");
		}
		return result.trim();
	}
}
