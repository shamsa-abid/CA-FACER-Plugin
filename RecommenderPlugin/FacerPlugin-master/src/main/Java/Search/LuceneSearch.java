package Search;

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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//import support.Constants;

public class LuceneSearch
{
	//private static final String INDEX_DIR = "D:\\JetBrainsInternship\\sub_intellij_Resources\\LuceneIndex";
	private static final String INDEX_DIR = "D:\\JetBrainsInternship\\LuceneIndex\\LuceneData1011dbnew";
	Map<String, Integer> wordMap = new HashMap<String, Integer>();

	public static void main(String[] args) throws Exception 
	{
		//printAllMethodIDsComments();
		//testFACER();
		testSoucerer();

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
		TopDocs foundDocs = searchByFunctionId(8, searcher);

		System.out.println("Total Results :: " + foundDocs.totalHits);
		String query = "";
		for (ScoreDoc sd : foundDocs.scoreDocs) 
		{

			Document d = searcher.doc(sd.doc);
			//System.out.println(String.format(d.get("F1_fulltext")));
			//System.out.println(String.format(d.get("F2_FQN")));
			System.out.println(String.format(d.get("F3_simpleName")));
			query = String.format(d.get("F4_APIData"));
			System.out.println("Query: "+query);
		}

		foundDocs = searchByAPICalls(query, searcher);
		//TopDocs foundDocs = searchByFunctionId(8, searcher);
		System.out.println("Total Results :: " + foundDocs.totalHits);

		for (ScoreDoc sd : foundDocs.scoreDocs)
		{
			Document d = searcher.doc(sd.doc);
			//System.out.println(String.format(d.get("F1_fulltext")));
			//System.out.println(String.format(d.get("F2_FQN")));
			//System.out.println(String.format(d.get("F3_simpleName")));
			System.out.println(String.format(d.get("F4_APIData")));
			System.out.println(sd.score);
		}


	}

	private static void testFACER() throws IOException, Exception {
		IndexSearcher searcher = createSearcher();
		
		//Search by ID
		TopDocs foundDocs = searchByFunctionId(14, searcher);
		
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
	public static TopDocs searchByCommentsKeywordsOR(String query, IndexSearcher searcher) throws Exception
	{
		
		MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"functionname","FQN","comments", "data"}, new StandardAnalyzer());
		//MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"functionname","comments"}, new StandardAnalyzer());
		
		qp.setDefaultOperator(QueryParser.OR_OPERATOR);		
		//QueryParser qp = new QueryParser("data", new StandardAnalyzer());
		Query featureQuery = qp.parse(query);
		
		System.out.println(featureQuery.toString());
		TopDocs hits = searcher.search(featureQuery, 100);
		
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

	public static TopDocs searchByAPICalls(String query, IndexSearcher searcher) throws Exception
	{
		System.out.println("Searching with Sourcerer Algorithm");
		//MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"F1_fulltext", "F3_simpleName"}, new StandardAnalyzer());
		//qp.setDefaultOperator(QueryParser.OR_OPERATOR);
		QueryParser qp = new QueryParser("F4_APIData", new StandardAnalyzer());
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
	
	public static IndexSearcher createSearcher2(String indexdir) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexdir));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}
