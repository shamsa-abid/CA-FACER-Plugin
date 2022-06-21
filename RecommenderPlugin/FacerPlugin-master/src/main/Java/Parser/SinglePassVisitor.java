package Parser;

import org.eclipse.jdt.core.dom.*;
import repository.APICall;
import repository.Method;
import structure_extraction.CodeMetadata;

import java.util.ArrayList;
import java.util.List;

public class SinglePassVisitor extends ASTVisitor{
    private CodeMetadata codeMetadata;
    SinglePassVisitor()
    {
        codeMetadata = CodeMetadata.getInstance();
    }


    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    List<MethodInvocation> method_invocations = new ArrayList<MethodInvocation>();

   Statement s ;

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
    public boolean visit(TypeDeclaration node)
    {
        String typeName = node.getName().toString();
        Parser.ParseInOneGo.currentClass = typeName;
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {

        ParseInOneGo.m_nStatementCount = 0;
        methods.add(node);
        ParseInOneGo.methodEntered += 1;
        if(ParseInOneGo.methodEntered > 1)
            ParseInOneGo.isNestedFunction = true;

        if (!ParseInOneGo.isNestedFunction)
        {
            ParseInOneGo.currentFunctionName = node.getName().toString();
            ParseInOneGo.F3_simpleName = tokenize4sourcerer(node.getName().toString());
            ParseInOneGo.F2_FQN = tokenizeFQN4sourcerer(ParseInOneGo.currentFilePath)+ " " + ParseInOneGo.F3_simpleName;
            //ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(ParseInOneGo.currentFunctionName));
            ParseInOneGo.currentFunctionID += 1;
            //System.out.println("Visiting method ID: "+ ParseInOneGo.currentFunctionID);
            ParseInOneGo.commentsList = new ArrayList<String>();
            startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
            int nodeLength = node.getLength();
            endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;
            ParseInOneGo.F1_fulltext = tokenize4sourcerer(getMethodText(ParseInOneGo.fullFile,startLine,endLine));

            //ParseInOneGo.currentFunctionName = node.getName().toString();
            //ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(ParseInOneGo.currentFunctionName));
            //ParseInOneGo.currentFunctionID += 1;
            ParseInOneGo.commentsList = new ArrayList<String>();

            String returnType;
            if(node.getReturnType2()!=null)
            {
                returnType = node.getReturnType2().toString();

                //int startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
                //int nodeLength = node.getLength();
                //int endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;


                ParseInOneGo.currentMethod = new Method(
                        ParseInOneGo.currentFunctionID,
                        ParseInOneGo.currentFunctionName,
                        0,
                        0,
                        startLine,
                        endLine,
                        ParseInOneGo.fileId, ParseInOneGo.m_nStatementCount);

            }
            else//It is a constructor!!
            {
                //int startLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
                //int nodeLength = node.getLength();
                //int endLine = ParseInOneGo.parse.getLineNumber(node.getStartPosition() + nodeLength) ;

                ParseInOneGo.currentMethod = new Method(
                        ParseInOneGo.currentFunctionID,
                        ParseInOneGo.currentFunctionName,
                        0,
                        0,
                        startLine,
                        endLine,
                        ParseInOneGo.fileId,
                        ParseInOneGo.m_nStatementCount);


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

    @Override
    public boolean visit(MethodInvocation node)
    {
        if(ParseInOneGo.methodEntered >= 1)
        {
            int lineNum = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
             if(ParseInOneGo.currentFunctionID != 0)
            {
                method_invocations.add(node);
                String APIName = "";
                String APIUsage = "";
                int callerObjTypeID = 0;
                Expression e = node.getExpression();


                //String methodName = node.getName().toString();
                //ITypeBinding typeBinding = node.getExpression().resolveTypeBinding();
                //IType type = (IType)typeBinding.getJavaElement();
                //typeBinding.isFromSource();
                //System.out.printf("Type %s (method %s) calls %s\n", type, methodName, type.getFullyQualifiedName());

                APIUsage = node.getName().toString();
                if(e instanceof Name)
                {
                    ParseInOneGo.currentApiCallID += 1;
                    Name n = (Name) e;
//    	 	String firstPart, middleMan = "";

//    	 	if(n.isQualifiedName())
//    	 	{
//    	 		String full = n.getFullyQualifiedName();
//    	 		System.out.println("isqualifiedname: "+full);
//       	 	 	}
                    //System.out.println(node.getExpression().toString());
                    //System.out.println(node.resolveMethodBinding().getName());

                    ITypeBinding typeBinding = e.resolveTypeBinding();
                    if ( typeBinding != null)//type resolution found actual type
                    {
    	 		/*try {
    	 		IType j = (IType)typeBinding.getDeclaringClass().getJavaElement();
    	 		IPackageFragmentRoot an = (IPackageFragmentRoot) j.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
    	 		System.out.println("Is archive: "+an.isArchive());

    	 			System.out.println(an.getResolvedClasspathEntry().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/

                        APIName = typeBinding.getName();

                        //boolean a = typeBinding.isTypeVariable();
                        //boolean b = typeBinding.isClass();
                        //boolean c = typeBinding.isFromSource();
                        //boolean d = typeBinding.isFromSource();
                        //String w = typeBinding.getKey();
                        //String f = typeBinding.getQualifiedName();
                        //System.out.println("binding FQN:"+f);
                        //System.out.println("Type: " + typeBinding.getName());
                        //get typeID from APIName
                        //callerObjTypeID = codeMetadata.getTypeId(APIName, ParseInOneGo.projectId);


                        //int targetID = codeMetadata.getMethodId(callerObjTypeID, APIUsage, ParseInOneGo.projectId);
                        //codeMetadata.addInCalls(new Calls(0, ParseInOneGo.currentFunctionID, targetID, lineNum,
                        //            0,0,0,0));


                            codeMetadata.addInAPICallsList(new APICall(0, ParseInOneGo.currentFunctionID, APIName, APIUsage, lineNum,
                                    0,0,0,0));

                            ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(APIName));
                            ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(APIUsage));

                    }
                    else
                    {
                        APIName = n.toString();
                        int baseType = codeMetadata.getBaseTypeByTypeName(APIName, ParseInOneGo.projectId);
                        if(baseType != 2 && baseType != 1)
                        {
                            codeMetadata.addInAPICallsList(new APICall(0, ParseInOneGo.currentFunctionID, APIName, APIUsage, lineNum,
                                    0,0,0,0));

                            ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(APIName));
                            ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(APIUsage));
                        }

                        //System.out.println("I have no typebinding:" + APIName);
                    }
                    //if API call has a middle man and has two levels like a.b.c() then check for a dot in API Name
//    	 	String firstPart, middleMan = "";
//    	 	firstPart = APIName;
//    	 	if(APIName.contains("."))
//    	 	{
//    	 		firstPart = APIName.substring(0, APIName.indexOf("."));
//    	 		//middleMan = APIName.substring(APIName.indexOf(".")+1);
//    	 		//APIName = firstPart + "." + middleMan;
//    	 	}
                    //APIUsage = node.getName().toString();

                    //System.out.println("API call: " + APIName + "."+ APIUsage);



                }
                /////simple method invocation
                //else
                //{
                    //System.out.println("Method Invocation name: " + node.getName());
                    //String funcName = node.getName().toString();//APIUsage

    		/*if(ParseInOneGo.currentFunctionName.contentEquals("onCreate"))
        	{
        		int targetOnStartMethodID = sharedSpace.getTargetOnStartMethodID(currentClassTypeID, ParseInOneGo.projectId);
        		sharedSpace.addInCalls(new Calls(0, method.id, targetOnStartMethodID, 0,
	        			1, 0, 0, 0));

        	}*/

                        //Method target = codeMetadata.getTargetMethodId(funcName, ParseInOneGo.projectId);
                        //codeMetadata.addInCalls(new Calls(0, ParseInOneGo.currentFunctionID, target.id,lineNum,
                        //        0,0,0,0));


                //}
            }
        }
        return super.visit(node);
    }

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
    private boolean isParentThread(int callerObjTypeID)
    {
        return codeMetadata.isParentThread(callerObjTypeID);

    }


    @Override
    public boolean visit(ClassInstanceCreation node)
    {
        if(ParseInOneGo.methodEntered >= 1) {
            //System.out.println("class instance created: "+ node.toString());
            String nodeType = node.getType().toString();

            //is it an API?
            //then add as an api call
            int lineNum = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
            String APIName = nodeType;
            if (!nodeType.contains(".")) {
                String APIUsage = "new";

                codeMetadata.addInAPICallsList(new APICall(0, ParseInOneGo.currentFunctionID, APIName, APIUsage, lineNum,
                        0, 0, 0, 0));
                ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(APIName));
                ParseInOneGo.functionData = ParseInOneGo.functionData.concat(" " + ParseInOneGo.splitCamelCase(APIUsage));

            }
        }
        return super.visit(node);

    }

    @Override
    public void endVisit(MethodDeclaration node) {
        //when we exit a method body whose name is onCreate() we have to add call of current method to
        //the onStart() method of the same class

//        try
//        {
//            int lineNum = ParseInOneGo.parse.getLineNumber(node.getStartPosition());
//            Method host;
//            int currentClassTypeID = codeMetadata.getTypeId(ParseInOneGo.currentClass, ParseInOneGo.projectId);
//            host = codeMetadata.getMethodId(ParseInOneGo.currentFunctionName, currentClassTypeID, ParseInOneGo.projectId);
//
//        }
//
//        catch(Exception ex)
//        {
//
//
//        }
        ParseInOneGo.methodEntered -= 1;
        if(ParseInOneGo.methodEntered == 0)
            ParseInOneGo.isNestedFunction = false;

        try
        {
            if(!ParseInOneGo.isNestedFunction)
            {
                ParseInOneGo.currentMethod.setExpressionStmtsCount(ParseInOneGo.m_nStatementCount);
                codeMetadata.addInMethod(ParseInOneGo.currentMethod);
                //ParseInOneGo.commentsList.addAll(ParseInOneGo.getComments(ParseInOneGo.currentFilePath, ParseInOneGo.currentFunctionID,startLine,endLine));
                //printComments(commentsList);
                //ParseInOneGo.createLuceneMethodDocument(ParseInOneGo.currentFunctionID, ParseInOneGo.splitCamelCase(ParseInOneGo.currentFunctionName), ParseInOneGo.commentsList, ParseInOneGo.functionData);
                ParseInOneGo.createLuceneMethodDocument4Sourcerer(ParseInOneGo.currentFunctionID, ParseInOneGo.F1_fulltext, ParseInOneGo.F2_FQN, ParseInOneGo.F3_simpleName, ParseInOneGo.functionData);
                ////System.out.println("Lucene method document: " + currentFunctionID + ")" + currentFunctionName );
                ParseInOneGo.functionData = "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
//    public void endVisit(MethodDeclaration node)
//    {
//
//        //System.out.println(ParseInOneGo.currentFunctionID+","+ParseInOneGo.m_nStatementCount);
//
//
//        ParseInOneGo.methodEntered -= 1;
//        if(ParseInOneGo.methodEntered == 0)
//            ParseInOneGo.isNestedFunction = false;
//        if (!ParseInOneGo.isNestedFunction)
//        {
//            ParseInOneGo.currentMethod.setExpressionStmtsCount(ParseInOneGo.m_nStatementCount);
//            codeMetadata.addInMethod(ParseInOneGo.currentMethod);
//        }
//    		/*try
//    		{
//    			if(!ParseInOneGo.isNestedFunction)
//    			{
//    				ParseInOneGo.commentsList.addAll(ParseInOneGo.getComments(ParseInOneGo.currentFilePath, ParseInOneGo.currentMethod));
//    				//printComments(commentsList);
//    				ParseInOneGo.createLuceneMethodDocument(ParseInOneGo.currentFunctionID, ParseInOneGo.currentFunctionName, ParseInOneGo.commentsList, ParseInOneGo.functionData);
//    				////System.out.println("Lucene method document: " + currentFunctionID + ")" + currentFunctionName );
//    				ParseInOneGo.functionData = "";
//    			}
//    		}
//    		catch (IOException e)
//    		{
//    			e.printStackTrace();
//    		}*/
//
//    }

    public boolean visit (ExpressionStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (AssertStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    /*public boolean visit (Block node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }*/
    public boolean visit (BreakStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }

    public boolean visit (ConstructorInvocation node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (ContinueStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (DoStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (EnhancedForStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (ForStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }

    public boolean visit (IfStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (LabeledStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (TryStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }

    public boolean visit (ReturnStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (VariableDeclarationStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (TypeDeclarationStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (WhileStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (ThrowStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (SynchronizedStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (SwitchStatement node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (SwitchCase node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }
    public boolean visit (SuperConstructorInvocation node) {
        ParseInOneGo.m_nStatementCount+=1;
        //System.out.println(node.toString());
        return true;
    }



    public List<MethodDeclaration> getMethods() {
        return methods;
    }



}
