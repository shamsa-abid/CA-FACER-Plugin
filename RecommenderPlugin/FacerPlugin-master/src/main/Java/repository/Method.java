package repository;

import java.util.ArrayList;

public class Method {
	
	public int id;
	public String name;
	public int return_type_id;
	public int host_type_id; //Confusing as 
	public int from_line_num;
	public int to_line_num;
	public int file_id;
	//added later for displaying recommendations
	public double score;
	public int projectID;
	//added for displaying clusterID
	public int clusterID;
	//added for tracking count of expression statements in a method
	public int expressionStmtsCount;

	public ArrayList<String> apiCallList;


	public int getExpressionStmtsCount() {
		return expressionStmtsCount;
	}

	public void setExpressionStmtsCount(int expressionStmtsCount) {
		this.expressionStmtsCount = expressionStmtsCount;
	}

	public Method()
	{}
	
	public Method(int id, String name, double score2, int projectID)
	{
		this.id = id;
		this.name = name;
		this.score = score2;
		this.projectID = projectID;
	}
	
	public Method(int Id, String Name, int ReturnTypeId, int HostTypeId, int startLineNo,int endLineNo, int fileId)
	{
		this.id = Id;
		this.name = Name;
		this.return_type_id = ReturnTypeId;
		this.host_type_id = HostTypeId;
		this.from_line_num = startLineNo;
		this.file_id = fileId;
		this.to_line_num =endLineNo;
	}

	
	public Method(int Id, String Name, int ReturnTypeId, int HostTypeId, int startLineNo,int endLineNo, int fileId, int expStmtsCount)
	{
		this.id = Id;
		this.name = Name;
		this.return_type_id = ReturnTypeId;
		this.host_type_id = HostTypeId;
		this.from_line_num = startLineNo;
		this.file_id = fileId;
		this.to_line_num =endLineNo;
		this.expressionStmtsCount = expStmtsCount;
	}
	
	
}
