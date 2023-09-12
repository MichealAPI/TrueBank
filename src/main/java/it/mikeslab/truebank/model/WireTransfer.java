package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class WireTransfer {

    private final double amount;

    private final UUID fromUUID, toUUID;

    private final String fromName, toName;



}
