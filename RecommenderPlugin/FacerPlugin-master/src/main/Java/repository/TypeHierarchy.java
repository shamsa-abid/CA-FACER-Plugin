package repository;

public class TypeHierarchy {
	
	public int child_type_id;
	public int parent_type_id;
	
    public TypeHierarchy() {
		
	}
	
	public TypeHierarchy(int child_type_id, int parent_type_id)
	{
		this.child_type_id = child_type_id;
		this.parent_type_id = parent_type_id;
	}

}
