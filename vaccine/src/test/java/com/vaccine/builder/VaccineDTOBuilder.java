package com.vaccine.builder;

import com.vaccine.dto.VaccineDTO;
import com.vaccine.enums.VaccineType;
import lombok.Builder;

@Builder
public class VaccineDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Pfizer";

    @Builder.Default
    private String brand = "BioNTech";

    @Builder.Default
    private double max = 30.000;

    @Builder.Default
    private double quantity = 15.000;

    @Builder.Default
    private VaccineType type = VaccineType.PFIZER;

    public VaccineDTO toVaccineDTO(){
        return new VaccineDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }

}
