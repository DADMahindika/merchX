/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Dilki Mahindika
 */
public class RulesFinder {
    
//    private List<> strongRules;
    
    private ArrayList<Pair<String, Float>> rules = new ArrayList<>();
    private HashMap<String, Object[]> nodes = new HashMap<>();
    private ArrayList<Integer[]> edges = new ArrayList<>();
    private int nextNode;
    
    private HashMap<String, Integer> itemFrequencies = new HashMap<>();
    
    
    public List<Pair<String, Float>>getRulesByLeftElement(List<Pair<String, Float>> strongRules, String checkItem,List<String[]> transactionBase){
       
       AprioriAlgorithm apriori = new AprioriAlgorithm(transactionBase); 
       Utils utils = new Utils();
      System.out.println("#####################\n");
       strongRules.forEach((item) -> {
            String rule = item.getLeft(); 
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleLeft = ruleElements.get(0);
            String[] checkItemArray = new String[]{checkItem};
            Float f = (float)1;
                       
            if(utils.isSubset(ruleLeft, checkItemArray, ruleLeft.length, checkItemArray.length)/*ArrayUtils.contains( ruleLeft, checkItem)*/){
                String[] ruleRight = ruleElements.get(1);
                String[] both = (String[])ArrayUtils.addAll(ruleLeft, ruleRight);
                float lift = apriori.calculateLift(apriori.getSupportOfItemSet(both), apriori.getSupportOfItemSet(ruleLeft) ,apriori.getSupportOfItemSet(ruleRight));
                rules.add(Pair.of(rule,lift));
//                 System.out.println(rule);
//                 System.out.println(apriori.getSupportOfItemSet(both)+","+apriori.getSupportOfItemSet(ruleLeft)+","+apriori.getSupportOfItemSet(ruleRight));
                
            } 

        });
       
       System.out.println("\n---- CHOSEN RULES FOR " + checkItem +" IN LEFT ----");
        rules.forEach((rule) -> {
            System.out.println(rule.getLeft() + " " + rule.getRight());

        });
        
//        System.out.println(rules.size());
        System.out.println("#####################\n");
        return rules;
        
    }
    
    public List<Pair<String, Float>>getRulesByRightElement(List<Pair<String, Float>> strongRules, String checkItem,List<String[]> transactionBase){
       
       AprioriAlgorithm apriori = new AprioriAlgorithm(transactionBase); 
       
       Utils utils = new Utils();
      System.out.println("#####################\n");
       strongRules.forEach((item) -> {
            String rule = item.getLeft(); 
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleRight = ruleElements.get(1);
            String[] checkItemArray = new String[]{checkItem};
            Float f = (float)1;
                       
            if(utils.isSubset(ruleRight, checkItemArray, ruleRight.length, checkItemArray.length)/*ArrayUtils.contains( ruleLeft, checkItem)*/){
                String[] ruleLeft = ruleElements.get(0);
                String[] both = (String[])ArrayUtils.addAll(ruleRight, ruleLeft);
                float lift = apriori.calculateLift(apriori.getSupportOfItemSet(both), apriori.getSupportOfItemSet(ruleLeft) ,apriori.getSupportOfItemSet(ruleRight));
                rules.add(Pair.of(rule,lift));
//                 System.out.println(rule);
//                 System.out.println(apriori.getSupportOfItemSet(both)+","+apriori.getSupportOfItemSet(ruleLeft)+","+apriori.getSupportOfItemSet(ruleRight));
            } 

        });
       
       System.out.println("\n---- CHOSEN RULES FOR " + checkItem +" IN RIGHT ----");
        rules.forEach((rule) -> {
            System.out.println(rule.getLeft() + " " + rule.getRight());

        });
        System.out.println("#####################\n");
        return rules;
        
    }
    
    public List<Pair<String, Float>>getRulesByLeftAndRightElement(List<Pair<String, Float>> strongRules, String checkItemLeft, String checkItemRight, List<String[]> transactionBase){
       
       AprioriAlgorithm apriori = new AprioriAlgorithm(transactionBase); 
       
       Utils utils = new Utils();
      System.out.println("#####################\n");
       strongRules.forEach((item) -> {
            String rule = item.getLeft(); 
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleRight = ruleElements.get(1);
            String[] ruleLeft = ruleElements.get(0);
            String[] checkItemArrayRight = new String[]{checkItemRight};
            String[] checkItemArrayLeft = new String[]{checkItemLeft};
//            Float f = (float)1;
                       
            if(utils.isSubset(ruleRight, checkItemArrayRight, ruleRight.length, checkItemArrayRight.length)/*ArrayUtils.contains( ruleLeft, checkItem)*/){
                if(utils.isSubset(ruleLeft, checkItemArrayLeft, ruleLeft.length, checkItemArrayLeft.length)){
                    String[] both = (String[])ArrayUtils.addAll(ruleRight, ruleLeft);
                    float lift = apriori.calculateLift(apriori.getSupportOfItemSet(both), apriori.getSupportOfItemSet(ruleLeft) ,apriori.getSupportOfItemSet(ruleRight));
                    rules.add(Pair.of(rule,lift));
                }
//                 System.out.println(rule);
//                 System.out.println(apriori.getSupportOfItemSet(both)+","+apriori.getSupportOfItemSet(ruleLeft)+","+apriori.getSupportOfItemSet(ruleRight));  
            } 

        });
       
       System.out.println("\n---- CHOSEN RULES FOR "+checkItemLeft+" AND "+checkItemRight+ " ----");
        rules.forEach((rule) -> {
            System.out.println(rule.getLeft() + " " + rule.getRight());

        });
        System.out.println("#####################\n");
        return rules;
        
    }
    
