/*package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.Role;
import utils.MyDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class DBUtils {

    // Connection instance
    Connection connection = MyDataSource.getInstance().getConn();

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String email) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

            // If email is provided, send it to the new scene
            if (email != null) {
                LogInController loggedInController = loader.getController();
                loggedInController.setUserInformation(email);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (root == null) {
            System.out.println("Error: Failed to load the FXML file.");
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String nom, String prenom, String email, String password,int num_telephone, LocalDate date_de_naissance, String genre, Role role, String image) {
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            Connection connection = MyDataSource.getInstance().getConn();

            // Check if the user already exists
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            psCheckUserExists.setString(1, email);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This email is already in use.");
                alert.show();
            } else {
                // Insert new user into the database
                psInsert = connection.prepareStatement(
                        "INSERT INTO user (nom, prenom, email, password, num_telephone, date_de_naissance, genre, role, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                psInsert.setString(1, nom);
                psInsert.setString(2, prenom);
                psInsert.setString(3, email);
                psInsert.setString(4, password);
                psInsert.setInt(5, num_telephone);
                psInsert.setObject(6, date_de_naissance);
                psInsert.setString(7, genre);
                psInsert.setString(8, role.toString()); // Assuming Role is an enum
                psInsert.setString(9, image);

                psInsert.executeUpdate();

                // Redirect to logged-in scene
                changeScene(event, "logged-in.fxml", "Welcome!", email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psCheckUserExists != null) psCheckUserExists.close();
                if (psInsert != null) psInsert.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void logInUser(ActionEvent event, String email, String password) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = MyDataSource.getInstance().getConn();
        try {

            //Connection connection = MyDataSource.getInstance().getConn();
            preparedStatement = connection.prepareStatement("SELECT mot_de_passe, role FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {  // Vérifie si l'utilisateur existe
                System.out.println("Utilisateur non trouvé !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Email ou mot de passe incorrect !");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("mot_de_passe");
                    String retrievedRole = resultSet.getString("role");

                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "logged-in.fxml", "Bienvenue !", email);
                    } else {
                        System.out.println("Mot de passe incorrect !");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Email ou mot de passe incorrect !");
                        alert.show();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}



















}

*/

/*package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.Role;
import utils.MyDataSource;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class DBUtils {
    Connection connection = MyDataSource.getInstance().getConn();
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String email) {
        Parent root = null;

        if (email != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LogInController loggedInController = loader.getController();
                loggedInController.setUserInformation(email);
            }catch (IOException e)
            {
                e.printStackTrace();
            }

        }else
        {
            try {
                root = FXMLLoader. load(DBUtils.class.getResource(fxmlFile));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,600,400));
        stage.show();

    }

    public static void changeScene(javafx.event.ActionEvent event, String fxmlFile, String logIn, String email) {
    }



    public static void signUpUser(ActionEvent event, String nom, String prenom , String email, String password, int num_telephone, LocalDate date_de_naissance , String genre , Role role , String image) {

        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;


        try {

            Connection connection = MyDataSource.getInstance().getConn();
        psCheckUserExists = connection.prepareStatement (  "SELECT * FROM user WHERE email = ?");

        psCheckUserExists.setString( 1, email);
        resultSet = psCheckUserExists.executeQuery();

        if (resultSet.isBeforeFirst()) {
            System.out.println("User already exists!");
            Alert alert = new Alert(Alert. AlertType. ERROR);
            alert.setContentText("You cannot use this username.");
            alert.show();
        } else {
            psInsert = connection.prepareStatement ( "INSERT INTO user (nom, prenom, email,password ,num_telephone,  date_de_naissance , genre , role ,image VALUES (?,?,?,?,?,?,?,?,?)");
            psInsert.setString(   1, nom);
            psInsert.setString(  2, prenom);
            psInsert.setString(  3, email);

            psInsert.executeUpdate ();

            changeScene(event,"logged-in.fxml","Welcome!",nom, prenom,email,password, num_telephone,  date_de_naissance , genre , role ,image);

 }

    }
}

*/
