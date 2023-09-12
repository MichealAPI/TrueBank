package it.mikeslab.truebank.economy;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.util.database.SQLiteDBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BalanceHandler {

    private final SQLiteDBUtil sqLiteDBUtil;

    public BalanceHandler(SQLiteDBUtil sqLiteDBUtil) {
        this.sqLiteDBUtil = sqLiteDBUtil;
    }

    public void updateBalance(UUID holderUUID, double amount) {
        String query = "UPDATE CreditCards SET Balance = Balance + ? WHERE HolderUUID = ?";
        try (PreparedStatement preparedStatement = sqLiteDBUtil.prepareStatement(query, amount, holderUUID.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setBalance(UUID holderUUID, double newBalance) {
        String query = "UPDATE CreditCards SET Balance = ? WHERE HolderUUID = ?";
        try (PreparedStatement preparedStatement = sqLiteDBUtil.prepareStatement(query, newBalance, holderUUID.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public double getBalance(UUID holderUUID) {
        String query = "SELECT Balance FROM CreditCards WHERE HolderUUID = ?";
        try (ResultSet resultSet = sqLiteDBUtil.executeParamQuery(query, holderUUID.toString())) {
            if (resultSet.next()) {
                return resultSet.getDouble("Balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
