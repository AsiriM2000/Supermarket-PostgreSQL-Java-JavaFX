package controller;

import model.Item;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCrud {
    public static ArrayList<String> getItemCodes() throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT itemCode FROM Item");
        ArrayList<String> codeList = new ArrayList<>();
        while (result.next()) {
            codeList.add(result.getString(1));
        }
        return codeList;
    }

    public static Item getItem(String code) throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT * FROM Item WHERE itemCode=?", code);
        if (result.next()) {
            return new Item(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4)
            );
        }
        return null;
    }
}
