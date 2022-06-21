package related_methods.APIUsageSequenceExtraction;

import repository.APICall;
import structure_extraction.CodeMetadata;
import structure_extraction.MySQLAccessLayer;
import RelatedMethods.utils.DepthFirstTraversal.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
//import utils.DepthFirstTraversal.Node;
//import utils.FoldableTree;
//import utils.Graph;
//import utils.TreeLayout;
//import utils.JGraphXAdapterDemo;
//import utils.JGraphXAdapterDemo.MyEdge;

public class APIUsageSequenceExtraction {
	
	private static ArrayList<Integer> userSelectedSequence;
	private static Object leafAncestorForSelectedSequence = new Object();

	private static CodeMetadata codeMetadata;
	private static MySQLAccessLayer mysqlAccess;

	static {
		try {
			mysqlAccess = MySQLAccessLayer.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LinkedHashMap<Integer, Integer> getCalledMethods2(int methodId) throws Exception
	{
		mysqlAccess = MySQLAccessLayer.getInstance();
		if(!mysqlAccess.recommendationConnectorInitialized)
			mysqlAccess.initializeConnectorForRecomemndation();
		LinkedHashMap<Integer, Integer> methodList;	//methodID, line_num
		try {
			methodList = mysqlAccess.getCalledMethods(methodId);
			return methodList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static LinkedHashMap<Integer, Integer> getCalledMethods(int methodId)
	{
		LinkedHashMap<Integer, Integer> methodList;	//methodID, line_num 	
		try {			
			methodList = mysqlAccess.getCalledMethods(methodId);
			return methodList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	
	public static ArrayList<Integer> getCalledMethodIDs(int methodId) 
	{
		
		
		ArrayList<Integer> methodList;	//methodID, line_num 	
		try {			
			methodList = mysqlAccess.getCalledMethodIDs(methodId);
			return methodList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<Integer> getDescendantMethodIDs(int method_id, ArrayList<Integer> cumulatingMethodIDs) throws Exception
	{
		mysqlAccess.initializeConnectorForRecomemndation();
		return extracted(method_id, cumulatingMethodIDs);

	}
	private static ArrayList<Integer> extracted(int method_id,
												ArrayList<Integer> cumulatingMethodIDs) throws Exception {
		//System.out.println("Method ID:" + method_id);

		Node rootMethod = new Node(method_id);
		rootMethod.isVisited = true;

		ArrayList<Integer> methodList = getCalledMethodIDs(method_id);

		//for each method in methodlist, make its neighbor

		if(methodList!=null)//interleave api usages in proper sequence
		{
			for(Integer obj: methodList)
			{
				int retrievedMethodID = obj;
				if(retrievedMethodID != rootMethod.value)//to avoid self-recursive loop
				{
					Node m = new Node(retrievedMethodID);
					//System.out.println("Child:" + retrievedMethodID + " of root: "+ method_id);
					rootMethod.addneighbours(m);

					if(!m.isVisited && cumulatingMethodIDs.size() <= 20)
					{
						cumulatingMethodIDs.add(retrievedMethodID);
						extracted(retrievedMethodID, cumulatingMethodIDs);

					}
				}
			}
		}
		return cumulatingMethodIDs;
	}
	
	public static ArrayList<String> DFSAPIUsageSequencePrinting(int method_id, ArrayList<String> fullAPIUsageSequence, int depth) 
	{
		int recDepth = depth;
		System.out.println("Method ID:" + method_id);
		Node rootMethod =new Node(method_id);			
		rootMethod.isVisited=true;		
		//print API sequence of method
		ArrayList<APICall> APIUsageSequenceList = getAPIUsage(method_id);			
		LinkedHashMap<Integer, Integer> methodList = getCalledMethods(method_id);
		//for each method in methodlist, make its neighbor 	
		if(methodList == null)
		{
			//simply converts arraylist of APICall to ArrayList of Strings
			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, recDepth);
			if(apiUsageSequence.size()!=0)
			{
				
				for(String s : apiUsageSequence)
		        {
		        	fullAPIUsageSequence.add(s);
		        }
			}
		}
		if(methodList!=null)//interleave api usages in proper sequence
		{
			List<Object> interleavedObjectsList = new ArrayList<Object>();
			interleavedObjectsList = merge(APIUsageSequenceList, methodList);
			for(Object obj: interleavedObjectsList)
			{
				 if (obj.getClass() == APICall.class) 
				 {
					 APICall retrievedAPICall = (APICall) obj;
										
					 if(isInteresting(retrievedAPICall) )//&& APIUsage.line_num!=prevLine )
					 {	
						String parameters = getAPICallParameters(retrievedAPICall.id); 
						//if(recDepth >= 1)
						//{
							//for(int i = 0; i < recDepth; i++)
						String tab = "";
						//for(int i = 0; i < recDepth*5; i++)
						//{
							//tab += " ";
						//}
								System.out.println(tab + recDepth);
						//}
			            System.out.println(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + parameters + ")" + "\t" + retrievedAPICall.line_num );
			            //fullAPIUsageSequence.add(tab + retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "( " +  ")" + "\t" );
			            fullAPIUsageSequence.add(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "( " +  ")" + "\t" );
					 }
						
				 }
				 else//its a methodList 
				 {
					int retrievedMethodID = (int) obj;
					if(retrievedMethodID != rootMethod.value)//to avoid self-recursive loop
					{	
						 Node m = new Node(retrievedMethodID);
						 System.out.println("Child:" + retrievedMethodID + " of root: "+ method_id);
						 rootMethod.addneighbours(m);						
								
						 if(!m.isVisited)
						 {
							 recDepth += 1;
							 //added below line from FOCUS2 workspace
							 if(support.Constants.restrictAPISequenceDepth && recDepth <= 4)
								 DFSAPIUsageSequencePrinting(retrievedMethodID, fullAPIUsageSequence, recDepth);
							 recDepth -= 1;
						 }
					}
				        					 
				 }
				
			}
			
		}
		return fullAPIUsageSequence;	
        
	}
	
	//printing API usages in ancestors of a method
	/*public static ArrayList<String> DFSAPIUsageSequenceAncestorsPrinting(int method_id, ArrayList<String> fullAPIUsageSequence, int depth) 
	{
		int recDepth = depth;
		System.out.println("Method ID:" + method_id);
		Node rootMethod =new Node(method_id);			
		rootMethod.visited=true;		
		//print API sequence of method
		ArrayList<APICall> APIUsageSequenceList = getAPIUsage(method_id);		
			
		LinkedHashMap<Integer, Integer> methodList = getHostMethods(method_id);
		//for each method in methodlist, make its neighbor 	
		if(methodList == null)
		{
			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, recDepth);
			if(apiUsageSequence.size()!=0)
			{
				
				for(String s : apiUsageSequence)
		        {
		        	fullAPIUsageSequence.add(s);
		        }
			}
		}
		if(methodList!=null)//interleave api usages in proper sequence
		{
			List<Object> interleavedObjectsList = new ArrayList<Object>();
			interleavedObjectsList = merge(APIUsageSequenceList, methodList);
			for(Object obj: interleavedObjectsList)
			{
				 if (obj.getClass() == APICall.class) 
				 {
					 APICall retrievedAPICall = (APICall) obj;
										
					 if(isInteresting(retrievedAPICall) )//&& APIUsage.line_num!=prevLine )
					 {	
						String parameters = getAPICallParameters(retrievedAPICall.id); 
						//if(recDepth >= 1)
						//{
							//for(int i = 0; i < recDepth; i++)
						String tab = "";
						for(int i = 0; i < recDepth*5; i++)
						{
							tab += " ";
						}
								System.out.println(tab + recDepth);
						//}
			            System.out.println(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + parameters + ")" + "\t" + retrievedAPICall.line_num );
			            fullAPIUsageSequence.add(tab + retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + parameters + ")" + "\t" + retrievedAPICall.line_num);
					 }
						
				 }
				 else//its a methodList 
				 {
					 int retrievedMethodID = (int) obj;
							
					 Node m = new Node(retrievedMethodID);
					 System.out.println("Child:" + retrievedMethodID + " of root: "+ method_id);
					 rootMethod.addneighbours(m);						
							
					 if(!m.visited)
					 {
						 recDepth+=1;
						 DFSAPIUsageSequenceAncestorsPrinting(retrievedMethodID, fullAPIUsageSequence, recDepth);
						 recDepth-=1;
					 }
				        					 
				 }
				
			}
			
		}
		return fullAPIUsageSequence;	
        
	}*/
	
	public static ArrayList<Integer> getDFSRoots(int method_id, ArrayList<Integer> rootsList) 
	{
		
		System.out.println("Method ID:" + method_id);
		Node startMethod =new Node(method_id);			
		startMethod.isVisited=true;		
					
		ArrayList<Integer> methodList = getHostMethodsOldStyle(method_id);
		
		if(methodList.size() == 0 && !rootsList.contains(method_id))
		{			
		    rootsList.add(method_id);		   
		}
		else
		{
			for(int i: methodList)
			{				 			
				 Node m = new Node(i);				 
				 startMethod.addneighbours(m);					
						
				 if(!m.isVisited)
				 {					
					 getDFSRoots(i, rootsList);
					 
				 }				
			}
		}
		return rootsList;	
        
	}
	
	public static ArrayList<Integer> getHostMethods(int method_id) {
		ArrayList<Integer> methodList;	//methodID, line_num 	
		try {			
			methodList = mysqlAccess.getHostMethods(method_id);
			return methodList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static ArrayList<Integer> getHostMethodsOldStyle(int method_id) {
		ArrayList<Integer> methodList;	//methodID, line_num 	
		try {			
			methodList = mysqlAccess.getHostMethodsOldStyle(method_id);
			return methodList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	



	//
	private static List<Object> merge(
			ArrayList<APICall> aPIUsageSequenceList,
			LinkedHashMap<Integer, Integer> methodList) {
		
		ArrayList<Object> mergedList = new ArrayList<Object>();
			
		while(aPIUsageSequenceList.size()!=0 || methodList.size()!=0)
			{
				if(aPIUsageSequenceList.size()!=0)
				{
					int alinenum = aPIUsageSequenceList.get(0).line_num;
					if(methodList.size()!=0)
					{
						Entry<Integer, Integer> entry = methodList.entrySet().iterator().next();
					
						Integer key = entry.getKey();
						Integer value = entry.getValue();
						int mlinenum = value;
						int MID = key;
						if(alinenum < mlinenum)
						{
							mergedList.add(aPIUsageSequenceList.get(0));
							aPIUsageSequenceList.remove(0);
						}
						else
						{
								mergedList.add(key);
								methodList.remove(MID);
							
						}
					}
					else
					{
						mergedList.add(aPIUsageSequenceList.get(0));
						aPIUsageSequenceList.remove(0);
					}
					
				}
				else
				{
					Entry<Integer, Integer> entry = methodList.entrySet().iterator().next();
					if(entry != null)
					{
						Integer key = entry.getKey();	
						Integer value = entry.getValue();
						int MID = key;
						mergedList.add(key);
						methodList.remove(MID);
					}
					
				}
				
			}
		
	
		
		return mergedList;
	}



	/*public static ArrayList<String> APIUsageSequencePrinting(int method_id) 
	{
		ArrayList<String> fullAPIUsageSequence = new ArrayList<String>();
		
		ArrayList<APICall> APIUsageSequenceList = APIUsageSequenceExtraction.getAPIUsage(method_id);		
		ArrayList<String> apiUsageSequence = APIUsageSequenceExtraction.printAPIUsageSequence(APIUsageSequenceList);
		for(String s : apiUsageSequence)
        {
        	fullAPIUsageSequence.add(s);
        }
		fullAPIUsageSequence.add("------------");
		LinkedHashMap<Integer, Integer> methodList = getCalledMethods(method_id);	
		
		Set<Integer> keys = methodList.keySet();		
        for(Integer MID: keys){   
        	//APIUsageSequencePrinting(MID); 
        	ArrayList<APICall> APIUsageSequenceList2 = getAPIUsage(MID);    	
        	//prints an individual methods API sequence
        	ArrayList<String> apiUsageSequence2 = printAPIUsageSequence(APIUsageSequenceList2);
            System.out.println("------------");
            for(String s : apiUsageSequence2)
            {
            	fullAPIUsageSequence.add(s);
            }
            fullAPIUsageSequence.add("------------");
        }
        return fullAPIUsageSequence;
        
	}*/

	public static ArrayList<String> printAPIUsageSequence(ArrayList<APICall> APIUsageSequenceList, int recDepth) {
	   
		ArrayList<String> APIUsageSequence = new ArrayList<String>(); 
		 int prevLine = -1;
		 for(APICall APIUsage: APIUsageSequenceList){
			
			 if(isInteresting(APIUsage) )//&& APIUsage.line_num!=prevLine )
			 {	
				//String parameters = getAPICallParameters(APIUsage.id); 
				//if(recDepth > 1)
				//{
				String tab = "";
					//for(int i = 0; i < recDepth*5; i++)
					//{
					//	tab += " ";
					//}
						//System.out.println(tab + recDepth);
				//}
	            //System.out.println(APIUsage.api_name + "." + APIUsage.api_usage + "(" + parameters + ")" + "\t" + APIUsage.line_num );
	            //APIUsageSequence.add(tab + APIUsage.api_name + "." + APIUsage.api_usage + "( " + ")");
	            APIUsageSequence.add(APIUsage.api_name + "." + APIUsage.api_usage + "( " + ")");
	            prevLine = APIUsage.line_num;
			 }
	       
		 }
		return APIUsageSequence;
	}

	private static String getAPICallParameters(int id) {
		ArrayList<String> APIParametersList;
		String concatenatedParameters = "";
		try {			
			APIParametersList = mysqlAccess.getAPICallParameters(id);
			for(String p: APIParametersList)
			{
				concatenatedParameters += p + ",";
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return concatenatedParameters;
	}



	private static boolean isInteresting(APICall aPIUsage) {
		//return Character.isUpperCase(aPIUsage.api_name.charAt(0));
		return !aPIUsage.api_name.contentEquals("Log");
	}

	public static ArrayList<APICall> getAPIUsage(int method_id) {
		
		ArrayList<APICall> APIUsageList;		
		try {			
			APIUsageList = mysqlAccess.getAPIUsages(method_id);
			return APIUsageList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
public static ArrayList<String> getAPIUsageForEvaluation(int method_id) {
		
		ArrayList<String> APIUsageList;		
		try {			
			APIUsageList = mysqlAccess.getAPIUsagesForEvaluation(method_id);
			return APIUsageList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


//	public static void DFSAPIUsageSequenceDrawing(int methodID, int depth) {
//		JGraphXAdapterDemo applet = new JGraphXAdapterDemo();
//        applet.init();
//        //
//        ListenableGraph<String, MyEdge> g = applet.getGraph();
//        ArrayList<String> fullAPIUsageSequence = new  ArrayList<String>();
//        g = createGraph(methodID, g, fullAPIUsageSequence, depth, "");
//        applet.setGraph(g);
//        //applet.addVerticesEdges();
//        applet.executeLayout();
//
//        JFrame frame = new JFrame();
//        frame.getContentPane().add(applet);
//        frame.setTitle("API sequence visualization");
//        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//
//	}
//
//	public static void DFSAPIUsageSequenceTree(int methodID, int depth) {
//		TreeLayout frame = new TreeLayout();
//
//        FoldableTree g = frame.getGraph();
//
//        Object parent = g.getDefaultParent();
//        g.getModel().beginUpdate();
//
//        try
//        {
//        	ArrayList<String> fullAPIUsageSequence = new  ArrayList<String>();
//        	ArrayList<Object> v = new  ArrayList<Object>();
//        	g = createTree(methodID, g, parent, fullAPIUsageSequence, depth, v);
//        	//g = createDummyTree(g, parent);
//        	frame.setGraph(g);
//        	frame.getTreeLayout().execute(parent);
//        }
//        finally
//        {
//        	g.getModel().endUpdate();
//        }
//
//        frame.addGraph();
//
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 320);
//        frame.setVisible(true);
//	}
//
//
//
//	private static FoldableTree createDummyTree(FoldableTree graph, Object parent) {
//		 Object root = graph.insertVertex(parent, "treeRoot", "BluetoothAdapter.isEnabled()\nBluetoothAdapter.ACTION_REQUEST_ENABLE", 0, 0, 60, 40,"fillColor=yellow");
//         graph.updateCellSize(root);
//
//         Object root2 = graph.insertVertex(parent, "treeRoot2", "Root2", 0, 0, 60, 40);
//
//         Object v1 = graph.insertVertex(parent, "v1", "Child 1", 0, 0, 60, 40);
//
//         graph.insertEdge(parent, null, "", root, v1);
//         graph.insertEdge(parent, null, "", root2, root);
//
//         Object v2 = graph.insertVertex(parent, "v2", "Child 2", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", root, v2);
//
//         Object v3 = graph.insertVertex(parent, "v3", "Child 3", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", root, v3);
//
//         Object v11 = graph.insertVertex(parent, "v11", "Child 1.1", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v1, v11);
//
//         Object v12 = graph.insertVertex(parent, "v12", "Child 1.2", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v1, v12);
//
//         Object v21 = graph.insertVertex(parent, "v21", "Child 2.1", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v2, v21);
//
//         Object v22 = graph.insertVertex(parent, "v22", "Child 2.2", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v2, v22);
//
//         Object v221 = graph.insertVertex(parent, "v221", "Child 2.2.1", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v22, v221);
//
//         Object v222 = graph.insertVertex(parent, "v222", "Child 2.2.2", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v22, v222);
//
//         Object v31 = graph.insertVertex(parent, "v31", "Child 3.1", 0, 0, 60, 40);
//         graph.insertEdge(parent, null, "", v3, v31);
//
//         return graph;
//	}
//
//
//
//	private static ListenableGraph<String, MyEdge> createGraph(int method_id,  ListenableGraph<String, MyEdge> g,  ArrayList<String> fullAPIUsageSequence, int depth, String v0) {
//		int recDepth = depth;
//
//		System.out.println("Method ID:" + method_id);
//		Node rootMethod = new Node(method_id);
//		rootMethod.isVisited=true;
//		if(v0.contentEquals(""))
//			g.addVertex(v0);
//
//		//print API sequence of method
//		ArrayList<APICall> APIUsageSequenceList = getAPIUsage(method_id);
//
//		LinkedHashMap<Integer, Integer> methodList = getCalledMethods(method_id);
//		//for each method in methodlist, make its neighbor
//		if(methodList == null)
//		{
//			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, recDepth);
//			if(apiUsageSequence.size()!=0)
//			{
//				String vertex = "";
//				for(String s : apiUsageSequence)
//		        {
//		        	fullAPIUsageSequence.add(s);
//		        	vertex = vertex + "\n" + s;
//		        }
//				g.addVertex(vertex);
//				g.addEdge(v0, vertex);
//			}
//
//		}
//		if(methodList!=null)//interleave api usages in proper sequence
//		{
//			List<Object> interleavedObjectsList = new ArrayList<Object>();
//			interleavedObjectsList = merge(APIUsageSequenceList, methodList);
//			String vertex = "";
//			for(Object obj: interleavedObjectsList)
//			{
//				 if (obj.getClass() == APICall.class)
//				 {
//					 APICall retrievedAPICall = (APICall) obj;
//
//					 if(isInteresting(retrievedAPICall) )//&& APIUsage.line_num!=prevLine )
//					 {
//						String parameters = getAPICallParameters(retrievedAPICall.id);
//						//if(recDepth >= 1)
//						//{
//							//for(int i = 0; i < recDepth; i++)
//						String tab = "";
//						//for(int i = 0; i < recDepth*5; i++)
//						//{
//						//	tab += " ";
//						//}
//						//		System.out.println(tab + recDepth);
//						//}
//			            System.out.println(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + parameters + ")" + "\t" + retrievedAPICall.line_num );
//			            //fullAPIUsageSequence.add(tab + retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "( " +  ")" + "\t" );
//			            fullAPIUsageSequence.add(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "( " +  ")" + "\t" );
//			            vertex = vertex + "\n" + retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + ")";
//					 }
//					 	//
//
//				 }
//				 else//its a methodList
//				 {
//					 int retrievedMethodID = (int) obj;
//					if(retrievedMethodID != rootMethod.value)//to avoid self-recursive loop
//					{
//						 Node m = new Node(retrievedMethodID);
//						 System.out.println("Child:" + retrievedMethodID + " of root: "+ method_id);
//						 rootMethod.addneighbours(m);
//						 g.addVertex(vertex);
//
//						 if(!m.isVisited)
//						 {
//							 recDepth+=1;
//							 createGraph(retrievedMethodID, g, fullAPIUsageSequence, recDepth, vertex);
//							 recDepth-=1;
//						 }
//					}
//				 }
//			}
//			g.addVertex(vertex);
//			g.addEdge(v0, vertex);
//
//		}
//		return g;
//
//	}
//
//
//	private static FoldableTree createTree(int method_id, FoldableTree g, Object parent, ArrayList<String> fullAPIUsageSequence, int depth, ArrayList<Object> v0) {
//		int recDepth = depth;
//
//		System.out.println("Method ID:" + method_id);
//		Node rootMethod = new Node(method_id);
//		rootMethod.isVisited=true;
//		Object dv = null;
//		if(v0.size()==0)
//		{
//			if(depth != method_id)
//			{
//				dv = g.insertVertex(parent, Integer.toString(method_id), method_id, 0, 0, 60, 40);
//				v0.add(dv);
//			}
//			else
//			{
//				dv = g.insertVertex(parent, Integer.toString(method_id), method_id, 0, 0, 60, 40, "fillColor=yellow");
//				v0.add(dv);
//			}
//
//		}
//		else
//		{
//			dv = v0.get(v0.size()-1);
//		}
//		//print API sequence of method
//		ArrayList<APICall> APIUsageSequenceList = getAPIUsage(method_id);
//
//		LinkedHashMap<Integer, Integer> methodList = getCalledMethods(method_id);
//		//for each method in methodlist, make its neighbor
//		if(methodList == null)
//		{
//			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, recDepth);
//			if(apiUsageSequence.size()!=0)
//			{
//				String vertex = "";
//				for(String s : apiUsageSequence)
//		        {
//		        	fullAPIUsageSequence.add(s);
//		        	vertex = vertex + "\n" + s;
//		        }
//				if(depth != method_id)
//				{
//					v0.add(g.insertVertex(parent, "v"+method_id, vertex, 0, 0, 60, 40));
//				}
//				else
//				{
//					v0.add(g.insertVertex(parent, "v"+method_id, vertex, 0, 0, 60, 40, "fillColor=yellow"));
//				}
//				g.updateCellSize(v0.get(v0.size()-1));
//				g.insertEdge(parent, null, "", dv, v0.get(v0.size()-1));
//			}
//
//		}
//		if(methodList!=null)//interleave api usages in proper sequence
//		{
//			List<Object> interleavedObjectsList = new ArrayList<Object>();
//			interleavedObjectsList = merge(APIUsageSequenceList, methodList);
//			String vertex = "";
//			for(Object obj: interleavedObjectsList)
//			{
//				 if (obj.getClass() == APICall.class)
//				 {
//					 APICall retrievedAPICall = (APICall) obj;
//
//					 if(isInteresting(retrievedAPICall) )//&& APIUsage.line_num!=prevLine )
//					 {
//						String parameters = getAPICallParameters(retrievedAPICall.id);
//						//if(recDepth >= 1)
//						//{
//							//for(int i = 0; i < recDepth; i++)
//						String tab = "";
//						for(int i = 0; i < recDepth*5; i++)
//						{
//							tab += " ";
//						}
//								System.out.println(tab + recDepth);
//						//}
//			            System.out.println(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + parameters + ")" + "\t" + retrievedAPICall.line_num );
//			            //fullAPIUsageSequence.add(tab + retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "( " +  ")" + "\t" );
//			            fullAPIUsageSequence.add(retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "( " +  ")" + "\t" );
//			            vertex = vertex + "\n" + retrievedAPICall.api_name + "." + retrievedAPICall.api_usage + "(" + ")";
//					 }
//					 	//
//
//				 }
//				 else//its a methodList
//				 {
//
//					 int retrievedMethodID = (int) obj;
//
//					 Node m = new Node(retrievedMethodID);
//					 System.out.println("Child:" + retrievedMethodID + " of root: "+ method_id);
//					 rootMethod.addneighbours(m);
//
//					 //check
//					 if(!vertex.contentEquals(""))
//					 {
//						if(depth != retrievedMethodID)
//						{
//							v0.add(g.insertVertex(parent,"v"+retrievedMethodID,vertex,0, 0, 60, 40));
//						}
//						else
//						{
//							v0.add(g.insertVertex(parent,"v"+retrievedMethodID,vertex,0, 0, 60, 40, "fillColor=yellow"));
//						}
//						g.updateCellSize(v0.get(v0.size()-1));
//						g.insertEdge(parent, null, "", dv, v0.get(v0.size()-1));
//						vertex = "";
//
//					 }
//
//					 //end check
//
//					 //v0.add(g.insertVertex(parent,"v"+retrievedMethodID,retrievedMethodID,0, 0, 60, 40));
//					 //g.updateCellSize(v0.get(v0.size()-1));
//
//					 if(!m.isVisited)
//					 {
//						 recDepth+=1;
//						 createTree(retrievedMethodID, g, parent, fullAPIUsageSequence, recDepth, v0);
//						 recDepth-=1;
//					 }
//				 }
//			}
//			 if(!vertex.contentEquals(""))
//			 {
//				if(depth != method_id)
//				{
//					v0.add(g.insertVertex(parent,"v"+method_id,vertex,0, 0, 60, 40));
//				}
//				else
//				{
//					v0.add(g.insertVertex(parent,"v"+method_id,vertex,0, 0, 60, 40, "fillColor=yellow"));
//				}
//				g.updateCellSize(v0.get(v0.size()-1));
//				g.insertEdge(parent, null, "", dv, v0.get(v0.size()-1));
//			 }
//
//		}
//		return g;
//
//	}
//
//
//
//	public static ArrayList<ArrayList<Integer>> makeAncestorGraph(int method_id) {
//
//		Graph graph = new Graph();
//		ArrayList<ArrayList<Integer>> ancestorSequences = new  ArrayList<ArrayList<Integer>>();
//		Node startMethod = new Node(method_id, false);
//		//recursive
//		makeAncestorGraphUtility(startMethod, graph);
//		graph.printAllPaths(startMethod);
//		ancestorSequences = graph.printReverseSequences();
//		return ancestorSequences;
//
//	}
//
//	public static void makeAncestorGraphUtility(Node startMethod, Graph graph)
//	{
//		startMethod.isVisited = true;
//		ArrayList<Integer> hostMethodsList = getHostMethods(startMethod.value);
//
//		if(hostMethodsList.size() == 0)
//		{
//			startMethod.isSink = true;
//		}
//		else
//		{
//			for(int i: hostMethodsList)
//			{
//				 Node m = new Node(i, false);
//				 graph.addEdge(startMethod, m);
//				 if (!m.isVisited)
//		         {
//		             makeAncestorGraphUtility(m, graph);
//
//		         }
//			}
//		}
//		startMethod.isVisited = false;
//	}
//
//
//
//	public static ArrayList<String> ancestorAPIUsagePrinting(ArrayList<Integer> selectedSequence) {
//		//In this method we just iterate over the methodIDs in the input list
//		//For each method, we simply get the API usages and keep building the ancestorAPIUsageList
//		ArrayList<String> ancestorAPIUsageList = new ArrayList<String>();
//		for (Integer methodID: selectedSequence)
//		{
//			ArrayList<APICall> APIUsageSequenceList = getAPIUsage(methodID);
//			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, 0);
//			//apiUsageSequence.add("");
//			ancestorAPIUsageList.addAll(apiUsageSequence);
//		}
//		ancestorAPIUsageList.add("//end of ancestor functions usages");
//		return ancestorAPIUsageList;
//	}
//
//	public static FoldableTree getAncestorAPIUsageGraph(ArrayList<Integer> selectedSequence, FoldableTree g, Object parent, ArrayList<Object> verticesList) {
//		//In this method we just iterate over the methodIDs in the input selectedSequence list
//		//For each method, we simply get the API usages and keep building the ancestorAPIUsageGraph
//
//		Object currentVertex = null;
//		Object previousVertex = null;
//
//		for (Integer methodID: selectedSequence)
//		{
//			ArrayList<APICall> APIUsageSequenceList = getAPIUsage(methodID);
//			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, 0);
//			if(apiUsageSequence.size()!=0)
//			{
//				String vertex = "";
//				for(String s : apiUsageSequence)
//		        {
//		        	vertex = vertex + "\n" + s;
//		        }
//				currentVertex = g.insertVertex(parent, "v" + methodID, vertex, 0, 0, 60, 40);
//				verticesList.add(currentVertex);
//
//				g.updateCellSize(verticesList.get(verticesList.size()-1));
//				if(previousVertex != null)
//					g.insertEdge(parent, null, "", previousVertex, currentVertex);
//			}
//			previousVertex = currentVertex;
//		}
//		//ancestorAPIUsageList.add("=end of ancestors=");
//		leafAncestorForSelectedSequence = currentVertex;
//		return g;
//	}
//
//
//
//	public static void CallGraphBasedAPIUsageSequenceTree(Integer selectedSequenceRootMethodID, int selectedMethodID) {
//
//		TreeLayout frame = new TreeLayout();
//        FoldableTree g = frame.getGraph();
//        Object parent = g.getDefaultParent();
//        g.getModel().beginUpdate();
//
//        try
//        {
//        	ArrayList<String> consolidatedAPIUsageSequence = new  ArrayList<String>();
//        	ArrayList<Object> vertices = new  ArrayList<Object>();
//        	g = getAncestorAPIUsageGraph(userSelectedSequence, g, parent, vertices);
//        	g = createCallGraph(g, parent, selectedMethodID, vertices, null);
//        	//g = createDummyTree(g, parent);
//        	frame.setGraph(g);
//        	frame.getTreeLayout().execute(parent);
//        }
//        finally
//        {
//        	g.getModel().endUpdate();
//        }
//
//        frame.addGraph();
//
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 320);
//        frame.setVisible(true);
//
//	}
//
//	private static FoldableTree createCallGraph(FoldableTree g, Object parent, int selectedMethodID, ArrayList<Object> verticesList, Object currentVertex) {
//
//		if(currentVertex == null)
//		{
//			//currentVertex = new Node(selectedMethodID);
//			//Object aVertex = null;
//			ArrayList<APICall> APIUsageSequenceList = getAPIUsage(selectedMethodID);
//			ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, 0);
//			String vertex = "";
//			for(String s : apiUsageSequence)
//	        {
//	        	vertex = vertex + "\n" + s;
//	        }
//			currentVertex = g.insertVertex(parent, "selectedMethod", vertex, 0, 0, 60, 40, "fillColor=yellow");
//			verticesList.add(currentVertex);
//			g.updateCellSize(verticesList.get(verticesList.size()-1));
//			//for  leaf ancestor from ancestor sequence add an edge
//			g.insertEdge(parent, null, "", leafAncestorForSelectedSequence, currentVertex);
//		}
//
//		//currentVertex.isVisited = true;
//
//		LinkedHashMap<Integer, Integer> methodList = getCalledMethods(selectedMethodID);
//		//for each method in methodlist
//		if(methodList != null)
//		{
//			Iterator<Integer> it = methodList.keySet().iterator();
//			while(it.hasNext())
//			{
//				Integer key = it.next();
//				Integer MID = key;
//
//				ArrayList<APICall> APIUsageSequenceList = getAPIUsage(MID);
//				ArrayList<String> apiUsageSequence = printAPIUsageSequence(APIUsageSequenceList, 0);
//				if(apiUsageSequence.size()!=0)
//				{
//					String vertex = "";
//					for(String s : apiUsageSequence)
//			        {
//			        	vertex = vertex + "\n" + s;
//			        }
//					Object nextVertex = g.insertVertex(parent, "v" + MID.toString(), vertex, 0, 0, 60, 40);
//					verticesList.add(nextVertex);
//
//					g.updateCellSize(verticesList.get(verticesList.size()-1));
//					g.insertEdge(parent, null, "", currentVertex, nextVertex);
//					createCallGraph(g, parent, MID, verticesList, nextVertex);
//				}
//				else
//				{
//					Object nextVertex = g.insertVertex(parent, "v" + MID.toString(), "", 0, 0, 60, 40);
//					verticesList.add(nextVertex);
//					g.updateCellSize(verticesList.get(verticesList.size()-1));
//					g.insertEdge(parent, null, "", currentVertex, nextVertex);
//					createCallGraph(g, parent, MID, verticesList, nextVertex);
//				}
//			}
//
//
//		}
//
//		return g;
//
//	}
//
//
//
//	public static void setUserSelectedSequence(ArrayList<Integer> selectedSequence) {
//		userSelectedSequence = selectedSequence;
//
//	}
//
//	public static ArrayList<Integer> getDescendantMethodIDs(int method_id, ArrayList<Integer> cumulatingMethodIDs) throws Exception
//	{
//		MySQLAccessLayer.SINGLETON.initializeConnectorForRecomemndation();
//		return extracted(method_id, cumulatingMethodIDs);
//
//	}
//
//	private static ArrayList<Integer> extracted(int method_id,
//			ArrayList<Integer> cumulatingMethodIDs) throws Exception {
//		//System.out.println("Method ID:" + method_id);
//
//		Node rootMethod = new Node(method_id);
//		rootMethod.isVisited = true;
//
//		ArrayList<Integer> methodList = getCalledMethodIDs(method_id);
//
//		//for each method in methodlist, make its neighbor
//
//		if(methodList!=null)//interleave api usages in proper sequence
//		{
//			for(Integer obj: methodList)
//			{
//				int retrievedMethodID = obj;
//				if(retrievedMethodID != rootMethod.value)//to avoid self-recursive loop
//				{
//					Node m = new Node(retrievedMethodID);
//					//System.out.println("Child:" + retrievedMethodID + " of root: "+ method_id);
//					rootMethod.addneighbours(m);
//
//					if(!m.isVisited && cumulatingMethodIDs.size() <= 20)
//					{
//						cumulatingMethodIDs.add(retrievedMethodID);
//						extracted(retrievedMethodID, cumulatingMethodIDs);
//
//					}
//				}
//			}
//		}
//		return cumulatingMethodIDs;
//	}
//
	
}