    public HashMap<String, Integer> getFrequencies(List<String[]> transactionBase){
        FrequentItems frequentItems = new FrequentItems(transactionBase);
        Set<String> uniqueItems = frequentItems.getAllUniqueItems();
        AprioriAlgorithm apriori = new AprioriAlgorithm(transactionBase);
        for(String item: uniqueItems){
            String[] itemArray = new String[]{item};
            itemFrequencies.put(item,apriori.getSupportOfItemSet(itemArray) );
        }
        
        System.out.println("\nFREQUENCIES\n");
        
        itemFrequencies.forEach((k,v)->{
            System.out.println(k+" -> "+v);
        });
        
        return itemFrequencies;

        
    }
    
    public void exportData(){
        
        int size = this.rules.size();
        this.nextNode = size+1;
        Utils utils = new Utils();
        
        this.rules.forEach((item) -> {
            String rule = item.getLeft(); 
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleLeft = ruleElements.get(0);
            String[] ruleRight = ruleElements.get(1);
            
            
            for(String element: ruleLeft){
                Object[] array = new Object[2];
                if(nodes.containsKey(element) == false){  
                    array[0] = nextNode;
                    array[1] = " ";
                    nodes.put(element, array);
                    this.nextNode++;
                }
            }
            for(String element: ruleRight){
                Object[] array = new Object[2];
                if(nodes.containsKey(element) == false){ 
                    array[0] = nextNode;
                    array[1] = " ";
                    nodes.put(element, array);
                    this.nextNode++;
                }
            }
            
        });
        
        //print hashmap
//        System.out.println("\nNodes\n");
//        nodes.forEach((k,v)->{
//            System.out.println(k+","+v);
//        });
        
     
        for(int i=1; i<=size; i++){
            Object[] array = new Object[2];
            Pair<String, Float> item = rules.get(i-1);
            String rule = item.getLeft(); 
            
            array[0] = i;
            array[1] = item.getRight();
            nodes.put("rule"+i, array);
            
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleLeft = ruleElements.get(0);
            String[] ruleRight = ruleElements.get(1);
            
            
            for(String element: ruleLeft){
                
                Integer[] edge = new Integer[2];
                edge[0] = (int)nodes.get(element)[0];
                edge[1] = i; 
                edges.add(edge);
            }
            
            for(String element: ruleRight){
                Integer[] edge = new Integer[2];
                edge[0] = i;
                edge[1] = (int)nodes.get(element)[0];
                
                edges.add(edge);
            }
            
        }
        
//        System.out.println("\nNodes\n");
//        nodes.forEach((k,v)->{
//            System.out.println(k+","+v[0]);
//        });
//        
//        System.out.println("\nEdges\n");
//        for(Integer[] edge:edges){
//            System.out.println(edge[0]+"->"+edge[1]);
//        }
        
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("nodes.csv"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RulesFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder sb_nodes = new StringBuilder();
        sb_nodes.append("id");
        sb_nodes.append(',');
        sb_nodes.append("Label");
        sb_nodes.append(',');
        sb_nodes.append("lift");
        sb_nodes.append('\n');

        
        nodes.forEach((k,v)->{
            sb_nodes.append(v[0]);
        sb_nodes.append(',');
        sb_nodes.append(k);
        sb_nodes.append(',');
        sb_nodes.append(v[1]);
        sb_nodes.append('\n');
        });

        pw.write(sb_nodes.toString());
        pw.close();
//        System.out.println("nodes done!");
        
        
        // write to edges.csv
        try {
            pw = new PrintWriter(new File("edges.csv"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RulesFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder sb_edges = new StringBuilder();
        sb_edges.append("source");
        sb_edges.append(',');
        sb_edges.append("target");
        sb_edges.append(',');
        sb_edges.append("type");
        sb_edges.append('\n');

        for(Integer[] edge:edges){
            sb_edges.append(edge[0]);
        sb_edges.append(',');
        sb_edges.append(edge[1]);
        sb_edges.append(',');
        sb_edges.append("Directed");
        sb_edges.append('\n');
        }
        
        pw.write(sb_edges.toString());
        pw.close();
//        System.out.println("edges done!");   
    }
  
}
