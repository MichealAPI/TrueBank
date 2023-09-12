package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Transaction {

    private int id = -1;

    private final String description;
    private final double amount;

    private final String sender, receiver;

    private long currentMillis;

}
