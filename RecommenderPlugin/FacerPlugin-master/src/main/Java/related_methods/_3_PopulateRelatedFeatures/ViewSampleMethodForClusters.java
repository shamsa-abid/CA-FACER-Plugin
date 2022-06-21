package related_methods._3_PopulateRelatedFeatures;

import RelatedMethods.DataObjects.Method;
import RelatedMethods.db_access_layer.DatabaseAccessLayer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ViewSampleMethodForClusters {

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException
	{	//15 and 8 is a really great suggestion of related features
		//input cluster ID
		//String clusterIDs ="7 116 115 63 72 333 233 328 ";
		String clusterIDs ="8263 755 ";
		String[] clustersList = clusterIDs.split(" ");
		//
		viewMethodsAgainstClusterIDs(clustersList);
		
		int PID = 6;
		//viewMethodsAgainstClusterIDsPID(clustersList,PID);
		//ArrayList<Integer> mList = new ArrayList<Integer>();
		//mList.add(18383);
		//mList.add(19198);
		//mList.add(4557);
		//viewMethods(mList);
		
		
		
	}



	private static void viewMethodsAgainstClusterIDsPID(String[] clustersList,
			int pID) 
		throws ClassNotFoundException, SQLException, IOException {
		
			DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
			dbLayer.initializeConnectorToDisplayMethodBodies();	
			
			//get the first method  in each cluster along with projectID and fileName 
			ArrayList<Method> methodsList = new ArrayList<Method>();
			for(String clusterID: clustersList)
			{
				Method method = dbLayer.getMethodFromProject(Integer.parseInt(clusterID), pID);
				methodsList.add(method);
				
			}
			
			//and display their bodies
			//iterate over methodIDs and display each method body
			int methodnum = 1;
			for(Method m: methodsList)
			{
				ArrayList<String> body = getMethodBody(m);
				ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
				System.out.println("-----------------------------------------------------------");
				System.out.println("Method No. " + methodnum);
				System.out.println("-----------------------------------------------------------");
				methodnum+=1;
				//System.out.println("Project ID:" + m.projectID);
				//System.out.println("Method ID:" + m.id);
				System.out.println("Method Name:" + m.name);
				//System.out.println("File Name:" + m.file_name);
				System.out.println("Method Body:");
				for(String s: body)
				{
					System.out.println(s);
				}
				System.out.println("Method API calls:");
				for(String s: api_calls)
				{
					System.out.println(s);
				}
			}
			dbLayer.closeConnector();
		}



	public static void viewMethodsAgainstClusterIDs(String[] clustersList)
			throws ClassNotFoundException, SQLException, IOException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(String clusterID: clustersList)
		{
			Method method = dbLayer.getFirstMethod(Integer.parseInt(clusterID));
			methodsList.add(method);
			
		}
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		for(Method m: methodsList)
		{
			ArrayList<String> body = getMethodBody(m);
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			System.out.println("-----------------------------");
			System.out.println("Project ID:" + m.projectID);
			System.out.println("Method ID:" + m.id);
			System.out.println("Method Name:" + m.name);
			System.out.println("File Name:" + m.file_name);
			//System.out.println("Method Body:");
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		}
		dbLayer.closeConnector();
	}
	
	public static void viewMethodsAgainstClusterIDs(Integer[] clustersList)
			throws ClassNotFoundException, SQLException, IOException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getFirstMethod(clusterID);
			methodsList.add(method);
			
		}
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		int recNo = 1;
		for(Method m: methodsList)
		{
			System.out.println("-------------------------------------------------------");
			System.out.println("Recommendation No. "+ recNo++);
			System.out.println("-------------------------------------------------------");
			ArrayList<String> body = getMethodBody(m);
			
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			
			//System.out.println("Project ID:" + m.projectID);
			//System.out.println("Cluster ID:" + m.clusterID);
			//System.out.println("Method ID:" + m.id);
			//System.out.println("Method Name:" + m.name);
			//System.out.println("File Name:" + m.file_name);
			//System.out.println("Method Body:");
			
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("-------------------------------------------------------");
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		}
		dbLayer.closeConnector();
	}

	public static void viewMethodsAgainstClusterIDsNew(Integer[] clustersList, int PID)
			throws ClassNotFoundException, SQLException, IOException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getMethodFromProject(clusterID, PID);
			if(method.id == 0)
				method = dbLayer.getFirstMethod(clusterID);
			methodsList.add(method);
			
		}
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		int recNo = 1;
		for(Method m: methodsList)
		{
			System.out.println("-------------------------------------------------------");
			System.out.println("Recommendation No. "+ recNo++);
			System.out.println("-------------------------------------------------------");
			ArrayList<String> body = getMethodBody(m);
			
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			
			//System.out.println("Project ID:" + m.projectID);
			//System.out.println("Cluster ID:" + m.clusterID);
			//System.out.println("Method ID:" + m.id);
			//System.out.println("Method Name:" + m.name);
			//System.out.println("File Name:" + m.file_name);
			//System.out.println("Method Body:");
			
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("-------------------------------------------------------");
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		}
		dbLayer.closeConnector();
	}
	
	

	public static ArrayList<String> getMethodBody(Method m) throws IOException
	{
		ArrayList<String> s = new ArrayList<String>();
				
			String file_path = m.file_name;
			int from_line = m.from_line_num;
			int to_line = m.to_line_num;
			
			int line_num = from_line;
			try{
			while(line_num <= to_line)
			{
				String line = Files.readAllLines(Paths.get(file_path)).get(line_num-1);
				s.add(line);
				line_num++;
			}
			}catch(Exception ex)
			{
				System.out.println("No data available...something wrong with file");
			}
		
		return s; 
		
	}



	public static ArrayList<Integer> viewMethodsAgainstClusterIDs(Integer[] clustersList,
			int projectID) throws ClassNotFoundException, SQLException, IOException 
	{
		ArrayList<Integer> methodIDs = new ArrayList<Integer>();
		
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getMethodFromProject(clusterID, projectID);
			if(method.id == 0)
				method = dbLayer.getFirstMethod(clusterID);
			methodsList.add(method);
			methodIDs.add(method.id);
		}
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		for(Method m: methodsList)
		{
			ArrayList<String> body = getMethodBody(m);
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			System.out.println("-----------------------------");
			//System.out.println("Project ID:" + m.projectID);
			//System.out.println("Cluster ID:" + m.clusterID);
			//System.out.println("Method ID:" + m.id);
			//System.out.println("Method Name:" + m.name);
			//System.out.println("File Name:" + m.file_name);
			//System.out.println("Method Body:");
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		}
		dbLayer.closeConnector();
		return methodIDs;
	}
	public static ArrayList<Method> getMethodsAgainstClusterIDsWithSupport(Map<Integer, Integer> clustersList,
																			int projectID) throws ClassNotFoundException, SQLException, IOException
	{
		ArrayList<Integer> methodIDs = new ArrayList<Integer>();

		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();

		//get the first method  in each cluster along with projectID and fileName
		ArrayList<Method> methodsList = new ArrayList<Method>();

		Set set = clustersList.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();

			int clusterID = Integer.parseInt(me.getKey().toString());
			Method method = dbLayer.getMethodFromProjectRelativeFilePath(clusterID, projectID);
			if(method.id == 0)
				method = dbLayer.getFirstMethodRelativePath(clusterID);
			method.support = Integer.parseInt(me.getValue().toString());
			methodsList.add(method);
			methodIDs.add(method.id);
		}



		//and display their bodies
		//iterate over methodIDs and display each method body
		//for(Method m: methodsList)
		//{
		//ArrayList<String> body = getMethodBody(m);
		//ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
		//System.out.println("-----------------------------");
		//System.out.println("Project ID:" + m.projectID);
		//System.out.println("Cluster ID:" + m.clusterID);
		//System.out.println("Method ID:" + m.id);
		//System.out.println("Method Name:" + m.name);
		//System.out.println("File Name:" + m.file_name);
		//System.out.println("Method Body:");
		//for(String s: body)
		//{
		//	System.out.println(s);
		//}
		//System.out.println("Method API calls:");
		//for(String s: api_calls)
		//{
		//	System.out.println(s);
		//}
		//}
		dbLayer.closeConnector();
		return methodsList;
	}
	public static ArrayList<Integer> getMethodsAgainstClusterIDs(Integer[] clustersList,
			int projectID) throws ClassNotFoundException, SQLException, IOException 
	{
		ArrayList<Integer> methodIDs = new ArrayList<Integer>();
		
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getMethodFromProject(clusterID, projectID);
			if(method.id == 0)
				method = dbLayer.getFirstMethod(clusterID);
			methodsList.add(method);
			methodIDs.add(method.id);
		}
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		//for(Method m: methodsList)
		//{
			//ArrayList<String> body = getMethodBody(m);
			//ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			//System.out.println("-----------------------------");
			//System.out.println("Project ID:" + m.projectID);
			//System.out.println("Cluster ID:" + m.clusterID);
			//System.out.println("Method ID:" + m.id);
			//System.out.println("Method Name:" + m.name);
			//System.out.println("File Name:" + m.file_name);
			//System.out.println("Method Body:");
			//for(String s: body)
			//{
			//	System.out.println(s);
			//}
			//System.out.println("Method API calls:");
			//for(String s: api_calls)
			//{
			//	System.out.println(s);
			//}
		//}
		dbLayer.closeConnector();
		return methodIDs;
	}

	public static ArrayList<Integer> getMethodIDsAgainstClusterIDs(Integer[] clustersList,
			int projectID) throws ClassNotFoundException, SQLException, IOException 
	{
		ArrayList<Integer> methodIDs = new ArrayList<Integer>();
		
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getMethodFromOtherProject(clusterID, projectID);
			if(method.id == 0)
				method = null;
			methodsList.add(method);
			methodIDs.add(method.id);
		}
		
		
		dbLayer.closeConnector();
		return methodIDs;
	}



	public static void viewMethods(ArrayList<Integer> methodIDsList) throws ClassNotFoundException, SQLException, IOException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer methodID: methodIDsList)
		{
			Method method = dbLayer.getMethod(methodID);
			methodsList.add(method);
			
		}
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		for(Method m: methodsList)
		{
			ArrayList<String> body = getMethodBody(m);
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			System.out.println("-----------------------------");
			
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		}
		dbLayer.closeConnector();
		
	}
	public static void viewMethod(int methodID) throws ClassNotFoundException, SQLException, IOException {
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		Method m = dbLayer.getMethod(methodID);
	
		
		//and display their bodies
		//iterate over methodIDs and display each method body
		
			ArrayList<String> body = getMethodBody(m);
			ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
			System.out.println("-----------------------------");
			
			for(String s: body)
			{
				System.out.println(s);
			}
			System.out.println("Method API calls:");
			for(String s: api_calls)
			{
				System.out.println(s);
			}
		
		dbLayer.closeConnector();
		
	}



	public static ArrayList<Integer> getMethodIDsAgainstClusterIDsFromOtherProjects(Integer[] clustersList,
			int projectID) throws ClassNotFoundException, SQLException, IOException 
	{
		ArrayList<Integer> methodIDs = new ArrayList<Integer>();
		
		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();	
		
		//get the first method  in each cluster along with projectID and fileName 
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getMethodFromProject(clusterID, projectID);
			if(method.id == 0)
				method = dbLayer.getFirstMethod(clusterID);
			methodsList.add(method);
			methodIDs.add(method.id);
		}
		
		
		dbLayer.closeConnector();
		return methodIDs;
	}

	public static ArrayList<Method> getMethodsAgainstClusterIDsArrayList(ArrayList<Integer> clustersList,
																 int projectID) throws ClassNotFoundException, SQLException, IOException
	{
		//ArrayList<Method> methodIDs = new ArrayList<Integer>();

		DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
		dbLayer.initializeConnectorToDisplayMethodBodies();

		//get the first method  in each cluster along with projectID and fileName
		ArrayList<Method> methodsList = new ArrayList<Method>();
		for(Integer clusterID: clustersList)
		{
			Method method = dbLayer.getMethodFromProjectRelativeFilePath(clusterID, projectID);
			if(method.id == 0)
				method = dbLayer.getFirstMethodRelativePath(clusterID);
			//method.support = Integer.parseInt(me.getValue().toString());
			methodsList.add(method);
			//methodIDs.add(method);

		}

		//and display their bodies
		//iterate over methodIDs and display each method body
		//for(Method m: methodsList)
		//{
		//ArrayList<String> body = getMethodBody(m);
		//ArrayList<String> api_calls = dbLayer.getMethodAPICalls(m);
		//System.out.println("-----------------------------");
		//System.out.println("Project ID:" + m.projectID);
		//System.out.println("Cluster ID:" + m.clusterID);
		//System.out.println("Method ID:" + m.id);
		//System.out.println("Method Name:" + m.name);
		//System.out.println("File Name:" + m.file_name);
		//System.out.println("Method Body:");
		//for(String s: body)
		//{
		//	System.out.println(s);
		//}
		//System.out.println("Method API calls:");
		//for(String s: api_calls)
		//{
		//	System.out.println(s);
		//}
		//}
		dbLayer.closeConnector();
		return methodsList;
	}


}
