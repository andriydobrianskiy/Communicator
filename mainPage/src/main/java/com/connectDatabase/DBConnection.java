package com.connectDatabase;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import com.login.User;
import com.mainPage.page.MainPageController;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection implements InterfaceDataBase {
    private static Logger log = Logger.getLogger(DBConnection.class.getName());


    public Connection connection;



    public static final String DB_DRIVERCLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";


    private static ComboPooledDataSource dataSource;


    private static MainPageController mainWindow;

    public static String URL;

    private void createPoolConnections() {
        try {
            dataSource = new ComboPooledDataSource();

            dataSource.setUser(User.getName());
            dataSource.setDriverClass(DB_DRIVERCLASS);
            dataSource.setPassword(User.getPassword());
            dataSource.setJdbcUrl(URL);
            dataSource.setMinPoolSize(3);
            dataSource.setMaxPoolSize(500);
            dataSource.setAcquireIncrement(5);
        } catch (PropertyVetoException e) {
            log.log(Level.SEVERE, "Connection exception: " + e);
        }

    }

    @Override
    public boolean connect(String url) {
        try {
            this.URL = url;
            Class.forName(DB_DRIVERCLASS);
            connection = DriverManager.getConnection(url);
            createPoolConnections();

            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Connection exception: " + e);
            return false;
        }

    }

    @Override
    public void disconnect() {
        try {
            mainWindow.setDisableWindow(true);
            connection.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Exception caused by disconnect: " + e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public boolean reconnect() {
        try {
            System.out.println("START");
            Class.forName(DB_DRIVERCLASS);
            connection = DriverManager.getConnection(URL);
            mainWindow.setDisableWindow(false);
            return true;
        } catch (Exception e) {
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));

            pauseTransition.setOnFinished(
                    pause -> {
                        System.err.println(e);
                        System.out.println("RETRY");
                        disconnect();
                        reconnect();
                    }
            );
            pauseTransition.play();
        }
        return false;
    }

    @Override
    public boolean isDbConnected() {
        return false;
    }


    public void initMainWindow(MainPageController mainWindow) {
        this.mainWindow = mainWindow;
    }


}

