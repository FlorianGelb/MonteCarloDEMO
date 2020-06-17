import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.text.DecimalFormat;
import java.util.ArrayList;


public class Main extends Application {
    BorderPane layout;
    SettingsBox s = new SettingsBox();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> methods = new ArrayList<>();
    LineChart<Number,Number> lineChart;
    ArrayList<Double> Histogram = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.###");

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

        Button startButton = new Button();
            for(Button B : buttons){
                if(B.getText().equals("Berechnen")){startButton = B;}
            }


           startButton.setOnAction(new EventHandler<ActionEvent>() {
               @Override public void handle(ActionEvent e) {
                   getValuesFromUI();
               }
           });


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
        for(double V : Histogram)
        {
            totalArea += V;
        }
        totalArea = totalArea / Histogram.size();

        labels.get(0).setText(df.format(totalArea) + "FE");


    }

   private void plotAndCalculate(ArrayList<Double> values, int function, int method){
        System.out.println(function);
        MonteCarloHOM HOM = new MonteCarloHOM(0,0);
        MonteCarloDirect MTC = new MonteCarloDirect(0,0);
        double calculatedAreaDirect = 0;
        double calculatedAreaHOM =  0;


        switch(method) {
            case 0:
                HOM = new MonteCarloHOM((int) Math.round(values.get(0)), function);
                calculatedAreaHOM = HOM.calculateIntegral(values.get(1), values.get(2));
                Histogram.add(calculatedAreaHOM);

            case 1:
                MTC = new MonteCarloDirect((int) Math.round(values.get(0)), function);
                calculatedAreaDirect= MTC.calculateIntegral(values.get(1), values.get(2));
                Histogram.add(calculatedAreaDirect);

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
                if (n.get(1) <= HOM.function(n.get(0))) {
                    MTCNodesH.getData().add(new XYChart.Data<>(n.get(0), n.get(1)));
                }
                if(n.get(1) > HOM.function(n.get(0)))
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

        loadHistogram();

   }

   private void loadHistogram(){

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
        LineChart<Number,Number> errorChaort = new LineChart<>(xAxisError,yAxisError);

        CategoryAxis  xAxisHistogram = new CategoryAxis ();
        NumberAxis yAxisHistogram = new NumberAxis();
        BarChart<String,Number> histogram = new BarChart<>(xAxisHistogram,yAxisHistogram);

        VBox leftChartBox = new VBox();
        leftChartBox.getChildren().addAll(errorChaort, histogram);

        layout.setRight(leftChartBox);
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
