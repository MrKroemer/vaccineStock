package com.vaccine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VaccineType {

    ASTRAZENECA("AstraZeneca"),
    MODERNA("Moderna"),
    PFIZER("Pfizer"),
    SPUTNIK_V("Sputnik V"),
    CORONAVAC("Coronavac"),
    JANSSEN("Janssen"),
    SINOPHARM("Sinopharm");




    private final String description;
}
