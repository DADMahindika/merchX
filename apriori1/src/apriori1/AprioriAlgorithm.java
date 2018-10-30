package apriori1;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AprioriAlgorithm {
    private float minimumSupport;
    private int support;
    private float minimumConfidence;
    private List<String[]> transactionBase;
    private HashMap<String[], Integer> mostFrequentSets;
    private LinkedList<Pair<String[], Integer>> itemsWithMinSupport;
    private List<Pair<String, Float>> strongRules;
    private HashMap<String, Float>  strongRulesWithLift = new HashMap<>();
    private FrequentItems frequentItems;
    
    private List<Pair<String, Float>> rules;
    private HashMap<String, Object[]> nodes = new HashMap<>();
    private ArrayList<Integer[]> edges = new ArrayList<>();
    private int nextNode;

    public AprioriAlgorithm(float minimumSupport, float minimumConfidence, List<String[]> transactionBase) {
        this.transactionBase = transactionBase;
        this.minimumConfidence = minimumConfidence / 100.0f;
        this.minimumSupport = minimumSupport / 100.0f;
        this.mostFrequentSets = new HashMap<>();
        this.itemsWithMinSupport = new LinkedList<>();
        this.strongRules = new ArrayList<>();
        this.support = (int) (this.transactionBase.size() * (minimumSupport / 100.0f));
    }
    public AprioriAlgorithm(List<String[]> transactionBase){
        this.transactionBase = transactionBase;
        
    }
    
    public int getTotalTransaction(){
        return this.transactionBase.size();
    }

    public float getMinimumSupport() {
        return this.minimumSupport;
    }

    public void setMinimumSupport(float minimumSupport) {
        this.minimumSupport = minimumSupport;
    }

    public float getMinimumConfidence() {
        return this.minimumConfidence;
    }

    public void setMinimumConfidence(float minimumConfidence) {
        this.minimumConfidence = minimumConfidence;
    }

    public List<Pair<String, Float>> generateSuperFormula() {
        frequentItems = new FrequentItems(transactionBase);
        mostFrequentSets.putAll(getMostFrequentSets(frequentItems.getItemSetsFrequency()));
        return generateStrongRulesWithMinConfidence(mostFrequentSets);
    }

    
    // why comparing length here?
    private HashMap<String[], Integer> getMostFrequentSets(List<HashMap<String[], Integer>> itemSetsFrequency) {
        LinkedList<Pair<String[], Integer>> mostFrequentItemsWithMinSupport = getMostFrequentItemsWithMinSupport(itemSetsFrequency);
        HashMap<String[], Integer> mostFrequentSets = new HashMap<>();
        int lengthOfLastMostFrequentSet = mostFrequentItemsWithMinSupport.getLast().getLeft().length;
        mostFrequentItemsWithMinSupport.forEach((mostFrequentSet) -> {
            if (mostFrequentSet.getLeft().length == lengthOfLastMostFrequentSet) {
                mostFrequentSets.put(mostFrequentSet.getLeft(), mostFrequentSet.getRight());
            }
        });
        System.out.println("---- MOST FREQUENT SETS IN TRANSACTION BASE WITH MIN SUPPORT ----");
        mostFrequentSets.forEach((k, v) -> System.out.println(Arrays.toString(k) + " " + v));
        return mostFrequentSets;
    }

    private LinkedList<Pair<String[], Integer>> getMostFrequentItemsWithMinSupport(List<HashMap<String[], Integer>> itemSetsFrequency) {
        itemSetsFrequency.forEach((itemFrequency) ->
                itemFrequency.forEach((item, frequency) -> {
                    if (frequency >= support) {
                        Pair<String[], Integer> itemFrequencyMap = Pair.of(item, frequency);
                        itemsWithMinSupport.add(itemFrequencyMap);
                    }
                }));
        System.out.println("---- MOST FREQUENT ITEMS IN TRANSACTION BASE WITH MIN SUPPORT ----");
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        // edited here
//        itemsWithMinSupport.remove(0);
//        System.out.println(Arrays.toString(itemsWithMinSupport.get(0).getLeft()) + " " + itemsWithMinSupport.get(0).getLeft());
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        
        
        itemsWithMinSupport.forEach((itemFrequencySet) -> System.out.println(Arrays.toString(itemFrequencySet.getLeft()) + " " + itemFrequencySet.getRight()));
        return itemsWithMinSupport;
    }

    private List<Pair<String, Float>> generateStrongRulesWithMinConfidence(HashMap<String[], Integer> mostFrequentSets) {
        List<HashMap<String[], String[]>> lattice = generateLattice(mostFrequentSets);
        int iterator = 0;
        

        for (Map.Entry<String[], Integer> mostFrequentSetFrequency : mostFrequentSets.entrySet()) {
            lattice.get(iterator).forEach((k, v) -> itemsWithMinSupport.forEach((itemFrequencyPair) -> {
                if (Arrays.asList(itemFrequencyPair.getLeft()).equals(Arrays.asList(k))) {
                    float confidence = calculateConfidence(mostFrequentSetFrequency.getValue(), itemFrequencyPair.getRight());
                    float lift = calculateLift(mostFrequentSetFrequency.getValue(), itemFrequencyPair.getRight(),getSupportOfItemSet(v));
                    if (confidence >= minimumConfidence) {
                        String rule = createRule(k, v);
//                        Float [] conf_lift = {confidence,lift};
                        strongRules.add(Pair.of(rule, confidence)); 
                        this.strongRulesWithLift.put(rule, lift);
                    }
                }
            }));
            iterator += 1;
        }
        System.out.println("\n\n\n---- STRONG RULES WITH CONFIDENCE AND LIFT ----");
        Collections.sort(strongRules, Comparator.comparing(p -> -p.getRight()));
        strongRules.forEach((rule) -> {
            System.out.println(rule.getLeft() + " " + rule.getRight() + "    " + strongRulesWithLift.get(rule.getLeft()));

        });
        
//        exportData();
        return strongRules;
    }
    
    
    
        public void exportData(){
        
        rules = strongRules.subList(0,3);
//        rules = strongRules;
        int size = this.rules.size();
        this.nextNode = size+1;
        Utils utils = new Utils();
        
        this.rules.forEach((item) -> {
            String rule = item.getLeft(); 
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleLeft = ruleElements.get(0);
            String[] ruleRight = ruleElements.get(1);
            
//            for(String element: ruleLeft){
//                if(nodes.containsKey(element) == false){  
//                    nodes.put(element, nextNode);
//                    this.nextNode++;
//                }
//            }
            for(String element: ruleLeft){
                Object[] array = new Object[2];
                if(nodes.containsKey(element) == false){  
                    array[0] = nextNode;
                    array[1] = " ";
                    nodes.put(element, array);
                    this.nextNode++;
                }
            }
//            for(String element: ruleRight){
//                if(nodes.containsKey(element) == false){  
//                    nodes.put(element, nextNode);
//                    this.nextNode++;
//                }
//            }
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
//            nodes.put("rule"+i, i);
            
            ArrayList<String[]> ruleElements = utils.getRuleElements(rule);
            String[] ruleLeft = ruleElements.get(0);
            String[] ruleRight = ruleElements.get(1);
            
            
            for(String element: ruleLeft){
                
//                Integer[] edge = new Integer[2];
//                edge[0] = nodes.get(element);
//                edge[1] = i; 
//                edges.add(edge);
                Integer[] edge = new Integer[2];
                edge[0] = (int)nodes.get(element)[0];
                edge[1] = i; 
                edges.add(edge);
            }
            
            for(String element: ruleRight){
//                Integer[] edge = new Integer[2];
//                edge[0] = i;
//                edge[1] = nodes.get(element);
//                
//                edges.add(edge);
                Integer[] edge = new Integer[2];
                edge[0] = i;
                edge[1] = (int)nodes.get(element)[0];
                
                edges.add(edge);
            }
            
        }
        
//        System.out.println("\nNodes\n");
//        nodes.forEach((k,v)->{
//            System.out.println(k+","+v);
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

        
//        nodes.forEach((k,v)->{
//            sb_nodes.append(v);
//        sb_nodes.append(',');
//        sb_nodes.append(k);
//        sb_nodes.append('\n');
//        });
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
    
    /**
     * new method
     * @param mostFrequentSets
     * @return 
     */
//    public List<Pair<String, Float>> getStrongRules(HashMap<String[], Integer> mostFrequentSets){
//        List<HashMap<String[], String[]>> lattice = generateLattice(mostFrequentSets);
//        int iterator = 0;
//        for (Map.Entry<String[], Integer> mostFrequentSetFrequency : mostFrequentSets.entrySet()) {
//            lattice.get(iterator).forEach((k, v) -> itemsWithMinSupport.forEach((itemFrequencyPair) -> {
//                if (Arrays.asList(itemFrequencyPair.getLeft()).equals(Arrays.asList(k))) {
//                    float confidence = calculateConfidence(mostFrequentSetFrequency.getValue(), itemFrequencyPair.getRight());
////                    float lift = calculateLift(mostFrequentSetFrequency.getValue(), itemFrequencyPair.getRight(), itemFrequencyPair.getLeft());
////                    System.out.println("###########################\n");
////                    System.out.println(mostFrequentSetFrequency.getValue()+" , "+ itemFrequencyPair.getRight()+" , "+itemFrequencyPair.getLeft());
////                    System.out.println("############################\n");
//                    System.out.println(confidence);
//                    if (confidence >= minimumConfidence) {
//                        
//                        String rule = createRule(k, v);
//                        strongRules.add(Pair.of(rule, confidence));
//                    }
//                }
//            }));
//            iterator += 1;
//        }
//        
//        return strongRules;
//    }

    private float calculateConfidence(Integer xy, Integer x) {
        return (float) xy / x;
    }
    
    /**
     * Calculate Lift
     * @return 
     */
    public float calculateLift(Integer xy, Integer x, Integer y){
        int totalTransaction = this.getTotalTransaction();
        float upper = (float)xy/(float)x;
        float lower = (float)y/(float)totalTransaction;
//        System.out.println(xy+","+x+","+y);
//        System.out.println(upper+","+lower);
//        System.out.println((float)(upper/lower));
        return (float)(upper/lower);
    }
    

    private String createRule(String[] left, String[] right) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Arrays.toString(left)).append(" -> ").append(Arrays.toString(right));
        return stringBuffer.toString();
    }
   

    private List<HashMap<String[], String[]>> generateLattice(HashMap<String[], Integer> mostFrequentSetsWithFrequency) {
        List<LinkedList<String[]>> powerSets = new ArrayList<>();
        List<HashMap<String[], String[]>> lattice = new ArrayList<>();
        List<String[]> mostFrequentSets = new ArrayList<>();
        mostFrequentSetsWithFrequency.forEach((k, v) -> mostFrequentSets.add(k));
        mostFrequentSets.forEach((mostFrequentSet) -> {
            Set<String> set = new HashSet<>();
            Collections.addAll(set, mostFrequentSet);
            powerSets.add((LinkedList) frequentItems.generatePowerSet(set));
        });
        int iterator = 0;
        for (LinkedList<String[]> powerSet : powerSets) {
            powerSet.removeLast();
            Set<String> mostFrequentSet = new HashSet<>(Arrays.asList(mostFrequentSets.get(iterator)));
            HashMap<String[], String[]> latticeItem = new HashMap<>();
            for (String[] itemInPowerSet : powerSet) {
                Set<String> powerItemSet = new HashSet<>(Arrays.asList(itemInPowerSet));
                Set<String> differenceSet = Sets.difference(mostFrequentSet, powerItemSet);
                String[] diff = differenceSet.toArray(new String[differenceSet.size()]);
                latticeItem.put(itemInPowerSet, diff);
            }
            lattice.add(latticeItem);
            iterator += 1;
        }
        System.out.println("---- LATTICE ----");
        lattice.forEach((singleLattice) -> singleLattice.forEach((k, v) -> System.out.println(Arrays.toString(k) + " -> " + Arrays.toString(v))));
        return lattice;
    }
    
    public Integer getSupportOfItemSet(String[] itemset){ // 
        int count=0;
        Utils utils = new Utils();
        
        for(String[] bill: transactionBase ){
            boolean isSubset = utils.isSubset(bill,itemset,bill.length,itemset.length);
            if(isSubset == true){
                count++;
            }    
        }
        
        return count;
    }
    
    
}
