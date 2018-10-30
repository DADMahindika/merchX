/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Dilki Mahindika
 */
public class ProductPriceDatabase {
    
    private static List<String[]> productsPricesBaseCsv;
    private HashMap<String, Float> productPrices = new HashMap<>();
    
    public HashMap<String, Float> readProductPrices(){
        CsvReader csvReader = new CsvReader("C:\\Users\\Dilki Mahindika\\Desktop\\data_t.csv");
        try {
            productsPricesBaseCsv = csvReader.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        productsPricesBaseCsv.forEach((row) -> {
            productPrices.put(row[0],Float.valueOf(row[1]));  
            
        });
        
        return productPrices;
    }
   
}
