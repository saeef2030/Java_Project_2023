package Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import org.json.JSONObject;

import static Common.CommonMethods.printInGoodWay;
import static Common.CommonMethods.readFile;


public class mangeInfoCheck {
    private static ObservableList<String> items = FXCollections.observableArrayList();

    public static BorderPane managInfoCheckPane(String path,Boolean info)
    {




     // Create the list view and add the JSON objects to it
        ListView<String> submissionList = new ListView<>();
        submissionList.setItems(items);
        updatedData(path,info);

    // Create the button to refresh the list and add an action listener to it
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            updatedData(path,info);
        });

     // Create the button to clear the list and add an action listener to it
        Button clearButton = new Button("Clear List");
        clearButton.setOnAction(e -> {
            items.clear();
        });

     // Create the button to restore the original items and add an action listener to it
        Button restoreButton = new Button("Restore List");
        restoreButton.setOnAction(e -> {
            updatedData(path,info);
        });

    // Create the layout and add the list view and buttons to it
        BorderPane CheckSubmissionPane = new BorderPane();
        CheckSubmissionPane.setTop(submissionList);

    // Create an HBox to hold the buttons
        HBox checkSubmission = new HBox(refreshButton, clearButton, restoreButton);
        checkSubmission.setSpacing(30);
        checkSubmission.setAlignment(Pos.CENTER);
        CheckSubmissionPane.setCenter(checkSubmission);

        CheckSubmissionPane.setPadding(new Insets(15));




        return CheckSubmissionPane;

    }

    public static void updatedData(String path,Boolean info)
    {
        items.clear();
        String message="";
        JSONArray messageOrinfo=readFile(path);
        for (int i=0;i<messageOrinfo.length();i++)
        {

            if ( info )
            {
                 message=printInGoodWay((JSONObject) messageOrinfo.get(i));
            }
            else
                 message=printInGoodWayAdminCheck((JSONObject) messageOrinfo.get(i));

            items.add(0,message);
        }
    }


    public static String printInGoodWayAdminCheck(JSONObject jsonObject )
    {
        String messagetoPrintandOtherID;
        String name=jsonObject.getString("name");
        String subject=jsonObject.getString("subject");
        String Content = jsonObject.getString("Content");
        String time = jsonObject.getString("time");


        messagetoPrintandOtherID=  "name: "+name+"subject: "+subject+" Content:"+Content+" time:"+time+"\n";
        return messagetoPrintandOtherID;
    }



}

