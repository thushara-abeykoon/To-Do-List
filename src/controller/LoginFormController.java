package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    public AnchorPane root;
    public void btnCreateAccOnAction(ActionEvent event) throws IOException  {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/RegisterForm.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Register");
        stage.setScene(scene);


    }
}
