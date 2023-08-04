package Admin;
import MainPackage.LoginApp;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import org.json.JSONObject;

import static Admin.mangeInfoCheck.managInfoCheckPane;
import static Common.CommonLayout.createButtonStyle;
import static Common.CommonLayout.createVBoxStyle;
import static Common.userProfile.profilePane;


public class adminMain  {

    private static Stage adminStage;

    public static BorderPane adminInfo = new BorderPane();


    public static Scene  startAdmin(Stage stage,JSONObject Admin) {
        adminStage = stage;
        // Create the admin container scene
        Scene adminScene = mainAdminScene(Admin);
        return adminScene;
    }

    public static Scene mainAdminScene(JSONObject User) {
        // Create buttons for admin features

        Button adminInfoPageButton = createButtonStyle(" admin Info Page");
        Button manageUsersButton = createButtonStyle("Manage users");
        Button manageInformationsButton = createButtonStyle("Manage Informations");
        Button checkSubmissionButton = createButtonStyle("check submission");
        Button logOutButton = createButtonStyle("Logout");

        // Create a VBox and add buttons to it
        VBox adminFeatureSidebar = createVBoxStyle();
        adminFeatureSidebar.getChildren().addAll(adminInfoPageButton, manageUsersButton, manageInformationsButton, checkSubmissionButton, logOutButton);

        // Create a BorderPane to contain admin features
        // adminInfo.setStyle("-fx-background-color:White;");
        handleAdminInfoButton(User);

        // Create a pane to contain all components
        BorderPane fullAdminScene = new BorderPane();
        fullAdminScene.setCenter(adminInfo);
        fullAdminScene.setLeft(adminFeatureSidebar);

        // Handle side bar button actions
        adminInfoPageButton.setOnAction(e -> {
            handleAdminInfoButton(User);
            System.out.println("adminInfoPageButton");
        });

        manageUsersButton.setOnAction(e -> {
            // Get the VBox for managing users
            BorderPane manageUserPane = manageUsers.manageUsersMain();
            // Set the VBox as the top of the adminInfo BorderPane
                adminInfo.setTop(manageUserPane);
            System.out.println("manageUsersButton");
        });

        manageInformationsButton.setOnAction(e -> {
            BorderPane  managInfoPane=managInfoCheckPane("message.json",true);
            adminInfo.setTop(managInfoPane);
            System.out.println("manageInformationsButton");
        });

        checkSubmissionButton.setOnAction(e -> {
           BorderPane checkSubmissionPane=managInfoCheckPane("checkSubmission.json",false);
            adminInfo.setTop(checkSubmissionPane);
            System.out.println("checkSubmissionButton");
        });

        logOutButton.setOnAction(e -> {
           handlelogOutButton();
            System.out.println("logOutButton");
        });

        // Create a scene to contain the BorderPane
        return new Scene(fullAdminScene, 800, 600);
    }

    public static void handlelogOutButton() {
        LoginApp app=new LoginApp();
        app.start(adminStage);


    }


    // To handle admin info button
    public static void handleAdminInfoButton(JSONObject User) {
        // Get the admin info grid
        // testing object
        GridPane adminInfoGrid = profilePane(User);

        // Set the admin info grid as the top of the adminInfo BorderPane
        adminInfo.setTop(adminInfoGrid);
    }
}
