package it.mikeslab.truebank.util;

import java.util.Random;

public class CardNumber {

    private final String cardNumber;

    public CardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public static CardNumber generate() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            int randomDigit = random.nextInt(10);
            stringBuilder.append(randomDigit);
        }

        return new CardNumber(stringBuilder.toString());
    }
    public static int generateSecurityPin() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Generates a random 4-digit PIN (between 1000 and 9999).
    }
}
