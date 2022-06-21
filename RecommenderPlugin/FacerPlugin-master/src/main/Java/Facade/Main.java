package Facade;

import related_methods._3_PopulateRelatedFeatures.PopulateRelatedFeatures;
import support.Constants;

public class Main {

    public static void main(String args[]) throws Exception {
        //download dataset
        //GithubReposDownloader.downloadDataset();
        //once the dataset is downloaded you need to extract each zip file to a separate folder
        //parse into database
        Constants.PROJECTS_ROOT = "Resources/Dataset";
        Constants.DATABASE = "jdbc:mysql://localhost/"+ "plugins" + "?useSSL=false&user=root";
        //Constants.CLASSPATH = args[1];
        Constants.CLASSPATH = "C:\\Program Files\\Java\\jre1.8.0_144\\lib\\rt.jar";
        Constants.LUCENE_INDEX_DIR = "Resources/LuceneIndex";
        //ParseInOneGo.parse();
        //cluster method clones
        //PairwiseSequenceScoring.calculatePairwiseSimilarities();
        //GraphClustering.cluster();
        //mine co-occurring cross-file patterns
        //TransactionTable.fileBasedTT();
        PopulateRelatedFeatures.populateRelatedFeatures(Constants.DATABASE);
        //now you can execute Gradle's runIde to test the FACER recommender

    }
}
