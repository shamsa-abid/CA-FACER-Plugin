package structure_extraction;

import repository.*;
import support.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class MySQLAccessLayer 
{
	public boolean recommendationConnectorInitialized = false;
	public boolean connectionEstablished = false;
	private static MySQLAccessLayer mysqlInstance = null;
	private MySQLAccessLayer()
	{

	}
	public static MySQLAccessLayer getInstance() throws Exception {
		//System.out.println("In mysqlaccesslayer getinstance");
		if (mysqlInstance == null) {
			mysqlInstance = new MySQLAccessLayer();
		}
		if(!mysqlInstance.connectionEstablished)
			mysqlInstance.initializeConnector();
		return mysqlInstance;
	}
	private Connection connector;
	
	private PreparedStatement projectInsertion;
	private PreparedStatement fileInsertion;
	private PreparedStatement typeInsertion;
	private PreparedStatement methodInsertion;
	private PreparedStatement methodParametersInsertion;
	private PreparedStatement methodParametersInsertion2;
	private PreparedStatement fieldInsertion;
	private PreparedStatement typeHierarchyInsertion;
	private PreparedStatement callsInsertion;
	private PreparedStatement usesInsertion;
	private PreparedStatement accessesInsertion;
	private PreparedStatement APIcallsInsertion;
	private PreparedStatement APIparametersInsertion;
	private PreparedStatement importsInsertion;
	//private int methodparametersId = 0;
	
	private PreparedStatement ps_getIDFile; 
	private PreparedStatement ps_getMethodID;
	private PreparedStatement ps_getTargetMethodID;
	private PreparedStatement ps_getMethodIDbyFuncNameProjID;
	private PreparedStatement ps_getMethodFilePath;
	private PreparedStatement ps_1;
	private PreparedStatement ps_2;
	private PreparedStatement ps_3;
	private PreparedStatement ps_getClusterIDofMethod;
	private PreparedStatement ps_getFileNameByFileID;
	private PreparedStatement ps_getFieldID;
	private PreparedStatement ps_getProjName;
	private PreparedStatement ps_getTypeByNameProjID;
	private PreparedStatement ps_UpdateTypeTable;
	private PreparedStatement ps_getTypeFromField; 
	private PreparedStatement ps_getHostMethods2;  
	private PreparedStatement ps_getAPIUsages  ;
	PreparedStatement ps_hasAPIUsages ;
	PreparedStatement ps_hasMCSMemship ;
	private PreparedStatement ps_getHostMeths;
	private PreparedStatement ps_isParentAThread; 
	private  PreparedStatement ps_getTargetMethID;
	private PreparedStatement getTargetOnStartMethID;
	private PreparedStatement ps_getFilPath ;
	private PreparedStatement getLastProjIDFrmDB; 
	private PreparedStatement ps_getLastFuncIDFrmDB; 
	private PreparedStatement ps_getBaseTypeByTypeName;
	private PreparedStatement ps_getProjID ;
	private PreparedStatement ps_getFilePathFromMethod; 
	private PreparedStatement ps_getHostMethods1;
	private PreparedStatement ps_getLastFileIDFrmDB;
	private PreparedStatement ps_getParentIDBasicPath; 
	private PreparedStatement ps_getCalledMethods ;
	private PreparedStatement ps_getTypesFromRepo2;
	private PreparedStatement ps_getBaseTypeByTypeID;
	private PreparedStatement ps_getTypeByIDProjID;
	private PreparedStatement ps_getFunctionID;
	private PreparedStatement ps_getMethodsByProjID;
	private PreparedStatement ps_gTFRPN ;
	private PreparedStatement ps_getParentsFromRepo; 
	private PreparedStatement ps_getCallsFrmRepo ;
	private PreparedStatement ps_getImpFromRepo;
	private PreparedStatement ps_getAPICallParams; 
	private PreparedStatement ps_getTypesFromRepo1; 
	private PreparedStatement ps_checkProjectExists;
	private PreparedStatement ps_getCalledMethodIDs;
	private PreparedStatement ps_getProjIDByProjName;
	private PreparedStatement ps_getAPIUsagesForEvaluation;
	private PreparedStatement ps_getMethodsSQLWayLOOCV;
	private PreparedStatement ps_caclculateMethodStats;
	private PreparedStatement ps_updateMethodStats;
	
	private PreparedStatement APICallSelection;
	private PreparedStatement APICallIndexSelection;
	private PreparedStatement APICallIndexInsertion;
	private PreparedStatement APICallUpdation;
	private PreparedStatement DistinctAPICallSelection;
	private PreparedStatement APICallIndexInsertionNew;
	private PreparedStatement APICallIndexIdUpdate;
	private PreparedStatement APICallUpdate;

	private PreparedStatement AllMethodSelection;
	private PreparedStatement AllMethodSelectionAPICall;
	private PreparedStatement ps_getMethodFilePathRel;
    
	private PreparedStatement ps_getMethodAPIUsages;

	public static String getPreProjectPath()
	{
		
		return Constants.PRE_FILE_PATH;
		
	}
    public void initialize() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
    	System.out.println("Connected to DB in initialize");
        this.connector = DriverManager.getConnection(Constants.DATABASE);
        this.typeInsertion = this.connector
				.prepareStatement("INSERT INTO type VALUES (0, ?, ?, ? )");
        this.projectInsertion = this.connector
				.prepareStatement("INSERT INTO project VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		this.fileInsertion = this.connector
				.prepareStatement("INSERT INTO file VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		this.methodInsertion = this.connector
				.prepareStatement("INSERT INTO method VALUES(?,?,?,?,?,?,?,?,0)");
		this.methodParametersInsertion = this.connector
				.prepareStatement("INSERT INTO  method_parameter VALUES(0,?,?,?,?)");
		this.methodParametersInsertion2 = this.connector
				.prepareStatement("INSERT INTO  method_parameter VALUES(0,?,0,?,?,?)");
		this.fieldInsertion = this.connector
				.prepareStatement("INSERT INTO field VALUES(0,?,?,?,?)");
		this.typeHierarchyInsertion = this.connector
				.prepareStatement("INSERT INTO type_hierarchy VALUES(0,?,?)");
		this.callsInsertion = this.connector
				.prepareStatement("INSERT INTO calls VALUES(0,?,?,?,?,?,?,?)");	
		this.usesInsertion = this.connector
				.prepareStatement("INSERT INTO uses VALUES(0,?,?,?)");	
		this.accessesInsertion = this.connector
				.prepareStatement("INSERT INTO  accesses VALUES(0,?,?,?)");
		this.APIcallsInsertion = this.connector
				.prepareStatement("INSERT INTO  api_call VALUES(0,?,?,?,?,?,?,?,?,0,?)");
		this.APIparametersInsertion = this.connector
				.prepareStatement("INSERT INTO  api_call_parameter VALUES(0,?,?,?)");
		this.importsInsertion = this.connector
				.prepareStatement("INSERT INTO  imports VALUES(0,?,?)");
		
		this.APICallSelection = this.connector.prepareStatement(
   				"SELECT id, api_name, api_usage from api_call where api_name NOT IN ('Toast', 'Log', 'Intent', 'EditText', 'String') ");
		this.DistinctAPICallSelection = this.connector.prepareStatement(
				"SELECT distinct apicall from api_call");

		this.APICallIndexSelection = this.connector
				.prepareStatement("SELECT id FROM api_call_index WHERE INSTR(api_call, ?) > 0 ");
        this.APICallIndexInsertion = this.connector
				.prepareStatement("INSERT INTO api_call_index VALUES(0,?)", Statement.RETURN_GENERATED_KEYS);
		this.APICallIndexInsertionNew = this.connector
				.prepareStatement("INSERT INTO api_call_index VALUES(0,?)");
		this.APICallIndexIdUpdate = this.connector
			//	.prepareStatement("update api_call a ,api_call_index i set a.api_call_index_id = i.id where a.apicall = i.api_call");
				.prepareStatement("update api_call t1 inner join api_call_index t2 on t1.apicall=t2.api_call set t1.api_call_index_id = t2.id");
		this.APICallUpdate = this.connector
				.prepareStatement("update api_call set apicall=concat(api_name,'.',api_usage)");

		this.APICallUpdation = this.connector
				.prepareStatement("UPDATE api_call set api_call_index_id = ? where id = ?");
        this.AllMethodSelection = this.connector.prepareStatement("select distinct(api_call.host_method_id) as methodID,method.api_calls_density from api_call "+
        		"inner join method on method.id=api_call.host_method_id "+
        		"where api_call.api_call_index_id!=0 and api_call.host_method_id in(select api_call.host_method_id as MID "+
        		"from api_call GROUP by api_call.host_method_id HAVING count(distinct(api_call_index_id))>=3)"
        		);
       
        this.AllMethodSelectionAPICall = this.connector.prepareStatement("select api_call.host_method_id,api_call.api_call_index_id "+
        		"from api_call inner join method on method.id=api_call.host_method_id "+
        		"where api_call.api_call_index_id!=0 and api_call.host_method_id in "+
        		"(select api_call.host_method_id as MID from api_call GROUP by api_call.host_method_id "+ 
        		"HAVING count(distinct(api_call_index_id))>=3)");
		this.connector.setAutoCommit(false);
		/////
		prepareStatements(this.connector);
    }
    public void initializeConnector() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
    	System.out.println("Connected to DB in initialize connector");
        this.connector = DriverManager.getConnection(Constants.DATABASE);
        
		this.connector.setAutoCommit(false);
		
		prepareStatements(this.connector);
    }
    public void initializeConnectorExternal(String DB) throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
    	System.out.println("Connected to DB in initialize connector");
        this.connector = DriverManager.getConnection(DB);
        
		this.connector.setAutoCommit(false);
		
		prepareStatements(this.connector);
    }
    public void initializeConnectorForRecomemndation() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
    	recommendationConnectorInitialized = true;
        // Setup the connection with the DB
        this.connector = DriverManager.getConnection(Constants.DATABASE);
        
		this.connector.setAutoCommit(false);
		
		this.ps_getCalledMethodIDs = this.connector.prepareStatement("SELECT target_method_id from calls where host_method_id = ? and (in_basic_path = ? or in_conditional = ?) order by line_num ASC");
		
		this.ps_getCalledMethods =  this.connector.prepareStatement("SELECT target_method_id,line_num from calls where host_method_id = ? and (in_basic_path = ? or in_conditional = ?) order by line_num ASC");
    
		this.ps_getHostMethods1 = this.connector.prepareStatement("SELECT host_method_id from calls where target_method_id = ? and in_basic_path = ? order by line_num ASC");
		this.ps_getHostMethods2 = this.connector.prepareStatement("SELECT method.id FROM method WHERE method.name= 'onStart' and host_type_id IN (select host_type_id from method where id = ? and name != 'onCreate')");
		
			
    }
   	
    private void prepareStatements(Connection connector) throws SQLException
    {
    	//
		// System.out.println("In prepare statements");
    	ps_getAPIUsagesForEvaluation = connector.prepareStatement("SELECT id, api_name,api_usage from api_call where host_method_id = ? and (in_basic_path = ? or in_conditional = ?) and api_name != 'Log' ");
    	
    	ps_getProjIDByProjName = connector.prepareStatement("select id from project where name=?");
		
    	ps_getHostMethods2 = connector.prepareStatement("SELECT method.id FROM method WHERE method.name= 'onStart' and host_type_id IN (select host_type_id from method where id = ? and name != 'onCreate')");
		
    	 ps_getTypesFromRepo2 = connector.prepareStatement(
 				" SELECT t.name FROM uses u, type t" +
 				" WHERE u.host_method_id = ? and u.target_type_id = t.id");  
    	 ps_checkProjectExists = connector.prepareStatement(
  				" SELECT * FROM project where name = ?" );  
    	 ps_getAPIUsages = connector.prepareStatement("SELECT id, api_name,api_usage,line_num from api_call where host_method_id = ? and (in_basic_path = ? or in_conditional = ?) order by line_num ASC");
    	 ps_getMethodAPIUsages = connector.prepareStatement("SELECT api_name,api_usage from api_call where host_method_id = ? order by line_num ASC");
		 ps_hasAPIUsages = connector.prepareStatement("SELECT count(id) from api_call where host_method_id = ?");
    	 ps_hasMCSMemship = connector.prepareStatement("SELECT cluster.methodID from cluster inner join related_features on related_features.cluster_id=cluster.clusterID where cluster.methodID =?");
    	 ps_getHostMeths = connector.prepareStatement("SELECT host_method_id from calls where target_method_id = ? and in_basic_path = ? order by line_num ASC");
    	 ps_isParentAThread = connector.prepareStatement(
 				" SELECT * FROM type_hierarchy" +
 				" inner join type on type.id = type_hierarchy.parent_type_id where type_hierarchy.child_type_id = ? and type.name = 'Thread'");  
    	 ps_getTargetMethID = connector.prepareStatement(
 				" SELECT method.id from method inner join type on" +
 				" type.id = method.host_type_id where type.name = ? and type.project_id = ?");  
    	 getTargetOnStartMethID = connector.prepareStatement(
 				" SELECT method.id from method inner join type on" +
 				" type.id = method.host_type_id where type.id = ? and type.project_id = ? and method.name = 'onStart'");  
    	 ps_getFilPath = connector.prepareStatement(
 				"SELECT file_name from file where id = ? ");
    	 getLastProjIDFrmDB = connector.prepareStatement(
 				" SELECT max(id) from project");  
    	 ps_getLastFuncIDFrmDB = connector.prepareStatement(
 				" SELECT max(id) from method");  
    	 ps_getBaseTypeByTypeName = connector.prepareStatement(
 				"SELECT base_type_id from type where project_id = ? and name = ?");
    	 ps_getProjID = connector.prepareStatement(
 				"SELECT project_id from method inner join file on method.file_id = file.id where method.id = ? ");
    	 ps_getFilePathFromMethod = connector.prepareStatement(
 				"SELECT file_name from method inner join file on method.file_id = file.id where method.id = ? ");
    	 ps_getHostMethods1 = connector.prepareStatement("SELECT host_method_id from calls where target_method_id = ? and in_basic_path = ? order by line_num ASC");
 		
    	 ps_getLastFileIDFrmDB = connector.prepareStatement(
 				" SELECT max(id) from file");  
    	 ps_getParentIDBasicPath = connector.prepareStatement(
 				"SELECT host_method_id from calls where target_method_id = ? ");
    	 ps_getCalledMethodIDs = connector.prepareStatement("SELECT target_method_id from calls where host_method_id = ? and (in_basic_path = ? or in_conditional = ?) order by line_num ASC");
   		
    	 ps_getCalledMethods = connector.prepareStatement("SELECT target_method_id,line_num from calls where host_method_id = ? and (in_basic_path = ? or in_conditional = ?) order by line_num ASC");
    	     	ps_getIDFile = connector.prepareStatement(
   				"SELECT id  from file where file_name = ? ");
    	ps_getMethodID = connector.prepareStatement(
				"SELECT method.id, method.file_id from method inner join file on file.id = method.file_id where method.name = ? and method.host_type_id = ? and file.project_id = ?");
    	ps_getFunctionID = connector.prepareStatement(
				"SELECT method.id FROM method inner join type on method.host_type_id = type.id inner join file on method.file_id = file.id inner join project on file.project_id = project.id where method.name = ? and type.name = ? and project.name = ?");
    	ps_getMethodsByProjID = connector.prepareStatement(
				"SELECT method.id, method.name, method.host_type_id, method.from_line_num, method.to_line_num, method.file_id from method inner join file on file.id = method.file_id where  file.project_id = ?");
    	
    	ps_getTargetMethodID = connector.prepareStatement(
				"SELECT method.id, method.file_id from method inner join file on file.id = method.file_id where method.name = ? and file.project_id = ?");
    	ps_getMethodIDbyFuncNameProjID = connector.prepareStatement(
				"SELECT method.id from method inner join file on file.id = method.file_id where method.host_type_id = ? and file.project_id = ? and method.name = ? ");
    	ps_getMethodFilePath = connector.prepareStatement(
				"SELECT file_name from file where id = ? ");
    	ps_getMethodFilePathRel = connector.prepareStatement(
				"SELECT relative_path from file where id = ? ");
    	ps_1 = connector.prepareStatement("SELECT method.id, cluster.clusterID, name, from_line_num, to_line_num, file_id, file.project_id" + 
				" FROM method inner join cluster on cluster.methodID = method.id INNER JOIN file on file.id = method.file_id" + 
				" WHERE method.id = ? AND EXISTS(" + 
				" SELECT id FROM related_features" +
				" WHERE related_features.cluster_id = cluster.clusterID)");
    	ps_2= connector.prepareStatement(
				"SELECT method.id, cluster.clusterID, name, from_line_num, to_line_num, file_id, file.project_id from method inner join cluster on cluster.methodID = method.id INNER JOIN file on file.id = method.file_id where method.id = ?");
		ps_3 = connector.prepareStatement(
    				"SELECT method.id, name, from_line_num, to_line_num, file_id,file.project_id from method INNER JOIN file on file.id = method.file_id where method.id = ?");
		ps_getClusterIDofMethod = connector.prepareStatement(
				"SELECT cluster.clusterID from cluster inner join method on cluster.methodID=method.id where method.id = ?");
		
		ps_getFileNameByFileID = connector.prepareStatement(
				"select file_name from file where id=?");
		ps_getFieldID = connector.prepareStatement(
				"SELECT field.id FROM field inner join type on field.class_id = type.id WHERE field_name = ? and field.class_id = ? and type.project_id = ?");
		ps_getProjName = connector.prepareStatement("select name from project where id=?");
		ps_getTypeByNameProjID = connector.prepareStatement(
				"SELECT id, name, base_type_id from type where name = ? and project_id=?");
		ps_UpdateTypeTable = connector.prepareStatement("update type set base_type_id=? where project_id=? and name = ?");
		
		ps_getTypeFromField = connector.prepareStatement(
				"SELECT field_type_id from field where field_name = ? and file_id=?");
		ps_getBaseTypeByTypeID = connector.prepareStatement(
				"SELECT base_type_id from type where id = ? and project_id = ?");
		ps_getTypeByIDProjID = connector.prepareStatement(
				"SELECT id, name from type where id = ? and project_id=?");
		ps_gTFRPN = connector.prepareStatement(
				"SELECT type.name from method_parameter inner join type on method_parameter.parameter_type_id = type.id where method_parameter.method_id = ? and method_parameter.method_parameter_name = ? and type.project_id = ?");
		ps_getParentsFromRepo = connector.prepareStatement(
				" SELECT DISTINCT t.name FROM type t, type_hierarchy th, method m" +
				" WHERE m.id = ?"  +
				" AND m.host_type_id = th.child_type_id AND th.parent_type_id = t.id");
		ps_getCallsFrmRepo = connector.prepareStatement(
				" SELECT DISTINCT api_usage FROM api_call" +
				" WHERE host_method_id = ?");  
		ps_getImpFromRepo = connector.prepareStatement(
				" SELECT fqn FROM imports" +
				" inner join method on method.file_id = imports.file_id where method.id = ?");  
		ps_getAPICallParams = connector.prepareStatement(
				"SELECT api_parameter_name from api_call_parameter where api_call_id = ?");
	
		ps_getTypesFromRepo1 = connector.prepareStatement(
				" SELECT t.name FROM accesses a, field f, type t" +
				" WHERE a.host_method_id = ? and a.target_field_id = f.id and f.field_type_id = t.id");  
		
		ps_caclculateMethodStats = connector.prepareStatement("SELECT api_call.host_method_id, "
				+ "(count(DISTINCT(api_call.line_num))/method.num_statements ) as density "
				+ "from api_call inner join method on method.id=api_call.host_method_id "
				+ "group by api_call.host_method_id");
		
		ps_updateMethodStats = connector.prepareStatement("update method set method.api_calls_density = ? where method.id =?");
		
		
    }
    
    ///////////////////////////////////////////////////////////////////         
    @SuppressWarnings("deprecation")
	public void insertProjectIntoDb(int projectId, String name, String path)
    {    	
    	try
    	{    		
    		java.util.Date today = new java.util.Date();    	    
    		projectInsertion.setInt(1, projectId);
    		projectInsertion.setString(2, name);
    		projectInsertion.setString(3, String.valueOf(new java.sql.Date(today.getTime())));   
    		projectInsertion.setString(4, path);
    		projectInsertion.execute();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}    	
    }
    
    public int insertIntoType(Type obj, CodeMetadata codemetadata)
    {
    	
    	try
    	{
    		Type type = this.getTypeByName(obj.name, obj.project_id);
    		if(type != null)
    		{
    			obj.id = type.id;
				codemetadata.typeTable.put(type.name,type.id);
    			return obj.id;
    		}
    		
    		typeInsertion.setString(1, obj.name);
    		typeInsertion.setInt(2, obj.base_type_id);
    		typeInsertion.setInt(3, obj.project_id);    		
    		typeInsertion.executeUpdate();
    		
    		type = this.getTypeByName(obj.name,obj.project_id);
    		obj.id = type.id;
    		return obj.id;    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return 0;
    }
    
    public int insertIntoType_v2(Type obj)
    {    	
    	try
    	{    		
    		typeInsertion.setString(1, obj.name);
    		typeInsertion.setInt(2, obj.base_type_id);
    		typeInsertion.setInt(3, obj.project_id);    		
    		typeInsertion.addBatch();
    		return obj.id;    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return 0;
    }
    
    public String insertUsesIntoDb(Uses obj)
    {
    	String fileid= null;
    	try
    	{
    		usesInsertion.setInt(1, obj.host_method_id);
    		usesInsertion.setInt(2, obj.target_method_id);
    		usesInsertion.setInt(3, obj.line_num);
    		usesInsertion.addBatch();    	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return fileid;	
    }
    
    public void insertImportsIntoDb(Imports obj)
    {    	
    	try
    	{
    		importsInsertion.setString(1, obj.fqn);
    		importsInsertion.setInt(2, obj.file_id);
    		importsInsertion.addBatch();    	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    }
    
    public void insertFileIntoDb(int id, String name,int projectId)
    {   	
    	try
    	{
    		fileInsertion.setInt(1, id);
    		fileInsertion.setString(2, name);
    		fileInsertion.setInt(3, projectId);
			int rootlength = Constants.PROJECTS_ROOT.length();
			fileInsertion.setString(4, name.substring(rootlength+1));
    		fileInsertion.execute();		    		    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}    	
    }
   
    public void insertFieldIntoDb(Field field)
    {
    	try
    	{
    		fieldInsertion.setString(1, field.field_name);
    		fieldInsertion.setInt(2, field.field_type_id);
    		fieldInsertion.setInt(3, field.file_id);
    		fieldInsertion.setInt(4, field.class_id);   		
    		fieldInsertion.addBatch();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void insertCallsIntoDb(Calls obj)
    {
    	try
    	{
    		callsInsertion.setInt(1, obj.host_method_id);
    		callsInsertion.setInt(2, obj.target_method_id);
    		callsInsertion.setInt(3, obj.line_num); 
    		callsInsertion.setInt(4, obj.in_basic_path);
    		callsInsertion.setInt(5, obj.in_conditional);
    		callsInsertion.setInt(6, obj.in_else);
    		callsInsertion.setInt(7, obj.in_exception);
    		callsInsertion.addBatch();    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void inserttypeHeirarchyIntoDb(TypeHierarchy obj)
    {
    	try
    	{
    		typeHierarchyInsertion.setInt(1, obj.child_type_id);
    		typeHierarchyInsertion.setInt(2, obj.parent_type_id);    		
    		typeHierarchyInsertion.addBatch();    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
     
    public void insertAccessesIntoDb(Accesses obj)
    {
    	try
    	{
    		accessesInsertion.setInt(1, obj.host_method_id);
    		accessesInsertion.setInt(2, obj.target_field_id);
    		accessesInsertion.setInt(3, obj.line_num);    		
    		accessesInsertion.addBatch();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void insertMethodintoDb(Method method)
    {
    	try
    	{   
    		methodInsertion.setInt(1, method.id); 		
    		methodInsertion.setString(2, method.name);
    		methodInsertion.setInt(3, method.host_type_id);
    		methodInsertion.setInt(4, method.return_type_id);    		
    		methodInsertion.setInt(5, method.from_line_num);
    		methodInsertion.setInt(6, method.to_line_num);
    		methodInsertion.setInt(7, method.file_id);
    		methodInsertion.addBatch();
    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void insertMethodintoDb2(Method method)
    {
    	try
    	{   
    		methodInsertion.setInt(1, method.id); 		
    		methodInsertion.setString(2, method.name);
    		methodInsertion.setInt(3, method.host_type_id);
    		methodInsertion.setInt(4, method.return_type_id);    		
    		methodInsertion.setInt(5, method.from_line_num);
    		methodInsertion.setInt(6, method.to_line_num);
    		methodInsertion.setInt(7, method.file_id);
    		methodInsertion.setInt(8, method.expressionStmtsCount);
    		methodInsertion.addBatch();
    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void insertMethodParamIntoDb(MethodParameter obj)
    {
    	//methodparametersId += 1;
    	try
    	{
    		//methodParametersInsertion.setInt(1, methodparametersId);
    		methodParametersInsertion.setString(1, obj.method_parameter_name);
    		methodParametersInsertion.setInt(2, obj.parameter_type_id);
    		methodParametersInsertion.setInt(3, obj.file_id);
    		methodParametersInsertion.setInt(4, obj.method_id);  
    		methodParametersInsertion.addBatch();    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }  
    
    
    public void insertMethodParamIntoDb2(MethodParameter obj)
    {
    	//methodparametersId += 1;
    	try
    	{
    		//methodParametersInsertion.setInt(1, methodparametersId);
    		methodParametersInsertion2.setString(1, obj.method_parameter_name);
    		//methodParametersInsertion.setInt(2,  obj.parameter_type_id);
    		methodParametersInsertion2.setInt(2, obj.file_id);
    		methodParametersInsertion2.setInt(3, obj.method_id);
    		methodParametersInsertion2.setString(4, obj.typeName);
    		//methodParametersInsertion2.setString(5, obj.className);
    		methodParametersInsertion2.addBatch();    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }  
    
    public void insertAPICallParameterIntoDb(APICallParameter obj)
    {    	
    	try
    	{
    		if(obj.api_parameter_name.length()>250)
    			obj.api_parameter_name = obj.api_parameter_name.substring(0,250);
    		APIparametersInsertion.setString(1, obj.api_parameter_name);
    		APIparametersInsertion.setInt(2, obj.parameter_type_id);
    		APIparametersInsertion.setInt(3, obj.api_call_id); 
    		APIparametersInsertion.addBatch();    		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    } 

    public void insertAPICallsIntoDb(APICall obj) 
	{
		try
    	{
    		APIcallsInsertion.setInt(1, obj.host_method_id);
    		if(obj.api_name.length()>149)
    			obj.api_name = obj.api_name.substring(0,149);
    		APIcallsInsertion.setString(2, obj.api_name);
    		APIcallsInsertion.setString(3, obj.api_usage);
    		APIcallsInsertion.setInt(4, obj.line_num);   
    		APIcallsInsertion.setInt(5, obj.in_basic_path);
    		APIcallsInsertion.setInt(6, obj.in_conditional);
    		APIcallsInsertion.setInt(7, obj.in_else);
    		APIcallsInsertion.setInt(8, obj.in_exception);
			APIcallsInsertion.setString(9, obj.api_name+"."+obj.api_usage);
    		APIcallsInsertion.addBatch();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
		
	}
    ///////////////////////////////////////////////////////////////////    
    public String getIdFile(String methodName,int projectId)
    {
    	String fileid= null;
    	try
    	{
       		
    		ps_getIDFile.setString(1,methodName);
            ResultSet resultSet = ps_getIDFile.executeQuery();
            
            if(resultSet.next())
            {
            	 fileid= resultSet.getString(1);
            	
            }
            
            resultSet.close();
            //ps_getIDFile.close();
            return fileid;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return fileid;
    	}
    }
    
    public Method getMethodId(String methodName, int currentClassTypeID, int projectID){
    	Method obj = new Method();
    	try
    	{    		
    		ps_getMethodID.setString(1, methodName);
    		//preparedStatement.setInt(2, start_line);
    		ps_getMethodID.setInt(2, currentClassTypeID);
    		ps_getMethodID.setInt(3, projectID);
            ResultSet resultSet = ps_getMethodID.executeQuery();
            
            if(resultSet.next())
            {            	
            	obj.id = resultSet.getInt(1);
            	obj.file_id = resultSet.getInt(2);            	
            }            
            resultSet.close();
            //ps_getMethodID.close();
            return obj;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return obj;
    	}
    }
    
    public Method getTargetMethodId(String funcName, int projectID) {
    	Method obj = new Method();
    	try
    	{    		
    		ps_getTargetMethodID.setString(1, funcName);
    		ps_getTargetMethodID.setInt(2, projectID);
    		
            ResultSet resultSet = ps_getTargetMethodID.executeQuery();
            
            if(resultSet.next())
            {            	
            	obj.id = resultSet.getInt(1);
            	obj.file_id = resultSet.getInt(2);            	
            }            
            resultSet.close();
           // ps_getTargetMethodID.close();
            return obj;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return obj;
    	}
	}
    
    public int getMethodId(int callerObjTypeID, String funcName, int projectId) {
    	int methodID = 0;
    	try
    	{    		
    		ps_getMethodIDbyFuncNameProjID.setInt(1, callerObjTypeID);
    		ps_getMethodIDbyFuncNameProjID.setInt(2, projectId);
    		ps_getMethodIDbyFuncNameProjID.setString(3, funcName);
            ResultSet resultSet = ps_getMethodIDbyFuncNameProjID.executeQuery();
            
            if(resultSet.next())
            {            	
            	methodID = resultSet.getInt(1);         	
         	
            }            
            resultSet.close();
            //ps_getMethodIDbyFuncNameProjID.close();
            return methodID;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return methodID;
    	}
	}
    
    public String getMethodFilePath(int id, int file_id) 
    {    	
    	String filePath = "";
    	try
    	{    		
    		
    		ps_getMethodFilePath.setInt(1, file_id);
            ResultSet resultSet = ps_getMethodFilePath.executeQuery();
            
            if(resultSet.next())
            {            	
            	filePath = resultSet.getString(1);            	
            }            
            resultSet.close();
            //ps_getMethodFilePath.close();
            return filePath;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return filePath;
    	}
  	
	}
    
    public String getMethodFilePathRelative(int id, int file_id) 
    {    	
    	String filePath = "";
    	try
    	{    		
    		
    		ps_getMethodFilePathRel.setInt(1, file_id);
            ResultSet resultSet = ps_getMethodFilePathRel.executeQuery();
            
            if(resultSet.next())
            {            	
            	filePath = resultSet.getString(1);            	
            }            
            resultSet.close();
            //ps_getMethodFilePath.close();
            return filePath;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return filePath;
    	}
  	
	}
    
    public Method getMethodById(int methodID){
    	Method method = new Method();
    	try
    	{    		
    		PreparedStatement ps_getMethodByID_;
    		
    		if(Constants.retrieveRelatedFeaturePatternMembers)
    			ps_getMethodByID_ = ps_1;
    		
    		else if(Constants.retrieveClusterMembers)
    			ps_getMethodByID_ = ps_2;
    		else
    			ps_getMethodByID_ = ps_3;
    		
    
    		ps_getMethodByID_.setInt(1, methodID);
            ResultSet resultSet = ps_getMethodByID_.executeQuery();
            
            if(resultSet.next())
            {  
            	if(Constants.retrieveRelatedFeaturePatternMembers)
            	{
            		method.id = methodID;
	            	method.clusterID = resultSet.getInt(2);
	            	method.name = resultSet.getString(3);
	            	method.from_line_num = resultSet.getInt(4);
	            	method.to_line_num = resultSet.getInt(5);
	            	method.file_id = resultSet.getInt(6);
	            	method.projectID = resultSet.getInt(7);
            	}
            	else
            		
            	if(Constants.retrieveClusterMembers)//not retrieving cluster members = false
            	{
            		method.id = methodID;
	            	method.clusterID = resultSet.getInt(2);
	            	method.name = resultSet.getString(3);
	            	method.from_line_num = resultSet.getInt(4);
	            	method.to_line_num = resultSet.getInt(5);
	            	method.file_id = resultSet.getInt(6);
	            	method.projectID = resultSet.getInt(7);
            	}
            	else
	            {
            		ps_getClusterIDofMethod.setInt(1, methodID);
                    ResultSet clusterID = ps_getClusterIDofMethod.executeQuery();
                    
                    if(clusterID.first())
                    {                    	
                    	method.clusterID = clusterID.getInt(1);
                    }
                    
                    
	            	method.id = methodID;
	            	//method.clusterID = resultSet.getInt(2);
	            	method.name = resultSet.getString(2);
	            	method.from_line_num = resultSet.getInt(3);
	            	method.to_line_num = resultSet.getInt(4);
	            	method.file_id = resultSet.getInt(5);
	            	method.projectID = resultSet.getInt(6);
	            	
	            	clusterID.close();
	            }
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
    
    //get field id of xyz if it is a valid field of a user defined type/class A in file A.java
    public int getFieldId(String calleeFieldName,String callerObjectName, int projectID)
    {
    	int id = 0;
    	try
    	{   
    		Type callerType = getTypeByName(callerObjectName, projectID);
    		if (callerType != null)
    		{
	    		int callerClassID = callerType.id;
	    		
	    		//let A.xyz be field access for field xyz by object A    		
	    		ps_getFieldID.setString(1, calleeFieldName);
	    		ps_getFieldID.setInt(2, callerClassID);
	    		ps_getFieldID.setInt(3, projectID);
	            ResultSet resultSet = ps_getFieldID.executeQuery();
	           
	            //if A found in type table
	            if(resultSet.next())
	            {        	
	            	id = resultSet.getInt(1);            	           	
	            }            
	            resultSet.close();
	            //ps_getFieldID.close();            
	            
    		}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return id;
    	}
    	return id;
    }
	
    private String getFileName(int fileId)
    {
		String filename = null;
		try 
		{
			
			ps_getFileNameByFileID.setInt(1,fileId);
            ResultSet resultSet = ps_getFileNameByFileID.executeQuery();
           
            if(resultSet.next())
            {
            	filename = resultSet.getString(1);
            }
            resultSet.close();
           // ps_getFileNameByFileID.close();
                       
		} 
		catch (Exception e)
		{	
			e.printStackTrace();
		}
		return filename;
	}
	
	/*private String getProjectName(int projectId)
	{		
		String projectname = null;
		try 
		{			
			ps_getProjName.setInt(1,projectId);
            ResultSet resultSet = ps_getProjName.executeQuery();
           
            if(resultSet.next())
            {
            	projectname = resultSet.getString(1);
            }
            resultSet.close();
            //ps_getProjName.close();           
            
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return projectname;	
		
	}	
	*/
	public Type getTypeByName( String name,int projectId)
    {    	
    	try
    	{
    		ps_getTypeByNameProjID.setString(1,name);
    		ps_getTypeByNameProjID.setInt(2,projectId);
            ResultSet resultSet = ps_getTypeByNameProjID.executeQuery();
            
            if(resultSet.next())
            {
            	Type obj = new Type();
            	obj.id = resultSet.getInt(1);
            	obj.name = resultSet.getString(2);
            	obj.base_type_id = resultSet.getInt(3);
                resultSet.close();
                //ps_getTypeByNameProjID.close();
            	return obj;
            }
            else
            {
                resultSet.close();
                //ps_getTypeByNameProjID.close();

            }
            return null;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
    }
    
    public void updateTypeTable(Type obj)
    {
    	try
    	{
       		ps_UpdateTypeTable.setInt(1, obj.base_type_id);
    		ps_UpdateTypeTable.setInt(2, obj.project_id);
    		ps_UpdateTypeTable.setString(3, obj.name);        	
    		ps_UpdateTypeTable.executeUpdate();    		
    		//ps_UpdateTypeTable.close();      		
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
	
	public int getTypeFromField(String typeName, int fileId) 
	{
		try
    	{
    		ps_getTypeFromField.setString(1, typeName);
    		ps_getTypeFromField.setInt(2, fileId);
            ResultSet resultSet = ps_getTypeFromField.executeQuery();
            
            if(resultSet.next())
            {            	
            	int Id = resultSet.getInt(1);            	
                resultSet.close();
               // ps_getTypeFromField.close();
            	return Id;
            }
            else
            {
                resultSet.close();
                //ps_getTypeFromField.close();
            }
        	return -1;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return -1;
        }
	}

	public int getBaseTypeByTypeId(int typeID, int projectId) {
		try
    	{
    		ps_getBaseTypeByTypeID.setInt(1,typeID);
    		ps_getBaseTypeByTypeID.setInt(2,projectId);
            ResultSet resultSet = ps_getBaseTypeByTypeID.executeQuery();
            
            if(resultSet.next())
            {            	
            	int id = resultSet.getInt(1);            	 
                resultSet.close();
               // ps_getBaseTypeByTypeID.close();
            	return id;
            }
            else
            {
                resultSet.close();
                //ps_getBaseTypeByTypeID.close();

            }
            return 0;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}

	public Type getTypeById(int typeID, int projectId) {
		try
    	{
    		ps_getTypeByIDProjID.setInt(1,typeID);
    		ps_getTypeByIDProjID.setInt(2,projectId);
            ResultSet resultSet = ps_getTypeByIDProjID.executeQuery();
            if(resultSet.next())
            {
            	Type obj = new Type();
            	obj.id = resultSet.getInt(1);
            	obj.name = resultSet.getString(2);
                resultSet.close();
                //ps_getTypeByIDProjID.close();
            	return obj;
            }
            else
            {
                resultSet.close();
                //ps_getTypeByIDProjID.close();
            } 
            return null;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	
	public String getTypeFromParameterName(int methodID, String aPIName, int projectID) {
		try
    	{
    		ps_gTFRPN.setInt(1, methodID);
    		ps_gTFRPN.setString(2, aPIName);
    		ps_gTFRPN.setInt(3, projectID);
            ResultSet resultSet = ps_gTFRPN.executeQuery();
            if(resultSet.first())
            {
            	String type = null;
            	type = resultSet.getString(1);
            	
                resultSet.close();
                //ps_gTFRPN.close();
            	return type;
            }
            else
            {
                resultSet.close();
                //ps_gTFRPN.close();
            } 
            return null;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	
	///////////////////////////////////////////////////////////////////
	public List<String> getParentsFromRepo(Method matchingMethod) {
		try
    	{
			int method_id = matchingMethod.id;
			List<String> parents = new ArrayList<String>();
    		ps_getParentsFromRepo.setInt(1,method_id);
    		
            ResultSet resultSet = ps_getParentsFromRepo.executeQuery();
            while(resultSet.next())
            {	
            	parents.add(resultSet.getString(1));
            }
            
            resultSet.close();
            //ps_getParentsFromRepo.close();
            
            return parents;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}

	public List<String> getCallsFromRepo(Method matchingMethod) {
		try
    	{
			int method_id = matchingMethod.id;
			List<String> calls = new ArrayList<String>();
    			ps_getCallsFrmRepo.setInt(1,method_id);
    		
            ResultSet resultSet = ps_getCallsFrmRepo.executeQuery();
            while(resultSet.next())
            {	
            	calls.add(resultSet.getString(1));
            }
            
            resultSet.close();
           // ps_getCallsFrmRepo.close();
            
            return calls;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	
	public List<String> getImportsFromRepo(Method matchingMethod) {
		try
    	{
			int method_id = matchingMethod.id;
			List<String> imports = new ArrayList<String>();
    		ps_getImpFromRepo.setInt(1, method_id);
    		
            ResultSet resultSet = ps_getImpFromRepo.executeQuery();
            while(resultSet.next())
            {	
            	imports.add(resultSet.getString(1));
            }
            
            resultSet.close();
            //ps_getImpFromRepo.close();
            
            return imports;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	
	public ArrayList<String> getAPICallParameters(int apiCallID) {
		
		ArrayList<String> parameters = new ArrayList<String>();
		
    	try
    	{    		
    			ps_getAPICallParams.setInt(1,apiCallID);
            ResultSet resultSet = ps_getAPICallParams.executeQuery();
            
            while(resultSet.next())
            {            	
            	String parameterName = resultSet.getString(1);
            	parameters.add(parameterName); 	           	         	
            	
            }
            
            resultSet.close();
            //ps_getAPICallParams.close();
            return parameters;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
	}

	public List<String> getTypesFromRepo(Method matchingMethod) {
		try
    	{
			int method_id = matchingMethod.id;
			List<String> types = new ArrayList<String>();
    		ps_getTypesFromRepo1.setInt(1,method_id);
    		
            ResultSet resultSet = ps_getTypesFromRepo1.executeQuery();
            
            while(resultSet.next())
            {	
            	types.add(resultSet.getString(1));
            }  
            
           ps_getTypesFromRepo2.setInt(1,method_id);
    		
            resultSet = ps_getTypesFromRepo2.executeQuery();
            
            while(resultSet.next())
            {	
            	types.add(resultSet.getString(1));
            }
            
            resultSet.close();
            //ps_getTypesFromRepo1.close();
            //ps_getTypesFromRepo2.close();
            
            return types;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	
	public LinkedHashMap<Integer, Integer> getCalledMethods(int methodId){
    	LinkedHashMap<Integer, Integer> resultTable = new LinkedHashMap<Integer, Integer>();
    	try
    	{
    		
    		ps_getCalledMethods.setInt(1, methodId);
    		ps_getCalledMethods.setInt(2, 1);
    		ps_getCalledMethods.setInt(3, 1);
            ResultSet resultSet = ps_getCalledMethods.executeQuery();
            
            while(resultSet.next())
            {
            	
            	int mId = resultSet.getInt(1);
            	int lineno = resultSet.getInt(2);
            	if(mId!=0)
            	{
            		resultTable.put(mId, lineno);
            	}
            	
            }
            
            resultSet.close();
            //ps_getCalledMethods.close();            
            return resultTable;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
    }
	
	

	public ArrayList<APICall> getAPIUsages(int method_id) {
		
		Hashtable<String, Integer> resultTable = new Hashtable<String, Integer>();
		ArrayList<APICall> resultList = new ArrayList<APICall>();
		
    	try
    	{
    		
    		ps_getAPIUsages.setInt(1,method_id);
    		ps_getAPIUsages.setInt(2,1);
    		ps_getAPIUsages.setInt(3,1);
            ResultSet resultSet = ps_getAPIUsages.executeQuery();
            
            while(resultSet.next())
            {
            	int id = resultSet.getInt(1);
            	String APIName = resultSet.getString(2);
            	String APIUsage = resultSet.getString(3);
            	int linenum = resultSet.getInt(4);
            	
            	resultTable.put(APIName+"."+APIUsage+"()", linenum);
            	//#rug : all zeros added temporarily
            	resultList.add(new APICall(id, method_id, APIName, APIUsage, linenum, 0, 0, 0, 0));           	
            	
            }
            
            resultSet.close();
           // ps_getAPIUsages.close();
            return resultList;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
	}
	
public boolean hasAPIUsages(int method_id) {
		
		//Hashtable<String, Integer> resultTable = new Hashtable<String, Integer>();
		//ArrayList<APICall> resultList = new ArrayList<APICall>();
		
    	try
    	{
    		
    		ps_hasAPIUsages.setInt(1,method_id);
    		//ps_hasAPIUsages.setInt(2,1);
    		//ps_hasAPIUsages.setInt(3,1);
            ResultSet resultSet = ps_hasAPIUsages.executeQuery();
            
            if(resultSet.first())
            {
				if(resultSet.getInt(1)>2)
            	return true;         	
            	
            }
            
            return false;
           
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
    	}
	}
    ///////////////////////////////////////////////////////////////////
	public void close() throws SQLException 
	{		
		this.projectInsertion.close();
		this.fileInsertion.close();
		this.methodInsertion.close();
		this.methodParametersInsertion.close();
		this.methodParametersInsertion2.close();
		this.fieldInsertion.close();
		this.typeHierarchyInsertion.close();
		this.fieldInsertion.close();
		this.callsInsertion.close();
		this.usesInsertion.close();
		this.accessesInsertion.close();
		this.APIcallsInsertion.close();
		this.APIparametersInsertion.close();
		this.importsInsertion.close();
		
		this.connector.close();		
	}
	public void flushTypes() throws SQLException 
	{	
		this.typeInsertion.executeBatch();			
		this.connector.commit();		
	}
	public void flushFirstWalk() throws SQLException 
	{	
		//this.importsInsertion.executeBatch();
		this.methodInsertion.executeBatch();
		this.typeHierarchyInsertion.executeBatch();
		//this.methodParametersInsertion2.executeBatch();
		//this.fieldInsertion.executeBatch();
		this.connector.commit();		
	}
	public void flushSinglePassWalk() throws SQLException
	{
		//this.importsInsertion.executeBatch();
		this.methodInsertion.executeBatch();
		this.APIcallsInsertion.executeBatch();
		//this.typeHierarchyInsertion.executeBatch();
		//this.methodParametersInsertion2.executeBatch();
		//this.fieldInsertion.executeBatch();
		this.connector.commit();
	}

	public void flushSecondWalk() throws SQLException 
	{		
		//this.fieldInsertion.executeBatch();
		//this.methodParametersInsertion.executeBatch();
		this.callsInsertion.executeBatch();	
		//this.usesInsertion.executeBatch();			
		this.connector.commit();			
	}
	
	
	public void flushThirdWalk() throws SQLException 
	{
		//this.usesInsertion.executeBatch();
		//this.accessesInsertion.executeBatch();	
		try{
		this.APIcallsInsertion.executeBatch();
		}catch(Exception ex)
		{
			ex.toString();
		}
		//this.APIparametersInsertion.executeBatch();
		this.connector.commit();			
		
	}
	
	public boolean isParentAThread(int callerObjTypeID) {
		try
    	{
			boolean isThread = false;
			
    		ps_isParentAThread.setInt(1, callerObjTypeID);
    		
            ResultSet resultSet = ps_isParentAThread.executeQuery();
            if(!resultSet.next())            	
            	isThread = false;            
            else
            	isThread = true;
            resultSet.close();
           // ps_isParentAThread.close();
            
            return isThread;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
        }
	}
	public int getTargetMethodID(String currentIntentClass, int projectID) {
		try
    	{
			int targetID = 0;
			
    		ps_getTargetMethID.setString(1, currentIntentClass);
    		ps_getTargetMethID.setInt(2, projectID);
    		
            ResultSet resultSet = ps_getTargetMethID.executeQuery();
            if(resultSet.next())            	
            	targetID = resultSet.getInt(1);
            
            resultSet.close();
           // ps_getTargetMethID.close();
            
            return targetID;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public int getTargetOnStartMethodID(int currentClassTypeID, int projectId) {
		try
    	{
			int targetID = 0;
			
    		getTargetOnStartMethID.setInt(1, currentClassTypeID);
    		getTargetOnStartMethID.setInt(2, projectId);
    		
            ResultSet resultSet = getTargetOnStartMethID.executeQuery();
            if(resultSet.next())            	
            	targetID = resultSet.getInt(1);
            
            resultSet.close();
           // getTargetOnStartMethID.close();
            
            return targetID;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public String getFilePath(int file_id) {
		try
    	{
    		ps_getFilPath.setInt(1, file_id);    		
            ResultSet resultSet = ps_getFilPath.executeQuery();
            if(resultSet.first())
            {
            	String filePath = resultSet.getString(1);            	
                resultSet.close();
                //ps_getFilPath.close();
            	return filePath;
            }
            else
            {
                resultSet.close();
                //ps_getFilPath.close();
            } 
            return null;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	public int getLastProjectIdFromDB() {
		try
    	{
			int projectID = 0;
			
    		    		
            ResultSet resultSet = getLastProjIDFrmDB.executeQuery();
            if(resultSet.next())            	
            	projectID = resultSet.getInt(1);
            
            resultSet.close();
            //getLastProjIDFrmDB.close();
            
            return projectID;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public int getLastFunctionIDFromDB() {
		try
    	{
			int fileID = 0;
			
    		 ResultSet resultSet = ps_getLastFuncIDFrmDB.executeQuery();
            if(resultSet.next())            	
            	fileID = resultSet.getInt(1);
            
            resultSet.close();
            //ps_getLastFuncIDFrmDB.close();
            
            return fileID;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public int getLastFileIdFromDB() {
		try
    	{
			int fileID = 0;
			
    		    		
            ResultSet resultSet = ps_getLastFileIDFrmDB.executeQuery();
            if(resultSet.next())            	
            	fileID = resultSet.getInt(1);
            
            resultSet.close();
            //ps_getLastFileIDFrmDB.close();
            
            return fileID;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public int getBaseTypeByTypeName(String typeName, int projectId) {
		try
    	{
			ps_getBaseTypeByTypeName.setInt(1,projectId);
    		ps_getBaseTypeByTypeName.setString(2,typeName);

            ResultSet resultSet = ps_getBaseTypeByTypeName.executeQuery();
            
            if(resultSet.next())
            {            	
            	int id = resultSet.getInt(1);            	 
                resultSet.close();
                //ps_getBaseTypeByTypeName.close();
            	return id;
            }
            else
            {
                resultSet.close();
                //ps_getBaseTypeByTypeName.close();

            }
            return 0;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public int getParentIDInBasicPath(int methodID) {
		try
    	{
    		
    		ps_getParentIDBasicPath.setInt(1,methodID);
            ResultSet resultSet = ps_getParentIDBasicPath.executeQuery();
            
            if(resultSet.next())
            {            	
            	int id = resultSet.getInt(1);            	 
                resultSet.close();
                //ps_getParentIDBasicPath.close();
            	return id;
            }
            else
            {
                resultSet.close();
                //ps_getParentIDBasicPath.close();

            }
            return 0;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public int getProjectID(int methodID) {
		try
    	{
    		
    		ps_getProjID.setInt(1,methodID);
            ResultSet resultSet = ps_getProjID.executeQuery();
            
            if(resultSet.next())
            {            	
            	int id = resultSet.getInt(1);            	 
                resultSet.close();
                //ps_getProjID.close();
            	return id;
            }
            else
            {
            	
                resultSet.close();
               // ps_getProjID.close();

            }
            return 0;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return 0;
        }
	}
	public String getFilePathFromMethod(int methodID) {
		try
    	{
    		ps_getFilePathFromMethod.setInt(1, methodID);    		
            ResultSet resultSet = ps_getFilePathFromMethod.executeQuery();
            if(resultSet.first())
            {
            	String filePath = resultSet.getString(1);            	
                resultSet.close();
               // ps_getFilePathFromMethod.close();
            	return filePath;
            }
            else
            {
                resultSet.close();
                //ps_getFilePathFromMethod.close();
            } 
            return null;
        }
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
        }
	}
	public ArrayList<Integer> getHostMethods(int method_id) {
		ArrayList<Integer> resultList = new ArrayList<Integer>();
    	try
    	{
    		boolean hasHost = false;
    		ps_getHostMethods1.setInt(1, method_id);
    		ps_getHostMethods1.setInt(2, 1);
            ResultSet resultSet = ps_getHostMethods1.executeQuery();
            
            while(resultSet.next())
            {
            	
            	int mId = resultSet.getInt(1);
            	
            	if(mId!=0 && !resultList.contains(mId) )
            	{
            		resultList.add(mId);
            		hasHost = true;
            	}
            }
            //add start method of host class (if exists) as host method 
            if (!hasHost)
            {
            	ps_getHostMethods2.setInt(1, method_id);
        		
                resultSet = ps_getHostMethods2.executeQuery();
                while(resultSet.next())
                {
                	
                	int mId = resultSet.getInt(1);
                	
                	if(mId!=0 && !resultList.contains(mId))
                	{
                		resultList.add(mId);
                		hasHost = true;
                	}
                }
            }
            
            resultSet.close();
           // ps_getHostMethods1.close();      
           // ps_getHostMethods2.close();  
            return resultList;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
	}
	public ArrayList<Integer> getHostMethodsOldStyle(int method_id) {
		ArrayList<Integer> resultList = new ArrayList<Integer>();
    	try
    	{
    		boolean hasHost = false;
    		ps_getHostMeths.setInt(1, method_id);
    		ps_getHostMeths.setInt(2, 1);
            ResultSet resultSet = ps_getHostMeths.executeQuery();
            
            while(resultSet.next())
            {
            	
            	int mId = resultSet.getInt(1);
            	
            	if(mId!=0 && !resultList.contains(mId) )
            	{
            		resultList.add(mId);
            		hasHost = true;
            	}
            }
           
            
            resultSet.close();
            //ps_getHostMeths.close();            
            return resultList;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
	}
	public boolean projectExistsInDb(String name) {
		try
    	{
    		
    		ps_checkProjectExists.setString(1, name);
    
            ResultSet resultSet = ps_checkProjectExists.executeQuery();
            
            while(resultSet.next())
            {
            	return true;         	
            	
            }
            
            return false;
           
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
    	}
	}
	public ArrayList<Integer> getCalledMethodIDs(int methodId){
    	ArrayList<Integer> result = new ArrayList<Integer>();
    	try
    	{
    		
    		ps_getCalledMethodIDs.setInt(1, methodId);
    		ps_getCalledMethodIDs.setInt(2, 1);
    		ps_getCalledMethodIDs.setInt(3, 1);
            ResultSet resultSet = ps_getCalledMethodIDs.executeQuery();
            
            while(resultSet.next())
            {
            	
            	int mId = resultSet.getInt(1);
            	
            	if(mId!=0)
            	{
            		result.add(mId);
            	}
            	
            }
            
            resultSet.close();
            //ps_getCalledMethods.close();            
            return result;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
    }
	
	
	public int getFunctionID(String className, String functionName,
			String projectName) {
		int methodID = 0;
    	try
    	{    		
    		ps_getFunctionID.setString(1, functionName);
    		ps_getFunctionID.setString(2, className);
    		ps_getFunctionID.setString(3, projectName);
            ResultSet resultSet = ps_getFunctionID.executeQuery();
            
            if(resultSet.next())
            {            	
            	methodID = resultSet.getInt(1);         	
         	
            }            
            resultSet.close();
            //ps_getMethodIDbyFuncNameProjID.close();
            return methodID;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return methodID;
    	}
	}
	public String getProjectName(int projectId)
	{		
		String projectname = null;
		try 
		{			
			ps_getProjName.setInt(1,projectId);
            ResultSet resultSet = ps_getProjName.executeQuery();
           
            if(resultSet.next())
            {
            	projectname = resultSet.getString(1);
            }
            resultSet.close();
            //ps_getProjName.close();           
            
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return projectname;	
		
	}
	
public Integer getProjectID(String name) throws SQLException {
		
		ps_getProjIDByProjName.setString(1, name);		
		ResultSet resultSet = ps_getProjIDByProjName.executeQuery();
		int activeProjID = 0;
		while(resultSet.next())
			activeProjID = resultSet.getInt(1);
		
		return activeProjID;
	}
///////////////////////////////////////////////////////////////////
	
	
public ArrayList<Method> getMatchingMethodsSQLWayLOOCV(
		ArrayList<String> apiInvocations, String functionName,
		String activeProject) throws Exception {
	ArrayList<Method> resultList = new ArrayList<Method>();
	ps_getProjIDByProjName.setString(1, activeProject);		
	ResultSet resultSet = ps_getProjIDByProjName.executeQuery();
	int activeProjID = 0;
	while(resultSet.next())
		activeProjID = resultSet.getInt(1);
	ps_getMethodsSQLWayLOOCV.setInt(1, activeProjID);
	

	String fName = splitCamelCase(functionName);
	String[] splitFunctionName = fName.split(" ");
	StringBuilder sb = new StringBuilder();
	for(String s: splitFunctionName)
	{
		sb.append(s);
		sb.append("|");			
	}
	String name = sb.toString();
	name = name.substring(0, name.length()-1);

	ps_getMethodsSQLWayLOOCV.setString(2, name );

	sb = new StringBuilder();
	for(String s: apiInvocations)
	{
		sb.append(s);
		sb.append("|");			
	}
	String api_Invocations = sb.toString();
	api_Invocations = api_Invocations.substring(0, api_Invocations.length()-1);

	ps_getMethodsSQLWayLOOCV.setString(3, api_Invocations);


	
	try{
		resultSet = ps_getMethodsSQLWayLOOCV.executeQuery();
	}catch(Exception ex)
	{
		System.out.println("I am breaking here");

	}
	while(resultSet.next())
	{
		Method m = new Method();
		m.id = resultSet.getInt(1);
		resultList.add(m);            
	}          

	return resultList;
}

public ArrayList<String> getAPIUsagesForEvaluation(int method_id) {
	
	ArrayList<String> resultList = new ArrayList<String>();
	
	try
	{
		
		ps_getAPIUsagesForEvaluation.setInt(1,method_id);
		ps_getAPIUsagesForEvaluation.setInt(2,1);
		ps_getAPIUsagesForEvaluation.setInt(3,1);
        ResultSet resultSet = ps_getAPIUsagesForEvaluation.executeQuery();
        
        while(resultSet.next())
        {
        	int id = resultSet.getInt(1);
        	String APIName = resultSet.getString(2);
        	String APIUsage = resultSet.getString(3);       	
         	resultList.add(APIName+ "/"+ APIUsage);                  	
        }            
        resultSet.close();           
        return resultList;
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
		return null;
	}
}
public static String splitCamelCase(String input)
{
	ArrayList<String> list = new ArrayList<String> ();
	String  result="";
	
	for (String w : input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
		w = w.replaceAll("_"," ");
		w = w.replaceAll("/"," ");
		w = w.replaceAll("#"," ");
		w = w.replaceAll("[^a-zA-Z0-9]", " "); // this line replaces all special characters with spaces

		
		result += w + " ";
	}
	return result.trim();
}
public void updateMethodStats() {
	ArrayList<String> resultList = new ArrayList<String>();
	
	try
	{		
        ResultSet resultSet = ps_caclculateMethodStats.executeQuery();        
        while(resultSet.next())
        {
        	int mid = resultSet.getInt(1);
        	float density = resultSet.getFloat(2);
        	
        	ps_updateMethodStats.setFloat(1, density);
        	ps_updateMethodStats.setInt(2, mid);
        	ps_updateMethodStats.addBatch();
        }            
          
        ps_updateMethodStats.executeBatch();
       
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
		
	}
}
public boolean hasMCSMemship(int id) {
	try
	{
		
		ps_hasMCSMemship.setInt(1,id);
		
        ResultSet resultSet = ps_hasMCSMemship.executeQuery();
        
        while(resultSet.next())
        {
        	return true;         	
        	
        }
        
        return false;
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
		return false;
	}
}
public void updateAPICallIndex() throws SQLException {

    	int count = 0;
    	
    	APICall apiCall = new APICall();
    	ResultSet apiCallsResultSet = APICallSelection.executeQuery();

    	//iterate on every api call
    	while(apiCallsResultSet.next())
    	{            	
    		count += 1;
    		//System.out.println("Currently on: " + count);
    		
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
        		//System.out.println("Added");
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

	public void updateAPICallIndexTable() throws SQLException {

		APICallUpdate.executeUpdate();
		//get all distinct api calls from api_call table
		APICall apiCall = new APICall();
		ResultSet apiCallsResultSet = DistinctAPICallSelection.executeQuery();

		//iterate over results set and keep adding to a batch of inserts into api_call_index table
		while(apiCallsResultSet.next())
		{
			apiCall.fullAPIcall = apiCallsResultSet.getString(1);
			APICallIndexInsertionNew.setString(1, apiCall.fullAPIcall);
			APICallIndexInsertionNew.addBatch();

		}
		APICallIndexInsertionNew.executeBatch();
		//apiCallsResultSet.close();
		connector.commit();

		//execute the apicallindexidupdate statement to update the api_call table with index id values

		APICallIndexIdUpdate.executeUpdate();
		connector.commit();

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
public ArrayList<String> getMethodsAPICall() throws SQLException {
	ArrayList<String> result= new ArrayList<String>();
	
	ResultSet resultSet = AllMethodSelectionAPICall.executeQuery();
	while(resultSet.next())
	{
		result.add(resultSet.getInt(1)+","+resultSet.getInt(2));
	}
	return result;
}


    public int getClusterID(int id) throws SQLException {
		int result = 0;
		ps_getClusterIDofMethod.setInt(1, id);
		ResultSet clusterID = ps_getClusterIDofMethod.executeQuery();

		if(clusterID.first())
		{
			result = clusterID.getInt(1);
		}
		return result;
    }


	public ArrayList<String> getMethodAPIUsages(int id) throws SQLException {
		ArrayList<String> result= new ArrayList<String>();
		ps_getMethodAPIUsages.setInt(1, id);
		ResultSet resultSet = ps_getMethodAPIUsages.executeQuery();

		while(resultSet.next())
		{
			result.add(resultSet.getString(1)+"."+resultSet.getString(2));
		}
		return result;
	}
}