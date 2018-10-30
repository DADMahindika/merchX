/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import weka.associations.Apriori;
import weka.core.Instances;

/**
 *
 * @author Dilki Mahindika
 */
public class Apriori1 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        BufferedReader reader = new BufferedReader( new
//            FileReader( "F:\\SEMESTER7\\FYP\\data1.csv" ) );
//            System.out.println("..........");
//            System.out.println(reader.read());
//            System.out.println("...........");
        
        
        Instances data = null;
        try {
            BufferedReader reader = new BufferedReader( new
            FileReader( "F:\\SEMESTER7\\FYP\\data2.arff" ) );
            data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        double deltaValue = 0.05;
        double lowerBoundMinSupportValue = 0.1;
        double minMetricValue = 0.5;
        int numRulesValue = 20;
        double upperBoundMinSupportValue = 1.0;
        String resultapriori;
        Apriori apriori = new Apriori();
        apriori.setDelta(deltaValue);
        apriori.setLowerBoundMinSupport(lowerBoundMinSupportValue);
        apriori.setNumRules(numRulesValue);
        apriori.setUpperBoundMinSupport(upperBoundMinSupportValue);
        apriori.setMinMetric(minMetricValue);
        try
        {
            apriori.buildAssociations( data );
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        //System.out.println("ONE\n");
        resultapriori = apriori.toString();
        //System.out.println("two\n");
        System.out.println(resultapriori);
        //System.out.println("three");
    }
    
}
