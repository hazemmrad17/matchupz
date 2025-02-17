package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private static MyDatabase instance;

    private final String URL = "jdbc:mysql://localhost:3306/matchupz";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private Connection conn ;
    //private static MyDatabase instance;

    private MyDatabase() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/matchupz", "root", "");
            System.out.println("Database connection established");
        } catch (SQLException var2) {
            SQLException e = var2;
            System.out.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
