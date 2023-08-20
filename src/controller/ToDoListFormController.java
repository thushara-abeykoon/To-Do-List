package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.ToDoTM;

import java.io.IOException;
import java.sql.*;

public class ToDoListFormController {
    public Label txtUsername;
    public Label txtUserid;
    public AnchorPane root;
    public ListView<ToDoTM> lstTodo;
    public TextField txtAreaUpdateOrDelete;
    public Button btnDelete;
    public Button btnUpdate;
    public TextField txtAreaAddNew;
    public String selectedId;


    Connection connection = DBConnection.getDbConnection().getConnection();

    public void initialize(){
        txtUserid.setText(LoginFormController.loginUserId);
        txtUsername.setText(LoginFormController.loginUserName);
        loadListData();
        txtAreaAddNew.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    lstClearSelection();
                    setDisableCommon(true);
                    txtAreaUpdateOrDelete.clear();
                }
                else{
                    lstTodo.getSelectionModel().selectAll();
                }
            }
        });

        lstSelectItem();
    }

    public void lstClearSelection(){
        lstTodo.getSelectionModel().clearSelection();
    }

    public void lstSelectItem(){
        lstTodo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTM>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTM> observable, ToDoTM oldValue, ToDoTM newValue) {
                setDisableCommon(false);
                if (newValue==null)
                    return;
                txtAreaUpdateOrDelete.setText(newValue.getDescription());
                selectedId = newValue.getId();
                txtAreaUpdateOrDelete.requestFocus();
            }
        });
    }

    public void setDisableCommon(boolean isDisable){
        txtAreaUpdateOrDelete.setDisable(isDisable);
        btnDelete.setDisable(isDisable);
        btnUpdate.setDisable(isDisable);
    }

    private void loadListData() {

        ObservableList<ToDoTM> items = lstTodo.getItems();
        items.clear();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where user_id = ?");
            preparedStatement.setObject(1,txtUserid.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;
            while(resultSet.next()){
                ToDoTM toDoTM = new ToDoTM(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
                items.add(toDoTM);
            }
            lstTodo.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
        if(todo.length()==0)
            return;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");
            preparedStatement.setObject(1,getTodoId());
            preparedStatement.setObject(2, todo);
            preparedStatement.setObject(3,txtUserid.getText());

            int i = preparedStatement.executeUpdate();

            loadListData();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (StringIndexOutOfBoundsException e ){
            Alert alert = new Alert(Alert.AlertType.ERROR,"List Full!!");
            alert.showAndWait();
        }

    }

    private  String getTodoId(){
        int intId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo where user_id = '"+txtUserid.getText()+"' order by id desc limit 1");
            if(resultSet.next()){
                String stringID = resultSet.getString(1);
                intId = Integer.parseInt(stringID.substring(1))+1;

            }else{
                intId = 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String stringId = Integer.toString(intId);
        String originalId = "T";
        if (stringId.length()>4)
            throw new StringIndexOutOfBoundsException();
        for (int i = 0; i < 4 - stringId.length(); i++) originalId += "0";

        return originalId + stringId;
    }

//    public void btnAddNewOnAction (ActionEvent event) {
//        System.out.println("Hi");
//    }

    public void btnAddNewOnAction(ActionEvent event) {
        addNewToDo(txtAreaAddNew.getText());
        txtAreaAddNew.clear();
    }

    public void btnUpdateOnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");
            preparedStatement.setObject(1,txtAreaUpdateOrDelete.getText());
            preparedStatement.setObject(2,selectedId);

            preparedStatement.executeUpdate();
            txtAreaUpdateOrDelete.clear();
            loadListData();
            setDisableCommon(true);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnDeleteOnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id = ?");
            preparedStatement.setObject(1,selectedId);

            preparedStatement.executeUpdate();

            txtAreaUpdateOrDelete.clear();
            loadListData();
            setDisableCommon(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
