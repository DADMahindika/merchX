/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori1;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Dilki Mahindika
 */
public class Validation {
    
    private static List<String[]> transactionBaseCsv;
    
    public List<String[]> generateTrainingSet(){
        
        CsvReader csvReader = new CsvReader("C:\\Users\\Dilki Mahindika\\Desktop\\transactions_categoryNames_distinct2.2.csv");
        
        try {
            transactionBaseCsv = csvReader.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
          
        return transactionBaseCsv;
    }
    
}
