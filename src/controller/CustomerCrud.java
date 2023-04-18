package controller;

import model.Customer;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerCrud {
    public static ArrayList<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT id FROM Customer");
        ArrayList<String> ids= new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    public static Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        ResultSet result= SQLUtil.executeQuery("SELECT * FROM Customer WHERE id=?", id);
        if (result.next()){
            return new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4)
            );
        }
        return null;
    }
}
