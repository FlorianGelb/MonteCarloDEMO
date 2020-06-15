import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.ArrayList;


public class Main extends Application {
    BorderPane layout;
    SettingsBox s = new SettingsBox();

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
       int function = 0;



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
        

    }

   private void plotAndCalculate(ArrayList<Double> values, int function, int mehtod, boolean averaging){

   }

   private void generateUI()
    {
        generateSettingsbox();
        generateCharts();

    }

    private void generateCharts(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

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
        s.addLabel("Anzahl der Punkte: ");
        s.addPlaceholder(10);
        s.addTextField("10");

        s.addPlaceholder(20);
        s.addLabel("f(x): ");
        s.addPlaceholder(10);

        ArrayList<String> options = new ArrayList<>();
        options.add("x");
        options.add("x^2");
        options.add("x^3");
        options.add("sin(x)");
        options.add("sin(x)");
        options.add("cos(x)");
        options.add("Sigmoid");
        options.add("e^x");

        s.addComboBox(options);

        s.addPlaceholder(20);
        s.addLabel("x0: ");
        s.addPlaceholder(10);
        s.addTextField("1");

        s.addPlaceholder(20);
        s.addLabel("x: ");
        s.addPlaceholder(10);
        s.addTextField("1");

        s.addPlaceholder(20);
        s.addLabel("Verfahren");
        s.addPlaceholder(10);

        ArrayList<String> methods = new ArrayList<>();
        methods.add("HMO");
        methods.add("Direkt");
        methods.add("Beides");
        s.addComboBox(methods);

        s.addPlaceholder(20);
        s.addLabel("Wiederholungen für Mittelung");
        s.addPlaceholder(10);
        s.addTextField("1");

        s.addPlaceholder(20);
        s.addButton("Berechnen");

        layout.setLeft(s);

    }



}
