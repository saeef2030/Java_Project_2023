package StudentTeacher;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import static Common.CommonLayout.creatAlert;
import static Common.CommonLayout.createVBoxStyle;
import static Common.CommonMethods.readFile;

public class Courses {
    private static JSONArray userCourses;
    private static TableView<Course> subjectTable;
    private static VBox coursesVbox = new VBox();
    private static JSONObject User;
    private static ArrayList<String> coursesName=new ArrayList<>();

    public static BorderPane CoursesVbox(JSONObject user) {
        User=user;
        createVBoxStyle();
        coursesVbox=new VBox();
        coursesVbox.setStyle("-fx-background-color:white;");

        //createButton to add and delete pdfs
        Button addPdf=new Button("Add");
        Button delPdf=new Button("delete");



        HBox buttonhbox = new HBox();
        buttonhbox.setSpacing(10);
        buttonhbox.setAlignment(Pos.TOP_RIGHT);
        buttonhbox.getChildren().addAll(addPdf, delPdf); // Add the buttons instead of the VBox itself

        BorderPane CoursesPane = new BorderPane();
        CoursesPane.setBottom(buttonhbox);

        createTable(false,"");
        // Add the table to the VBox
        coursesVbox.getChildren().addAll(subjectTable, buttonhbox); // Add both the table and buttonVbox

        //make add delet visble to teacher only
        if (Objects.equals(User.getString("userType"), "Teacher"))
        {
            buttonhbox.setVisible(true);
        }
        else
        {
            buttonhbox.setVisible(false);
        }

        addPdf.setOnAction(e->
        {
            openPopupWindow(false);
            System.out.println("ADD");

        });

        delPdf.setOnAction(e->
        {
            openPopupWindow(true);
            System.out.println("del");

        });

        CoursesPane.setTop(coursesVbox);
        return CoursesPane;

    }



    public static void createTable(Boolean Pds,String CoursesName) {
        // Create table for courses
        subjectTable = new TableView<>();
        subjectTable.setPrefHeight(120);

        // Create table columns for courses
        TableColumn<Course, String> courseNameColumn = new TableColumn<>("Name");
        TableColumn<Course, String> choiceColumn = new TableColumn<>("Choice");

        // Add columns to the table
        subjectTable.getColumns().addAll(courseNameColumn, choiceColumn);

        // to delete the null column in the last of table
        subjectTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Set value for columns
        courseNameColumn.setCellValueFactory(data -> data.getValue().getCourseName());
        choiceColumn.setCellValueFactory(data -> data.getValue().getCourseLink());

        if (!Pds) {
            // Set button cell factory for choiceColumn
            choiceColumn.setCellFactory(column -> new openPdfsButton());

            // Take courses from user
            userCourses = User.getJSONArray("Courses");

            // Add courses in Arraylist
            for (int i = 0; i < userCourses.length(); i++) {
                JSONObject userSingleCourse = userCourses.getJSONObject(i);
                String courseName=userSingleCourse.getString("Name");
                if (!coursesName.contains(courseName))
                       coursesName.add(userSingleCourse.getString("Name"));
                if (!subjectTable.getItems().contains(userSingleCourse))
                     subjectTable.getItems().add(new Course(userSingleCourse));
            }
        } else {
            // Set button cell factory for choiceColumn
            choiceColumn.setCellFactory(column -> new OpenLinkButton());
        }

        // Refresh the table view
        subjectTable.refresh();

        if (Pds) {
            findCourse(CoursesName);
            coursesVbox.getChildren().clear();
            coursesVbox.getChildren().add(subjectTable);
        }
    }



    public static void findCourse( String courseName) {
      JSONArray coursesData=readFile("Courses.json");
      for (int i=0;i<coursesData.length();i++)
      {
          JSONObject courseData=coursesData.getJSONObject(i);

              if ( courseData.getString("Name").equals(courseName))
              {
                  JSONArray Data=courseData.getJSONArray("lecs");
                  subjectTable.getItems().clear();
                  for (int j=0;j<Data.length();j++)
                  {
                      JSONObject DataofsingleCourse=Data.getJSONObject(j);
                      subjectTable.getItems().add(new Course(DataofsingleCourse));
                  }
                  break;
              }


      }
    }

