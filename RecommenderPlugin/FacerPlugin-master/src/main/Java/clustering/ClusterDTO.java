package clustering;

import java.util.HashSet;

public class ClusterDTO {

	public int clusterID;
	public int seqID;
	public int methodID;
	private HashSet<Integer> methodsList;

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public int getMethodID() {
		return methodID;
	}

	public void setMethodID(int methodID) {
		this.methodID = methodID;
	}

	public HashSet<Integer> getMethodsList() {
		return methodsList;
	}

	public void setMethodsList(HashSet<Integer> methodsList) {
		this.methodsList = methodsList;
	}

	public ClusterDTO() {

	}
	public ClusterDTO(int clusterID, int methodID) {
		
		this.clusterID = clusterID;
		this.methodID = methodID;
	}

	public void addToCluster(int methodID)
	{
		this.methodsList.add(methodID);
	}


	public HashSet<Integer> getmethodsList() {
		return this.methodsList;
	}
}
