package it.mikeslab.truebank.util;

import it.mikeslab.truebank.model.CreditCard;

import java.util.Random;

public class CardNumber {




    public static CreditCard generate(int cardTypeID) {

        String securityPIN = generateRandom(4); // todo Make customizable (?)
        String creditCardNumber = generateRandom(16);

        return new CreditCard(cardTypeID, securityPIN, creditCardNumber);
    }


    private static String generateRandom(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomDigit = random.nextInt(10);
            stringBuilder.append(randomDigit);
        }

        return stringBuilder.toString();
    }
}
