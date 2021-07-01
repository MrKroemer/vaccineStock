package com.vaccine.controller;

import com.vaccine.service.VaccineService;
import com.vaccine.dto.QuantityDTO;
import com.vaccine.dto.VaccineDTO;
import com.vaccine.exception.VaccineAlreadyRegException;
import com.vaccine.exception.VaccineNotFoundExcep;
import com.vaccine.exception.VaccineStockExceedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vaccines")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VaccineController {

    private final VaccineService vaccineService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VaccineDTO createVacine(@RequestBody @Valid VaccineDTO vaccineDTO) throws VaccineAlreadyRegException, VaccineStockExceedException {
     return vaccineService.createVaccine(vaccineDTO);
    }
     @GetMapping("/{name}")
     public VaccineDTO findByName(@PathVariable String name) throws VaccineNotFoundExcep{
        return vaccineService.findByName(name);
    }

    @GetMapping
    public List<VaccineDTO> listVaccines(){
        return vaccineService.listAll();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws VaccineNotFoundExcep, VaccineNotFoundExcep{
        vaccineService.deleteById(id);
    }
    @PatchMapping("/{id}/increment")
    public VaccineDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO)throws VaccineNotFoundExcep, VaccineStockExceedException{
        return vaccineService.increment(id, quantityDTO.getQuantity());
    }
}
