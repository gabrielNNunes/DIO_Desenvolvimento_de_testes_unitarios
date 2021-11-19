package one.digitalinnovation.spaceXfoodstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpaceXfoodStockExceededException extends Exception {

    public SpaceXfoodStockExceededException(Long id, int quantityToIncrement) {
        super(String.format("Foods with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
