package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Banknote {

    private final double value;
    private final Icon icon;

}
