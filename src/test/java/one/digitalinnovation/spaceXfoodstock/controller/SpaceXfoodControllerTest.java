package one.digitalinnovation.spaceXfoodstock.controller;

import one.digitalinnovation.spaceXfoodstock.builder.SpaceXfoodDTOBuilder;
import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.dto.QuantityDTO;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodNotFoundException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodStockExceededException;
import one.digitalinnovation.spaceXfoodstock.service.SpaceXfoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.spaceXfoodstock.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SpaceXfoodControllerTest {

    private static final String FOOD_API_URL_PATH = "/api/v1/foods";
    private static final long VALID_FOOD_ID = 1L;
    private static final long INVALID_FOOD_ID = 2l;
    private static final String FOOD_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String FOOD_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private SpaceXfoodService spaceXfoodService;

    @InjectMocks
    private SpaceXfoodController spaceXfoodController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(spaceXfoodController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAFoodIsCreated() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        // when
        when(spaceXfoodService.createFood(spaceXfoodDTO)).thenReturn(spaceXfoodDTO);

        // then
        mockMvc.perform(post(FOOD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(spaceXfoodDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(spaceXfoodDTO.getName())))
                .andExpect(jsonPath("$.brand", is(spaceXfoodDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(spaceXfoodDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        spaceXfoodDTO.setBrand(null);

        // then
        mockMvc.perform(post(FOOD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(spaceXfoodDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        //when
        when(spaceXfoodService.findByName(spaceXfoodDTO.getName())).thenReturn(spaceXfoodDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(FOOD_API_URL_PATH + "/" + spaceXfoodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(spaceXfoodDTO.getName())))
                .andExpect(jsonPath("$.brand", is(spaceXfoodDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(spaceXfoodDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        //when
        when(spaceXfoodService.findByName(spaceXfoodDTO.getName())).thenThrow(SpaceXfoodNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(FOOD_API_URL_PATH + "/" + spaceXfoodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithFoodsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        //when
        when(spaceXfoodService.listAll()).thenReturn(Collections.singletonList(spaceXfoodDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(FOOD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(spaceXfoodDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(spaceXfoodDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(spaceXfoodDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutFoodsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        //when
        when(spaceXfoodService.listAll()).thenReturn(Collections.singletonList(spaceXfoodDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(FOOD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();

        //when
        doNothing().when(spaceXfoodService).deleteById(spaceXfoodDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(FOOD_API_URL_PATH + "/" + spaceXfoodDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(SpaceXfoodNotFoundException.class).when(spaceXfoodService).deleteById(INVALID_FOOD_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(FOOD_API_URL_PATH + "/" + INVALID_FOOD_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
        spaceXfoodDTO.setQuantity(spaceXfoodDTO.getQuantity() + quantityDTO.getQuantity());

        when(spaceXfoodService.increment(VALID_FOOD_ID, quantityDTO.getQuantity())).thenReturn(spaceXfoodDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(FOOD_API_URL_PATH + "/" + VALID_FOOD_ID + FOOD_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(spaceXfoodDTO.getName())))
                .andExpect(jsonPath("$.brand", is(spaceXfoodDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(spaceXfoodDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(spaceXfoodDTO.getQuantity())));
    }

//    @Test
//    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
//        spaceXfoodDTO.setQuantity(spaceXfoodDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(spaceXfoodService.increment(VALID_FOOD_ID, quantityDTO.getQuantity())).thenThrow(SpaceXfoodStockExceededException.class);
//
//        mockMvc.perform(patch(FOOD_API_URL_PATH + "/" + VALID_FOOD_ID + FOOD_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void whenPATCHIsCalledWithInvalidFoodIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        when(spaceXfoodService.increment(INVALID_FOOD_ID, quantityDTO.getQuantity())).thenThrow(SpaceXfoodNotFoundException.class);
//        mockMvc.perform(patch(FOOD_API_URL_PATH + "/" + INVALID_FOOD_ID + FOOD_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
//        spaceXfoodDTO.setQuantity(spaceXfoodDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(spaceXfoodService.decrement(VALID_FOOD_ID, quantityDTO.getQuantity())).thenReturn(spaceXfoodDTO);
//
//        mockMvc.perform(patch(FOOD_API_URL_PATH + "/" + VALID_FOOD_ID + FOOD_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(spaceXfoodDTO.getName())))
//                .andExpect(jsonPath("$.brand", is(spaceXfoodDTO.getBrand())))
//                .andExpect(jsonPath("$.type", is(spaceXfoodDTO.getType().toString())))
//                .andExpect(jsonPath("$.quantity", is(spaceXfoodDTO.getQuantity())));
//    }
//
//    @Test
//    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(60)
//                .build();
//
//        SpaceXfoodDTO spaceXfoodDTO = SpaceXfoodDTOBuilder.builder().build().toFoodDTO();
//        spaceXfoodDTO.setQuantity(spaceXfoodDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(spaceXfoodService.decrement(VALID_FOOD_ID, quantityDTO.getQuantity())).thenThrow(SpaceXfoodStockExceededException.class);
//
//        mockMvc.perform(patch(FOOD_API_URL_PATH + "/" + VALID_FOOD_ID + FOOD_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void whenPATCHIsCalledWithInvalidFoodIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        when(spaceXfoodService.decrement(INVALID_FOOD_ID, quantityDTO.getQuantity())).thenThrow(SpaceXfoodNotFoundException.class);
//        mockMvc.perform(patch(FOOD_API_URL_PATH + "/" + INVALID_FOOD_ID + FOOD_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
}
