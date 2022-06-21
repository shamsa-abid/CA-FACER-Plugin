package repository;

public class Field {
	public int Id;
	public String field_name;
	public int field_type_id;
	public int file_id;
	public int class_id;
	
	public Field()
	{
		
	}
	
	public Field(int id, String name, int TypeId, int fileId, int classId)
	{
		this.Id = id;
		this.field_name = name;
		this.field_type_id = TypeId;
		this.file_id = fileId;
		this.class_id = classId;
	}

}
