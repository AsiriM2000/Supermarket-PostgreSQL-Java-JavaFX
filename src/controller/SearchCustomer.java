package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchCustomer {
    public TextField customerId;
    public TextField customerName;
    public TextField customerAddress;
    public TextField customerSalary;

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result =  SQLUtil.executeQuery("SELECT * FROM Customer WHERE id=?",customerId.getText());
            if (result.next()) {
                customerName.setText(result.getString(2));
                customerAddress.setText(result.getString(3));
                customerSalary.setText(result.getString(4));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void searchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result =  SQLUtil.executeQuery("SELECT * FROM Customer WHERE id=?",customerId.getText());
            if (result.next()) {
                customerName.setText(result.getString(2));
                customerAddress.setText(result.getString(3));
                customerSalary.setText(result.getString(4));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
