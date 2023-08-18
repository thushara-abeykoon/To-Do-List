package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RegisterFormController {
    public AnchorPane root;
    public PasswordField txtPasswd;
    public PasswordField txtPasswdConfirm;
    public Label txtPasswordNotMatchStatus;
    public Label txtPasswordMatchStatus;
    public TextField txtEmail;
    public TextField txtUsername;

    public void btnLoginOnAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void btnRegisterOnAction(ActionEvent event) {
        if (!txtPasswd.getText().equals(txtPasswdConfirm.getText())){
            txtPasswordNotMatchStatus.setVisible(true);
            txtPasswordMatchStatus.setVisible(false);

            setBorderColor("red");

        } else if (txtPasswd.getText().equals(txtPasswdConfirm.getText())&&(txtPasswd.getText().length()>0||txtPasswdConfirm.getText().length()>0)) {
            txtPasswordMatchStatus.setVisible(true);
            txtPasswordNotMatchStatus.setVisible(false);

            setBorderColor("transparent");

            addNewUser();
        } else{
            txtPasswordNotMatchStatus.setVisible(false);
            txtPasswordMatchStatus.setVisible(false);
            setBorderColor("transparent");
        }
    }

    public void setBorderColor(String color){
        txtPasswd.setStyle("-fx-border-color: "+color);
        txtPasswdConfirm.setStyle("-fx-border-color: "+color);
    }

    private void addNewUser(){
        String userId = getUserId();
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPasswdConfirm.getText();

        Connection connection = DBConnection.getDbConnection().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values (?,?,?,?)");
            preparedStatement.setObject(1,userId);
            preparedStatement.setObject(2,username);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,password);

            int i = preparedStatement.executeUpdate();
            if (i>0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Success....!");
                alert.showAndWait();

                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));

                Scene scene = new Scene(parent);
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Login");
                stage.centerOnScreen();
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getUserId(){

        Connection connection = DBConnection.getDbConnection().getConnection();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");

            boolean isUserExist = resultSet.next();

            if (isUserExist){
                String temp = resultSet.getString(1);
                String userNumber = Integer.toString(Integer.parseInt(temp.substring(1))+1);
                if(temp.length()-userNumber.length()<1){
                    return temp.charAt(0) + userNumber;
                }
                else {
                    return temp.substring(0,temp.length()-userNumber.length())+userNumber;
                }
            }
            else {
                return "U0001";
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
