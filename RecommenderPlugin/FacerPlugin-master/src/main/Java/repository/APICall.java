package repository;

public class APICall {
	
		
		public int id;
		public String api_name ;
		public int host_method_id ;
		public String api_usage ; //Confusing as 
		public int line_num;
		public int in_basic_path;
		public int in_conditional;
		public int in_else;
		public int in_exception;
		public String fullAPIcall;
				
		public APICall()
		{
		
		}
		
		public APICall(int Id, int hostmethodid, String APIName, String APIusage, int linenenum,
				int in_basic_path, int in_conditional, int in_else, int in_exception)
		{
			this.id = Id;
			this.host_method_id = hostmethodid;
			this.api_name = APIName;
			this.api_usage = APIusage;
			this.line_num = linenenum;
			this.in_basic_path = in_basic_path;
			this.in_conditional = in_conditional;
			this.in_else = in_else;
			this.in_exception = in_exception;
		
		}
		
		public APICall(int Id, String APIName, String APIusage)
		{
			this.id = Id;		
			this.api_name = APIName;
			this.api_usage = APIusage;
			this.fullAPIcall = APIName + "." + APIusage;
	 		
		
		}
}
