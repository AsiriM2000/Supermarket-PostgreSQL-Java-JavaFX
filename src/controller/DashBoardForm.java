package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardForm {
    public void saveCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("saveCustomer");
    }

    public void searchCustomerOnAction(ActionEvent actionEvent) {
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) {
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) {
    }

    public void loadAllCustomersOnAction(ActionEvent actionEvent) {
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
    }

    private void setUi(String URI) throws IOException {
        Parent parent  = FXMLLoader.load(getClass().getResource("../views/"+URI+".fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle(URI);
        stage.show();
    }
}
