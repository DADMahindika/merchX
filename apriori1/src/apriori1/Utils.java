/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori1;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;


/**
 *
 * @author Dilki Mahindika
 */
public class Utils {
    
    private ArrayList<Pair<String, Float>> rules = new ArrayList<>();
    
   public boolean isSubset(String arr1[], 
                String arr2[], int m, int n)
    {
        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++)
        {
            for (j = 0; j < m; j++)
                if(arr2[i].equals(arr1[j]) )
                    break;
             
            /* If the above inner loop 
            was not broken at all then
            arr2[i] is not present in
            arr1[] */
            if (j == m)
                return false;
        }
         
        /* If we reach here then all
        elements of arr2[] are present
        in arr1[] */
        return true;
    }
   
   public ArrayList<String[]> getRuleElements(String rule){
        
       ArrayList<String[]> ruleElements =new ArrayList<String[]>();
       
        String[] ruleItems = rule.split("->");

        ruleItems[0] = ruleItems[0].replace(" ","");
        ruleItems[1] = ruleItems[1].replace(" ","");
//        System.out.println(ruleItems[0]+","+ruleItems[1]);
        Integer length = ruleItems[0].length();
        String left = ruleItems[0].substring(1,length-1);
        
        length = ruleItems[1].length();
        String right = ruleItems[1].substring(1,length-1);
        
        ruleElements.add(left.split(","));
        ruleElements.add(right.split(","));
        
        
        return ruleElements;
    }
   
   public ArrayList<Object[]> itemSetPrice(ArrayList<Pair<String, Float>> rules, HashMap<String, Float> productPrices){
     
       ArrayList<Object[]> ruleData = new ArrayList<Object[]>();
       
       rules.forEach((item) -> {
           
           ArrayList<String[]> ruleElements = getRuleElements(item.getLeft());
           String[] ruleLeft = ruleElements.get(0);
           String[] ruleRight = ruleElements.get(1);
           String[] itemset = (String[])ArrayUtils.addAll(ruleLeft, ruleRight);  //all items in the rule
           
           Float price = (float)0;
           for(String product:itemset ){
               price+= productPrices.get(product);
           }
           Object[] data = new Object[3];
           data[0] = item.getLeft();  //rule
           data[1] = item.getRight(); // confidence
           data[2] = price;
           
           ruleData.add(data);
       });
       
       return ruleData;
   }
    
   
}
