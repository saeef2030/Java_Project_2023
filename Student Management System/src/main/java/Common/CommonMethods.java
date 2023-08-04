package Common;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CommonMethods {


    public static JSONObject validateCredentials(String username, String password, String type,Boolean remove) throws IOException {

        //Read students data in  string
        JSONObject user=null;
        String path = filePath(type) ;

        if(path != ""){

            JSONArray  users = readFile(path);
            //iterates over the students data and compare it with the values the user entered
            for (int i = 0; i < users.length(); i++) {

                JSONObject stdObj = (JSONObject) users.get(i);
                String userName = stdObj.getString("Username");
                String pass = stdObj.getString("password");

                if (username.equals(userName) && password.equals(pass)) {
                    user=stdObj;
                    if(remove)
                    {
                        removeJsonObjectFromFile(users,i,path);
                    }
                    break;  // exit the loop since a match is found
                }
            }
        }
        return user;
    }
     public static void removeJsonObjectFromFile(JSONArray users,int index,String path)
     {
         users.remove(index);
         try {
             FileWriter file=new FileWriter("jsonFiles/"+path);
             file.write(users.toString());
             file.close();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
    public static String filePath(String type)
    {
        String path="";
       if (type.equals("Student"))
           path= "Student.json";
       else if (type.equals("Teacher")) {
           path="Teacher.json";
       } else if (type.equals("Administrators")){
           path= "Administrators.json";
       }
        return path;
    }

    public static JSONArray readFile(String  path)
    {
        //Read the file
        JSONTokener file = null;
        try {
            file = new JSONTokener(new FileReader("jsonFiles/"+path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONArray users = new JSONArray(file);
        return users;
    }

    public static String printInGoodWay(JSONObject jsonObject )
    {
        String printing;
        String time = jsonObject.getString("time");
        String Content = jsonObject.getString("message");
        String sender =jsonObject.getString("sender");
        String reciver=jsonObject.getString("receiver");
        String type=jsonObject.getString("type");


        printing= "Sender: "+sender+"  reciver: "+reciver+" Content:"+Content+" time:"+time+"\n";
        return printing;
    }


    public static void switchingScenes(Stage stage, Scene scene) {
        stage.setScene(scene);
    }
}