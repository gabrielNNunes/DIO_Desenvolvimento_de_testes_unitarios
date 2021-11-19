package one.digitalinnovation.spaceXfoodstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpaceXfoodNotFoundException extends Exception {

    public SpaceXfoodNotFoundException(String foodName) {
        super(String.format("Food with name %s not found in the SpaceX system.", foodName));
    }

    public SpaceXfoodNotFoundException(Long id) {
        super(String.format("Food with id %s not found in the SpaceX system.", id));
    }
}
