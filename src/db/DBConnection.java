package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection = null;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Thogakade", "postgres", "1234");
    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        return (dbConnection==null)? dbConnection= new DBConnection(): dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
