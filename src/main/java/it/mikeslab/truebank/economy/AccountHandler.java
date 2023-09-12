package it.mikeslab.truebank.economy;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.CreditCard;
import it.mikeslab.truebank.util.database.SQLiteDBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AccountHandler {
    private final SQLiteDBUtil sqLiteDBUtil;
    private final TrueBank instance;

    public AccountHandler(SQLiteDBUtil sqLiteDBUtil, TrueBank instance) {
        this.sqLiteDBUtil = sqLiteDBUtil;
        this.instance = instance;
    }


    public CreditCard fromUUID(UUID holderUUID) {
        String query = "SELECT * FROM CreditCards WHERE HolderUUID = ?";
        try (ResultSet resultSet = sqLiteDBUtil.executeParamQuery(query, holderUUID.toString())) {
            if (resultSet.next()) {
                int cardTypeID = resultSet.getInt("CardTypeID");
                int securityPIN = resultSet.getInt("SecurityPIN");
                String cardNumber = resultSet.getString("CardNumber");
                return new CreditCard(cardTypeID, securityPIN, cardNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrUpdateCreditCard(UUID holderUUID, Double balance, CreditCard creditCard) {
        CreditCard oldCreditCard = fromUUID(holderUUID);

        int cardTypeID = getUpdatedValue(creditCard.getCardTypeID(), oldCreditCard.getCardTypeID());
        int securityPIN = getUpdatedValue(creditCard.getSecurityPIN(), oldCreditCard.getSecurityPIN());
        balance = getUpdatedValue(balance, instance.getBalanceHandler().getBalance(holderUUID));
        String cardNumber = getUpdatedValue(creditCard.getCardNumber(), oldCreditCard.getCardNumber());

        String query = "INSERT OR REPLACE INTO CreditCards (HolderUUID, CardTypeID, SecurityPIN, Balance, CardNumber) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = sqLiteDBUtil.prepareStatement(query, holderUUID.toString(), cardTypeID, securityPIN, balance, cardNumber)) {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    private <T> T getUpdatedValue(T newValue, T defaultValue) {
        return (newValue == null || newValue.equals(-1)) ? defaultValue : newValue;
    }

}
