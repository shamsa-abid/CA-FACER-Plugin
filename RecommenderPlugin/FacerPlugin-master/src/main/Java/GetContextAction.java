import Parser.ContextVisitor;
import Parser.ParseInOneGo;
import RelatedMethods.db_access_layer.EvaluationDAL;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import methodsearch.StudentsEvaluatorStage1;
import org.jetbrains.annotations.NotNull;
import support.SpecialCharFileReader;

import java.util.ArrayList;
import java.util.HashMap;

public class GetContextAction extends AnAction{

    public static Editor editor = null;
    Project projectRef = null;
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        ContextVisitor.contextMethodStrings = new ArrayList<>();
        projectRef = e.getProject();
        editor = e.getData(CommonDataKeys.EDITOR);

        //two lines below get the open file code as string and parse
        String fileContent = editor.getDocument().getText();

        PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
        VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
        String path = vFile.getPath();
        ParseInOneGo.fullFile = SpecialCharFileReader.getFileText(path);

        boolean parseStatus= false;
        ParseInOneGo.parse = Parser.ParseInOneGo.parseActiveFile(fileContent);

        System.out.println(parseStatus);
//        CompilationUnit cu = Parser.ParseInOneGo.parse(fileContent);
//
        if(ParseInOneGo.parse != null)
        {
            ContextVisitor walk0 = new ContextVisitor();
            ParseInOneGo.parse.accept(walk0);
            //parsedSuccessfully = true;
        }
        System.out.println(ContextVisitor.contextMethodStrings);
        //System.out.println(ContextVisitor.contextAPICalls);
        StudentsEvaluatorStage1 studentsEvaluator = new StudentsEvaluatorStage1();
        FACERConfigurationStateComponent configurationComponent = FACERConfigurationStateComponent.getInstance();

        try {
            //loop over contextMethodStrings Arraylist of methods in context
            //doing it for first method only for now
            FACERSearchService.contextFeatureMappings =  new ArrayList<Integer>();
            FACERSearchService.contextProjectsMappings =  new ArrayList<Integer>();
            FACERSearchService.projectIDScores = new HashMap<>();
            for(String s:ContextVisitor.contextMethodStrings) {
                int featureID = studentsEvaluator.searchContextFeature(s, 10, configurationComponent.getDatabaseURL(), configurationComponent.getResourcesFolderRootPath());
                ArrayList<Integer> projectIDs = new ArrayList<>();
                if(featureID!=0) {
                    projectIDs = EvaluationDAL.getInstance().getProjectsContainingFeature(featureID);
                    FACERSearchService.contextFeatureMappings.add(featureID);
                    for(int pid: projectIDs)
                        FACERSearchService.contextProjectsMappings.add(pid);

                }
            }
            ArrayList<Integer> projectIDs = new ArrayList<>();
/*
            for(String s:ContextVisitor.contextAPICalls) {
                FACERSearchService.projectIDScores = EvaluationDAL.getInstance().getProjectsContainingAPICall(s, FACERSearchService.projectIDScores);
//                    FACERSearchService.contextFeatureMappings.add(featureID);
//                    for(int pid: projectIDs)
//                        FACERSearchService.contextProjectsMappings.add(pid);
            }

  */
            //below code updates the projectIDscores with actual scores
            //int numAPICalls = ContextVisitor.contextAPICalls.size();

//            Iterator it = FACERSearchService.projectIDScores.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                int pid = (int) pair.getKey();
//                double count = (double) pair.getValue();
//                pair.setValue(count/numAPICalls);
//                FACERSearchService.contextProjectsMappings.add(pid);
//            }



            //for(int pid: projectIDs)
              //  FACERSearchService.contextProjectsMappings.add(pid);

            FACERSearchService.contextProjectCloneIDs = EvaluationDAL.getInstance().getContextBasedProjectCloneIDs(FACERSearchService.contextProjectsMappings);
            System.out.println(FACERSearchService.contextFeatureMappings);
            //ArrayList<Integer> featureRecommendations = McMillanEvaluation.getMcMillanFeatureRecommendations(false,10,0.5,0,0,contextFeatureMappings,0,false,0);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }
    }