package com.vaccine.exception;

public class VaccineStockExceedException extends Exception{

    public VaccineStockExceedException(Long id, int quantityToIncrement){
        super(String.format("Instruments with %s ID increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
