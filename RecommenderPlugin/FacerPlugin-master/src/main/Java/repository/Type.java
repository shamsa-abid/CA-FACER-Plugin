package repository;

public class Type {
	
	public int id;
	public String name;
	public int base_type_id;
	public int project_id;
	private int file_id;
	
	public Type() {
		
	}
	
	public Type(int id, int base_type_id, String Name, int projectId, int fileId)
	{
		this.id = id;
		this.name = Name;
		this.base_type_id = base_type_id;
		this.project_id = projectId;
		this.file_id = fileId;
	}
}
