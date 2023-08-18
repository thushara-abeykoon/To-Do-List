package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ToDoListFormController {
    public Label txtUsername;
    public Label txtUserid;
    public AnchorPane root;

    private static int counter;
    public TextField txtAreaAddNew;

    public void initialize(){
        txtUserid.setText(LoginFormController.loginUserId);
        txtUsername.setText(LoginFormController.loginUserName);
    }
    public void btnLogOutOnAction(ActionEvent event) throws IOException {
        loadStage("../view/LoginForm.fxml","Login");
    }

    public void loadStage(String fxml, String title) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource(fxml));
        Scene scene = new Scene(parent);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    private void addNewToDo(String todo){
        Connection connection = DBConnection.getDbConnection().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");
            preparedStatement.setObject(1,counter++);
            preparedStatement.setObject(2, todo);
            preparedStatement.setObject(3,txtUserid);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnAddNewOnAction(ActionEvent event) {
        addNewToDo(txtAreaAddNew.getText());
    }

    public void btnUpdateOnAction(ActionEvent event) {
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }
}
