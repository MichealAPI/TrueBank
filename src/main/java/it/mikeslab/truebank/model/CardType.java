package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CardType {

    private final int id;

    private final Icon icon;

    private final String name;
    private final double creationCost;

    // limits

    private final double
            dailyTransferLimit,
            dailyWithdrawLimit,
            dailyDepositLimit,
            depositLimit; // Deposit limit is different from dailyDepositLimit!


}
