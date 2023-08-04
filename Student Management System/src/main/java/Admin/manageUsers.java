package Admin;

import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import static Admin.adminMain.adminInfo;
import static Common.CommonMethods.*;
import static Common.CommonLayout.*;public class manageUsers {

    public static BorderPane manageUsersMain() {
        // Create buttons
        Button addUsersButton = createButtonStyle("add users");
        Button removeUsersButton = createButtonStyle("remove user");

        // Create a VBox to hold the buttons
        VBox manageUsersButtons = createVBoxStyle();
        manageUsersButtons.setStyle("-fx-background-color:White;");
        manageUsersButtons.getChildren().addAll(addUsersButton, removeUsersButton);
        BorderPane manageUsersButtonsPane=new BorderPane();
        manageUsersButtonsPane.setCenter(manageUsersButtons);

        // Event handler for addUsersButton
        addUsersButton.setOnAction(e -> {
            // Set the top content of adminInfo to the result of AddUsers() method
            adminInfo.setTop(AddUsers());
            System.out.println("addUsersButton");
        });

        // Event handler for removeUsersButton
        removeUsersButton.setOnAction(e -> {
            // Set the top content of adminInfo to the result of removeUserGrid() method
            adminInfo.setTop(removeUserGrid());
            System.out.println("removeUsersButton");
        });

        return manageUsersButtonsPane;
    }


    public static GridPane AddUsers() {
        // Create the Form control
        Label lblUserType = new Label("User Type:");
        ChoiceBox<String> choiceBoxUserType = new ChoiceBox<>();
        choiceBoxUserType.getItems().addAll("StudentTeacher", "Teacher", "Administrators");

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Label lblID = new Label("ID:");
        TextField txtID = new TextField();
        Label lblLevel = new Label("Level:");
        TextField txtLevel = new TextField();
        Button btnAdduser = new Button("add User");
        txtID.setVisible(true);
        txtPassword.setVisible(true);
        txtLevel.setVisible(true);
        txtUsername.setVisible(true);

        lblLevel.setVisible(false);
        txtLevel.setVisible(false);

        // Create the user form pane
        GridPane userFormPane = new GridPane();
        userFormPane.setPadding(new Insets(10));
        userFormPane.setHgap(5);
        userFormPane.setVgap(5);

        // Add nodes to the pane
        userFormPane.add(lblUserType, 0, 0);
        userFormPane.add(choiceBoxUserType, 1, 0);
        userFormPane.add(lblUsername, 0, 1);
        userFormPane.add(txtUsername, 1, 1);
        userFormPane.add(lblPassword, 0, 2);
        userFormPane.add(txtPassword, 1, 2);
        userFormPane.add(lblID, 0, 3);
        userFormPane.add(txtID, 1, 3);
        userFormPane.add(lblLevel, 0, 4);
        userFormPane.add(txtLevel, 1, 4);
        userFormPane.add(btnAdduser, 1,4 );
        btnAdduser.setVisible(false);
        String[] path = {""};
        // Event listener for choiceBoxUserType to hide level from teacher if he  chooses   level and courses from admin
        choiceBoxUserType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                path[0] = filePath(newValue);
                System.out.println("path: "+ path[0]);

                if (newValue.equals("StudentTeacher")) {
                    lblLevel.setVisible(true);
                    txtLevel.setVisible(true);

                    userFormPane.setConstraints(btnAdduser,1,5);
                    btnAdduser.setVisible(true);
                }
                 if (newValue.equals("Teacher")) {
                    lblLevel.setVisible(false);
                    txtLevel.setVisible(false);
                    userFormPane.setConstraints(btnAdduser,1,4);


                }

                 if (newValue.equals("Administrators")) {
                    lblLevel.setVisible(false);
                    txtLevel.setVisible(false);
                    userFormPane.setConstraints(btnAdduser,1,4); // change button place to be the laste of form



                }
            }
        });

        btnAdduser.setOnAction(e->
        {
            JSONObject userJsonObject = new JSONObject();
            System.out.println("action: "+ path[0]);
            // create json object from data
            userJsonObject.put("Username",txtUsername.getText());
            userJsonObject.put("ID", txtID.getText());
            userJsonObject.put("password", txtPassword.getText());
            userJsonObject.put("userType",choiceBoxUserType.getValue());


            if (Objects.equals(choiceBoxUserType.getValue(), "StudentTeacher"))  // student only
            {
                userJsonObject.put("level", txtLevel.getText());
                // hard coded courses for the next version will  be in programmed way
                JSONArray stdCourses=new JSONArray();
                JSONObject stdCourse=new JSONObject();
                stdCourse.put("Teacher","Hoda Amir");
                stdCourse.put("Name","DSA");
                stdCourses.put(stdCourse);
                userJsonObject.put("Courses",stdCourses);
            }
            else if (Objects.equals(choiceBoxUserType.getValue(), "Teacher"))   // student only student and teacher
            {
                JSONArray teacCourses=new JSONArray();
                JSONArray Students=new JSONArray();
                JSONObject student=new JSONObject();
                student.put("Name","Khaled Abdullah");
                student.put("Attendebne_num",1);
                Students.put(student);

                JSONObject teacCourse=new JSONObject();
                teacCourse.put("Students",Students);
                teacCourse.put("Name","DSA");
                teacCourses.put(teacCourse);

                userJsonObject.put("Courses",teacCourses);
            }
            // to remoce data after added
            txtUsername.setText("");
            txtID.setText("");
            txtPassword.setText("");
            txtLevel.setText("");
            choiceBoxUserType.setValue(null);
            btnAdduser.setVisible(false);


            //variable to check that the user if founded
            Boolean checkUsrFounded=false;
            try
            {
                checkUsrFounded= validateCredentials(txtUsername.getText(), txtPassword.getText(),choiceBoxUserType.getValue(),false)==null;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            //check if the use is founded in data
            if (path[0] != ""&&checkUsrFounded==true) {
                //read file to add user
                JSONArray usersJson =readFile(path[0]);
                usersJson.put(userJsonObject);

                // write to the file after  the user is added
                FileWriter usersFile = null;
                try {
                    usersFile = new FileWriter("jsonFiles/"+path[0]);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    usersFile.write(usersJson.toString());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (path[0] !="") {
                    try {
                        usersFile.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //create alert to find infom that the user is added
                    Alert confirmAdd=creatAlert(Alert.AlertType.INFORMATION,"Added","Done","the user is added");
                    confirmAdd.showAndWait();
                }

            }

            else if (path[0] =="")
            {
                //create alert to find infom that the user is added
                Alert confirmAdd=creatAlert(Alert.AlertType.WARNING,"choose","choose type please","type choose");
                confirmAdd.showAndWait();
            }

            else if (checkUsrFounded==false)
            {
                //create alert to find infom that the user is added
                Alert confirmAdd=creatAlert(Alert.AlertType.WARNING,"Existed","this user is Existed "," Existed user" );
                confirmAdd.showAndWait();
            }




        });

        return userFormPane;

    }


    public static GridPane removeUserGrid() {
        // Create a GridPane for removing a user
        GridPane removeUserGrid = new GridPane();
        Button removeUser = new Button("Remove user");
        ChoiceBox<String> type = new ChoiceBox<>();
        type.setItems(FXCollections.observableArrayList("StudentTeacher", "Teacher", "Administrators"));

        // Add labels for user details
        Label nameLbl = new Label("User Name:");
        Label passLbl = new Label("Password:");
        Label typeLbl = new Label("type");

        // Add Texts for user data
        TextField nameTxt = createText("");
        TextField passTxt = createText("");
        nameTxt.setEditable(true);
        passTxt.setEditable(true);

        // Set grid properties
        removeUserGrid.setHgap(10);
        removeUserGrid.setVgap(10);
        removeUserGrid.setPadding(new Insets(10));

        // Add components to the grid
        removeUserGrid.add(nameLbl, 0, 0);
        removeUserGrid.add(nameTxt, 1, 0);
        removeUserGrid.add(passLbl, 0, 1);
        removeUserGrid.add(passTxt, 1, 1);
        removeUserGrid.add(typeLbl, 0, 2);
        removeUserGrid.add(type, 1, 2);
        removeUserGrid.add(removeUser, 1, 3);

        // Event handler for removeUser button
        removeUser.setOnAction(e -> {
            try {
                // Validate credentials and remove user
                JSONObject JSONObjectRemoved = validateCredentials(nameTxt.getText(), passTxt.getText(), type.getValue(), true);
                String path = filePath(type.getValue());
                if (JSONObjectRemoved != null && path != "") {
                    Alert waraning=creatAlert(Alert.AlertType.INFORMATION,"Removed","The user is Removed","Done");
                    waraning.showAndWait();
                    nameTxt.setText("");
                    passTxt.setText("");
                    type.setValue("");
                    System.out.println("delete");
                } else {
                    Alert waraning=creatAlert(Alert.AlertType.WARNING,"Warning","Wrong username or password","This user not founded");
                    waraning.showAndWait();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return removeUserGrid;
    }
}