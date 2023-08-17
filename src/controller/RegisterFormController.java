package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterFormController {
    public AnchorPane root;
    public PasswordField txtPasswd;
    public PasswordField txtPasswdConfirm;
    public Label txtPasswordNotMatchStatus;
    public Label txtPasswordMatchStatus;

    public void btnLoginOnAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle("Login");
        stage.setScene(scene);
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
        } else{
            txtPasswordNotMatchStatus.setVisible(false);
            txtPasswordMatchStatus.setVisible(false);

            setBorderColor("transparent");
        }
    }


    public void setBorderColor(String color){
        txtPasswd.setStyle("-fx-background-color: 10px; -fx-border-radius: 10px; -fx-border-color: "+color);
        txtPasswdConfirm.setStyle("-fx-background-color: 10px; -fx-border-radius: 10px; -fx-border-color: "+color);
    }
}
