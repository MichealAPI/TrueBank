package it.mikeslab.truebank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Transaction {

    private int transactionID;

    private final String description;
    private final double amount;

    private final String sender, receiver;

    private final long currentMillis;

}
