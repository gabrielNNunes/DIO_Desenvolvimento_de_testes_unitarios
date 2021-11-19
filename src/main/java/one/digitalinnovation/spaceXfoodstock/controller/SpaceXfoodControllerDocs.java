package one.digitalinnovation.spaceXfoodstock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodAlreadyRegisteredException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages SpaceX food stock")
public interface SpaceXfoodControllerDocs {

    @ApiOperation(value = "Food creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success food creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    SpaceXfoodDTO createFood(SpaceXfoodDTO spaceXfoodDTO) throws SpaceXfoodAlreadyRegisteredException;

    @ApiOperation(value = "Returns food found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success food found in the SpaceX system"),
            @ApiResponse(code = 404, message = "Food with given name not found.")
    })
    SpaceXfoodDTO findByName(@PathVariable String name) throws SpaceXfoodNotFoundException;

    @ApiOperation(value = "Returns a list of all foods registered in the SpaceX system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all foods registered in the SpaceX system"),
    })
    List<SpaceXfoodDTO> listFoods();

    @ApiOperation(value = "Delete a food found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success food deleted in the SpaceX system"),
            @ApiResponse(code = 404, message = "Food with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws SpaceXfoodNotFoundException;
}
