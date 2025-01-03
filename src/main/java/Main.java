import Laevis.Window;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.mysql.cj.jdbc.Driver;

import java.sql.*;

public class Main extends Application {
    static  boolean Loginisdone=false;

    public static void main(String[] args) {

        launch(args);
        Window window = Window.get();
        window.run();
    }








    @Override
    public void start(Stage stage) throws ClassNotFoundException {
        Button B1 = new Button("Login") ;
        Button B2 = new Button("Register");
        Button B3= new Button("skip for now");
        Label L1 = new Label("Username");
        Label L2 = new Label("Password");
        Label Msg = new Label();
        TextField TF1 = new TextField();
        TextField TF2 = new TextField();

        B1.setOnAction(e -> {
            try{

                //problem with this line is that it depends on your database's root and pass and the name of your db
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javapro?user=root&password=waleed");
                String username = TF1.getText();
                String password = TF2.getText();
                String level="NULL";
                if (!username.isEmpty() && !password.isEmpty()) {
                    String query = "SELECT * FROM Info WHERE username = ? AND password = ?";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        Msg.setText("Login Sucessful");
                        Thread.sleep(600);
                        stage.close();
                        System.out.println("JavaFX is closing and imgui should start now");
                    } else {
                        /*
                        String newName = "INSERT INTO Info VALUES ('"+username+"','"+password+"','"+level+"')";
                        Statement stmt = con.createStatement();
                        System.out.print(newName);
                        stmt.executeUpdate(newName);
                        stmt.close();
                        Msg.setText("Register Sucessful");
                        Thread.sleep(600);
                        stage.close();
                        System.out.println("JavaFX is closing and imgui should start now");*/
                        Msg.setText("This account doesn't exist \n You should register instead");
                    }
                    rs.close();
                    pstmt.close();
                }
                else if(username.isEmpty() && !password.isEmpty())
                {
                    Msg.setText("Please Enter The Username");
                }
                else if(password.isEmpty() && !username.isEmpty())
                {
                    Msg.setText("Please Enter The Password");
                }
                else {
                    Msg.setText("Please Enter Both Username And Password");
                }
                con.close();
            } catch (Exception f){
                f.printStackTrace();
                Msg.setText("Error Occurred: " + f.getMessage());}
        });
        B2.setOnAction(e -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javapro?user=root&password=waleed");
                String username = TF1.getText();
                String password = TF2.getText();
                String level="NULL";
                if (!username.isEmpty() && !password.isEmpty()) {
                    String query = "SELECT * FROM Info WHERE username = ?";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        Msg.setText("The Username Used Before");
                    } else {
                        String newName = "INSERT INTO Info VALUES ('"+username+"','"+password+"','"+level+"')";
                        Statement stmt = con.createStatement();
                        System.out.print(newName);
                        stmt.executeUpdate(newName);
                        stmt.close();
                        Msg.setText("Register Sucessful");
                        stage.close();
                        System.out.println("JavaFX is closing and imgui should start now");

                    }
                    rs.close();
                    pstmt.close();
                }
                else if(username.isEmpty() && !password.isEmpty())
                {
                    Msg.setText("Please Enter The Username");
                }
                else if(password.isEmpty() && !username.isEmpty())
                {
                    Msg.setText("Please Enter The Password");
                }
                else {
                    Msg.setText("Please Enter Both Username And Password.");
                }
                con.close();
            } catch (Exception f){
                f.printStackTrace();
                Msg.setText("Error Occurred : " + f.getMessage());}
        });
        B3.setOnAction(event -> stage.close());
        VBox GP1 = new VBox(10,L1,TF1,L2,TF2,B1,B2,B3,Msg);
        Scene scene = new Scene(GP1, 500, 400);
        stage.setTitle("login form");
        stage.setScene(scene);
        stage.show();

    }

}

