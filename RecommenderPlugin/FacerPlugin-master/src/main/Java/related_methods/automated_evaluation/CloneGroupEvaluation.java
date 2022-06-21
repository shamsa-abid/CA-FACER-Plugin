package related_methods.automated_evaluation;

import DataObjects.CloneGroupSample;
import RelatedMethods.db_access_layer.EvaluationDAL;

import java.io.FileWriter;
import java.util.ArrayList;

;
//also related is ViewClusterMethods.java which generates teh output methods to evaluate using input cluster IDs
public class CloneGroupEvaluation {

	public static void main(String args[]) throws Exception
	{
		//get CID, size
		EvaluationDAL dbLayer = EvaluationDAL.getInstance();		
		dbLayer.initializeCloneGroupEvaluationConnector();
		ArrayList<CloneGroupSample> list= new ArrayList<CloneGroupSample>();
		list = dbLayer.getClusterDiversity();
		 FileWriter myWriter = new FileWriter("CID_APIsizediversity.csv");
			//FileWriter myWriter = new FileWriter("mid_density_dugeonmaster3.csv");
			 myWriter.write("CID,size,APIsizediversity\n");
			 for(CloneGroupSample s: list)
			 {
				 
		      myWriter.write(s.CID+","+s.size+","+s.diversity+"\n");
		      
			 }
		      myWriter.close();
	}
}
