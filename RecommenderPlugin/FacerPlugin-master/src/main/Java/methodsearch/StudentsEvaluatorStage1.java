package methodsearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import repository.Method;
import structure_extraction.CodeMetadata;
import structure_extraction.MySQLAccessLayer;
import support.Constants;
import support.StopWordsRemoval;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudentsEvaluatorStage1 
{
	//path to your
	//private static final String INDEX_DIR = "F:/FACER_2020/LuceneData/faceremserepo";
	private static final String INDEX_DIR = "LuceneIndex";
	private static CodeMetadata codeMetadata;
	private static String stopwordsfile = "F:\\FACER_2020\\DataFiles\\stopwords.txt";
	private static MySQLAccessLayer mysqlAccess;

	static {
		try {
			mysqlAccess = MySQLAccessLayer.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception
	{
		JSONArray ja = searchMethodsFACERAS("connect to database", 10, "jdbc:mysql://203.135.63.70:3306/faceremserepopoint5?useSSL=false&user=shamsa&password=Mysql123!@#", "F:\\Maha2021\\FACER-AS\\FACER-AS");
		System.out.println(ja);
	}
	/*public static void main(String[] args) throws Exception 
	{
		//arg0 query
		//arg1 value of N for top N recs
	 
		String query = args[0];
		int topN = Integer.parseInt(args[1]);
		
		//set the value of database using arguments
		Constants.DATABASE = args[2];
		
		//Constants.DATABASE = "jdbc:mysql://localhost/"+ args[2] + "?useSSL=false&user=root";
		stopwordsfile = args[3]; //added for ayman
				
		List<String> stopwords = Files.readAllLines(Paths.get(stopwordsfile));
		query = StopWordsRemoval.removeStopWords(query, stopwords);
		ArrayList<Method> methods = new ArrayList<Method>();
		
		//code to search the indexed methods through comments
	    IndexSearcher searcher = LuceneReadIndexExample.createSearcher2(INDEX_DIR);
	    System.out.println("Searching....");
	    //TopDocs foundDocs2 = LuceneReadIndexExample.searchBySourcererF1F2F3(query, searcher);
	    TopDocs foundDocs2 = LuceneReadIndexExample.searchByCommentsKeywordsOR(query, searcher);
	    
		//system.out.println("Total Results :: " + foundDocs2.totalHits);
	    MySQLAccessLayer.SINGLETON.initializeConnector();
		for (ScoreDoc sd : foundDocs2.scoreDocs) 
		{
			Document d = searcher.doc(sd.doc);
			System.out.print(String.format(d.get("functionid")));
			System.out.print("\t");
			//System.out.print(String.format(d.get("functionname")));
			
			//String name = String.format(d.get("functionname"));
			System.out.print("\t");
			System.out.print(sd.score);
			
			int id = Integer.parseInt(d.get("functionid"));			
			int projectID = SharedSpace.getInstance().getProjectID(id);
			//String filePath = SharedSpace.getInstance().getFilePathFromMethod(id);
			//System.out.print(projectID);
			//System.out.print("\t");
			//System.out.println();
			//System.out.println(filePath);
			//System.out.println(String.format(d.get("comments")));
			
			Method newMethod = MySQLAccessLayer.SINGLETON.getMethodById(id);			
			int newMethodLength = newMethod.to_line_num - newMethod.from_line_num;
			//newMethod = new Method(id, newMethod.name, sd.score, projectID);
			//boolean  hasAPIUsages = MySQLAccessLayer.SINGLETON.hasAPIUsages(newMethod.id);
			//boolean  hasMCSMemship = MySQLAccessLayer.SINGLETON.hasMCSMemship(newMethod.id);
			if(!methods.contains(newMethod))// && hasAPIUsages )// && hasAPIUsages)// && hasAPIUsages)
			{
				methods.add(newMethod);
			}
			if(methods.size()==topN)
				break;
			
		}
		
		//display method bodies
		FileWriter myWriter = new FileWriter("searchresults.txt");
		
			
		
		for(Method m:methods )
		{
		//System.out.println("Displaying method: " + m.id);
		myWriter.write("Displaying method: " + m.id+"\n");
		
		ArrayList<String> s = new ArrayList<String>();
		String file_path = MySQLAccessLayer.SINGLETON.getMethodFilePath(m.id, m.file_id);
		//System.out.println(file_path);
		if(file_path.startsWith("/"))					
			file_path = MySQLAccessLayer.getPreProjectPath() + file_path;
		file_path = "" + file_path;
		int from_line = m.from_line_num;
		int to_line = m.to_line_num;
		
		int line_num = from_line;
		//while(line_num <= to_line)
		//{
			//String line = Files.readAllLines(Paths.get(file_path), StandardCharsets.UTF_8).get(line_num-1);
			//s.add(line);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file_path));
				for (int n = 0; n < from_line - 1 && reader.readLine() != null; ++ n)
				    ;
				String line;
				for (int n = 0; n < (to_line - from_line) + 1 && (line = reader.readLine()) != null; ++ n) {
				    s.add(line);
				    //write to file
				    System.out.println(line);
				    myWriter.write(line+"\n");
					
					
				}
				
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		myWriter.close();
		System.out.println("Search results written to text file searchresults.txt");
	}*/

	public JSONArray searchFACERStage1Methods(String query, int topN, String DB, String stopWordsFilePath, String LuceneIndexPath) throws Exception{
		Constants.DATABASE = DB;
		codeMetadata = CodeMetadata.getInstance();
		JSONArray methodRecsArray= new JSONArray();
		
		List<String> stopwords = Files.readAllLines(Paths.get(stopWordsFilePath));
		query = StopWordsRemoval.removeStopWords(query, stopwords);
		ArrayList<Method> methods = new ArrayList<Method>();
		
		//code to search the indexed methods through comments
	    IndexSearcher searcher = LuceneReadIndexExample.createSearcher2(LuceneIndexPath);
	    System.out.println("Searching....");
	    TopDocs foundDocs2 = LuceneReadIndexExample.searchBySourcererF1F2F3(query, searcher);
	    
		//system.out.println("Total Results :: " + foundDocs2.totalHits);
		mysqlAccess.initializeConnector();
		for (ScoreDoc sd : foundDocs2.scoreDocs) 
		{
			Document d = searcher.doc(sd.doc);
			int id = Integer.parseInt(d.get("functionid"));			
			int projectID = codeMetadata.getProjectID(id);
			Method newMethod = mysqlAccess.getMethodById(id);
			int newMethodLength = newMethod.to_line_num - newMethod.from_line_num;
			//newMethod = new Method(id, newMethod.name, sd.score, projectID);
			boolean  hasAPIUsages = mysqlAccess.hasAPIUsages(newMethod.id);
			//boolean  hasMCSMemship = mysqlAccess.hasMCSMemship(newMethod.id);
			if(!methods.contains(newMethod) && hasAPIUsages )// && hasAPIUsages)// && hasAPIUsages)
			{
				methods.add(newMethod);
			}
			if(methods.size()==topN)
				break;
			
		}
		
		//display method bodies
		
		for(Method m:methods )
		{				
		String s = "";
		String file_path = mysqlAccess.getMethodFilePath(m.id, m.file_id);
		if(file_path.startsWith("/"))					
			file_path = MySQLAccessLayer.getPreProjectPath() + file_path;
		file_path = "" + file_path;
		int from_line = m.from_line_num;
		int to_line = m.to_line_num;
		
		int line_num = from_line;
		BufferedReader reader;
			try {
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
			methodObject.put("Body", s.toString());	
			methodObject.put("Name", m.name);
			methodRecsArray.add(methodObject);
		}
		
		//JSONObject methodObject
		return methodRecsArray;
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

	public ArrayList<String> getMethodAPICallsList(int mID) throws Exception{

		//mysqlAccess.initializeConnector();
		ArrayList<String> apiCalls = mysqlAccess.getMethodAPIUsages(mID);
		return apiCalls;
	}
	public static JSONArray searchMethodsFACERAS(String query, int topN, String DB, String pathToFACERAS) throws Exception{
		Constants.DATABASE = DB;
		codeMetadata = CodeMetadata.getInstance();
		JSONArray methodRecsArray= new JSONArray();
		query = splitCamelCase(query);
		List<String> stopwords = Files.readAllLines(Paths.get(pathToFACERAS+"/stopwords.txt"));
		query = StopWordsRemoval.removeStopWords(query, stopwords);
		ArrayList<Method> methods = new ArrayList<Method>();
		
		//code to search the indexed methods through comments
	    IndexSearcher searcher = LuceneReadIndexExample.createSearcher2(pathToFACERAS+"/LuceneIndex");
	    System.out.println("Searching....");
	    TopDocs foundDocs2 = LuceneReadIndexExample.searchBySourcererNameAPI(query, searcher);
	    
		//system.out.println("Total Results :: " + foundDocs2.totalHits);
	    mysqlAccess.initializeConnector();
		for (ScoreDoc sd : foundDocs2.scoreDocs) 
		{
			Document d = searcher.doc(sd.doc);
			int id = Integer.parseInt(d.get("functionid"));			
			int projectID = codeMetadata.getProjectID(id);
			Method newMethod = mysqlAccess.getMethodById(id);
			//ArrayList<String> apiCalls = mysqlAccess.getMethodAPIUsages(id);
			//newMethod.apiCallList = apiCalls;
			int newMethodLength = newMethod.to_line_num - newMethod.from_line_num;
			//newMethod = new Method(id, newMethod.name, sd.score, projectID);
			//boolean  hasAPIUsages = mysqlAccess.hasAPIUsages(newMethod.id);
			//boolean  hasMCSMemship = mysqlAccess.hasMCSMemship(newMethod.id);
			if(!methods.contains(newMethod)  && newMethodLength >= 3)// && hasAPIUsages)
			{
				methods.add(newMethod);
			}
			if(methods.size()==topN)
				break;
			
		}
		
		//display method bodies
		
		for(Method m:methods )
		{				
		String s = "";
		String file_path = mysqlAccess.getMethodFilePathRelative(m.id, m.file_id);
		//if(file_path.startsWith("/"))					
			//file_path = MySQLAccessLayer.getPreProjectPath() + file_path;
		file_path = pathToFACERAS + "/Dataset/" + file_path;
		int from_line = m.from_line_num;
		int to_line = m.to_line_num;
		
		int line_num = from_line;
		BufferedReader reader;
			try {
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
			methodObject.put("Body", s.toString());	
			methodObject.put("Name", m.name);
			methodObject.put("APICalls", m.apiCallList);
			methodRecsArray.add(methodObject);

		}
		
		//JSONObject methodObject
		return methodRecsArray;
	}

	public static int searchContextFeature(String query, int topN, String DB, String pathToFACERAS) throws Exception{
		Constants.DATABASE = DB;
		int featureID = 0;
		codeMetadata = CodeMetadata.getInstance();
		//JSONArray methodRecsArray= new JSONArray();
		//query = splitCamelCase(query);
		List<String> stopwords = Files.readAllLines(Paths.get(pathToFACERAS+"/stopwords.txt"));
		query = StopWordsRemoval.removeStopWords(query, stopwords);
		ArrayList<Method> methods = new ArrayList<Method>();

		//code to search the indexed methods through comments
		IndexSearcher searcher = LuceneReadIndexExample.createSearcher2(pathToFACERAS+"/LuceneIndex");
		System.out.println("Searching....");
		TopDocs foundDocs2 = LuceneReadIndexExample.searchBySourcererNameAPI(query, searcher);

		//system.out.println("Total Results :: " + foundDocs2.totalHits);
		//mysqlAccess.initializeConnector();
		for (ScoreDoc sd : foundDocs2.scoreDocs)
		{
			Document d = searcher.doc(sd.doc);
			int id = Integer.parseInt(d.get("functionid"));
			//int projectID = codeMetadata.getProjectID(id);
			Method newMethod = mysqlAccess.getMethodById(id);
			int newMethodLength = newMethod.to_line_num - newMethod.from_line_num;
			//newMethod = new Method(id, newMethod.name, sd.score, projectID);
			boolean  hasAPIUsages = mysqlAccess.hasAPIUsages(newMethod.id);
			//boolean  hasMCSMemship = mysqlAccess.hasMCSMemship(newMethod.id);
			/*if(!methods.contains(newMethod)  && newMethodLength >= 3)// && hasAPIUsages)
			{
				methods.add(newMethod);
			}*/
			if(hasAPIUsages || methods.size()==topN) {
				//get feature ID or clone ID of method with API usages
				featureID = mysqlAccess.getClusterID(id);
				if(featureID != 0) {
					newMethod.clusterID = featureID;
					methods.add(newMethod);
					break;
				}
			}
		}

		//display method bodies
/*
		for(Method m:methods )
		{
			String s = "";
			String file_path = mysqlAccess.getMethodFilePathRelative(m.id, m.file_id);
			//if(file_path.startsWith("/"))
			//file_path = MySQLAccessLayer.getPreProjectPath() + file_path;
			file_path = pathToFACERAS + "/Dataset/" + file_path;
			int from_line = m.from_line_num;
			int to_line = m.to_line_num;

			int line_num = from_line;
			BufferedReader reader;
			try {
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
			//methodObject.put("Body", s.toString());
			methodObject.put("Name", m.name);

			methodRecsArray.add(methodObject);
		}

 */
		//JSONObject methodObject
		return featureID;
	}

	private static void printAllMethodIDsComments() throws Exception {
		IndexSearcher searcher = createSearcher();
		for(int i = 1; i<=777176; i++)
		{
			//Search by ID
			TopDocs foundDocs = searchByFunctionId(i, searcher);		
			for (ScoreDoc sd : foundDocs.scoreDocs) 
			{
				Document d = searcher.doc(sd.doc);
				System.out.print(i + ",");
				System.out.println(d.get("comments"));
			}
		}
		
	}

	private static void testSoucerer() throws Exception {
		IndexSearcher searcher = createSearcher();			
		//TopDocs foundDocs = searchBySourcererF1F2F3("add result", searcher);
		TopDocs foundDocs = searchByFunctionId(33, searcher);
		System.out.println("Total Results :: " + foundDocs.totalHits);
		
		for (ScoreDoc sd : foundDocs.scoreDocs) 
		{
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("F1_fulltext")));
			System.out.println(String.format(d.get("F2_FQN")));
			System.out.println(String.format(d.get("F3_simpleName")));
		
		}
		
	
		
	}

	private static void testFACER() throws IOException, Exception {
		IndexSearcher searcher = createSearcher();
		
		//Search by ID
		TopDocs foundDocs = searchByFunctionId(15, searcher);
		
		//TopDocs foundDocs = searchBySourcererF1F2F3("create result", searcher);
		System.out.println("Total Results :: " + foundDocs.totalHits);
		
		for (ScoreDoc sd : foundDocs.scoreDocs) 
		{
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("functionname")));
			System.out.println("meow");
			System.out.println(String.format(d.get("data")));
			System.out.println("meow");
			System.out.println(String.format(d.get("comments")));
			System.out.println("=================");
		}
		
		//Search by comments
		//TopDocs foundDocs2 = searchByComments("create document", searcher);
		
		//System.out.println("Total Results :: " + foundDocs2.totalHits);
		/*
		for (ScoreDoc sd : foundDocs2.scoreDocs) 
		{
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("methodBody")));
			System.out.println(String.format(d.get("id")));
		}*/
	}
	
	public static TopDocs searchByComments(String comments, IndexSearcher searcher) throws Exception
	{
		QueryParser qp = new QueryParser("comments", new StandardAnalyzer());
		Query featureQuery = qp.parse(comments);
		TopDocs hits = searcher.search(featureQuery, 10);
		return hits;
	}
	
	public static TopDocs searchByFunctionData(String query, IndexSearcher searcher) throws Exception
	{
		QueryParser qp = new QueryParser("data", new StandardAnalyzer());
		Query featureQuery = qp.parse(query);
		TopDocs hits = searcher.search(featureQuery, 20);
		return hits;
	}
	public static TopDocs searchByFunctionNameAPINames(String query, IndexSearcher searcher) throws Exception
	{
		
		MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"functionname", "data"}, new StandardAnalyzer());
		qp.setDefaultOperator(QueryParser.OR_OPERATOR);		
		//QueryParser qp = new QueryParser("data", new StandardAnalyzer());
		Query featureQuery = qp.parse(query);
		TopDocs hits = searcher.search(featureQuery, 20);
		
		return hits;
	}
	public static TopDocs searchByCommentsKeywords(String query, IndexSearcher searcher) throws Exception
	{
		
		MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"functionname","FQN","comments", "data"}, new StandardAnalyzer());
		//MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"functionname","comments"}, new StandardAnalyzer());
		
		qp.setDefaultOperator(QueryParser.AND_OPERATOR);		
		//QueryParser qp = new QueryParser("data", new StandardAnalyzer());
		Query featureQuery = qp.parse(query);
		
		System.out.println(featureQuery.toString());
		TopDocs hits = searcher.search(featureQuery, 20);
		
		return hits;
	}

	public static TopDocs searchBySourcererF1F2F3(String query, IndexSearcher searcher) throws Exception
	{
		System.out.println("Searching with Sourcerer Algorithm");
		MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"F1_fulltext", "F3_simpleName"}, new StandardAnalyzer());
		qp.setDefaultOperator(QueryParser.OR_OPERATOR);		
		//QueryParser qp = new QueryParser("data", new StandardAnalyzer());
		Query featureQuery = qp.parse(query);
		
		System.out.println(featureQuery.toString());
		TopDocs hits = searcher.search(featureQuery, 100);
		
		return hits;
	}

	public static TopDocs searchById(Integer id, IndexSearcher searcher) throws Exception
	{
		QueryParser qp = new QueryParser("id", new StandardAnalyzer());
		Query idQuery = qp.parse(id.toString());
		TopDocs hits = searcher.search(idQuery, 10);
		return hits;
	}
	
	private static TopDocs searchByFunctionId(Integer id, IndexSearcher searcher) throws Exception
	{
		QueryParser qp = new QueryParser("functionid", new StandardAnalyzer());
		Query idQuery = qp.parse(id.toString());
		TopDocs hits = searcher.search(idQuery, 10);
		return hits;
	}

	public static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}


}
