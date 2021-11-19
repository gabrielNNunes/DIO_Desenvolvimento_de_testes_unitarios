package one.digitalinnovation.spaceXfoodstock.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.dto.QuantityDTO;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodAlreadyRegisteredException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodNotFoundException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodStockExceededException;
import one.digitalinnovation.spaceXfoodstock.service.SpaceXfoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/spacexfoods")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceXfoodController implements SpaceXfoodControllerDocs {

    private final SpaceXfoodService spaceXfoodService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpaceXfoodDTO createFood(@RequestBody @Valid SpaceXfoodDTO spaceXfoodDTO) throws SpaceXfoodAlreadyRegisteredException {
        return spaceXfoodService.createFood(spaceXfoodDTO);
    }

    @GetMapping("/{name}")
    public SpaceXfoodDTO findByName(@PathVariable String name) throws SpaceXfoodNotFoundException {
        return spaceXfoodService.findByName(name);
    }

    @GetMapping
    public List<SpaceXfoodDTO> listFoods() {
        return spaceXfoodService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws SpaceXfoodNotFoundException {
        spaceXfoodService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public SpaceXfoodDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws SpaceXfoodNotFoundException, SpaceXfoodStockExceededException {
        return spaceXfoodService.increment(id, quantityDTO.getQuantity());
    }
}
