package Parser;


import org.eclipse.jdt.core.dom.*;
import repository.Type;
import structure_extraction.CodeMetadata;

import java.util.ArrayList;



public class Walk0Visitor extends ASTVisitor {
	private CodeMetadata codeMetadata;
	Walk0Visitor()
	{
		codeMetadata = CodeMetadata.getInstance();
	}
	private static ArrayList<String> classDeclarations = new ArrayList<>();
	
	
	//////////////////////////////////////////////////////////////////
	public static ArrayList<String> getClassDeclarations() {
		return classDeclarations;
	}

	public static void setClassDeclarations(ArrayList<String> classDeclarations) {
		Walk0Visitor.classDeclarations = classDeclarations;
	}

	
	//////////////////////////////////////////////////////////////
	@Override
    public boolean visit(TypeDeclaration node) 
	{		
		String typeName = node.getName().toString();
		ParseInOneGo.currentClass = typeName;
		if(!ParseInOneGo.types.contains(typeName))
		{
			ParseInOneGo.types.add(typeName);
			codeMetadata.addInType_v2(new Type(0, 2, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
		
  	   		classDeclarations.add(typeName);
  	   	
		}
		else
		{
			codeMetadata.removeFromTypesHashTable(typeName);
			codeMetadata.addInType_v2(new Type(0, 2, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
			
			//codeMetadata.updateTypeTable(new Type(0, 2, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
		}
		
  	   	
  	   	return super.visit(node);
    }
	
	@Override
    public boolean visit(SimpleType node) 
	{
		
		String typeName = node.getName().toString();
		if(!ParseInOneGo.types.contains(typeName))
		{
			ParseInOneGo.types.add(typeName);
  	   		codeMetadata.addInType_v2(new Type(0, 3, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
		}
        return super.visit(node);
    }
    
	@Override
    public boolean visit(PrimitiveType node) 
	{
		
		String typeName = node.toString();
		if(!ParseInOneGo.types.contains(typeName))
		{
			ParseInOneGo.types.add(typeName);
  	   		codeMetadata.addInType_v2(new Type(0, 1, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
		}
        return super.visit(node);
    }
	
	@Override
    public boolean visit(QualifiedType node) 
	{
		String qualifierName = node.getQualifier().toString();
		String typeName = node.getName().toString();
		if(!ParseInOneGo.types.contains(typeName))
		{
			ParseInOneGo.types.add(typeName);
			codeMetadata.addInType_v2(new Type(0, 3, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
	        
		}
		if(!ParseInOneGo.types.contains(qualifierName))
		{
			ParseInOneGo.types.add(qualifierName);
			codeMetadata.addInType_v2(new Type(0, 3, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
	        
		}
  	   	//codeMetadata.addInType_v2(new Type(0, 3, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
        return super.visit(node);
    }
	
	@Override
    public boolean visit(ArrayType node) 
	{		
		
		String typeName = node.getElementType().toString();
		if(!ParseInOneGo.types.contains(typeName))
		{
			ParseInOneGo.types.add(typeName);
			if(node.getElementType().isPrimitiveType())
			{
	  	   	//codeMetadata.addInType_v2(new Type(0, 1, typeName, FeaturesExtraction.getProjectId(), FeaturesExtraction.getFileId()));
			}
			else
			{
				codeMetadata.addInType_v2(new Type(0, 3, typeName, ParseInOneGo.projectId, ParseInOneGo.fileId));
				
			}
		}
		return super.visit(node);
    }

	
	
	
}
