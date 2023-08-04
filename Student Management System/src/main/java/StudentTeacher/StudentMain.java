package StudentTeacher;
import MainPackage.LoginApp;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import org.json.JSONObject;
import static Common.CommonLayout.*;
import static Common.CommonLayout.createButtonStyle;
import static Common.userProfile.profilePane;
import static StudentTeacher.Attendance.Attendance;
import static StudentTeacher.chatProgram.chatMain;

public class StudentMain {
    private static Stage StudentStage;

    public static Scene startStudent(Stage Stage, JSONObject User) {
        StudentStage = Stage;

        return StudentMainPane(User);
    }

    public static Scene StudentMainPane(JSONObject User) {

        GridPane adminInfoGrid = profilePane(User);
        BorderPane studentFullPane = new BorderPane();


        //Create button for Student
        Button profileStdbtn = createButtonStyle("profile");
        Button coursesbtn = createButtonStyle("courses");
        Button chatbtn = createButtonStyle("chat");
        Button Attendance = createButtonStyle("Take Attendance");


        Button logOutbtn = createButtonStyle("log Out");

        //Button actions
        profileStdbtn.setOnAction(e ->
        {
            studentFullPane.setCenter(profilePane(User));
        });

        coursesbtn.setOnAction(e ->
        {
            studentFullPane.setCenter(Courses.CoursesVbox(User));
        });

        chatbtn.setOnAction(e ->
        {
            studentFullPane.setCenter(chatMain(User));
            System.out.println("chat");
        });

        Attendance.setOnAction(e ->
        {
            studentFullPane.setCenter(Attendance(User));

            System.out.println("add");
        });

        logOutbtn.setOnAction(e ->
        {
            LoginApp app = new LoginApp();
            app.start(StudentStage);

        });

        //create Vbox for student
        VBox studentFeatures = createVBoxStyle();
        if (User.getString("userType").equals("Teacher")) {
            studentFeatures.getChildren().addAll(profileStdbtn, coursesbtn, chatbtn,Attendance, logOutbtn);
        }
        else
            studentFeatures.getChildren().addAll(profileStdbtn, coursesbtn, chatbtn, logOutbtn);
        studentFullPane.setLeft(studentFeatures);
        studentFullPane.setCenter(profilePane(User));
        return new Scene(studentFullPane, 850, 520);
    }
}
