import related_methods.automated_evaluation.FACERStage2RelatedMethodsMaha;
import com.google.gson.Gson;
import methodsearch.StudentsEvaluatorStage1;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FACERSearchService   {

    private static FACERSearchService instance = null;
    private static ArrayList<Method> querySearchResults;
    private static ArrayList<Method> relatedSearchResults;
    public static ArrayList<Integer> contextFeatureMappings;//the fetureIDs from contextually similar projects where the match is based on search results
    public static ArrayList<Integer> contextProjectsMappings;//the project IDs of projects having these feature IDs
    public static HashMap<Integer, Double> projectIDScores;
    public static HashMap<Integer, Set<Integer>> contextProjectCloneIDs = new HashMap<Integer, Set<Integer>>();
    public static FACERConfigurationStateComponent configurationComponent = FACERConfigurationStateComponent.getInstance();
    public static StudentsEvaluatorStage1 studentsEvaluator;
    private FACERSearchService() {}

    public static void main(String args[])
    {
        //System.out.println(getContextRelatedMethods(33399));

    }
    public static FACERSearchService getInstance() {
        if (instance == null) {
            instance = new FACERSearchService();
        }
        return instance;
    }

    public ArrayList getRecommendationsForQuery(String query) {
        ArrayList methodNames = new ArrayList();
        try {
            studentsEvaluator = new StudentsEvaluatorStage1();
            JSONArray results = studentsEvaluator.searchMethodsFACERAS(query, 20, configurationComponent.getDatabaseURL(), configurationComponent.getResourcesFolderRootPath());
            if (results != null) {
                querySearchResults = new ArrayList();
                relatedSearchResults = null;
                int len = results.size();
                for (int i = 0; i < len; i++){
                    Object methodJson = results.get(i);
                    Method method = new Gson().fromJson(methodJson.toString(), Method.class);
                    method.setType("query");
                    querySearchResults.add(method);
                    //methodNames.add(method.id + ": "+ method.projectID+": "+ method.name);
                    methodNames.add(method.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodNames;
    }

    public ArrayList getRelatedMethods(int methodId) {
        ArrayList methodNames = new ArrayList();
        try {
            FACERStage2RelatedMethodsMaha relatedMethodsEvaluator = new FACERStage2RelatedMethodsMaha();
            JSONArray relatedMethods = relatedMethodsEvaluator.getRelatedMethodsWithSupport(methodId, 3, configurationComponent.getDatabaseURL(), configurationComponent.getResourcesFolderRootPath());
            if (relatedMethods != null) {
                relatedSearchResults = new ArrayList();
                int len = relatedMethods.size();
                for (int i = 0; i < len; i++){
                    Object methodJson = relatedMethods.get(i);
                    Method method = new Gson().fromJson(methodJson.toString(), Method.class);
                    method.setType("related");
                    relatedSearchResults.add(method);
                    //methodNames.add(method.projectID+ ": " +method.support + ": " + method.name);
                    if(method.support==-1)
                    methodNames.add(method.name);
                    else methodNames.add(method.name+ " : support : "+ method.support);
                    //methodNames.add(method.id + ": " + method.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodNames;
    }

    public static ArrayList getContextRelatedMethods(int methodId) {
        ArrayList methodNames = new ArrayList();
        try {
            FACERStage2RelatedMethodsMaha relatedMethodsEvaluator = new FACERStage2RelatedMethodsMaha();

            JSONArray relatedMethods = relatedMethodsEvaluator.getContextRelatedMethods(methodId, contextFeatureMappings, contextProjectsMappings, contextProjectCloneIDs, projectIDScores, configurationComponent.getDatabaseURL(), configurationComponent.getResourcesFolderRootPath());
            if (relatedMethods != null) {
                relatedSearchResults = new ArrayList();
                int len = relatedMethods.size();
                for (int i = 0; i < len; i++){
                    Object methodJson = relatedMethods.get(i);
                    Method method = new Gson().fromJson(methodJson.toString(), Method.class);
                    method.setType("context relevant");
                    relatedSearchResults.add(method);
                    //methodNames.add(method.projectID+ ": " +method.support + ": " + method.name);
                    methodNames.add(method.name);
                    //methodNames.add(method.id + ": " + method.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodNames;
    }


    public Method getQueryResultMethod(int index) {
        if (index < querySearchResults.size()) {
            return querySearchResults.get(index);
        }
        return null;
    }

    public ArrayList<String> getAPICalls(int MID) throws Exception {
        return studentsEvaluator.getMethodAPICallsList(MID);
    }

    public Method getRelatedMethodAt(int index) {
        if (index < relatedSearchResults.size()) {
            return relatedSearchResults.get(index);
        }
        return null;
    }

    public String getAlgoForRelatedMethodAt(int index) {
        if (index < relatedSearchResults.size()) {
            return relatedSearchResults.get(index).algo;
        }
        return null;
    }

    CodeFile getCodeFileForMethod(int methodId){
        CodeFile codeFile = new CodeFile();
        try {
            FACERStage2RelatedMethodsMaha relatedMethodsEvaluator = new FACERStage2RelatedMethodsMaha();
            JSONObject codeFileJson = relatedMethodsEvaluator.getFileBody(methodId, configurationComponent.getDatabaseURL(), configurationComponent.getResourcesFolderRootPath());
            if (codeFileJson != null) {
                    codeFile = new Gson().fromJson(codeFileJson.toString(), CodeFile.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeFile;
    }

    public ArrayList getCalledMethods(int methodId) {
        ArrayList methodNames = new ArrayList();
        try {
            FACERStage2RelatedMethodsMaha relatedMethodsEvaluator = new FACERStage2RelatedMethodsMaha();
            JSONArray calledMethods = relatedMethodsEvaluator.getCalledMethods(methodId, configurationComponent.getDatabaseURL(), configurationComponent.getResourcesFolderRootPath());
            if (calledMethods != null) {
                relatedSearchResults = new ArrayList();
                int len = calledMethods.size();
                for (int i = 0; i < len; i++){
                    Object methodJson = calledMethods.get(i);
                    Method method = new Gson().fromJson(methodJson.toString(), Method.class);
                    method.setType("called");
                    relatedSearchResults.add(method);
                    methodNames.add(method.id + ": "+ method.projectID+": "+ method.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodNames;
    }

}
