package it.mikeslab.truebank.economy;

import it.mikeslab.truebank.model.Transaction;
import it.mikeslab.truebank.util.database.SQLiteDBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionHandler {

    private final SQLiteDBUtil sqLiteDBUtil;

    public TransactionHandler(SQLiteDBUtil sqLiteDBUtil) {
        this.sqLiteDBUtil = sqLiteDBUtil;
    }

    public void insertTransaction(Transaction transaction) {
        String query = "INSERT INTO Transactions (Description, Amount, Sender, Receiver) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = sqLiteDBUtil.prepareStatement(
                query,
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getSender(),
                transaction.getReceiver()
        )) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Transaction getTransactionByID(int id) {
        String query = "SELECT * FROM Transactions WHERE TransactionID = ?";
        try (PreparedStatement preparedStatement = sqLiteDBUtil.prepareStatement(query, id);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                String description = resultSet.getString("Description");
                double amount = resultSet.getDouble("Amount");
                long currentMillis = resultSet.getTimestamp("TransactionDate").getTime();
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                return new Transaction(id, description, amount, sender, receiver, currentMillis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTransactionByID(int id) {
        String deleteQuery = "DELETE FROM Transactions WHERE TransactionID = ?";
        try (PreparedStatement preparedStatement = sqLiteDBUtil.prepareStatement(deleteQuery, id)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactions(UUID uuid) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ?";
        try (ResultSet resultSet = sqLiteDBUtil.executeParamQuery(query, uuid.toString(), uuid.toString())) {
            while (resultSet.next()) {
                int transactionID = resultSet.getInt("TransactionID");
                String description = resultSet.getString("Description");
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                long currentMillis = resultSet.getTimestamp("TransactionDate").getTime();
                transactions.add(new Transaction(transactionID, description, amount, sender, receiver, currentMillis));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
