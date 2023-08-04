package MainPackage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.IOException;
import static Admin.adminMain.startAdmin;
import static Common.CommonLayout.creatAlert;
import static Common.CommonMethods.validateCredentials;
import static StudentTeacher.StudentMain.startStudent;
public class LoginApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {

        primaryStage = stage;

        setupLoginPage();

    }


    private  void setupLoginPage(){
        primaryStage.setTitle("Login");
        Image icon = new Image("file:"+"Images/Icon.png");
        Image backgroundImage = new Image("file:"+"Images/BackGround.jpg");

        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        primaryStage.getIcons().add(icon);

        // Create the root layout with background style
        BorderPane root = new BorderPane();
        root.setBackground(new Background(background));


        // Create UI components
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label typeLabel = new Label("Type:");
        ChoiceBox<String> typeChoiceBox = new ChoiceBox<>();
        typeChoiceBox.setPrefWidth(150);
        typeChoiceBox.getItems().addAll("Student", "Teacher", "Administrators");
        Button loginButton = new Button("Login");

        // Create a grid pane and set its properties
        GridPane loginComponentsGrid = new GridPane();
        loginComponentsGrid.setPadding(new Insets(30));
        loginComponentsGrid.setHgap(10);
        loginComponentsGrid.setVgap(10);

        // Add UI components to the grid pane
        loginComponentsGrid.add(usernameLabel, 0, 0);
        loginComponentsGrid.add(usernameField, 1, 0);
        loginComponentsGrid.add(passwordLabel, 0, 1);
        loginComponentsGrid.add(passwordField, 1, 1);
        loginComponentsGrid.add(typeLabel, 0, 2);
        loginComponentsGrid.add(typeChoiceBox, 1, 2);
        loginComponentsGrid.add(loginButton, 1, 3);
        loginButton.setTranslateX(50);

        loginComponentsGrid.setTranslateX(250);
        loginComponentsGrid.setTranslateY(200);
        root.setCenter(loginComponentsGrid);


        // Create a scene and set it on the stage
        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Event handler for the login button
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String type = typeChoiceBox.getValue();
            // Check if the type is selected
            if (type == null || type.isEmpty()) {
                Alert alert = creatAlert(AlertType.ERROR,"Invalid Type","Please select a valid type.",null) ;
                alert.showAndWait();
            }

            else
            {
                JSONObject user=null;
                try {
                     user=validateCredentials( username,  password,  type, false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (user!=null)
                {
                    if (type.equals("Administrators"))
                    {

                        primaryStage.setScene(startAdmin(primaryStage,user));
                        primaryStage.setTitle("Administrators");
                    }
                    else
                    {
                        primaryStage.setScene(startStudent(primaryStage,user));
                        primaryStage.setTitle(user.getString("userType"));

                    }
                }
                else
                {
                    Alert alert = creatAlert(AlertType.ERROR,"In correct User","Please select a valid Informations.",null) ;
                    alert.showAndWait();
                }
                }

        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
