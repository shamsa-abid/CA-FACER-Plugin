package repository;

public class Uses {

	public int host_method_id;
	public int target_method_id;
	public int line_num;
	
	public Uses()
	{
		
	}
	
	public Uses(int host_method_id, int target_method_id, int lineNo)
	{
		this.host_method_id = host_method_id;
		this.target_method_id = target_method_id;
		this.line_num = lineNo;
	}
}
