package repository;

public class MethodParameter {

	public int id;
	public String method_parameter_name;
	public int parameter_type_id;
	public int file_id;
	public int method_id;
	public String className;
	public String typeName;
	
	public MethodParameter()
	{
		
	}
	
	public MethodParameter(int id, String name, int TypeId, int fileId,int methodId)
	{
		this.id = id;
		this.method_parameter_name = name;
		this.parameter_type_id = TypeId;
		this.file_id = fileId;
		this.method_id = methodId;
	}
	
	public MethodParameter(int id, String name, String type, int TypeId, int fileId,int methodId)
	{
		this.id = id;
		this.method_parameter_name = name;
		this.typeName = type;		
		this.parameter_type_id = TypeId;
		this.file_id = fileId;
		this.method_id = methodId;
	}
}
