package methodsearch;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import support.Constants;

public class LuceneWriteIndexExample 
{
	private static final String INDEX_DIR = Constants.LUCENE_INDEX_DIR;

	public static void main(String[] args) throws Exception 
	{
		//testFACER();
		testSourcerer();
		
	}

	private static void testSourcerer() throws IOException {
		IndexWriter writer = createWriter();
		List<Document> documents = new ArrayList<>();
		//method id, comments, keywords, code or method body
		Document document1 = createDocument4Sourcerer(1, "add two numbers", "result x y", "result = x + y", "");
		documents.add(document1);
		
		Document document2 = createDocument4Sourcerer(2, "create a document", "create Document id firstName lastName website add ", "Document document = new Document();", "");
		documents.add(document2);
		
		//Let's clean everything first
		writer.deleteAll();
		
		writer.addDocuments(documents);
		writer.commit();
	    writer.close();
	    
	    System.out.println("Writing Index Complete");
		
	}

	private static void testFACER() throws IOException {
		IndexWriter writer = createWriter();
		List<Document> documents = new ArrayList<>();
		//method id, comments, keywords, code or method body
		Document document1 = createDocument(1, "add two numbers", "result x y", "result = x + y");
		documents.add(document1);
		
		Document document2 = createDocument(2, "create a document", "create Document id firstName lastName website add ", "Document document = new Document();");
		documents.add(document2);
		
		//Let's clean everything first
		writer.deleteAll();
		
		writer.addDocuments(documents);
		writer.commit();
	    writer.close();
	    
	    System.out.println("Writing Index Complete");
	}

	private static Document createDocument(Integer id, String comments, String keywords, String methodBody) 
	{
    	Document document = new Document();
    	document.add(new StringField("id", id.toString() , Field.Store.YES));
    	document.add(new TextField("comments", comments , Field.Store.YES));
    	document.add(new TextField("keywords", keywords , Field.Store.YES));
    	document.add(new TextField("methodBody", methodBody , Field.Store.YES));
    	return document;
    }

	public static IndexWriter createWriter() throws IOException 
	{
		FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(dir, config);
		return writer;
	}

	public static Document createDocument(int currentFunctionID, String currentFunctionName, ArrayList<String> commentsList, String functionData) 
	{
		Document document = new Document();
    	document.add(new StringField("functionid", Integer.toString(currentFunctionID) , Field.Store.YES));
    	document.add(new TextField("functionname", currentFunctionName , Field.Store.YES));
    	String comments = "";
    	for (int i = 0; i < commentsList.size(); i++) {
			//System.out.println(commentsList.get(i));
			extractTextFromComments();
			comments +=  commentsList.get(i);			
		}
    	//System.out.println("Combined comments for "+ currentFunctionName);
		//System.out.println(comments);
    	document.add(new TextField("comments", comments , Field.Store.YES));
    	document.add(new TextField("data", functionData , Field.Store.YES));
    	return document;
	}
	
	public static Document createDocumentUpdated(int currentFunctionID, String splitCurrentFunctionName, ArrayList<String> commentsList, String SplitAPINames) 
	{
		Document document = new Document();
    	document.add(new StringField("functionid", Integer.toString(currentFunctionID) , Field.Store.YES));
    	
    	Field F1 = new TextField("functionname", splitCurrentFunctionName , Field.Store.YES);
    	F1.setBoost(50.0f);
    	document.add(F1); 
    
    	String comments = "";
    	for (int i = 0; i < commentsList.size(); i++) {
			//System.out.println(commentsList.get(i));
			extractTextFromComments();
			comments +=  commentsList.get(i);			
		}
    	//System.out.println("Combined comments for "+ currentFunctionName);
		//System.out.println(comments);
    	
    	Field F2 = new TextField("comments", comments , Field.Store.YES);
    	F2.setBoost(25.0f);
    	document.add(F2);
    	
    	Field F3 = new TextField("data", SplitAPINames , Field.Store.YES);
    	F3.setBoost(25.0f);
    	document.add(F3);
    	
    	return document;
	}

	private static void extractTextFromComments() {
		// TODO Auto-generated method stub
		
	}

	public static Document createDocument4Sourcerer(int currentFunctionID2,
			String f1, String f2, String f3, String APICallsData) {
		Document document = new Document();
    	document.add(new StringField("functionid", Integer.toString(currentFunctionID2) , Field.Store.YES));
    	Field F1 = new TextField("F1_fulltext", f1 , Field.Store.YES);
    	F1.setBoost(25.0f);
    	document.add(F1); 
    	Field F2 = new TextField("F2_FQN", f2 , Field.Store.YES);
    	F2.setBoost(15.0f);
    	document.add(F2);
    	Field F3 = new TextField("F3_simpleName", f3 , Field.Store.YES);
    	F3.setBoost(40.0f);
    	document.add(F3);
		Field APIData = new TextField("F4_APIData", APICallsData , Field.Store.YES);
		APIData.setBoost(20.0f);
		document.add(APIData);
    	return document;
	}

	public static Document createDocumentUpdated(int currentFunctionID,
			String simpleFuncName, String fQN, ArrayList<String> commentsList,
			String SplitAPINames) {
		Document document = new Document();
    	document.add(new StringField("functionid", Integer.toString(currentFunctionID) , Field.Store.YES));
    	
    	Field F1 = new TextField("functionname", simpleFuncName , Field.Store.YES);
    	F1.setBoost(50.0f);
    	document.add(F1); 
    	
    	Field FQN = new TextField("FQN", fQN , Field.Store.YES);
    	FQN.setBoost(25.0f);
    	document.add(FQN);
    
    	String comments = "";
    	for (int i = 0; i < commentsList.size(); i++) {
			//System.out.println(commentsList.get(i));
			extractTextFromComments();
			comments +=  commentsList.get(i);			
		}
    	//System.out.println("Combined comments for "+ currentFunctionName);
		//System.out.println(comments);
    	
    	Field F2 = new TextField("comments", comments , Field.Store.YES);
    	F2.setBoost(12.50f);
    	document.add(F2);
    	
    	Field F3 = new TextField("data", SplitAPINames , Field.Store.YES);
    	F3.setBoost(12.5f);
    	document.add(F3);
    	
    	return document;
	}

	
}
