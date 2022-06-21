package structure_extraction;

import repository.*;

import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class CodeMetadata {

	private static CodeMetadata _instance = null;
	private static MySQLAccessLayer mysqlAccess;

	static {
		try {
			mysqlAccess = MySQLAccessLayer.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Hashtable<String, Integer> typeTable;
	Hashtable<String, Type> typesHashTable;
	ArrayList<Type> typeList;	
	//ArrayList<Type> usesTypeList;
	//public ArrayList<Field> fieldList;
	private ArrayList<Method> methodList;	
	private ArrayList<Calls> callsList;	
	//private ArrayList<Accesses> accesessList;
	private ArrayList<TypeHierarchy> typeHeirarchy;
	//private ArrayList<MethodParameter> methodParamList;
	//private ArrayList<APICallParameter> apiCallParametersList;
	//public ArrayList<Uses> usesList;
	private ArrayList<APICall> APICallsList;
	//private ArrayList<Imports> importsList;
	
	private CodeMetadata()
	{
		//System.out.println("In sharedspace constructor");
		typeTable = new Hashtable<String, Integer>();
		typesHashTable = new Hashtable<String, Type>();
		typeList = new ArrayList<>();
		//fieldList = new ArrayList<>();
		methodList = new ArrayList<>();
		callsList = new ArrayList<>();
		typeHeirarchy = new ArrayList<>();
		//accesessList = new ArrayList<>();
		//methodParamList = new ArrayList<>();
		//usesList = new ArrayList<>();
		//usesTypeList = new ArrayList<>();
		APICallsList = new ArrayList<>();
		//apiCallParametersList = new ArrayList<>();
		//importsList = new ArrayList<>();
	}

	public static CodeMetadata getInstance()
	{
		//m.out.println("In getInstance of SharedSpace");
		if(_instance == null)
			_instance = new CodeMetadata();
		return _instance;
	}
	
	public static void destroyInstance()
	{	

		//System.out.println("In destroy Instance");
		_instance = null;
	}
	
	//----------------------------------------------------------------
	
	public int addInType(Type obj)
	{
		obj.id = typeList.size() + 1;
		typeList.add(obj);
		typeTable.put(obj.name, obj.id);		
		return obj.id;
	}
	
	public int addInType_v2(Type obj)
	{
		typesHashTable.put(obj.name,obj);		
		return obj.id;
	}
	
	public void removeFromTypesHashTable(String typeName) {
		typesHashTable.remove(typeName);
		
	}
	public int addInCalls(Calls obj)
	{
		obj.id = callsList.size() + 1;
		callsList.add(obj);
		return obj.id;
	}
	
//	public void addInUses(Uses obj)
//	{
//		usesList.add(obj);
//	}
//
//	public int addInAccesses(Accesses obj)
//	{
//		obj.id = accesessList.size() + 1;
//		accesessList.add(obj);
//		return obj.id;
//	}
	
	public int addInTypeHeirarchy(TypeHierarchy obj)
	{
		typeHeirarchy.add(obj);
		return 0;
	}

//	public int addInField(Field obj)
//	{
//		obj.Id = fieldList.size()+1;
//		fieldList.add(obj);
//		return obj.Id;
//	}
//
//	public int addInMethodParamsList(MethodParameter obj)
//	{
//		obj.id = methodParamList.size()+1;
//		methodParamList.add(obj);
//		return obj.id;
//	}
//
	public int addInMethod(Method obj)
	{		
		methodList.add(obj);
		return obj.id;
	}
	
	public void addInAPICallsList(APICall apiCalls) 
	{
		APICallsList.add(apiCalls);		
	}
	
//	public void addInAPICallParamsList(APICallParameter apiCallParameter)
//	{
//		apiCallParametersList.add(apiCallParameter);
//	}
//
//	public void addInImports(Imports imports)
//	{
//		importsList.add(imports);
//	}
	
	//----------------------------------------------------------------
	
	public void insertProject(int projectId, String projectName, String path)
	{
		mysqlAccess.insertProjectIntoDb(projectId, projectName, path);
	}
	
	public void insertFile(int fileId, String name,int projectid)
	{
		mysqlAccess.insertFileIntoDb(fileId, name, projectid);
	}
	
	//----------------------------------------------------------------
	
	private void dumpTypeToConsole()
	{
		//System.out.println("Type");
		for(Type obj : typeList)
		{
			//System.out.println(obj.id + " - " + obj.name);
		}
	}
	
	private void dumpMethodToConsole()
	{
		//System.out.println("Methods List:");
		for(Method obj : methodList)
		{
			//System.out.println(obj.id + " - " + obj.name);
		}
	}
	
//	private void dumpFieldToConsole()
//	{
//		//System.out.println("Field");
//		for(Field obj : fieldList)
//		{
//			//System.out.println(obj.Id + " - " + obj.field_name + " - " + obj.field_type_id);
//		}
//	}
	
	public void dumpTypeInDb()
	{		
		
		for(Type obj : typeList)
		{
			mysqlAccess.insertIntoType(obj, _instance);
		}
		//dumpTypeToConsole();
	}
	public void dumpTypeInDb_v2() throws SQLException
	{		
		Set<String> keys = typesHashTable.keySet();
        for(String key: keys){
			mysqlAccess.insertIntoType_v2(typesHashTable.get(key));
        }

		mysqlAccess.flushTypes();
	}
	public void dumpMethodInDb()
	{		
		for(Method obj : methodList)
		{
			mysqlAccess.insertMethodintoDb2(obj);
		}
		//dumpMethodToConsole();
	}
	
//	public void dumpFieldInDb()
//	{
//		for(Field obj : fieldList)
//		{
//			mysqlAccess.insertFieldIntoDb(obj);
//		}
//		//dumpFieldToConsole();
//	}
	
	public void dumpCallsInDb()
	{		
		for(Calls obj : callsList)
		{
			mysqlAccess.insertCallsIntoDb(obj);
		}		
	}
	
	public void dumpTypeHeirarchyInDb()
	{		
		for(TypeHierarchy obj : typeHeirarchy)
		{
			mysqlAccess.inserttypeHeirarchyIntoDb(obj);
		}
	}
	
//	public void dumpAccessesInDb()
//	{
//		for(Accesses obj : accesessList)
//		{
//			mysqlAccess.insertAccessesIntoDb(obj);
//		}
//	}
	
	public void dumpAPICallsInDb() 
	{		
		for(APICall obj : APICallsList)
		{
			mysqlAccess.insertAPICallsIntoDb(obj);
		}		
	}
	
//	public void dumpAPICallParameterInDb()
//	{
//		for(APICallParameter obj : apiCallParametersList)
//		{
//			mysqlAccess.insertAPICallParameterIntoDb(obj);
//		}
//	}
//
//	public void dumpUsesInDb()
//	{
//		for(Uses obj : usesList)
//		{
//			mysqlAccess.insertUsesIntoDb(obj);
//		}
//	}
//
//	public void dumpMethodParamInDb()
//	{
//		for(MethodParameter obj : methodParamList)
//		{
//			mysqlAccess.insertMethodParamIntoDb(obj);
//		}
//	}
//
//	public void dumpMethodParamInDb2()
//	{
//		for(MethodParameter obj : methodParamList)
//		{
//			mysqlAccess.insertMethodParamIntoDb2(obj);
//		}
//	}
//
//	public void dumpImportsInDb()
//	{
//		for(Imports obj : importsList)
//		{
//			mysqlAccess.insertImportsIntoDb(obj);
//		}
//	}

	//----------------------------------------------------------------

	public void initializeDatabaseConnection() throws Exception 
	{
		mysqlAccess.initialize();
	}
	public void flushTypes() throws SQLException 
	{
		mysqlAccess.flushTypes();
	}
	public void flushFirstWalk() throws SQLException 
	{
		mysqlAccess.flushFirstWalk();
	}
	public void flushSinglePassWalk() throws SQLException
	{
		mysqlAccess.flushSinglePassWalk();
	}

	public void flushSecondWalk() throws SQLException 
	{
		mysqlAccess.flushSecondWalk();
	}
	
	public void flushThirdWalk() throws SQLException
	{
		mysqlAccess.flushThirdWalk();
		
	}

	public void close() throws SQLException 
	{
		mysqlAccess.close();
	}
	
	//----------------------------------------------------------------

	public void updateTypeTable(Type obj)
	{		
		mysqlAccess.updateTypeTable(obj);		
	}
	
	public int getTypeId(String typeName,int projectId) 
	{	
		try 
		{			
			Type obj= mysqlAccess.getTypeByName(typeName,projectId);			
			if(null == obj)
			{
				return -1;
			}
			return obj.id;
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return -1;
	}
	
	public Method getMethodId(String methodName,int currentClassTypeID, int projectID)
	{
		Method method = new Method(); 		
		method = mysqlAccess.getMethodId(methodName, currentClassTypeID, projectID);
		return method;
	}
	
	public Method getTargetMethodId(String funcName, int projectID) {
		Method method = new Method(); 		
		method = mysqlAccess.getTargetMethodId(funcName, projectID);
		return method;
	}
	
	public int getMethodId(int callerObjTypeID, String funcName, int projectId) {
		
		return mysqlAccess.getMethodId(callerObjTypeID, funcName,  projectId);
	}

	public Object getTypeNameByName(String aPIName, int projectId) 
	{
		try 
		{
			Type obj= mysqlAccess.getTypeByName(aPIName,projectId);
			if(null == obj)
			{
				return null;
			}
			return obj.name;
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return null;
	}
	
	public Type getTypeInfoByName(String aPIName, int projectId) 
	{
		try 
		{
			Type obj= mysqlAccess.getTypeByName(aPIName,projectId);
			if(null == obj)
			{
				return null;
			}
			return obj;
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return null;
	}

	public int getBaseTypeByTypeId(int typeID, int projectId) 
	{
		try 
		{			
			int Id = mysqlAccess.getBaseTypeByTypeId(typeID, projectId);
			return Id;
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getBaseTypeByTypeName(String typeName, int projectId) 
	{
		try 
		{			
			int Id = mysqlAccess.getBaseTypeByTypeName(typeName, projectId);
			return Id;
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return 0;
	}

	public String getTypeNameByTypeID(int typeID, int projectId) 
	{		
		try 
		{			
			Type obj= mysqlAccess.getTypeById(typeID, projectId);			
			if(null == obj)
			{
				return null;
			}
			return obj.name;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String getTypeFromParameterName(int methodID, String aPIName, int projectID) {
		
		return mysqlAccess.getTypeFromParameterName(methodID, aPIName, projectID);
	}
	
	public String getIdFile(String name,int projectid)
	{
		
		
		String id = mysqlAccess.getIdFile( name, projectid);
		
		return id;
	}
	
	public int getFieldId(String calleeFieldName,String callerObjectName, int projectID)
	{		
		int id = mysqlAccess.getFieldId(calleeFieldName, callerObjectName, projectID);		
		return id;
	}
	
	public int getTypeIdFromField(String typeName,int fileId) 
	{
		try 
		{
			int Id= mysqlAccess.getTypeFromField(typeName,fileId);
			return Id;
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getCurrentMethodID()
	{
		return methodList.size();
	}

	public boolean searchInArray(ArrayList<String> classArray, String searchName)
	{
		for(int i=0;i< classArray.size(); i++)
		{
			if(classArray.get(i).equals(searchName)){
				return true;
			}
		}
		return false;
	}

	public boolean isParentThread(int callerObjTypeID) {
		try 
		{			
			return mysqlAccess.isParentAThread(callerObjTypeID);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return false;
	}

	public int getTargetMethodID(String currentIntentClass, int projectID) {
		try 
		{			
			return mysqlAccess.getTargetMethodID(currentIntentClass, projectID);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return 0;
	}

	public int getTargetOnStartMethodID(int currentClassTypeID, int projectId) {
		try 
		{			
			return mysqlAccess.getTargetOnStartMethodID(currentClassTypeID, projectId);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return 0;
	}

	public String getFilePath(int file_id) {
		try 
		{			
			return mysqlAccess.getFilePath(file_id);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return null;
	}

	public int getLastProjectIdFromDB() {
		try 
		{			
			return mysqlAccess.getLastProjectIdFromDB();			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getLastFileIdFromDB() {
		try 
		{			
			return mysqlAccess.getLastFileIdFromDB();			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getLastFunctionIDFromDB() {
		try 
		{			
			return mysqlAccess.getLastFunctionIDFromDB();			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getParentIDInBasicPath(int methodID) {
		try 
		{			
			return mysqlAccess.getParentIDInBasicPath(methodID);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getProjectID(int methodID) {
		try 
		{			
			return mysqlAccess.getProjectID(methodID);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return 0;
	}

	public String getFilePathFromMethod(int id) {
		try 
		{			
			return mysqlAccess.getFilePathFromMethod(id);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return null;
	}

	public boolean projectExistsInDb(String name) {
		try 
		{			
			return mysqlAccess.projectExistsInDb(name);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return false;
	}

	


	public int getFunctionID(String className, String functionName,
			String projectName) throws SQLException {
		return mysqlAccess.getFunctionID(className, functionName,  projectName);
		
	}
	public String getProjectName(int projectID) {
		try 
		{			
			return mysqlAccess.getProjectName(projectID);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return null;
	}

	public Integer getProjectID(String name) {
		try 
		{			
			return mysqlAccess.getProjectID(name);			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		return 0;
	}

	public void updateMethodStats() {
		try 
		{			
			mysqlAccess.updateMethodStats();			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		
		
	}

	public void updateAPICallIndex() {
		try 
		{			
			mysqlAccess.updateAPICallIndex();			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
	}
	public void updateAPICallIndexTable() {
		try
		{
			mysqlAccess.updateAPICallIndexTable();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	public void createDensityCSVFile() {
		try 
		{		
			ArrayList<String> methods = mysqlAccess.getMethods();

			//save arraylist in csv file
			
			 FileWriter myWriter = new FileWriter("output/mid_apicalldensity.csv");
			//FileWriter myWriter = new FileWriter("mid_density_dugeonmaster3.csv");
			 myWriter.write("mID,density\n");
			 for(String s: methods)
			 {
		      myWriter.write(s+"\n");
		      
			 }
		      myWriter.close();
			
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
	}

	public void createMethodAPICallCSVFile() {
		try 
		{		
			ArrayList<String> methods = mysqlAccess.getMethodsAPICall();

			//save arraylist in csv file
			
			 FileWriter myWriter = new FileWriter("output/mid_apicall.csv");
			//FileWriter myWriter = new FileWriter("mid_density_dugeonmaster3.csv");
			 myWriter.write("mID,APICall\n");
			 for(String s: methods)
			 {
		      myWriter.write(s+"\n");
		      
			 }
		      myWriter.close();
				
			
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
	}
	


	

}
