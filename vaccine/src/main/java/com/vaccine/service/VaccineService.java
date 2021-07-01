package com.vaccine.service;

import com.vaccine.exception.VaccineAlreadyRegException;
import com.vaccine.exception.VaccineNotFoundExcep;
import com.vaccine.exception.VaccineStockExceedException;
import com.vaccine.repository.VaccineRepository;
import com.vaccine.dto.VaccineDTO;
import com.vaccine.entity.Vaccine;
import com.vaccine.mapper.VaccineMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper = VaccineMapper.INSTANCE;

    public VaccineDTO createVaccine(VaccineDTO vaccineDTO) throws VaccineAlreadyRegException{
        verifyIfAlreadyRegistered(vaccineDTO.getName());
        Vaccine vaccine = vaccineMapper.toModel(vaccineDTO);
        Vaccine saveVaccine = vaccineRepository.save(vaccine);
        return vaccineMapper.toDTO(saveVaccine);
    }

    public VaccineDTO findByName(String name) throws VaccineNotFoundExcep {
        Vaccine foundVaccine = vaccineRepository.findByName(name)
                .orElseThrow(() -> new VaccineNotFoundExcep(name));
        return vaccineMapper.toDTO(foundVaccine);
    }

    public List<VaccineDTO> listAll(){
    return vaccineRepository.findAll()
            .stream()
            .map(vaccineMapper::toDTO)
            .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws VaccineNotFoundExcep {
        verifyIfExists(id);
        vaccineRepository.deleteById(id);
    }

    private void verifyIfAlreadyRegistered(String name) throws VaccineAlreadyRegException {
        Optional<Vaccine> optSavedVaccine = vaccineRepository.findByName(name);
        if (optSavedVaccine.isPresent()){
            throw new VaccineAlreadyRegException(name);
        }
    }

    private Vaccine verifyIfExists(Long id) throws VaccineNotFoundExcep {
        return vaccineRepository.findById(id)
                .orElseThrow(() -> new VaccineNotFoundExcep(id));
    }

    public VaccineDTO increment(Long id, int quantityToIncrement) throws VaccineNotFoundExcep, VaccineStockExceedException {
        Vaccine vaccineToIncrementStock = verifyIfExists(id);
        int quantityAfterincrement = quantityToIncrement + vaccineToIncrementStock.getQuantity();
        if(quantityAfterincrement <= vaccineToIncrementStock.getMax()){
            vaccineToIncrementStock.setQuantity(vaccineToIncrementStock.getQuantity() + quantityToIncrement);
            Vaccine IncrementVaccineStock = vaccineRepository.save(vaccineToIncrementStock);
            return vaccineMapper.toDTO(IncrementVaccineStock);
        }
        throw new VaccineStockExceedException(id, quantityToIncrement);
    }


}
