package it.mikeslab.truebank.util.database;

import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDBUtil {

    @Getter
    private Connection connection;

    public SQLiteDBUtil(File dbFile) {
        try {

            // Create the database file if it doesn't exist.
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }

            String url = "jdbc:sqlite:" + dbFile;
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to execute an SQL query and return a ResultSet.
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to execute an SQL update (INSERT, UPDATE, DELETE).
    public int executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error code, adjust as needed.
        }
    }

    // Method to execute a parameterized SQL query and return a ResultSet.
    public ResultSet executeParamQuery(String query, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameter values based on the provided arguments.
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement;
    }

    // Method to close the database connection.
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Plug-in related methods


    public void createDefaultDatabaseTables() {
        createTransactionsTable();
        createCreditCardsTable();
    }

    private void createCreditCardsTable() {
        String query = "CREATE TABLE IF NOT EXISTS CreditCards (" +
                "HolderUUID TEXT PRIMARY KEY," +
                "CardTypeID INTEGER," +
                "SecurityPIN INTEGER," +
                "CardNumber TEXT);";
        this.executeQuery(query);
    }

    private void createTransactionsTable() {
        String query = "CREATE TABLE IF NOT EXISTS Transactions (" +
                "    TransactionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    Description TEXT," +
                "    Amount REAL," +
                "    Sender TEXT," +
                "    Receiver TEXT," +
                "    TransactionDate DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";
        this.executeQuery(query);
    }


}
