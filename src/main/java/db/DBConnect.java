package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Locale;

public class DBConnect {

    private String userName;
    private String password;
    private String host;

    public DBConnect(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.host = "jdbc:oracle:thin:@localhost:1521:xe";
    }
    
    public void setHost(String host) {
        this.host = host;
    }
 
    public Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conn;
            Locale.setDefault(Locale.ENGLISH);
            Properties connectionProps = new Properties();
            connectionProps.put("user", this.userName);
            connectionProps.put("password", this.password);
            conn = DriverManager.getConnection(this.host, connectionProps);
            return conn;
        } catch (SQLException | ClassNotFoundException err) {
            System.out.println(err.getMessage());
            return null;
        }
    }
}
