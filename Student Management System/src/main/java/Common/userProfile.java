package Common;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import org.json.JSONObject;

import static Common.CommonLayout.createGridPane;
import static Common.CommonLayout.createText;


public class userProfile {
    // Method to create the userProfile Information information grid pane
    public static GridPane profilePane(JSONObject user) {
        // Profile Pane

        // Create the user form pane
        GridPane userFormPane = createGridPane();
        // Add a header
        Text profileHeader = new Text(user.get("userType")+" Profile");
        profileHeader.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        // Create the scene controls
        //Label lblUserType = new Label("User Type:");

        Label lblUsername = new Label("Username:");
        TextField txtUsername = createText((String) user.get("Username"));;
        Label lblID = new Label("ID:");
        TextField txtID = createText((String) user.get("ID"));
        Label lblmail = new Label("Email:");
        TextField txtmail = createText((String) user.get("ID")+"@eng.zu.edu");
        if (user.get("userType").equals("StudentTeacher")) {
            Label lblLevel = new Label("level");
            TextField txtLevel = createText((String) user.get("level"));
            userFormPane.add(lblLevel, 0, 5);
            userFormPane.add(txtLevel, 1, 5);
        }




        // Add nodes to the pane
        userFormPane.add(profileHeader, 0,0);
        userFormPane.add(lblUsername, 0, 2);
        userFormPane.add(txtUsername, 1, 2);
        userFormPane.add(lblID, 0, 3);
        userFormPane.add(txtID, 1, 3);
        userFormPane.add(lblmail, 0, 4);
        userFormPane.add(txtmail, 1, 4);





        return userFormPane;
    }

}
