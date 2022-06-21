package related_methods.automated_evaluation;

public class InputParameter {
	
	protected int clusterID;
	protected int methodID;
	protected int projectID;
	
	InputParameter(int clusterID, int methodID, int projectID) {
			
			this.clusterID = clusterID;
			this.methodID = methodID;
			this.projectID = projectID;
		}
	
	private InputParameter(int clusterID) {
		
		this.clusterID = clusterID;
	
	}
	
	InputParameter(int methodID, int projectID) {
		
		this.methodID = methodID;
		this.projectID = projectID;
	}



}
