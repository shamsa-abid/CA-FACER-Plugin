package related_methods._3_PopulateRelatedFeatures;
import RelatedMethods.CustomUtilities.Constants;
import RelatedMethods.db_access_layer.DatabaseAccessLayer;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class FACER_CD_GetCSVFiles {
    public static void main(String[] args) throws Exception {

        Constants.DATABASE =  "jdbc:mysql://localhost/"+ args[0] + "?useSSL=false&user=root";
        String csvFilePathOfRScriptClustResults = args[1];
        //load all methodID to an ArrayList
        DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
        dbLayer.initializeConnectorForMCSMining(Constants.DATABASE);
        ArrayList<Integer> methods = dbLayer.getMethodIDs();
        //read csv file
        File file = new File(csvFilePathOfRScriptClustResults);
        dbLayer.insertClusterID(file,methods);
        //output csv files for saads python script to get performance metrics and confusion matrices
        int length = args[2].length()+2;//args 2 is path to dataset folder containing methods
        ArrayList<String> sList = dbLayer.getClusteringResultsForPythonScript(length);
        FileWriter myWriter = new FileWriter("output/ClusteringResultsForPythonScript.csv");
        for(String s: sList)
        {
            myWriter.write(s + "\n");
        }
        myWriter.close();
    }
}
