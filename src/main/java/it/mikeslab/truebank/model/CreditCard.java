package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreditCard {

    private final int cardTypeID;
    private final int securityPIN;

}
