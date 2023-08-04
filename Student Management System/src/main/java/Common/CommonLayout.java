package Common;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CommonLayout {
    public static VBox createVBoxStyle() {
        // Set up VBox for containing admin features
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPrefWidth(200);
        vbox.setStyle("-fx-background-color:#2B2B2B;");
        vbox.setPadding(new Insets(20));

        return vbox;
    }
    public static HBox createHboxStyle()
    {
        HBox Hbox = new HBox();
        Hbox.setSpacing(30);
        Hbox.setSpacing(10);
        Hbox.setPrefWidth(200);
        //Hbox.setStyle("-fx-background-color:#2B2B2B;");
        Hbox.setPadding(new Insets(20));

        Hbox.setAlignment(Pos.CENTER);

        return Hbox;
    }
    // Method to create a grid pane with specified properties
    public static GridPane createGridPane() {
        // Create a grid pane for the profile
        GridPane profilePane = new GridPane();
        //profilePane.setAlignment(HPos.CENTER);
        profilePane.setHgap(5);
        profilePane.setVgap(5);
        profilePane.setPadding(new Insets(5));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPrefWidth(70); // Adjust the width as needed

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPrefWidth(200); // Adjust the width as needed

        profilePane.getColumnConstraints().addAll(column1, column2);



        return profilePane;
    }

    // Method to create a TextField with specified properties
    public static TextField createText(String text) {
        // Create a TextField for user data
        TextField txt = new TextField(text);
        txt.setEditable(false);
        txt.setPrefWidth(200);
        txt.setStyle("-fx-background-color: #ffffff; -fx-background-insets: 0; -fx-background-radius: 0; -fx-text-fill: black;");

        return txt;
    }


    // To create sidebar buttons and their style
    public static Button createButtonStyle(String text) {
        // Create a button with the specified text
        Button btn = new Button(text);
        // Set the button style
        btn.setStyle("-fx-background-color: #555555;-fx-text-fill: white;-fx-font-weight: bold");
        btn.setPrefWidth(200);
        btn.setPadding(new Insets(10));

        return btn;
    }
    public static Alert creatAlert(Alert.AlertType alertType, String title, String content, String header)
    {
        Alert alert =new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);

        return alert;
    }


}
