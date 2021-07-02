package com.vaccine.service;


import com.vaccine.builder.VaccineDTOBuilder;
import com.vaccine.dto.VaccineDTO;
import com.vaccine.entity.Vaccine;
import com.vaccine.exception.VaccineAlreadyRegException;
import com.vaccine.exception.VaccineNotFoundExcep;
import com.vaccine.exception.VaccineStockExceedException;
import com.vaccine.mapper.VaccineMapper;
import com.vaccine.repository.VaccineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VaccineServiceTest {

    private static final long INVALID_ private static final long INVALID_VACCINE_ID = 1L;

    @Mock
    private VaccineRepository vaccineRepository;

    private VaccineMapper vaccineMapper = VaccineMapper.INSTANCE;

    @InjectMocks
    private VaccineService vaccineService;

    @Test
     void whenVaccineInformedThenItShouldBeCreated() throws VaccineAlreadyRegException {
        // given
        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedSavedVaccine = vaccineMapper.toModel(expectedVaccineDTO);

        // when
        when(vaccineRepository.findByName(expectedVaccineDTO.getName())).thenReturn(Optional.empty());
        when(vaccineRepository.save(expectedSavedVaccine)).thenReturn(expectedSavedVaccine);

        //then
        VaccineDTO createdVaccineDTO = vaccineService.createVaccine(expectedVaccineDTO);

        assertThat(createdVaccineDTO.getId(), is(equalTo(expectedVaccineDTO.getId())));
        assertThat(createdVaccineDTO.getName(), is(equalTo(expectedVaccineDTO.getName())));
        assertThat(createdVaccineDTO.getQuantity(), is(equalTo(expectedVaccineDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredVaccineInformedThenAnExceptionShouldBeThrown() {
        // given
        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine duplicatedVaccine = vaccineMapper.toModel(expectedVaccineDTO);

        // when
        when(vaccineRepository.findByName(expectedVaccineDTO.getName())).thenReturn(Optional.of(duplicatedVaccine));

        // then
        assertThrows(VaccineAlreadyRegException.class, () -> vaccineService.createVaccine(expectedVaccineDTO));
    }

    @Test
    void whenValidVaccineNameIsGivenThenReturnAVaccine() throws VaccineNotFoundExcep {
        // given
        VaccineDTO expectedFoundVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedFoundVaccine = vaccineMapper.toModel(expectedFoundVaccineDTO);

        // when
        when(vaccineRepository.findByName(expectedFoundVaccine.getName())).thenReturn(Optional.of(expectedFoundVaccine));

        // then
        VaccineDTO foundVaccineDTO = vaccineService.findByName(expectedFoundVaccineDTO.getName());

        assertThat(foundVaccineDTO, is(equalTo(expectedFoundVaccineDTO)));
    }

    @Test
    void whenNotRegisteredVaccineNameIsGivenThenThrowAnException() {
        // given
        VaccineDTO expectedFoundVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();

        // when
        when(vaccineRepository.findByName(expectedFoundVaccineDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(VaccineNotFoundExcep.class, () -> vaccineService.findByName(expectedFoundVaccineDTO.getName()));
    }

    @Test
    void whenListVaccineIsCalledThenReturnAListOfVaccines() {
        // given
        VaccineDTO expectedFoundVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedFoundVaccine = vaccineMapper.toModel(expectedFoundVaccineDTO);

        //when
        when(vaccineRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundVaccine));

        //then
        List<VaccineDTO> foundListVaccinesDTO = vaccineService.listAll();

        assertThat(foundListVaccinesDTO, is(not(empty())));
        assertThat(foundListVaccinesDTO.get(0), is(equalTo(expectedFoundVaccineDTO)));
    }

    @Test
    void whenListVaccineIsCalledThenReturnAnEmptyListOfVaccines() {
        //when
        when(vaccineRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<VaccineDTO> foundListVaccinesDTO = vaccineService.listAll();

        assertThat(foundListVaccinesDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAVAccineShouldBeDeleted() throws VaccineNotFoundExcep {
        // given
        VaccineDTO expectedDeletedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedDeletedVaccine = vaccineMapper.toModel(expectedDeletedVaccineDTO);

        // when
        when(vaccineRepository.findById(expectedDeletedVaccineDTO.getId())).thenReturn(Optional.of(expectedDeletedVaccine));
        doNothing().when(vaccineRepository).deleteById(expectedDeletedVaccineDTO.getId());

        // then
        vaccineService.deleteById(expectedDeletedVaccineDTO.getId());

        verify(vaccineRepository, times(1)).findById(expectedDeletedVaccineDTO.getId());
        verify(vaccineRepository, times(1)).deleteById(expectedDeletedVaccineDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementVaccineStock() throws VaccineNotFoundExcep, VaccineStockExceedException, VaccineStockExceedException, VaccineNotFoundExcep {
        //given
        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);

        //when
        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
        when(vaccineRepository.save(expectedVaccine)).thenReturn(expectedVaccine);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = (int) (expectedVaccineDTO.getQuantity() + quantityToIncrement);

        // then
        VaccineDTO incrementedVaccineDTO = vaccineService.increment(expectedVaccineDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedVaccineDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedVaccineDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);

        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));

        int quantityToIncrement = 80;
        assertThrows(VaccineStockExceedException.class, () -> vaccineService.increment(expectedVaccineDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);

        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));

        int quantityToIncrement = 45;
        assertThrows(VaccineStockExceedException.class, () -> vaccineService.increment(expectedVaccineDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(vaccineRepository.findById(INVALID_VACCINE_ID)).thenReturn(Optional.empty());

        assertThrows(VaccineNotFoundExcep.class, () -> vaccineService.increment(INVALID_VACCINE_ID, quantityToIncrement));
    }
//
//    @Test
//    void whenDecrementIsCalledThenDecrementVacineStock() throws VaccineNotFoundException, VaccineStockExceededException {
//        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
//        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);
//
//        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
//        when(vaccineRepository.save(expectedVaccine)).thenReturn(expectedVaccine);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedVaccineDTO.getQuantity() - quantityToDecrement;
//        VaccineDTO incrementedVaccineDTO = vaccineService.decrement(expectedVaccineDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedVaccineDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyVaccineStock() throws VaccineNotFoundException, VaccineStockExceededException {
//        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
//        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);
//
//        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
//        when(vaccineRepository.save(expectedVaccine)).thenReturn(expectedVaccine
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedVaccineDTO.getQuantity() - quantityToDecrement;
//        VaccineDTO incrementedVaccineDTO = vaccineService.decrement(expectedVaccineDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedVaccineDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
//        Vaccine expectedVaccine = Mapper.toModel(vaccineexpectedVaccineDTO);
//
//        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
//
//        int quantityToDecrement = 80;
//        assertThrows(VaccineStockExceededException.class, () -> vaccineService.decrement(expectedVaccineDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(vaccineRepository.findById(INVALID_VACCINE_ID)).thenReturn(Optional.empty());
//
//        assertThrows(VaccineNotFoundException.class, () -> vaccineService.decrement(INVALID_VACCINE_ID, quantityToDecrement));
//    }_ID = 1L;

    /*@Mock
    private VaccineRepository vaccineRepository;

    private VaccineMapper vaccineMapper = VaccineMapper.INSTANCE;

    @InjectMocks
    private VaccineService vaccineService;*/

    @Test
    void whenExclusionIsCalledWithValidIdThenAVaccineShouldBeDeleted() throws VaccineNotFoundExcep{
        // given
        VaccineDTO expectedDeletedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedDeletedVaccine = vaccineMapper.toModel(expectedDeletedVaccineDTO);

        // when
        when(vaccineRepository.findById(expectedDeletedVaccineDTO.getId())).thenReturn(Optional.of(expectedDeletedVaccine));
        doNothing().when(vaccineRepository).deleteById(expectedDeletedVaccineDTO.getId());

        // then
        vaccineService.deleteById(expectedDeletedVaccineDTO.getId());

        verify(vaccineRepository, times(1)).findById(expectedDeletedVaccineDTO.getId());
        verify(vaccineRepository, times(1)).deleteById(expectedDeletedVaccineDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementVaccineStock() throws VaccineNotFoundExcep, VaccineStockExceedException {
        //given
        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);

        //when
        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
        when(vaccineRepository.save(expectedVaccine)).thenReturn(expectedVaccine);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = (int) (expectedVaccineDTO.getQuantity() + quantityToIncrement);

        // then
        VaccineDTO incrementedVaccineDTO = vaccineService.increment(expectedVaccineDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedVaccineDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedVaccineDTO.getMax()));
    }


//
//    @Test
//    void whenDecrementIsCalledThenDecrementVaccineStock() throws VaccineNotFoundException, VaccineStockExceededException {
//        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
//        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);
//
//        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
//        when(vaccineRepository.save(expectedVaccine)).thenReturn(expectedVaccine);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedVaccineDTO.getQuantity() - quantityToDecrement;
//        VaccineDTO incrementedVaccineDTO = vaccineService.decrement(expectedVaccineDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedVaccineDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyVaccineStock() throws VaccineNotFoundException, VaccineStockExceededException {
//        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
//        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);
//
//        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
//        when(vaccineRepository.save(expectedVaccine)).thenReturn(expectedVaccine);
//
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedVaccineDTO.getQuantity() - quantityToDecrement;
//        VaccineDTO incrementedVaccineDTO = vaccineService.decrement(expectedVaccineDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedVaccineDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        VaccineDTO expectedVaccineDTO = VaccineDTOBuilder.builder().build().toVaccineDTO();
//        Vaccine expectedVaccine = vaccineMapper.toModel(expectedVaccineDTO);
//
//        when(vaccineRepository.findById(expectedVaccineDTO.getId())).thenReturn(Optional.of(expectedVaccine));
//
//        int quantityToDecrement = 80;
//        assertThrows(VaccineStockExceededException.class, () -> vaccineService.decrement(expectedVaccineDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(vaccineRepository.findById(INVALID_VACCINE_ID)).thenReturn(Optional.empty());
//
//        assertThrows(VaccineNotFoundException.class, () -> vaccineService.decrement(INVALID_VACCINE_ID, quantityToDecrement));
//    }


}
