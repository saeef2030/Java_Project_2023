package Fake.data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class FakeDataCreation {

    // Arrays to store first names and last names
    private static String[] firstNames = {"Mohammed", "Ahmed", "Badr", "Elsadiq", "Wagdi", "Khaled", "Nour", "Somia", "Hoda", "Eman", "Rahma", "Reem", "Sara", "Amera", "Abdulrhman"};
    private static String[] lastNames = {"Nader", "Samir", "Abdullah", "Amir", "Yasser", "Zaid", "Mohsen", "Elsadiq", "Ali"};
    private static String [] levels={"prep","1st","2nd","3rd","4th"};
    private static String[] coursesTaked = {"Java", "Math", "DSP", "DSA"};


    // Lists to store IDs and passwords
    private static List<String> ids = new ArrayList<>();
    private static List<String> passwords = new ArrayList<>();

    // Initialize the lists with initial IDs and passwords
    public static void initializeDataLists()
    {
        generatePasswords ();
        generateIds();
    }
    public static void generatePasswords () {
        String baseString="Pass";
        int count=200;
        for (int i = 0; i < count; i++) {
            passwords.add(baseString + i); // Modify this line as per your requirements
        }
    }

    public static void generateIds() {
        String baseString="20812020565";
        int count=200;
        for (int i = 0; i < count; i++) {
            ids.add(baseString + i); // Modify this line as per your requirements
        }

    }

    // Get a random ID from the list and remove it to ensure uniqueness
    private static String getRandomId() {
        int index = (int) (Math.random() * ids.size());
        return ids.remove(index);
    }

    // Get a random password from the list and remove it to ensure uniqueness
    private static String getRandomPassword() {
        int index = (int) (Math.random() * passwords.size());
        return passwords.remove(index);
    }




        public static JSONObject createCourse() {
            JSONObject course = new JSONObject();
            int randomIndex = ThreadLocalRandom.current().nextInt(coursesTaked.length);
            course.put("Name", coursesTaked[randomIndex]);
            course.put("Path", "Path");
            return course;
        }

        public static JSONArray creatListOfCourses(int count) {
            JSONArray listOfCourses = new JSONArray();
            for (int i = 0; i < count; i++) {
                JSONObject course = createCourse();
                listOfCourses.put(course);
            }
            return listOfCourses;
        }








    // Create a JSON object for a student with random first name, last name, ID, and password
    public static JSONObject createStudentJsonObject(String userType) {

        JSONObject userJsonObject = new JSONObject();
        String userName= firstNames[(int) (Math.random() * (firstNames.length - 1))]+" "+
                lastNames[(int) (Math.random() * (lastNames.length - 1))];
        userJsonObject.put("Username",userName);
        userJsonObject.put("userType",userType);
        if (userType== "StudentTeacher")  // student only
        userJsonObject.put("level", levels[(int) (Math.random() * (levels.length - 1))]);
        if (userType!="Administrators")   // student only student and teacher
            userJsonObject.put("Courses", creatListOfCourses(2));
        userJsonObject.put("ID", getRandomId());

        userJsonObject.put("password", getRandomPassword());
        return userJsonObject;
    }

     //Generate an array of student JSON objects with a given count
    public static JSONArray generateStudentData(int count,String userType) {
        JSONArray studentsArray = new JSONArray();
        for (int i = 0; i < count; i++) {
            studentsArray.put(createStudentJsonObject(userType));
        }
        return studentsArray;
    }


    // append new users to old ones
    public static JSONArray readJsonFileandAppend(JSONArray newjsonArray,String filePath ) throws FileNotFoundException {
        JSONTokener pastJsonObject=new JSONTokener(new FileReader(filePath));
        JSONArray usertobeReaded =new JSONArray(pastJsonObject);

        for (int i=0;i<newjsonArray.length();i++)
        {
            usertobeReaded.put(newjsonArray.get(i));

        }

       return newjsonArray;
    }



    //Write the JSON array to a file
    public static void writeJsonFile(JSONArray newjsonArray, String filePath,Boolean append )  {
        if (append) {
            try {
                newjsonArray = readJsonFileandAppend(newjsonArray, filePath);
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Creating a new one.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(filePath);
                    fileWriter.write(newjsonArray.toString());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } else {
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(newjsonArray.toString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the ID and password lists
        initializeDataLists();

        try {
            // Generate student JSON data and write it to a file
            JSONArray studentsData = generateStudentData(2, "StudentTeacher");
            writeJsonFile(studentsData, "Student.json",false);
           // Generate teacher JSON data and write it to a file
            JSONArray teacherData = generateStudentData(2,"Teacher");
            writeJsonFile(teacherData, "Teacher.json",false);

            // Generate admin JSON data and write it to a file
            JSONArray adminData = generateStudentData(2,"Administrators");
            writeJsonFile(adminData, "Administrators.json",false);
            System.out.println("55a");

            JSONTokener pastJsonObject=new JSONTokener(new FileReader("Administrators.json"));
            JSONArray usertobeReaded =new JSONArray(pastJsonObject);

            System.out.println(usertobeReaded.get(2));


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}




