package one.digitalinnovation.spaceXfoodstock.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.entity.SpaceXfood;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodAlreadyRegisteredException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodNotFoundException;
import one.digitalinnovation.spaceXfoodstock.exception.SpaceXfoodStockExceededException;
import one.digitalinnovation.spaceXfoodstock.mapper.SpaceXfoodMapper;
import one.digitalinnovation.spaceXfoodstock.repository.SpaceXfoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceXfoodService {

    private final SpaceXfoodRepository spaceXfoodRepository;
    private final SpaceXfoodMapper spaceXfoodMapper = SpaceXfoodMapper.INSTANCE;

    public SpaceXfoodDTO createFood(SpaceXfoodDTO spaceXfoodDTO) throws SpaceXfoodAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(spaceXfoodDTO.getName());
        SpaceXfood spaceXfood = spaceXfoodMapper.toModel(spaceXfoodDTO);
        SpaceXfood savedSpaceXfood = spaceXfoodRepository.save(spaceXfood);
        return spaceXfoodMapper.toDTO(savedSpaceXfood);
    }

    public SpaceXfoodDTO findByName(String name) throws SpaceXfoodNotFoundException {
        SpaceXfood foundSpaceXfood = spaceXfoodRepository.findByName(name)
                .orElseThrow(() -> new SpaceXfoodNotFoundException(name));
        return spaceXfoodMapper.toDTO(foundSpaceXfood);
    }

    public List<SpaceXfoodDTO> listAll() {
        return spaceXfoodRepository.findAll()
                .stream()
                .map(spaceXfoodMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws SpaceXfoodNotFoundException {
        verifyIfExists(id);
        spaceXfoodRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws SpaceXfoodAlreadyRegisteredException {
        Optional<SpaceXfood> optSavedFood = spaceXfoodRepository.findByName(name);
        if (optSavedFood.isPresent()) {
            throw new SpaceXfoodAlreadyRegisteredException(name);
        }
    }

    private SpaceXfood verifyIfExists(Long id) throws SpaceXfoodNotFoundException {
        return spaceXfoodRepository.findById(id)
                .orElseThrow(() -> new SpaceXfoodNotFoundException(id));
    }

    public SpaceXfoodDTO increment(Long id, int quantityToIncrement) throws SpaceXfoodNotFoundException, SpaceXfoodStockExceededException {
        SpaceXfood spaceXfoodToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + spaceXfoodToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= spaceXfoodToIncrementStock.getMax()) {
            spaceXfoodToIncrementStock.setQuantity(spaceXfoodToIncrementStock.getQuantity() + quantityToIncrement);
            SpaceXfood incrementedSpaceXfoodStock = spaceXfoodRepository.save(spaceXfoodToIncrementStock);
            return spaceXfoodMapper.toDTO(incrementedSpaceXfoodStock);
        }
        throw new SpaceXfoodStockExceededException(id, quantityToIncrement);
    }
    public SpaceXfoodDTO decrement(Long id, int quantityToDecrement) throws SpaceXfoodNotFoundException, SpaceXfoodStockExceededException {
        SpaceXfood spaceXfoodToDecrementStock = verifyIfExists(id);
        int foodStockAfterDecremented = spaceXfoodToDecrementStock.getQuantity() - quantityToDecrement;
        if (foodStockAfterDecremented >= 0) {
            spaceXfoodToDecrementStock.setQuantity(foodStockAfterDecremented);
            SpaceXfood decrementedSpaceXfoodStock = spaceXfoodRepository.save(spaceXfoodToDecrementStock);
            return spaceXfoodMapper.toDTO(decrementedSpaceXfoodStock);
        }
        throw new SpaceXfoodStockExceededException(id, quantityToDecrement);
    }
}
