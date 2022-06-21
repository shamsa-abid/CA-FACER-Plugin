package repository;

public class Calls {
	public int id;
	public int host_method_id;
	public int target_method_id;
	public int line_num;
	public int in_basic_path;
	public int in_conditional;
	public int in_else;
	public int in_exception;

	
	public Calls(int id, int host, int target,int lineNo,
			int in_basic_path, int in_conditional, int in_else, int in_exception)
	{
		this.id = id;
		this.host_method_id = host;
		this.target_method_id = target;
		this.line_num = lineNo;
		this.in_basic_path = in_basic_path;
		this.in_conditional = in_conditional;
		this.in_else = in_else;
		this.in_exception = in_exception;
	}
}
