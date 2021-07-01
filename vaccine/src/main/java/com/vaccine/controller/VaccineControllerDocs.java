package com.vaccine.controller;

import com.vaccine.dto.VaccineDTO;
import com.vaccine.exception.VaccineAlreadyRegException;
import com.vaccine.exception.VaccineNotFoundExcep;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manager vaccine stock")
public interface VaccineControllerDocs {
    @ApiOperation(value = "Vaccine creation operation")

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success vaccine creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    VaccineDTO createVaccine(VaccineDTO vaccineDTO) throws VaccineAlreadyRegException;

    @ApiOperation(value = "Returns vaccine found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success vaccine found in the system"),
            @ApiResponse(code = 404, message = "Vaccine with given name not found")
    })
    VaccineDTO findByName(@PathVariable String name) throws VaccineNotFoundExcep;

    @ApiOperation(value = "Returns a list of all vaccines registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all vaccines registered in the system"),
    })
    List<VaccineDTO> listVaccines();

    @ApiOperation(value = "Delete a vaccine found by given valid id")
    @ApiResponses(value = {
            @ApiResponse(code = 294, message = "Succes vacine deleted in the system"),
            @ApiResponse(code = 404, message = "Vaccine with given id not found")
    })
    void deleteById(@PathVariable Long id) throws VaccineNotFoundExcep;
}
