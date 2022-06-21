package clustering;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TransactionTable {

    public static void main(String args[]) throws Exception
    {
        fileBasedTT();
    }

public static void projectBasedTT() throws Exception {
    //create a transaction table, output to console
//1.iterate over projects table and then over the method IDs and find a check
//if method is in cluster table, then get its cluster ID, see if that cluster ID has at least 3 instances
//(for frequent item) then add it into the transaction table row
    DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
    dbLayer.initializeConnectorToWriteTransaction();
//populate api_call_index table
    ArrayList<String> sList = dbLayer.getClusterIDsPerProject();
    dbLayer.closeConnector();
    //output to a file and then input TT to spmf via command line
    FileWriter myWriter = new FileWriter("output/transactiontable.txt");
    for(String s: sList)
    {
        myWriter.write(s+"\n");
    }
    myWriter.close();
    //run spmf
    try {
        Runtime.getRuntime().exec("java -jar G:/Downloads/spmf.jar run FPClose output/transactiontable.txt output/patterns.txt .3%",null);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    //output spmf to a file and then input to PopulateRelatedFeatures

}


    public static void fileBasedTT() throws Exception {
        //create a transaction table, output to console
//1.iterate over projects table and then over the method IDs and find a check
//if method is in cluster table, then get its cluster ID, see if that cluster ID has at least 3 instances
//(for frequent item) then add it into the transaction table row
        System.out.println("Starting mining patterns");
        DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
        dbLayer.initializeConnectorToWriteTransaction();
//populate api_call_index table
        ArrayList<String> sList = dbLayer.getClusterIDsPerFile();
        dbLayer.closeConnector();
        //output to a file and then input TT to spmf via command line
        FileWriter myWriter = new FileWriter("Resources/output/FBtransactiontable.txt");
        for(String s: sList)
        {
            myWriter.write(s+"\n");
        }
        myWriter.close();
        //run spmf
        try {
            Runtime.getRuntime().exec("java -jar libs/spmf.jar run FPClose Resources/output/FBtransactiontable.txt Resources/output/FBpatterns.txt .01%",null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //output spmf to a file and then input to PopulateRelatedFeatures

    }
}

