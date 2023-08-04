package StudentTeacher;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import static Common.CommonMethods.readFile;

public class Attendance {
     private static JSONObject teacher;
    public static   TableView <Student> AttendanceTableView;
    public static BorderPane Attendance(JSONObject Teacher) {
        teacher=Teacher;
        BorderPane fullAttendancePane = new BorderPane();

        VBox ChiocePane = new VBox(10);
        ChiocePane.setPadding(new Insets(10));

        ChoiceBox<String> coursesBoxForTeacher = new ChoiceBox<>();
        // Create text field
        Text profileHeader = new Text("Choose the Course");
        profileHeader.setFont(Font.font("Arial", 15));
        coursesBoxForTeacher.setPrefWidth(120);
        ChiocePane.setTranslateX(120);
        // add element to vbox
        ChiocePane.getChildren().addAll(profileHeader, coursesBoxForTeacher);
        createAttendanceTableView();
        AttendanceTableView.setVisible(false);  // hide the taable until teacher added


        // read teacher courses and store it in chioce box
        JSONArray TeacherCourses =teacher.getJSONArray("Courses");
        for (int i=0;i<TeacherCourses.length();i++)
        {
            if (!coursesBoxForTeacher.getItems().contains(TeacherCourses.getJSONObject(i).getString("Name")))
                     coursesBoxForTeacher.getItems().add(TeacherCourses.getJSONObject(i).getString("Name"));
        }



        //hadle choose in chioce box
        coursesBoxForTeacher.getSelectionModel().selectedItemProperty().addListener((obs,oldval,newVal)->
        {
            if (!newVal.equals(null)||!coursesBoxForTeacher.getValue().equals(null))
            {
                AttendanceTableView.getItems().clear();
                AttendanceTableView.setVisible(true);
                addStudentsTolist(newVal,TeacherCourses);
            }
            else
            {
                AttendanceTableView.getItems().clear();
                AttendanceTableView.setVisible(false);
            }
        });

        fullAttendancePane.setTop(ChiocePane);
        fullAttendancePane.setCenter(AttendanceTableView);
        return fullAttendancePane;
    }

    public static void createAttendanceTableView() {
        AttendanceTableView = new TableView<>();
        AttendanceTableView.setPrefHeight(300);

        // Create the columns
        TableColumn<Student, String> nameColumn = new TableColumn<>("Student Name");
        TableColumn<Student, Integer> removalColumn = new TableColumn<>("Edit");
        TableColumn<Student, Integer> totalAttendanceColumn = new TableColumn<>("Total Attendance");

        // Set the property value factories for each column
        nameColumn.setCellValueFactory(data -> data.getValue().getName());
        removalColumn.setCellValueFactory(data -> data.getValue().getAttendance().asObject());
        totalAttendanceColumn.setCellValueFactory(data -> data.getValue().getAttendance().asObject());


        AttendanceTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Create a custom cell factory for the removalColumn
        removalColumn.setCellFactory(column -> new TableCell<>() {
            private final Button addButton = new Button("+");
            private final Button subtractButton = new Button("-");
            private final Button confirmButton = new Button("Confirm");


            {
                addButton.setOnAction(e -> {
                    Student student = getTableRow().getItem();
                    if (student != null) {
                        student.increaseAttendance();
                        AttendanceTableView.refresh();
                    }
                });
                subtractButton.setOnAction(e -> {
                    Student student = getTableRow().getItem();
                    if (student != null) {
                        student.decreaseAttendance();
                        AttendanceTableView.refresh();
                    }
                });
                confirmButton.setOnAction(e -> {
                    Student student = getTableRow().getItem();
                    if (student != null) {
                        AttendanceConfirmEditing(student);
                      System.out.println("confirmed");
                    }
                });
            }

            @Override
            protected void updateItem(Integer removal, boolean empty) {
                super.updateItem(removal, empty);
                if (empty || removal == null) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(5);
                    buttonBox.setAlignment(Pos.CENTER);
                    buttonBox.getChildren().addAll(addButton, subtractButton,confirmButton);
                    setGraphic(buttonBox);
                }
            }
        });

        // Add the columns to the table view
        AttendanceTableView.getColumns().addAll(nameColumn, removalColumn, totalAttendanceColumn);

    }
    // creat function to show students that are
    public static void addStudentsTolist(String Course,JSONArray TeacherCourses)
    {
        for (int i=0;i<TeacherCourses.length();i++)
        {
            JSONObject teacherCourse=TeacherCourses.getJSONObject(i);
            if (teacherCourse.getString("Name").equals(Course)) {
                JSONArray teacherCourseStudents = teacherCourse.getJSONArray("Students");
                for (int j = 0; j < teacherCourseStudents.length(); j++) {
                    JSONObject Student = teacherCourseStudents.getJSONObject(j);
                    // Add data to the table view
                    AttendanceTableView.getItems().addAll(
                            new Student(Student.getString("Name"), Student.getInt("Attendebne_num"),i,j));
                }
                break;
            }
        }


    }

    public static void AttendanceConfirmEditing(Student student)
    {

        int stdIndex=student.getcourseIndex();
        int courseIndex=student.getcstudentIndex();

          // get student of course
        JSONArray Courses=teacher.getJSONArray("Courses");
        JSONObject StudentsCourse= Courses.getJSONObject(courseIndex);
        JSONArray StudentssOfCourse=StudentsCourse.getJSONArray("Students");
        JSONObject Student=StudentssOfCourse.getJSONObject(stdIndex);


        // modify data  student of course
        Student.put("Attendebne_num",student.getAttendance().getValue());
        StudentssOfCourse.put(stdIndex,Student);
        StudentsCourse.put("Students",StudentssOfCourse);
        Courses.put(courseIndex,StudentsCourse);
        teacher.put("Courses",Courses);


        AttendanceConfirmEditingJson();
    }

    // function to get user and add data
    public static void AttendanceConfirmEditingJson()  {

        JSONArray Teachers=readFile("Teacher.Json");
        for (int i=0;i<Teachers.length();i++)
        {
            JSONObject teacherneed=  Teachers.getJSONObject(i);
            if (teacherneed.getString("Username").equals(teacher.getString("Username")))
            {
                Teachers.put(i,teacher);
                break;
            }
        }
       try {
           FileWriter file = new FileWriter("jsonFiles/Teacher.Json");
           file.write(Teachers.toString());
           file.flush();
           file.close();
       }
       catch (IOException e)
       {
           System.out.println("eerror");
       }

    }


    // Student class with the required properties

    public static class Student {
            private StringProperty name;
            private IntegerProperty attendance;
            private int courseIndex;
           private int studentIndex;




            public Student(String name, int attendance,int courseIndex,int studentIndex) {
                this.name = new SimpleStringProperty(name);
                this.attendance =  new SimpleIntegerProperty(attendance);
                this.courseIndex=courseIndex;
                this.studentIndex=studentIndex;
            }

            public   StringProperty  getName() {
                return name;
            }
            public   IntegerProperty getAttendance() {
            return attendance;
        }
           public   int getcourseIndex() {
            return courseIndex;
        }
           public   int  getcstudentIndex() {
            return studentIndex;
        }

            public  void increaseAttendance() {
                attendance.set(attendance.get()+1);
                System.out.println(attendance);
            }

           public  void decreaseAttendance() {

               attendance.set(attendance.get()-1);
            }

        }



    }

