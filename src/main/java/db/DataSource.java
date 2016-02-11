package db;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author craftic
 */
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {
    private String userName;
    private String password;
    private String host;
    
    public void setHost(String host) {
        this.host = host;
    }
    private static DataSource     datasource;
    private ComboPooledDataSource cpds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        Locale.setDefault(Locale.ENGLISH);
        this.userName = "unc_user";
        this.password = "pass123";
        this.host = "jdbc:oracle:thin:@54.174.120.172:1521:xe";
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("oracle.jdbc.OracleDriver"); //loads the jdbc driver
        cpds.setJdbcUrl(this.host);
        cpds.setUser(this.userName);
        cpds.setPassword(this.password);

        // the settings below are optional -- c3p0 can work with defaults
        //cpds.setMinPoolSize(5);
        //cpds.setAcquireIncrement(5);
        //cpds.setMaxPoolSize(20);
        //cpds.setMaxStatements(180);

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}
