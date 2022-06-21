package Parser;

import methodsearch.LuceneWriteIndexExample;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import repository.Method;
import structure_extraction.CodeMetadata;
import support.Constants;
import support.DirectoryExplorer;
import support.SpecialCharFileReader;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class ParseInOneGo  {

    //private static ContextStructure contextStructure;
    private static CodeMetadata codeMetadata = CodeMetadata.getInstance();
    public static List<String> types = new ArrayList<String>();

    public static IndexWriter writer = null;
    private static List<Document> documents = new ArrayList<>();

    public static int projectId = 0;
    public static int fileId = 0;
    private static int lastFileId = 0;
    private static int firstFileId = 0;
    private static String currentProjectName = null;
    private static ArrayList<String> classDeclarations = new ArrayList<>();
    public static int currentFunctionID = 0;
    private static int firstFunctionID = 0;
    private static int lastFunctionID = 0;
    public static int currentApiCallID = 0;
    private static List<File> javaFilesList;
    //control flow detection flags and counters
    public static boolean ifBlockRegion = false;
    public static boolean elseBlockRegion = false;
    public static boolean catchBlockRegion = false;
    public static boolean basicBlockRegion = false;
    public static int ifCount = 0;
    public static int elseCount = 0;
    public static int catchCount = 0;

    public static String currentClass = null;
    public static String currentFunctionName = null;
    public static String currentFilePath = null;
    public static Method currentMethod = null;
    public static ArrayList<String> commentsList=new ArrayList<String>();
    public static int methodEntered = 0;
    public static int m_nStatementCount = 0;
    public static boolean isNestedFunction = false;
    public static String functionData = "";
    public static String fileString = "";
    public static List<String> fullFile = new ArrayList<String>();


    public static String F1_fulltext;
    public static String F2_FQN;
    public static String F3_simpleName;


    public static CompilationUnit parse;

    public static void main(String args[]) throws Exception {

        //Constants.PROJECTS_ROOT = args[0];
        //Constants.DATABASE = "jdbc:mysql://localhost/"+ args[1] + "?useSSL=false&user=root";
        //Constants.CLASSPATH = args[2];
        //Constants.LUCENE_INDEX_DIR = "LuceneSearchIndex";
        //Constants.LUCENE_INDEX_DIR = "F:/JetBrains/December2021/LuceneDataSmall";
        //Constants.LUCENE_INDEX_DIR = "D:\\JetBrainsInternship\\LuceneIndex\\LuceneData1011dbnew";
        Date startTime = new Date();
        initializeLuceneWriter();
        preprocessProjectSourceFilesStage1(Constants.PROJECTS_ROOT);
        closeDatabaseConnector();
        displayStartStopTime(startTime);


        //below only updates the apicallindex table and inserts all distinct apicall valeus and updates index id in apicall table
        //codeMetadata.initializeDatabaseConnection();
        //codeMetadata.updateAPICallIndexTable();

    }

    public static void parse() throws Exception {

//            Date startTime = new Date();
//        initializeLuceneWriter();
//        preprocessProjectSourceFilesStage1(Constants.PROJECTS_ROOT);
//        closeDatabaseConnector();
//        displayStartStopTime(startTime);
//

        //below only updates the apicallindex table and inserts all distinct apicall valeus and updates index id in apicall table
        codeMetadata.initializeDatabaseConnection();
        codeMetadata.updateAPICallIndexTable();

    }
    private static void displayStartStopTime(Date startTime) throws FileNotFoundException, UnsupportedEncodingException {
        final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        System.out.println("Start Date and Time was: " + f.format(startTime));
        System.out.println("Stop Date and Time was: " + f.format(new Date()));

        PrintWriter writer = new PrintWriter("output_report.txt", "UTF-8");
        writer.println("Start Date and Time was: " + f.format(startTime));
        writer.println("Stop Date and Time was: " + f.format(new Date()));
        writer.close();
    }


    private static void closeDatabaseConnector() throws Exception
    {
        codeMetadata.close();
        writer.close();
    }

    public static void preprocessProjectSourceFilesStage1(String projectsRootDirectory) throws Exception
    {
        codeMetadata.initializeDatabaseConnection();
        List<File> projectDirList = DirectoryExplorer.readRootDirectoryFolders(projectsRootDirectory);


        for(int project = 0; project < projectDirList.size(); project++ ) {
            projectId = codeMetadata.getLastProjectIdFromDB() + 1;
            currentProjectName = projectDirList.get(project).getName();
            if (alreadyExists(currentProjectName)) {
                System.out.println("Project " + currentProjectName + " already exists ");
                continue;
            }

            System.out.println("Processing project: " + currentProjectName);
            String path = projectDirList.get(project).getAbsolutePath();

            Constants.SOURCES = DirectoryExplorer.findDir(new File(path), "src");

            codeMetadata.insertProject(projectId, currentProjectName, path);
            javaFilesList = DirectoryExplorer.readProjectFiles(projectDirList.get(project).getAbsolutePath());
            Iterator<File> javaFileIter = javaFilesList.iterator();

            firstFileId = codeMetadata.getLastFileIdFromDB();//0//previously fileId
            currentFunctionID = codeMetadata.getLastFunctionIDFromDB();
            firstFunctionID = currentFunctionID;
            boolean parsedSuccessfully = false;
//this portion mines types
            while(javaFileIter.hasNext())
            {
                parsedSuccessfully = parseSourceFile(javaFileIter.next());
                //if(!parsedSuccessfully)
                //break;//i dont know why i break here...this stops processing any more files of that project

            }

            if(!parsedSuccessfully)	{
                System.out.println("Skipping to next project");
                continue;
            }


            codeMetadata.dumpTypeInDb_v2();
            CodeMetadata.destroyInstance();
            codeMetadata = CodeMetadata.getInstance();



                lastFileId = fileId;//2
                fileId = firstFileId;
                //walkCounter++;

                //System.out.println("Before Stage2");
                preprocessProjectSourceFilesSinglePass(Constants.PROJECTS_ROOT);
                //System.out.println("After Stage2");

                //fileId = firstFileId;
                //lastFunctionID = codeMetadata.getLastFunctionIDFromDB();
                //currentFunctionID = firstFunctionID;
                //walkCounter++;

                //System.out.println("Before Stage3");
                //preprocessProjectSourceFilesStage3(Constants.PROJECTS_ROOT);
                //System.out.println("After Stage3");

                currentFunctionID = lastFunctionID;
                fileId = lastFileId;
                types = new ArrayList<String>();
                indexLuceneDocuments();


        }
        updateMethodStats(Constants.PROJECTS_ROOT);
    }

    private static boolean parseSourceFile(File file) throws Exception
    {
        boolean parsedSuccessfully = false;
        fileId = codeMetadata.getLastFileIdFromDB() + 1;
        codeMetadata.insertFile(fileId, file.getAbsolutePath(), projectId);
        currentFilePath = file.getAbsolutePath();
        //System.out.println("Processing File in walk0:" + currentFilePath );
        parsedSuccessfully = parseSourceFileStage1(currentFilePath);
        return parsedSuccessfully;

    }
    private static boolean parseSourceFileStage1(String filePath) throws IOException
    {
        boolean parsedSuccessfully = false;
        fileString = readFileToString(filePath);

        try{

            parse = parse(fileString);

        }catch(Exception ex){System.out.println("Could not parse file!");}
        if(parse != null)
        {
            //CommentVisitor c= new CommentVisitor();
            //parse.accept(c);
            Walk0Visitor walk0 = new Walk0Visitor();
            parse.accept(walk0);
            parsedSuccessfully = true;
        }
        //System.out.println("Classes are: ");
        //for(String className: walk0.getClassDeclarations())
//		{
//        	 System.out.println(className);
//		}
//
//		System.out.println("Types are: ");
//		for(String type: types)
//		{
//        	 System.out.println(type);
//		}

        return parsedSuccessfully;


    }
    public static boolean parseContextFile(String filePath) throws IOException
    {
        boolean parsedSuccessfully = false;
        fileString = readFileToString(filePath);

        try{

            parse = parseActiveFile(fileString);

        }catch(Exception ex){System.out.println("Could not parse file!");}
        if(parse != null)
        {
            //CommentVisitor c= new CommentVisitor();
            //parse.accept(c);
            Walk0Visitor walk0 = new Walk0Visitor();
            parse.accept(walk0);
            parsedSuccessfully = true;
        }
        //System.out.println("Classes are: ");
        //for(String className: walk0.getClassDeclarations())
//		{
//        	 System.out.println(className);
//		}
//
//		System.out.println("Types are: ");
//		for(String type: types)
//		{
//        	 System.out.println(type);
//		}

        return parsedSuccessfully;


    }

    private static void updateMethodStats(String pROJECTS_ROOT) {
        codeMetadata.updateMethodStats();
        codeMetadata.updateAPICallIndexTable();
        //commented for saad's test on his machine
        //codeMetadata.createDensityCSVFile();
        //codeMetadata.createMethodAPICallCSVFile();

    }
    private static boolean alreadyExists(String currentProjectName2) {
        return codeMetadata.projectExistsInDb(currentProjectName2);
        //return false;
    }

    private static void indexLuceneDocuments() throws IOException
    {
        writer.addDocuments(documents);
        writer.commit();
        documents = new ArrayList<>();
    }




    private static void preprocessProjectSourceFilesSinglePass(String projectsRootDirectory) throws Exception
    {
        Iterator<File> javaFileIter = javaFilesList.iterator();
        while(javaFileIter.hasNext())
        {
            fileId += 1;
            File currentFile = javaFileIter.next();
            currentFilePath = currentFile.getAbsolutePath();
            //fileId = codeMetadata.getLastFileIdFromDB() + 1;
            //codeMetadata.insertFile(fileId, currentFilePath, projectId);
            fileString = readFileToString(currentFilePath);
            try{
                parse = parse(fileString);
            }catch(Exception ex){System.out.println("Could not parse file!");}
            if(parse!=null)
            {

                fullFile = SpecialCharFileReader.getFileText(currentFilePath);
                SinglePassVisitor walk1 = new SinglePassVisitor();
                parse.accept(walk1);

                //SharedSpace.getInstance().dumpImportsInDb();
                codeMetadata.dumpMethodInDb();
                codeMetadata.dumpAPICallsInDb();
                //SharedSpace.getInstance().dumpMethodParamInDb2();
                //SharedSpace.getInstance().dumpFieldInDb();
                //codeMetadata.dumpTypeHeirarchyInDb();
                codeMetadata.flushSinglePassWalk();

                CodeMetadata.destroyInstance();
                codeMetadata = CodeMetadata.getInstance();
            }
        }
    }
    private static void initializeLuceneWriter() throws IOException
    {
        writer = LuceneWriteIndexExample.createWriter();

    }





    /**
     * Reads a ICompilationUnit and creates the AST DOM for manipulating the
     * Java source file
     *
     * @param unit
     * @return
     */

    public static CompilationUnit parse(String unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);

        //parser.setCompilerOptions(options);
        //parser.setSource(unit.toCharArray());


        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        //Map options = JavaCore.getOptions();
        //parser.setCompilerOptions(options);

        File f = new File(currentFilePath);
        String unitName = f.getName();
        parser.setUnitName(unitName);
        //below commented for saad
        //String[] sources = {Constants.SOURCES};
        String[] classpath = {Constants.CLASSPATH};
        String[] sources = {};
        //below commented for saad
        //parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setEnvironment(classpath, sources, new String[] {}, true);
        parser.setSource(unit.toCharArray());






        CompilationUnit cu = null;
        try{
            cu = (CompilationUnit) parser.createAST(null);// parse
        }catch(Exception ex){
            System.out.println("Unable to create AST!" + ex.getMessage());
            return null;

        }
        return cu;
    }

    public static CompilationUnit parseActiveFile(String unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);

        //parser.setCompilerOptions(options);
        //parser.setSource(unit.toCharArray());


        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        //Map options = JavaCore.getOptions();
        //parser.setCompilerOptions(options);

        //File f = new File(currentFilePath);
        //String unitName = f.getName();
        //parser.setUnitName(unitName);
        //below commented for saad
        //String[] sources = {Constants.SOURCES};
        String[] classpath = {Constants.CLASSPATH};
        String[] sources = {};
        //below commented for saad
        //parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setEnvironment(classpath, sources, new String[] {}, true);
        parser.setSource(unit.toCharArray());






        CompilationUnit cu = null;
        try{
            cu = (CompilationUnit) parser.createAST(null);// parse
        }catch(Exception ex){
            System.out.println("Unable to create AST!" + ex.getMessage());
            return null;

        }
        return cu;
    }

    //read file content into a string
    public static String readFileToString(String filePath) throws IOException
    {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            //System.out.println(numRead);
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return  fileData.toString();
    }

    public static ArrayList<String> getComments(String currentFilePath, int functionID, int from, int to) throws IOException
    {
        //String filePath = SharedSpace.getInstance().getFilePath(.file_id);
        //List<String> fullFile = SpecialCharFileReader.getFileText(currentFilePath);
        ArrayList<String> commentsList = new ArrayList<String>();
        boolean multiLineCommentbody = false;
        int from_line = from;
        int to_line = to;
        String multilinecomment = "";

        int line_num = from_line;
        while(line_num <= to_line)
        {
            String line = "";
            try
            {
                line = fullFile.get(line_num - 1);
                line = line.trim();
            }
            catch(Exception ex)
            {ex.toString();}
            if(isSingleLinecomment(line) )
                commentsList.add(line.substring(2));
            if(isStartOfMultilineComment(line))
            {
                multiLineCommentbody = true;
                multilinecomment = line.substring(2);
            }
            if(isEndOfMultilineComment(line))
            {
                multiLineCommentbody = false;
                multilinecomment = multilinecomment.concat(" " + line);
                commentsList.add(multilinecomment);
            }
            if(multiLineCommentbody)
                multilinecomment = multilinecomment.concat(" " + line);
            line_num++;
        }

        return commentsList;
    }



    private static ArrayList<String> getLookbackMultiComment(int i, List<String> fullFile) throws IOException {
        ArrayList<String> comments = new ArrayList<String>();
        String line = fullFile.get(i-1);
        line = line.trim();
        if(line.startsWith("/*"))
        {
            comments.add(line);
            return comments;
        }
        else
        {
            comments.add(line);
            comments.addAll(getLookbackMultiComment(i-1, fullFile));
        }
        return comments;
    }

    private static ArrayList<String>  getLookbackSingleComment(int i, List<String> fullFile) throws IOException {
        ArrayList<String> comments = new ArrayList<String>();
        String line = fullFile.get(i-1);
        line = line.trim();
        if(!line.startsWith("//"))
        {
            return comments;
        }
        else
        {
            comments.add(line.substring(2));
            comments.addAll(getLookbackSingleComment(i-1, fullFile));
        }
        return comments;
    }

    private static boolean isEndOfMultilineComment(String line) {
        if(line.endsWith("*/"))
            return true;
        else
            return false;
    }

    private static boolean isStartOfMultilineComment(String line) {
        if(line.startsWith("/*"))
            return true;
        else
            return false;
    }

    private static boolean isSingleLinecomment(String line) {
        if(line.startsWith("//"))
            return true;
        else
            return false;
    }

    public static String splitCamelCase(String input) {
        String  result="";
        for (String w : input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {

            if(w.contains("_"))
            {
                for(String s: w.split("_"))
                {
                    result = result + " " + s;
                }
            }
            else if(w.contains("-"))
            {
                for(String s: w.split("-"))
                {
                    result = result + " " + s;
                }
            }
            else
                result = result + " " + w;
        }
        return result;
    }

    public static void createLuceneMethodDocument(
            int currentFunctionID2,	String currentFunctionName2, ArrayList<String> commentsList2, String functionData) throws IOException
    {
        //method id, comments, keywords, code or method body
        Document document = LuceneWriteIndexExample.createDocumentUpdated(currentFunctionID2, currentFunctionName2, commentsList2, functionData);
        documents.add(document);

    }


    public static void createLuceneMethodDocument4Sourcerer(
            int currentFunctionID2,	String F1, String F2, String F3, String APICallsData) throws IOException
    {
        //method id, comments, keywords, code or method body
        Document document = LuceneWriteIndexExample.createDocument4Sourcerer(currentFunctionID2, F1,F2,F3, APICallsData);
        documents.add(document);

    }

}