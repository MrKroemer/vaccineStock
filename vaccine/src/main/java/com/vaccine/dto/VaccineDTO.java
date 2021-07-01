package com.vaccine.dto;

import com.vaccine.enums.VaccineType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    private String brand;

    @NotNull
    @Size(min = 1, max = 200)
    private Integer max;

    @NotNull
    @Size(min = 1, max = 200)
    private Integer quantity;

    @Enumerated
    @NotNull
    private VaccineType classification;


}
