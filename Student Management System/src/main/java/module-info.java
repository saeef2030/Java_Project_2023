module com.example.student_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;
    exports Admin;
    exports StudentTeacher;
    exports MainPackage;





    opens com.example.student_management_system to javafx.fxml;
    exports Common;
    opens Common to javafx.fxml;
}