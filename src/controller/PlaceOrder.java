package controller;

import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Customer;
import model.Item;
import model.Disposal;
import model.OrderDetails;
import util.SQLUtil;
import views.tm.CartTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class PlaceOrder {
    public Label lblDate;
    public Label lblTime;
    public ComboBox<String> cmbCustomerId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public ComboBox<String> cmbItemCode;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public TableView tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotalCost;
    public TableColumn colButton;
    public Label lblTotalCost;

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colButton.setCellValueFactory(new PropertyValueFactory<>("btn"));


        loadDateAndTime();
        setCustomerIds();
        setItemCodes();

        //--------------------
        cmbCustomerId.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setCustomerDetails(newValue);
                });

        cmbItemCode.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setItemDetails(newValue);
                });
        //--------------------
    }
    private void setItemDetails(String selectedItemCode) {
        try {

            Item i = ItemCrud.getItem(selectedItemCode);
            if (i != null) {
                txtDescription.setText(i.getDescription());
                txtUnitPrice.setText(i.getUnitPrice());
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemCodes() {
        try {

            cmbItemCode.setItems(FXCollections.observableArrayList(ItemCrud.getItemCodes()));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCustomerDetails(String selectedCustomerId) {
        try {
            Customer c = CustomerCrud.getCustomer(selectedCustomerId);
            if (c != null) {
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtSalary.setText(String.valueOf(c.getSalary()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDateAndTime() {
        /*set Date*/
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        /*set Time*/
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" +
                    currentTime.getMinute() + ":" +
                    currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void setCustomerIds() {
        try {

            ObservableList<String> cIdObList = FXCollections.observableArrayList(
                    CustomerCrud.getCustomerIds()
            );
            cmbCustomerId.setItems(cIdObList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    ObservableList<CartTM> tmList = FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {
        String unitPrice = txtUnitPrice.getText();
        double v = Double.parseDouble(unitPrice);
        int qty = Integer.parseInt(txtQty.getText());
        double totalCost = v * qty;

        CartTM isExists = isExists(cmbItemCode.getValue());

        if (isExists != null) {
            for (CartTM temp : tmList
            ) {
                if (temp.equals(isExists)) {
                    temp.setQty((temp.getQty() + qty));
                    temp.setTotalCost(temp.getTotalCost() + totalCost);
                }
            }
        } else {
            Button btn = new Button("Delete");

            CartTM tm = new CartTM(
                    cmbItemCode.getValue(),
                    txtDescription.getText(),
                    v,
                    qty,
                    totalCost,
                    btn
            );

            btn.setOnAction(e -> {
                tmList.remove(tm);
                calculateTotal();
            });

            tmList.add(tm);
            tblCart.setItems(tmList);
        }
        tblCart.refresh();
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Disposal disposal = new Disposal(
                getOrderId(),
                date,
                cmbCustomerId.getValue()

        );
        ArrayList<OrderDetails> details = new ArrayList<>();
        for (CartTM tm : tmList
        ) {
            details.add(
                    new OrderDetails(
                            getOrderId(),
                            tm.getItemCode(),
                            tm.getQty(),
                            tm.getUnitPrice()
                    )
            );
        }

        //----------------------------

        Connection connection= null;

        try {
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isOrderSaved = new DisposalCrud().saveOrder(disposal);
            if (isOrderSaved) {
                boolean isDetailsSaved=new DisposalCrud().saveOrderDetails(details);
                if (isDetailsSaved){
                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION,"Saved!").show();
                }else{
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR,"Error!").show();
                }
            }else{
                connection.rollback();
                new Alert(Alert.AlertType.ERROR,"Error!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }finally {
            connection.setAutoCommit(true);
        }

    }

    public void openNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/saveCustomer.fxml.fxml"))));
        stage.show();
    }
    private CartTM isExists(String itemCode) {
        for (CartTM tm : tmList
        ) {
            if (tm.getItemCode().equals(itemCode)) {
                return tm;
            }
        }
        return null;
    }
    private void calculateTotal() {
        double total = 0;
        for (CartTM tm : tmList
        ) {
            total += tm.getTotalCost();
        }
        lblTotalCost.setText(String.valueOf(total));
    }
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet pet = SQLUtil.executeQuery("SELECT orderId FROM Disposal ORDER BY orderId DESC LIMIT 1");
        if (pet.next()) {
            String r = pet.getString("orderId");
            int co = r.length();
            String txt = r.substring(0, 3);
            String num = r.substring(3, co);
            int n = Integer.parseInt(num);
            n++;
            String snum = Integer.toString(n);
            String fxt = txt + snum;
            return fxt;
        }else {
            return "D001";
        }
    }
}