    private static void openPopupWindow(Boolean remove) {
        Stage popupStage = new Stage();

        GridPane popupRoot = new GridPane();
        popupRoot.setPadding(new Insets(10));
        popupRoot.setVgap(10);
        popupRoot.setHgap(10);



        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label urlLabel = new Label("URL:");
        TextField urlTextField = new TextField();
        Label ChooseCourseNamelabel = new Label("Choose Course:");
        ChoiceBox<String> CourseChoosen=new ChoiceBox<>();
        CourseChoosen.getItems().addAll(coursesName);
        Button saveButton;
        if (remove) {
            saveButton = new Button("Delete");
            popupStage.setTitle("Delete lecture /Assignment");
        } else {
            saveButton = new Button("Add");
            popupRoot.addRow(2, urlLabel, urlTextField);
            popupStage.setTitle("Add lecture /Assignment");
        }
        saveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String coursesChoosen=CourseChoosen.getValue();
            // read the file to add to it

            if (CourseChoosen.getValue()!=null) {
                if (remove) {
                    JSONArray coursesTormoveFromIt=readFile("Courses.Json");
                    int flag=0;
                    for(int i=0;i<coursesTormoveFromIt.length();i++)
                    {
                        JSONObject courseNum=coursesTormoveFromIt.getJSONObject(i);
                        // search about subject
                        if (courseNum.getString("Name").equals(coursesChoosen)&&courseNum.getString("Teacher").equals(User.getString("Username"))) {

                            JSONArray lecs = courseNum.getJSONArray("lecs");
                                  // Search for the lecture
                            for (int j = 0; j < lecs.length(); j++) {
                                JSONObject lecNum = lecs.getJSONObject(j);
                                if (lecNum.getString("Name").equals(name)) {
                                    lecs.remove(j);
                                    courseNum.put("lecs", lecs);
                                    coursesTormoveFromIt.put(i, courseNum);
                                    // Update the course in the coursesTormoveFromIt array
                                    // Write to the file
                                    writeToCourses(coursesTormoveFromIt);
                                    flag = 1;
                                    makeJsonObjeccheck( User.getString("Username"),coursesChoosen, name, "reomved");


                                    break;
                                }
                            }
                            if (flag==1) {
                                popupStage.close();
                                break;
                            }
                        }
                    }
                    if (flag==0)
                    {
                        Alert lecnotFounddd=creatAlert(Alert.AlertType.WARNING,"Lecture not founded","enteer a valid Name",null);

                        lecnotFounddd.showAndWait();

                    }

                } else {
                    String url = urlTextField.getText();
                    // creat json object for lecture
                    JSONObject newLec = new JSONObject();
                    newLec.put("Name", name);
                    newLec.put("link", url);

                    // read the file to add to it
                    JSONArray coursesToAddto=readFile("Courses.Json");
                    for(int i=0;i<coursesToAddto.length();i++)
                    {
                        JSONObject courseNum=coursesToAddto.getJSONObject(i);
                        if (courseNum.getString("Name").equals(coursesChoosen)&&courseNum.getString("Teacher").equals(User.getString("Username")))
                        {
                           // read from the file

                            JSONArray lecs=courseNum.getJSONArray("lecs");
                            lecs.put(newLec);
                            courseNum.put("lecs",lecs);
                            coursesToAddto.put(i,courseNum);

                            // Write to  the file
                            writeToCourses(coursesToAddto);
                            makeJsonObjeccheck( User.getString("Username"),coursesChoosen, name, "added");
                            break;
                        }
                    }


                    popupStage.close();
                }

            }
            else
            {
                Alert noCoursesChoosen=creatAlert(Alert.AlertType.WARNING,"No Courses Choosen","Please Choose a Course",null);
                noCoursesChoosen.showAndWait();
            }
        });
        popupRoot.addRow(0, ChooseCourseNamelabel, CourseChoosen);
        popupRoot.addRow(1, nameLabel, nameTextField);
        popupRoot.add(saveButton, 1, 3);

        Scene popupScene = new Scene(popupRoot, 300, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public static void writeToCourses(JSONArray coursesToAddorremove)
    {
        // Write to  the file
        try {
            FileWriter coursesFile=new FileWriter("jsonFiles/Courses.Json");
            coursesFile.write(coursesToAddorremove.toString());
            coursesFile.flush();
            coursesFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void  makeJsonObjeccheck(String name,String coursesChoosen,String content,String add)
    {
        JSONObject check=new JSONObject();
        check.put("name",name);
        check.put("subject",coursesChoosen);
        check.put("time","12AM");
        check.put("Content",content);
        check.put("add",add);

        JSONArray checkSubmission=readFile("checkSubmission.json");
        checkSubmission.put(check);

        // Write to  the file
        try {
            FileWriter coursesFile=new FileWriter("jsonFiles/checkSubmission.Json");
            coursesFile.write(checkSubmission.toString());
            coursesFile.flush();
            coursesFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}


class Course
{
    private StringProperty CourseName;
    private StringProperty link;
    Course(JSONObject course)
    {
        CourseName=new SimpleStringProperty(course.getString("Name"));

        // this when pass to hin lecture which have name and link
        try {
            link=new SimpleStringProperty(course.getString("Name")+"559"+course.getString("link"));
        }
        catch (Exception e)
        {

            link=new SimpleStringProperty(course.getString("Name")+"559"+"sdsd");

        }
    }


    public StringProperty getCourseName() {
        return this.CourseName;
    }
    public StringProperty getCourseLink() {
        return this.link;
    }
}

class openPdfsButton extends TableCell<Course, String> {
    private final Button button;
    private String CoursesName;

    public openPdfsButton() {
        button = new Button();
        setGraphic(button);
        button.setOnAction(e -> {
            Courses.createTable(true,CoursesName);
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || item.isEmpty()) {
            setGraphic(null);
        } else {
            String[] splitArray = item.split("559");
            CoursesName = splitArray[0];
            button.setText("Show");
            setGraphic(button);
        }
    }
}

class OpenLinkButton extends TableCell<Course, String> {
    private final Button button;
    private String CoursesName;
    private String link;

    public OpenLinkButton() {
        button = new Button();
        setGraphic(button);
        button.setOnAction(e -> {
            openLink();
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || item.isEmpty()) {
            setGraphic(null);
        } else {
            String[] splitArray = item.split("559");
            CoursesName = splitArray[0];
            button.setText("Show");
            setGraphic(button);

            link = splitArray[1];

        }
    }

    protected void openLink()
    {
        // Create a URI object from the URL string
        URI uri = null;
        try {
            uri = new URI(link);
        } catch (URISyntaxException e1) {
            throw new RuntimeException(e1);
        }

        // Check if the Desktop class is supported on the current platform
        if (Desktop.isDesktopSupported()) {
            // Get the Desktop instance
            Desktop desktop = Desktop.getDesktop();

            // Open the URL in the default browser
            try {
                desktop.browse(uri);
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }

        }

    }
}

