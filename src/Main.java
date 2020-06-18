import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Main extends Application {
    BorderPane layout;
    SettingsBox s = new SettingsBox();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> methods = new ArrayList<>();
    LineChart<Number,Number> lineChart;
    ArrayList<Double> histogramValueArray = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.###");
    BarChart<String,Number> histogramChart;
    LineChart<Number,Number> errorChart;

    @Override
    public void start(Stage primaryStage) throws Exception{
        layout = new BorderPane();
        generateUI();
        setUpBackEnd();
        primaryStage.setTitle("Integration für Arme");
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

   private void setUpBackEnd(){
        ArrayList<Button> buttons = s.getButtons();
       Button buttonErrorCalculate = new Button();

        Button startButton = new Button();
            for(Button B : buttons){
                if(B.getText().equals("Berechnen")){startButton = B;}
                if(B.getText().equals("E/n")){buttonErrorCalculate = B;}
            }


           startButton.setOnAction(new EventHandler<ActionEvent>() {
               @Override public void handle(ActionEvent e) {
                   getValuesFromUI();
               }
           });

            buttonErrorCalculate.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    plotErrorN();
                }
            });



   }

   private void plotErrorN()
   {
       int function = options.indexOf(s.getComboBoxes().get(0).getValue());
       int method = methods.indexOf(s.getComboBoxes().get(1).getValue());
       double x0 = Double.parseDouble(s.getTextFields().get(1).getText());
       double x = Double.parseDouble(s.getTextFields().get(2).getText());


       XYChart.Series errorSeries = new XYChart.Series();
       errorSeries.setName("Relativer Fehler - Anzahl Punkte");

       for(int i = 2; i <= 2000; i += 1)
       {

           MonteCarloHOM errorHOM = new MonteCarloHOM(i, function);
           MonteCarloDirect errorDirect = new MonteCarloDirect(i, function);

           if (method == 0) {
               double area = errorHOM.calculateIntegral(x0, x);
               double exactArea = 0;
               double a = x0;
               double b = x;

               ArrayList<Double> zerroPoints = errorHOM.zeroPoints(x0, x);
               ArrayList<Double> integrationIntervall = new ArrayList<>();
               integrationIntervall.add(x0);
               integrationIntervall.addAll(zerroPoints);
               integrationIntervall.add(x);

               for(double xValZero : integrationIntervall){
                   if(errorHOM.function((a + xValZero)/2) > 0) {
                       exactArea += errorHOM.calculateExactIntegral(a, xValZero);
                   }
                   else{exactArea -= errorHOM.calculateExactIntegral(a, xValZero);}
                   a = xValZero;

               }
               double e = 0;
               if (exactArea == 0)
               {
                   e = exactArea -area;
               }
               else{e = (exactArea - area) / exactArea;}
               errorSeries.getData().add(new XYChart.Data<>(i, e));

           }

           if (method == 1) {
               double area = errorDirect.calculateIntegral(x0, x);
               double exactArea = errorDirect.calculateExactIntegral(x0, x);
               double e = 0;
               if (exactArea == 0)
               {
                   e = exactArea -area;
               }
               else{e = (exactArea - area) / exactArea;}
               errorSeries.getData().add(new XYChart.Data<>(i, e));


           }

       }

       errorChart.getData().clear();
       errorChart.getData().add(errorSeries);
       errorSeries.getNode().setStyle("-fx-stroke: transparent");





   }

   private void getValuesFromUI(){

       ArrayList<TextField> textFields = s.getTextFields();
       ArrayList<Double> values = new ArrayList<>();
       ArrayList<ComboBox> comboBoxes = s.getComboBoxes();
       ArrayList<Label> labels = s.getLabels();
       int function = options.indexOf(comboBoxes.get(0).getValue());
       int method = methods.indexOf(comboBoxes.get(1).getValue());


       for (TextField t : textFields) {
           try {
               values.add(Double.parseDouble(t.getText()));
           }
           catch (Exception e){
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setTitle("Etwas ist schief gelaufen!");
               alert.setContentText(t.getText() + " ist keine valider Eingabe!");
               alert.showAndWait();
           }


       }

        for(int i = 0; i<values.get(3); i++)
        {
            plotAndCalculate(values, function, method);
        }
        double totalArea = 0;

        for(double V : histogramValueArray)
        {
            totalArea += V;
        }
        totalArea = totalArea / histogramValueArray.size();

        labels.get(0).setText(totalArea + "FE");

       loadHistogram();

    }


   private void plotAndCalculate(ArrayList<Double> values, int function, int method){
        MonteCarloHOM HOM = new MonteCarloHOM(0,0);
        MonteCarloDirect MTC = new MonteCarloDirect(0,0);
        double calculatedAreaDirect = 0;
        double calculatedAreaHOM =  0;



            if(method == 0) {
                HOM = new MonteCarloHOM((int) Math.round(values.get(0)), function);
                calculatedAreaHOM = HOM.calculateIntegral(values.get(1), values.get(2));
                histogramValueArray.add(calculatedAreaHOM);
            }

            if(method == 1) {
                MTC = new MonteCarloDirect((int) Math.round(values.get(0)), function);
                calculatedAreaDirect = MTC.calculateIntegral(values.get(1), values.get(2));
                histogramValueArray.add(calculatedAreaDirect);
            }


        ArrayList<ArrayList<Double>> nodesHOM = HOM.getMTCNodes();
        ArrayList<ArrayList<Double>> nodesDirect = MTC.getMTCNodes();

        XYChart.Series functionData = new XYChart.Series();
        functionData.setName("f(x)");

        XYChart.Series MTCNodesH = new XYChart.Series();
        XYChart.Series MTCNodesM = new XYChart.Series();
        MTCNodesH.setName("Punkte HOM Hit");
        MTCNodesM.setName("Punkte HOM Miss");

        XYChart.Series MTCNodesDirect = new XYChart.Series();
        MTCNodesDirect.setName("Punkte Direkt");

       lineChart.getData().clear();

        if(method == 0) {
            for (ArrayList<Double> n : nodesHOM) {
                double xPoint = n.get(0);
                double yPoint = n.get(1);
                double functionY = HOM.function(xPoint);
                double x0 = values.get(1);
                double x = values.get(2);

                if (((yPoint <= functionY && functionY > 0 && yPoint > 0) || (yPoint >= functionY && yPoint < 0 && functionY < 0)) && (xPoint <= x && xPoint >= x0)) {
                    MTCNodesH.getData().add(new XYChart.Data<>(n.get(0), n.get(1)));
                }
                else
                {

                    MTCNodesM.getData().add(new XYChart.Data<>(n.get(0), n.get(1)));
                }
            }


            for (double i = values.get(1); i <= values.get(2); i += 0.1) {
                double y = HOM.function(i);
                functionData.getData().add(new XYChart.Data<>(i, y));
            }

            lineChart.getData().add(MTCNodesH);
            MTCNodesH.getNode().setStyle("-fx-stroke: transparent");

            lineChart.getData().add(MTCNodesM);
            MTCNodesM.getNode().setStyle("-fx-stroke: transparent");

        }

       if(method == 1) {
           for (ArrayList<Double> n : nodesDirect) {
               MTCNodesDirect.getData().add(new XYChart.Data<>(n.get(0), n.get(1)));
           }

           for (double i = values.get(1); i <= values.get(2); i += 0.1) {
               double y = MTC.function(i);
               functionData.getData().add(new XYChart.Data<>(i, y));
           }

           lineChart.getData().add(MTCNodesDirect);
           MTCNodesDirect.getNode().setStyle("-fx-stroke: transparent");

       }



        lineChart.getData().add(functionData);
        functionData.getData().removeAll();



   }

   private void loadHistogram(){
       XYChart.Series hSeries = new XYChart.Series();
       Set<String> histogramValueDuplicatList = new HashSet<>();
       ArrayList<String> histogramValueBuffer = new ArrayList<>();


       for(double val: histogramValueArray){
           histogramValueDuplicatList.add(df.format(val));
           histogramValueBuffer.add(df.format(val));
       }

       Collections.sort(histogramValueArray);
       for(double val : histogramValueArray){
            if(histogramValueDuplicatList.contains(df.format(val))) {
               int occurrences = Collections.frequency(histogramValueBuffer, df.format(val));
               hSeries.setName("Häufigkeit der Werte");
               hSeries.getData().add(new XYChart.Data(df.format(val), occurrences));
               histogramValueDuplicatList.remove(df.format(val));
           }

        }

        histogramChart.getData().clear();
        histogramChart.getData().add(hSeries);
        histogramValueArray.clear();
   }
   private void generateUI()
    {
        generateSettingsbox();
        generateCharts();
    }

    private void generateCharts(){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickUnit(0.1);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(0.1);
        lineChart = new LineChart<>(xAxis,yAxis);

        NumberAxis xAxisError = new NumberAxis();
        NumberAxis yAxisError = new NumberAxis();
       errorChart = new LineChart<>(xAxisError,yAxisError);

        CategoryAxis  xAxisHistogram = new CategoryAxis ();
        NumberAxis yAxisHistogram = new NumberAxis();
        xAxisHistogram.setAnimated(false);
        histogramChart = new BarChart<>(xAxisHistogram,yAxisHistogram);

        GridPane rightPane = new GridPane();
        rightPane.add(errorChart, 0,0);
        rightPane.add(histogramChart, 0, 1);

        layout.setRight(rightPane);
        layout.setCenter(lineChart);
    }

    private void generateSettingsbox(){
        s.addLabel("0 FE");
        s.addPlaceholder(30);

        s.addLabel("Anzahl der Punkte: ");
        s.addPlaceholder(10);
        s.addTextField("10");

        s.addPlaceholder(20);
        s.addLabel("f(x): ");
        s.addPlaceholder(10);

        options.add("x");
        options.add("x^2");
        options.add("x^3");
        options.add("sin(x)");
        options.add("cos(x)");
        options.add("Sigmoid");
        options.add("e^x");

        s.addComboBox(options);

        s.addPlaceholder(20);
        s.addLabel("x0: ");
        s.addPlaceholder(10);
        s.addTextField("0");

        s.addPlaceholder(20);
        s.addLabel("x: ");
        s.addPlaceholder(10);
        s.addTextField("1");

        s.addPlaceholder(20);
        s.addLabel("Verfahren");
        s.addPlaceholder(10);

        methods.add("HOM");
        methods.add("Direkt");
        s.addComboBox(methods);

        s.addPlaceholder(20);
        s.addLabel("Wiederholungen für Mittelung");
        s.addPlaceholder(10);
        s.addTextField("1");

        s.addPlaceholder(20);
        s.addButton("Berechnen");

        s.addPlaceholder(20);
        s.addButton("E/n");

        layout.setLeft(s);

    }



}
