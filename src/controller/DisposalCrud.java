package controller;

import model.Disposal;
import model.OrderDetails;
import util.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class DisposalCrud {
    public boolean saveOrder(Disposal disposal) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("insert into Disposal values (?,?,?)",
                disposal.getOrderId(), disposal.getCustomerId(), disposal.getOrderDate());

    }
    public boolean saveOrderDetails(ArrayList<OrderDetails> details) throws SQLException, ClassNotFoundException {
        for (OrderDetails det:details
        ) {
            boolean isDetailsSaved=SQLUtil.executeUpdate("insert into OrderDetail values (?,?,?,?)",
                    det.getOrderId(),det.getItemCode(),det.getQty(),det.getUnitPrice());
            if (isDetailsSaved){
                if (!updateQty(det.getItemCode(), det.getQty())){
                    return false;
                }
            }else{
                return false;
            }
        }
        for (OrderDetails det : details){
            System.out.println(det.getOrderId()+" "+det.getItemCode()+" "+det.getQty()+" "+det.getUnitPrice());
        }

        return true;
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Item SET qtyOnHand=qtyOnHand-? WHERE itemCode=?", qty,itemCode);
    }
}
