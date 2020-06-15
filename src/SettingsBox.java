import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import javax.swing.*;
import java.util.ArrayList;

public class SettingsBox extends VBox
{
    private ArrayList<Label> labels = new ArrayList<>();
    private ArrayList<Label> placeholders = new ArrayList<>();
    private ArrayList<TextField> textFields = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Slider> sliders = new ArrayList<>();
    private ArrayList<ComboBox> comboBoxes = new ArrayList<>();

    public SettingsBox(){
        super();
        this.setPadding(new Insets(10,10,10,10));

    }

    public void addLabel(String text){
        Label l = new Label();
        l.setText(text);
        l.setFont(new Font("Arial", 20));
        labels.add(l);
        this.getChildren().add(l);

    }

    public void addSlider(int min, int max, double step){
        Slider s = new Slider();
        s.setBlockIncrement(step);
        s.setMax(max);
        s.setMin(min);
        sliders.add(s);
        this.getChildren().add(s);

    }

    public void addPlaceholder(int size){
        Label l = new Label();
        l.setFont(new Font("Arial", size));
        placeholders.add(l);
        this.getChildren().add(l);

    }

    public void addComboBox(ArrayList<String> options){
        ObservableList<String> o = FXCollections.observableArrayList();
        o.addAll(options);
        ComboBox c = new ComboBox(o);
        c.getSelectionModel().selectFirst();
        comboBoxes.add(c);
        this.getChildren().add(c);
    }

    public void addTextField(String defVal){
        TextField tf = new TextField();
        textFields.add(tf);
        tf.setText(defVal);
        this.getChildren().add(tf);
    }

    public void addButton(String text){
        Button b = new Button();
        b.setText(text);
        buttons.add(b);
        this.getChildren().addAll(b);
    }

    public ArrayList<Button> getButtons(){return buttons;}

    public ArrayList<TextField> getTextFields(){return textFields;}

    public ArrayList<ComboBox> getComboBoxes(){return comboBoxes;}


}
