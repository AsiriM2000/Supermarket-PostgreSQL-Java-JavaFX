package controller;

import model.Order;
import model.OrderDetails;
import util.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderCrud {
    public boolean saveOrder(Order order) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Order VALUES(?,?,?)",
                order.getId(), order.getDate(), order.getCustomerId());
    }
    public boolean saveOrderDetails(ArrayList<OrderDetails> details) throws SQLException, ClassNotFoundException {
        for (OrderDetails det:details
        ) {
            boolean isDetailsSaved=SQLUtil.executeUpdate("INSERT INTO OrderDetail VALUES(?,?,?,?)",
                    det.getOrderId(),det.getItemCode(),det.getQty(),det.getUnitPrice());
            if (isDetailsSaved){
                if (!updateQty(det.getItemCode(), det.getQty())){
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Item SET qtyOnHand=qtyOnHand-? WHERE code=?", qty,itemCode);
    }
}
