package repository;

public class APICallParameter {
	public int id;
	public String api_parameter_name;
	public int parameter_type_id;
	public int api_call_id;

	
	public APICallParameter(int id, String api_parameter_name, int parameter_type_id, int api_call_id)
	{
		this.id = id;
		this.api_parameter_name = api_parameter_name;
		this.parameter_type_id = parameter_type_id;
		this.api_call_id = api_call_id;
	}
}
