package com.vaccine.mapper;

import com.vaccine.dto.VaccineDTO;
import com.vaccine.entity.Vaccine;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VaccineMapper {

    VaccineMapper INSTANCE = Mappers.getMapper(VaccineMapper.class);

    Vaccine toModel(VaccineDTO vaccineDTO);

    VaccineDTO toDTO(Vaccine vaccine);
}
