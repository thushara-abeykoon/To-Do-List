package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginFormController {

    public AnchorPane root;
    public TextField txtLoginUsername;
    public PasswordField txtLoginPassword;

    public static String loginUserName;
    public static String loginUserId;

    public void btnCreateAccOnAction(ActionEvent event) throws IOException  {
        loadStage("../view/RegisterForm.fxml","Register");
    }

    public void btnLoginOnAction(ActionEvent event) {

        String username = txtLoginUsername.getText();
        String password = txtLoginPassword.getText();

        Connection connection = DBConnection.getDbConnection().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select name,password,id from user where name = '" + username + "';");

            if(!resultSet.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR,"User Not Found");
                alert.showAndWait();
                txtLoginUsername.clear();
                txtLoginPassword.clear();
                txtLoginUsername.requestFocus();
            }
            else{
                if (password.equals(resultSet.getString(2))){
                    loginUserName = resultSet.getString(1);
                    loginUserId = resultSet.getString(3);


                    loadStage("../view/ToDoListForm.fxml","ToDo");
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Wrong Password!");
                    alert.showAndWait();
                    txtLoginUsername.clear();
                    txtLoginPassword.clear();
                    txtLoginUsername.requestFocus();
                }
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"SqlExeption");
            alert.showAndWait();
//            throw new RuntimeException(e);
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR,"IOExeption");
            System.out.println(e);
            alert.showAndWait();
//            throw new RuntimeException(e);
        }
    }

    public void loadStage(String fxml, String title) throws  IOException {

        Parent parent = FXMLLoader.load(this.getClass().getResource(fxml));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
