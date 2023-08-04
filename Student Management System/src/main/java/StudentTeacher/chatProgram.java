package StudentTeacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static Common.CommonLayout.*;
import static Common.CommonMethods.printInGoodWay;
import static Common.CommonMethods.readFile;

public class chatProgram {

    private static ObservableList<String> chatData=null;
    public static TextField writeMeessagetxt;
    private static VBox choosePane=new VBox();
    private static JSONObject user=null;
    private static JSONObject   newMessageto;
    private static String newval="";
    static String [] reciverdata;
    private static ArrayList<String> coursesNameTeacherName=new ArrayList<>();



    public static BorderPane chatMain(JSONObject userSent)
    {
        ChoiceBox<String> courseStudentchioce=new ChoiceBox<>();

        BorderPane chatProgramStudentPane=new BorderPane();
        BorderPane FullchatProgramStudentPane=new BorderPane();

        user=userSent;


        // crate text field
        Text profileHeader = new Text("Choose the person");
        profileHeader.setFont(Font.font("Arial", 15));
        profileHeader.setTranslateX(90);
        if (!FullchatProgramStudentPane.getChildren().contains(choosePane)) {
            FullchatProgramStudentPane.setTop(choosePane);
        }


        JSONArray Courses= (JSONArray) user.get("Courses");

        for (int i=0;i<Courses.length();i++)
        {
                if (!user.getString("userType").equals("Teacher")) {
                    String TeacherName = (String) ((JSONObject) Courses.get(i)).get("Teacher");
                    if (!courseStudentchioce.getItems().contains(TeacherName)) {
                        courseStudentchioce.getItems().add(TeacherName);
                        coursesNameTeacherName.add(TeacherName + "**" + (String) ((JSONObject) Courses.get(i)).get("Name"));
                    }
                }

                else {
                    JSONArray TeacherStds = Courses.getJSONObject(i).getJSONArray("Students");
                    for (int j = 0; j < TeacherStds.length(); j++) {
                        String StudentName = TeacherStds.getJSONObject(j).getString("Name");
                        if (!courseStudentchioce.getItems().contains(StudentName)) {
                            courseStudentchioce.getItems().add(StudentName);
                            coursesNameTeacherName.add(user.getString("Username") + "**" + StudentName);
                        }
                    }
                }
        }
        courseStudentchioce.setTranslateX(90);
        courseStudentchioce.setPrefSize(100,30);


        choosePane.getChildren().clear();
        choosePane.getChildren().addAll(profileHeader, courseStudentchioce);
        FullchatProgramStudentPane.setTop(choosePane);

        courseStudentchioce.getSelectionModel().selectedItemProperty().addListener((Obs,oldV,newVal)
                        ->
                {
                    if (!newVal.equals(null)||!courseStudentchioce.getValue().equals(null))
                    {
                        chatProgramStudentPane.setVisible(true);
                        newval=newVal;
                        showMessages(newval);
                    }

                }


        );



        //create listview whih data and set items
        ListView<String> chatStudentHistory=new ListView<>();
        chatData= FXCollections.observableArrayList();
        chatStudentHistory.setStyle("-fx-background-color: lightgray; -fx-font-size: 12px;");
        chatStudentHistory.setStyle("-fx-background-color: #f2f2f2;");
        chatStudentHistory.setItems(chatData);
        // create vbox to contain  list
        VBox chatHistoryVbox=new VBox(chatStudentHistory);
        chatProgramStudentPane.setTop(chatHistoryVbox);

        // create controls to send
        Label writeMeessagelbl=new Label("write");
        writeMeessagelbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        writeMeessagetxt=createText("");
        writeMeessagetxt.setEditable(true);
        Button sendBtn=createButtonStyle("Send");
        Button Refresh=createButtonStyle("Refresh");
        Button clearMessages=createButtonStyle("clearMessages");

        sendBtn.setPrefWidth(90);
        sendBtn.setStyle(" -fx-background-insets: 0; -fx-background-radius: 0; -fx-text-fill: black;");

        Refresh.setPrefWidth(90);
        Refresh.setStyle(" -fx-background-insets: 0; -fx-background-radius: 0; -fx-text-fill: black;");

        clearMessages.setPrefWidth(130);
        clearMessages.setStyle(" -fx-background-insets: 0; -fx-background-radius: 0; -fx-text-fill: black;");



        //create Hbox to hold controls
        HBox chatControl=createHboxStyle();
        chatControl.getChildren().addAll(writeMeessagelbl,writeMeessagetxt,sendBtn,Refresh,clearMessages);
        chatProgramStudentPane.setCenter(chatControl);


        sendBtn.setOnAction(
                e->
                {

                    newMessageto=createNewMessage(newval);
                    writeOnuserInfoFterAddmessage("message.json",newMessageto);
                    chatData.add(0,printInGoodWay(newMessageto));
                    writeMeessagetxt.setText("");
                    chatProgramStudentPane.setVisible(true);
                }
        );

        Refresh.setOnAction(
                e->
                {
                    showMessages(newval);
                    chatProgramStudentPane.setVisible(true);

                }
        );
        clearMessages.setOnAction(
                e->
                {
                    chatData.clear();
                    chatProgramStudentPane.setVisible(true);
                    System.out.println("clear");
                }
        );

        chatProgramStudentPane.setVisible(false);
        FullchatProgramStudentPane.setTop(choosePane);
        FullchatProgramStudentPane.setCenter(chatProgramStudentPane);
        return FullchatProgramStudentPane;



    }
    public static void showMessages(Object newVal)
    {
        chatData.clear();
       JSONArray Messageesfile=readFile("message.json");
       for (int i=0;i<Messageesfile.length();i++)
       {
           JSONObject messages=Messageesfile.getJSONObject(i);

           if ((messages.getString("receiver").equals(newval) && messages.getString("sender").equals(user.get("Username"))) ||
                   (messages.getString("receiver").equals(user.get("Username")) && messages.getString("sender").equals(newval)))
           {
               chatData.add(0,printInGoodWay(messages));

           }
       }

    }

    public static String getCurrentTime()
    {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
    public static JSONObject createNewMessage(String reciver)
    {
        JSONObject newMessage=new JSONObject();
        newMessage.put("receiver",reciver);
        newMessage.put("sender",user.get("Username"));
        newMessage.put("time",getCurrentTime());
        newMessage.put("message",writeMeessagetxt.getText());
        newMessage.put("type", "StudentTeacher");
        return newMessage;
    }





    public static void writeOnuserInfoFterAddmessage(String path,JSONObject message)
    {

        //Read the file

        JSONArray messages = readFile(path);
        messages.put(message);


        //write to the file
        try(FileWriter file2 =new FileWriter("jsonFiles/"+path)) {
            file2.write(messages.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}