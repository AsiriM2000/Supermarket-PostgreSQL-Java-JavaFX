package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import util.SQLUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaveCustomer {
    public TextField customerId;
    public TextField customerName;
    public TextField customerAddress;
    public TextField customerSalary;

    public void initialize() throws SQLException, ClassNotFoundException {
        getId();
        customerId.setEditable(false);
    }
    public void saveOnAction(ActionEvent actionEvent) {
        Customer customer = new Customer(
                customerId.getText(),
                customerName.getText(),
                customerAddress.getText(),
                customerSalary.getText()
        );
        try {
            if (SQLUtil.executeUpdate("INSERT INTO Customer VALUES (?,?,?,?)",customer.getId(),customer.getName(),customer.getAddress(),customer.getSalary())){
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
                clear();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    public void  getId() throws SQLException, ClassNotFoundException {
        ResultSet pet = SQLUtil.executeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        if (pet.next()){
            String r = pet.getString("id");
            int co = r.length();
            String txt = r.substring(0,3);
            String num = r.substring(3,co);
            int n = Integer.parseInt(num);
            n++;
            String snum = Integer.toString(n);
            String fxt = txt + snum;
            customerId.setText(fxt);
        }else{
            customerId.setText("C001");
        }
    }
    public void clear() throws SQLException, ClassNotFoundException {
        customerId.clear();
        customerName.clear();
        customerAddress.clear();
        customerSalary.clear();
    }
    private void setUi(String URI) throws IOException {
        Parent parent  = FXMLLoader.load(getClass().getResource("../views/"+URI+".fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle(URI);
        stage.show();
    }
}
