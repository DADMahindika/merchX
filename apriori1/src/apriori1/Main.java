package apriori1;


import org.apache.commons.lang3.tuple.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

//import org.openide.util.Lookup;
//import org.gephi.project.api.ProjectController;
//import org.gephi.project.api.Workspace;
//import org.openide.util.Lookup;

public class Main {
    private static List<String[]> transactionBaseCsv;
    private static AprioriAlgorithm aprioriAlgorithm;
//    private static String PDF_SAVE_FOLDER = "F:\\SEMESTER7\\FYP\\Apriori\\apriori1\\";

    public static void main(String[] args) {
       
        //Change constructor parameter to your localization of .csv file
//        CsvReader csvReader = new CsvReader("C:\\Users\\Dilki Mahindika\\Desktop\\data_t.csv");
//        CsvReader csvReader = new CsvReader("C:\\Users\\Dilki Mahindika\\Desktop\\transactions_categoryNames_distinct3.2.csv");
//        CsvReader csvReader = new CsvReader("C:\\Users\\Dilki Mahindika\\Desktop\\transactions_categoryNames_distinct2.csv");
        CsvReader csvReader = new CsvReader("C:\\Users\\Dilki Mahindika\\Desktop\\transactions_categoryNames_distinct2.2.csv");
        float minimumSupport = 12.0f;
        float minimumConfidence = 60.0f;

        
        try {
            transactionBaseCsv = csvReader.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
          
        RulesFinder rf = new RulesFinder();
        
        aprioriAlgorithm = new AprioriAlgorithm(minimumSupport, minimumConfidence, transactionBaseCsv);
        // new codes
        List<Pair<String, Float>> strongRules = aprioriAlgorithm.generateSuperFormula();
        rf.getRulesByRightElement(strongRules, "Dress",transactionBaseCsv);
        rf.getRulesByLeftAndRightElement(strongRules, "Short", "Crewneck", transactionBaseCsv);
        rf.getFrequencies(transactionBaseCsv);
//        rf.exportData();





        //end
        //savePdf(aprioriAlgorithm.generateSuperFormula(), Pair.of(aprioriAlgorithm.getMinimumSupport(), aprioriAlgorithm.getMinimumConfidence()));
    }

//    public static void savePdf(List<Pair<String, Float>> strongRules, Pair<Float, Float> supportConfidencePair) {
//        AprioriPdfGenerator aprioriPdfGenerator = new AprioriPdfGenerator(strongRules, supportConfidencePair);
//        try {
//            aprioriPdfGenerator.generateDocument(new FileOutputStream(PDF_SAVE_FOLDER));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * Visualization
     */
//    ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
}
