package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Customer;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCustomer {
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

    public void updateOnAction(ActionEvent actionEvent) {
        Customer customer = new Customer(
                customerId.getText(),
                customerName.getText(),
                customerAddress.getText(),
                customerSalary.getText()
        );

        try{
            boolean isUpdated = SQLUtil.executeUpdate("UPDATE Customer SET name=? , address=? , salary=? WHERE id=?",customer.getName(),customer.getAddress(),customer.getSalary(),customer.getId());
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }
    }
}
