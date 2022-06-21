package repository;

public class Accesses {

	public int id;
	public int host_method_id;
	public int target_field_id;
	public int line_num;
	
	public Accesses()
	{
		
	}
	
	public Accesses(int id, int host_method_id, int target_field_id, int line_num)
	{
		this.id = id;
		this.host_method_id = host_method_id;
		this.target_field_id = target_field_id;
		this.line_num = line_num;
	}
}
