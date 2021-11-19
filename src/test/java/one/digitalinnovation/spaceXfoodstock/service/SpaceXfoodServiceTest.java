package one.digitalinnovation.spaceXfoodstock.service;

import one.digitalinnovation.spaceXfoodstock.builder.SpaceXfoodDTOBuilder;
import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.entity.SpaceXfood;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodAlreadyRegisteredException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodNotFoundException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodStockExceededException;
import one.digitalinnovation.spaceXfoodstock.mapper.SpaceXfoodMapper;
import one.digitalinnovation.spaceXfoodstock.repository.SpaceXfoodRepository;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpaceXfoodServiceTest {

    private static final long INVALID_FOOD_ID = 1L;

    @Mock
    private SpaceXfoodRepository spaceXfoodRepository;

    private SpaceXfoodMapper spaceXfoodMapper = SpaceXfoodMapper.INSTANCE;

    @InjectMocks
    private SpaceXfoodService spaceXfoodService;

    @Test
    void whenFoodInformedThenItShouldBeCreated() throws SpaceXfoodAlreadyRegisteredException {
        // given
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSavedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        // when
        when(spaceXfoodRepository.findByName(expectedSpaceXfoodDTO.getName())).thenReturn(Optional.empty());
        when(spaceXfoodRepository.save(expectedSavedSpaceXfood)).thenReturn(expectedSavedSpaceXfood);

        //then
        SpaceXfoodDTO createdSpaceXfoodDTO = spaceXfoodService.createFood(expectedSpaceXfoodDTO);

        assertThat(createdSpaceXfoodDTO.getId(), is(equalTo(expectedSpaceXfoodDTO.getId())));
        assertThat(createdSpaceXfoodDTO.getName(), is(equalTo(expectedSpaceXfoodDTO.getName())));
        assertThat(createdSpaceXfoodDTO.getQuantity(), is(equalTo(expectedSpaceXfoodDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredFoodInformedThenAnExceptionShouldBeThrown() {
        // given
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood duplicatedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        // when
        when(spaceXfoodRepository.findByName(expectedSpaceXfoodDTO.getName())).thenReturn(Optional.of(duplicatedSpaceXfood));

        // then
        assertThrows(SpaceXfoodAlreadyRegisteredException.class, () -> spaceXfoodService.createFood(expectedSpaceXfoodDTO));
    }

    @Test
    void whenValidFoodNameIsGivenThenReturnAFood() throws SpaceXfoodNotFoundException {
        // given
        SpaceXfoodDTO expectedFoundSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedFoundSpaceXfood = spaceXfoodMapper.toModel(expectedFoundSpaceXfoodDTO);

        // when
        when(spaceXfoodRepository.findByName(expectedFoundSpaceXfood.getName())).thenReturn(Optional.of(expectedFoundSpaceXfood));

        // then
        SpaceXfoodDTO foundSpaceXfoodDTO = spaceXfoodService.findByName(expectedFoundSpaceXfoodDTO.getName());

        assertThat(foundSpaceXfoodDTO, is(equalTo(expectedFoundSpaceXfoodDTO)));
    }

    @Test
    void whenNotRegisteredFoodNameIsGivenThenThrowAnException() {
        // given
        SpaceXfoodDTO expectedFoundSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        // when
        when(spaceXfoodRepository.findByName(expectedFoundSpaceXfoodDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(SpaceXfoodNotFoundException.class, () -> spaceXfoodService.findByName(expectedFoundSpaceXfoodDTO.getName()));
    }

    @Test
    void whenListFoodIsCalledThenReturnAListOfFoods() {
        // given
        SpaceXfoodDTO expectedFoundSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedFoundSpaceXfood = spaceXfoodMapper.toModel(expectedFoundSpaceXfoodDTO);

        //when
        when(spaceXfoodRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundSpaceXfood));

        //then
        List<SpaceXfoodDTO> foundListFoodsDTO = spaceXfoodService.listAll();

        assertThat(foundListFoodsDTO, is(not(empty())));
        assertThat(foundListFoodsDTO.get(0), is(equalTo(expectedFoundSpaceXfoodDTO)));
    }

    @Test
    void whenListFoodIsCalledThenReturnAnEmptyListOfFoods() {
        //when
        when(spaceXfoodRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<SpaceXfoodDTO> foundListFoodsDTO = spaceXfoodService.listAll();

        assertThat(foundListFoodsDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAFoodShouldBeDeleted() throws SpaceXfoodNotFoundException {
        // given
        SpaceXfoodDTO expectedDeletedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedDeletedSpaceXfood = spaceXfoodMapper.toModel(expectedDeletedSpaceXfoodDTO);

        // when
        when(spaceXfoodRepository.findById(expectedDeletedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedDeletedSpaceXfood));
        doNothing().when(spaceXfoodRepository).deleteById(expectedDeletedSpaceXfoodDTO.getId());

        // then
        spaceXfoodService.deleteById(expectedDeletedSpaceXfoodDTO.getId());

        verify(spaceXfoodRepository, times(1)).findById(expectedDeletedSpaceXfoodDTO.getId());
        verify(spaceXfoodRepository, times(1)).deleteById(expectedDeletedSpaceXfoodDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementFoodStock() throws SpaceXfoodNotFoundException, SpaceXfoodStockExceededException {
        //given
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        //when
        when(spaceXfoodRepository.findById(expectedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedSpaceXfood));
        when(spaceXfoodRepository.save(expectedSpaceXfood)).thenReturn(expectedSpaceXfood);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedSpaceXfoodDTO.getQuantity() + quantityToIncrement;

        // then
        SpaceXfoodDTO incrementedSpaceXfoodDTO = spaceXfoodService.increment(expectedSpaceXfoodDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedSpaceXfoodDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedSpaceXfoodDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        when(spaceXfoodRepository.findById(expectedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedSpaceXfood));

        int quantityToIncrement = 80;
        assertThrows(SpaceXfoodStockExceededException.class, () -> spaceXfoodService.increment(expectedSpaceXfoodDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        when(spaceXfoodRepository.findById(expectedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedSpaceXfood));

        int quantityToIncrement = 45;
        assertThrows(SpaceXfoodStockExceededException.class, () -> spaceXfoodService.increment(expectedSpaceXfoodDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(spaceXfoodRepository.findById(INVALID_FOOD_ID)).thenReturn(Optional.empty());

        assertThrows(SpaceXfoodNotFoundException.class, () -> spaceXfoodService.increment(INVALID_FOOD_ID, quantityToIncrement));
    }

    @Test
    void whenDecrementIsCalledThenDecrementFoodStock() throws SpaceXfoodNotFoundException, SpaceXfoodStockExceededException {
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        when(spaceXfoodRepository.findById(expectedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedSpaceXfood));
        when(spaceXfoodRepository.save(expectedSpaceXfood)).thenReturn(expectedSpaceXfood);

        int quantityToDecrement = 5;
        int expectedQuantityAfterDecrement = expectedSpaceXfoodDTO.getQuantity() - quantityToDecrement;
        SpaceXfoodDTO incrementedSpaceXfoodDTO = spaceXfoodService.decrement(expectedSpaceXfoodDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedSpaceXfoodDTO.getQuantity()));
        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
    }

    @Test
    void whenDecrementIsCalledToEmptyStockThenEmptyFoodStock() throws SpaceXfoodNotFoundException, SpaceXfoodStockExceededException {
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        when(spaceXfoodRepository.findById(expectedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedSpaceXfood));
        when(spaceXfoodRepository.save(expectedSpaceXfood)).thenReturn(expectedSpaceXfood);

        int quantityToDecrement = 10;
        int expectedQuantityAfterDecrement = expectedSpaceXfoodDTO.getQuantity() - quantityToDecrement;
        SpaceXfoodDTO incrementedSpaceXfoodDTO = spaceXfoodService.decrement(expectedSpaceXfoodDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(0));
        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedSpaceXfoodDTO.getQuantity()));
    }

    @Test
    void whenDecrementIsLowerThanZeroThenThrowException() {
        SpaceXfoodDTO expectedSpaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        SpaceXfood expectedSpaceXfood = spaceXfoodMapper.toModel(expectedSpaceXfoodDTO);

        when(spaceXfoodRepository.findById(expectedSpaceXfoodDTO.getId())).thenReturn(Optional.of(expectedSpaceXfood));

        int quantityToDecrement = 80;
        assertThrows(SpaceXfoodStockExceededException.class, () -> spaceXfoodService.decrement(expectedSpaceXfoodDTO.getId(), quantityToDecrement));
    }

    @Test
    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToDecrement = 10;

        when(spaceXfoodRepository.findById(INVALID_FOOD_ID)).thenReturn(Optional.empty());

        assertThrows(SpaceXfoodNotFoundException.class, () -> spaceXfoodService.decrement(INVALID_FOOD_ID, quantityToDecrement));
    }
}
