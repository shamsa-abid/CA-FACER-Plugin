package clustering;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GraphClustering {
    static ArrayList<ClusterDTO> clusterList = new ArrayList<>();
    static Set<Integer> clusteredMethodsSet = new HashSet<Integer>();
    //dfs method - do a dfs on each distinct mid1 and include everyone on its path in one cluster
        //forms overlapping clusters

    //single linkage heirarchical clustering method plus dfs method
    //for each row in simscore table(mid1,mid2,score)
    //order by simscore

    //look at edges with highest score and save in highscorelist(make new list after change in score)
    //check if m1 in some cluster then add m2 to existing cluster
    //else if m2 in some cluster then add m1 to existing cluster
    //else make new cluster and add m1 and m2 to cluster

    public static void main(String args[]) throws Exception {

        clusterPLBARTcsv();
        /*
        //make a query to get all simscores in a list datastructure(mid1,mid2,score) order by score desc
        DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
        dbLayer.initializeConnectorToWriteClustersFromR();

//        Edge e1 = new Edge(1,2,1.0);
//        Edge e2 = new Edge(1,3,1.0);
//        Edge e3 = new Edge(2,3,0.5);
//        Edge e4 = new Edge(4,6,0.5);
//        Edge e5 = new Edge(4,3,0.5);
//        ArrayList<Edge> edgesList = new ArrayList<>();
//        edgesList.add(e1);
//        edgesList.add(e2);
//        edgesList.add(e3);
//        edgesList.add(e4);
//        edgesList.add(e5);
        ArrayList<Edge> edgesList = dbLayer.getEdges();
        System.out.println("Got edgeslist of size: "+edgesList.size());
        ClusterDTO cluster = new ClusterDTO();

        //get score of first edge
        int start = 0;
        int end = 0;
        double currentScore = edgesList.get(0).getScore();
        //iterate over edges until score is different from first score
        while(end!=1) {
            for (int i = start; i < edgesList.size(); i++) {
                //if (edgesList.get(i).getScore() == currentScore) {
                    int mid1 = edgesList.get(i).getMid1();
                    int mid2 = edgesList.get(i).getMid2();
                    //check if m1 is in some cluster
                    int clusterID = inCluster(clusterList, mid1);
                    if (clusterID != -1)//mid1 is in some cluster
                    {

                        //add m2 to same cluster as m1
                        if(!clusteredMethodsSet.contains(mid2))
                            addToCluster(clusterID, mid2, clusterList);
                        clusteredMethodsSet.add(mid2);

                    } else {
                        //check if m2 is in some cluster
                        clusterID = inCluster(clusterList, mid2);
                        if (clusterID != -1)//mid1 is in some cluster
                        {
                            //add m1 to same cluster as m2
                            if(!clusteredMethodsSet.contains(mid1))
                                addToCluster(clusterID, mid1, clusterList);
                            clusteredMethodsSet.add(mid1);

                        } else {
                            addEdgeToCluster(mid1, mid2, clusterList);
                            clusteredMethodsSet.add(mid1);
                            clusteredMethodsSet.add(mid2);

                        }
                    }


            }

            end = 1;
        }

        //System.out.println(clusterList);
        System.out.println("Saving clusters to table");
        dbLayer.populateClusterTable(clusterList);

         */
    }
    public static void clusterPLBARTcsv() throws Exception {
        DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
        dbLayer.initializeConnectorToWriteClustersFromR();

//        Edge e1 = new Edge(1,2,1.0);
//        Edge e2 = new Edge(1,3,1.0);
//        Edge e3 = new Edge(2,3,0.5);
//        Edge e4 = new Edge(4,6,0.5);
//        Edge e5 = new Edge(4,3,0.5);
//        ArrayList<Edge> edgesList = new ArrayList<>();
//        edgesList.add(e1);
//        edgesList.add(e2);
//        edgesList.add(e3);
//        edgesList.add(e4);
//        edgesList.add(e5);
        ArrayList<Edge> edgesList = getPLBARTEdges("");
        System.out.println("Got edgeslist of size: "+edgesList.size());
        ClusterDTO cluster = new ClusterDTO();

        //get score of first edge
        int start = 0;
        int end = 0;
        double currentScore = edgesList.get(0).getScore();
        //iterate over edges until score is different from first score
        while(end!=1) {
            for (int i = start; i < edgesList.size(); i++) {
                //if (edgesList.get(i).getScore() == currentScore) {
                int mid1 = edgesList.get(i).getMid1();
                int mid2 = edgesList.get(i).getMid2();
                //check if m1 is in some cluster
                int clusterID = inCluster(clusterList, mid1);
                if (clusterID != -1)//mid1 is in some cluster
                {

                    //add m2 to same cluster as m1
                    if(!clusteredMethodsSet.contains(mid2))
                        addToCluster(clusterID, mid2, clusterList);
                    clusteredMethodsSet.add(mid2);

                } else {
                    //check if m2 is in some cluster
                    clusterID = inCluster(clusterList, mid2);
                    if (clusterID != -1)//mid1 is in some cluster
                    {
                        //add m1 to same cluster as m2
                        if(!clusteredMethodsSet.contains(mid1))
                            addToCluster(clusterID, mid1, clusterList);
                        clusteredMethodsSet.add(mid1);

                    } else {
                        addEdgeToCluster(mid1, mid2, clusterList);
                        clusteredMethodsSet.add(mid1);
                        clusteredMethodsSet.add(mid2);

                    }
                }


            }

            end = 1;
        }

        //System.out.println(clusterList);
        System.out.println("Saving clusters to table");
        dbLayer.populateClusterTable(clusterList);
    }

    private static ArrayList<Edge> getPLBARTEdges(String s) throws IOException {
        s = "D:\\wahab\\wahabcsv.csv";
        BufferedReader br = new BufferedReader(new FileReader(new File(s)));

        ArrayList<Edge> edgesList = new ArrayList<Edge>();
        String st;
        while ((st = br.readLine()) != null) {

            String MID1 = st.substring(0,st.indexOf(","));
            String MID2 = st.substring(MID1.length()+1,st.lastIndexOf(","));
            //System.out.println(MID2);
            edgesList.add(new Edge(Integer.parseInt(MID1),Integer.parseInt(MID2),1.0));
        }

        return edgesList;


    }

    public static void cluster() throws Exception {
        System.out.println("Starting clustering");
        //make a query to get all simscores in a list datastructure(mid1,mid2,score) order by score desc
        DatabaseAccessLayer dbLayer = DatabaseAccessLayer.getInstance();
        dbLayer.initializeConnectorToWriteClustersFromR();

        ArrayList<Edge> edgesList = dbLayer.getEdges();
        System.out.println("Got edgeslist of size: "+edgesList.size());
        ClusterDTO cluster = new ClusterDTO();

        //get score of first edge
        int start = 0;
        int end = 0;
        double currentScore = edgesList.get(0).getScore();
        //iterate over edges until score is different from first score
        while(end!=1) {
            for (int i = start; i < edgesList.size(); i++) {
                //if (edgesList.get(i).getScore() == currentScore) {
                int mid1 = edgesList.get(i).getMid1();
                int mid2 = edgesList.get(i).getMid2();
                //check if m1 is in some cluster
                int clusterID = inCluster(clusterList, mid1);
                if (clusterID != -1)//mid1 is in some cluster
                {

                    //add m2 to same cluster as m1
                    if(!clusteredMethodsSet.contains(mid2))
                        addToCluster(clusterID, mid2, clusterList);
                    clusteredMethodsSet.add(mid2);

                } else {
                    //check if m2 is in some cluster
                    clusterID = inCluster(clusterList, mid2);
                    if (clusterID != -1)//mid1 is in some cluster
                    {
                        //add m1 to same cluster as m2
                        if(!clusteredMethodsSet.contains(mid1))
                            addToCluster(clusterID, mid1, clusterList);
                        clusteredMethodsSet.add(mid1);

                    } else {
                        addEdgeToCluster(mid1, mid2, clusterList);
                        clusteredMethodsSet.add(mid1);
                        clusteredMethodsSet.add(mid2);

                    }
                }


            }

            end = 1;
        }

        //System.out.println(clusterList);
        System.out.println("Saving clusters to table");
        dbLayer.populateClusterTable(clusterList);
    }

    private static void addPartnersToCluster(int clusterID, int i, ArrayList<Edge> edgesList, int mid1) {

        //find mid1 in all edges and add
    }


    private static void addEdgeToCluster(int mid1, int mid2, ArrayList<ClusterDTO> clusterList) {
        int clusterID = clusterList.size()+1;
        HashSet<Integer> methodsList = new HashSet<>();
        methodsList.add(mid1);
        methodsList.add(mid2);
        ClusterDTO cluster = new ClusterDTO();
        cluster.setClusterID(clusterID);
        cluster.setMethodsList(methodsList);
        clusterList.add(cluster);
    }

    private static void addToCluster(int clusterID, int mid2, ArrayList<ClusterDTO> clusterList) {
        for(ClusterDTO cluster:clusterList)
        {
            if(cluster.getClusterID()==clusterID)
            {
                cluster.getMethodsList().add(mid2);
            }
        }
    }

    private static int inCluster(ArrayList<ClusterDTO> clusterList, int mid1) {
        int result = -1;
        for(ClusterDTO cluster:clusterList)
        {
            if(cluster.getmethodsList().contains(mid1))
                return cluster.getClusterID();
        }
        return result;
    }
    private static boolean isClustered(ArrayList<ClusterDTO> clusterList, int mid1) {
        boolean result = false;
        for(ClusterDTO cluster:clusterList)
        {
            if(cluster.getmethodsList().contains(mid1))
                return true;
        }
        return result;
    }
}
