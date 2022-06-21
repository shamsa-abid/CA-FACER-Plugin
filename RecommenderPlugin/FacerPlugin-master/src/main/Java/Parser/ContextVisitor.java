package Parser;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ContextVisitor extends ASTVisitor{
    //private CodeMetadata codeMetadata;
    public static ArrayList<String> contextMethodStrings = new ArrayList<String>();
    //public static ArrayList<String> contextAPICalls = new ArrayList<String>();

    public ContextVisitor()
    {
        //codeMetadata = CodeMetadata.getInstance();
    }


    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    //List<MethodInvocation> method_invocations = new ArrayList<MethodInvocation>();

    //Statement s ;

    private static int startLine;
    private static int endLine;



    private String tokenize4sourcerer(String input) {
        String tokens = "";
        String  result="";
        //removing special characters and numbers
        result = input.replaceAll("[^a-zA-Z]", " ");
        //tokenizing by space
        String[] tokenizedBySpaces = result.split("\\s+");
        //tokenize each term by camelcase and underscore and hyphen
        for(String term: tokenizedBySpaces)
        {
            for (String w : term.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {

                tokens = tokens + " " + w;
            }
        }
        return tokens;
    }



    @Override
    public boolean visit(MethodDeclaration node) {

        //ParseInOneGo.m_nStatementCount = 0;
        methods.add(node);
        ParseInOneGo.methodEntered += 1;
        if(ParseInOneGo.methodEntered > 1)
            ParseInOneGo.isNestedFunction = true;

        if (!ParseInOneGo.isNestedFunction)
        {
            //ParseInOneGo.currentFunctionName = node.getName().toString();
            //ParseInOneGo.F3_simpleName = tokenize4sourcerer(node.getName().toString());
            //ParseInOneGo.F2_FQN = tokenizeFQN4sourcerer(ParseInOneGo.currentFilePath)+ " " + ParseInOneGo.F3_simpleName;
            //ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(ParseInOneGo.currentFunctionName));
            //ParseInOneGo.currentFunctionID += 1;
            //System.out.println("Visiting method ID: "+ ParseInOneGo.currentFunctionID);
            //ParseInOneGo.commentsList = new ArrayList<String>();
            startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
            int nodeLength = node.getLength();
            endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;
            ParseInOneGo.F1_fulltext = tokenize4sourcerer(getMethodText(ParseInOneGo.fullFile,startLine,endLine));

            contextMethodStrings.add(ParseInOneGo.F1_fulltext);

            //ParseInOneGo.currentFunctionName = node.getName().toString();
            //ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(ParseInOneGo.currentFunctionName));
            //ParseInOneGo.currentFunctionID += 1;
            //ParseInOneGo.commentsList = new ArrayList<String>();

            String returnType;
            //if(node.getReturnType2()!=null)
            {
              //  returnType = node.getReturnType2().toString();

                //int startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
                //int nodeLength = node.getLength();
                //int endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;


//                ParseInOneGo.currentMethod = new Method(
//                        ParseInOneGo.currentFunctionID,
//                        ParseInOneGo.currentFunctionName,
//                        0,
//                        0,
//                        startLine,
//                        endLine,
//                        ParseInOneGo.fileId, ParseInOneGo.m_nStatementCount);

            }
            //else//It is a constructor!!
            {
                //int startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
                //int nodeLength = node.getLength();
                //int endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;

//                ParseInOneGo.currentMethod = new Method(
//                        ParseInOneGo.currentFunctionID,
//                        ParseInOneGo.currentFunctionName,
//                        0,
//                        0,
//                        startLine,
//                        endLine,
//                        ParseInOneGo.fileId,
//                        ParseInOneGo.m_nStatementCount);


            }

        }
        return super.visit(node);

    }

    private String getMethodText(List<String> fullFile, int startLine,
                                 int endLine) {
        String result = "";
        for(int i = startLine -1; i<endLine; i++)
            result = result + " " + fullFile.get(i);


        return result;
    }

    private String tokenizeFQN4sourcerer(String currentFilePath) {
        String tokens = "";
        String result = "";
        //take a substring from the file name from src to .java
        if(currentFilePath.contains("src") && currentFilePath.contains(".java")){
            result = currentFilePath.substring(currentFilePath.indexOf("\\src\\")+4, currentFilePath.indexOf(".java"));

            //removing special characters and numbers
            result = result.replaceAll("[^a-zA-Z]", " ");
            //tokenizing by space
            String[] tokenizedBySpaces = result.split("\\s+");
            //tokenize each term by camelcase
            for(String term: tokenizedBySpaces)
            {
                for (String w : term.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {

                    tokens = tokens + " " + w;
                }
            }
        }
        return tokens;
    }

//    @Override
//    public boolean visit(MethodInvocation node)
//    {
//        if(ParseInOneGo.methodEntered >= 1)
//        {
//
//            //if(ParseInOneGo.currentFunctionID != 0)
//            {
//
//                String APIName = "";
//                String APIUsage = "";
//
//                Expression e = node.getExpression();
//
//                APIUsage = node.getName().toString();
//                if(e instanceof Name)
//                {
//
//                    Name n = (Name) e;
//
//                    ITypeBinding typeBinding = e.resolveTypeBinding();
//                    if ( typeBinding != null)//type resolution found actual type
//                    {
//
//                        APIName = typeBinding.getName();
//
//                    }
//                    else
//                    {
//                        APIName = n.toString();
//
//                    }
//
//                }
//                contextAPICalls.add(APIName+"."+APIUsage);
//                //contextAPICalls.add(APIUsage);
//            }
//
//        }
//        return super.visit(node);
//    }

    private void setNodeRegion(StructuralPropertyDescriptor location) {

        if(location == IfStatement.EXPRESSION_PROPERTY ||
                location == IfStatement.THEN_STATEMENT_PROPERTY)
        {
            ParseInOneGo.ifBlockRegion = true;
        }
        else
        {
            if(location == IfStatement.ELSE_STATEMENT_PROPERTY)
            {
                ParseInOneGo.elseBlockRegion = true;
            }
            else
            {
                if(location == CatchClause.BODY_PROPERTY)
                {
                    ParseInOneGo.catchBlockRegion = true;
                }
                else
                {
                    ParseInOneGo.basicBlockRegion = true;
                }
            }
        }
    }




//    @Override
//    public boolean visit(MethodDeclaration node) {
//        ParseInOneGo.m_nStatementCount = 0;
//        methods.add(node);
//        ParseInOneGo.methodEntered += 1;
//        if(ParseInOneGo.methodEntered > 1)
//            ParseInOneGo.isNestedFunction = true;
//        if (!ParseInOneGo.isNestedFunction)
//        {
//            ParseInOneGo.currentFunctionName = node.getName().toString();
//            //ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(ParseInOneGo.currentFunctionName));
//            ParseInOneGo.currentFunctionID += 1;
//            ParseInOneGo.commentsList = new ArrayList<String>();
//
//            String returnType;
//            if(node.getReturnType2()!=null)
//            {
//                returnType = node.getReturnType2().toString();
//
//                int startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
//                int nodeLength = node.getLength();
//                int endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;
//
//
//                ParseInOneGo.currentMethod = new Method(
//                        ParseInOneGo.currentFunctionID,
//                        ParseInOneGo.currentFunctionName,
//                        codeMetadata.getTypeId(returnType, ParseInOneGo.projectId),
//                        codeMetadata.getTypeId(ParseInOneGo.currentClass, ParseInOneGo.projectId),
//                        startLine,
//                        endLine,
//                        ParseInOneGo.fileId, ParseInOneGo.m_nStatementCount);
//
//            }
//            else//It is a constructor!!
//            {
//                int startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
//                int nodeLength = node.getLength();
//                int endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;
//
//                ParseInOneGo.currentMethod = new Method(
//                        ParseInOneGo.currentFunctionID,
//                        ParseInOneGo.currentFunctionName,
//                        0,
//                        codeMetadata.getTypeId(ParseInOneGo.currentClass, ParseInOneGo.projectId),
//                        startLine,
//                        endLine,
//                        ParseInOneGo.fileId,
//                        ParseInOneGo.m_nStatementCount);
//
//
//            }
//        }
//
//        return super.visit(node);
//    }


    @Override
    public void endVisit(MethodDeclaration node)
    {

        //System.out.println(ParseInOneGo.currentFunctionID+","+ParseInOneGo.m_nStatementCount);


        ParseInOneGo.methodEntered -= 1;
        if(ParseInOneGo.methodEntered == 0)
            ParseInOneGo.isNestedFunction = false;
        //if (!ParseInOneGo.isNestedFunction)
        {
            //ParseInOneGo.currentMethod.setExpressionStmtsCount(ParseInOneGo.m_nStatementCount);
            //codeMetadata.addInMethod(ParseInOneGo.currentMethod);
        }
    		/*try
    		{
    			if(!ParseInOneGo.isNestedFunction)
    			{
    				ParseInOneGo.commentsList.addAll(ParseInOneGo.getComments(ParseInOneGo.currentFilePath, ParseInOneGo.currentMethod));
    				//printComments(commentsList);
    				ParseInOneGo.createLuceneMethodDocument(ParseInOneGo.currentFunctionID, ParseInOneGo.currentFunctionName, ParseInOneGo.commentsList, ParseInOneGo.functionData);
    				////System.out.println("Lucene method document: " + currentFunctionID + ")" + currentFunctionName );
    				ParseInOneGo.functionData = "";
    			}
    		}
    		catch (IOException e)
    		{
    			e.printStackTrace();
    		}*/

    }

//    public boolean visit (ExpressionStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (AssertStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    /*public boolean visit (Block node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }*/
//    public boolean visit (BreakStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//
//    public boolean visit (ConstructorInvocation node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (ContinueStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (DoStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (EnhancedForStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (ForStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//
//    public boolean visit (IfStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (LabeledStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (TryStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//
//    public boolean visit (ReturnStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (VariableDeclarationStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (TypeDeclarationStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (WhileStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (ThrowStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (SynchronizedStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (SwitchStatement node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (SwitchCase node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//    public boolean visit (SuperConstructorInvocation node) {
//        ParseInOneGo.m_nStatementCount+=1;
//        //System.out.println(node.toString());
//        return true;
//    }
//


    public List<MethodDeclaration> getMethods() {
        return methods;
    }



}
