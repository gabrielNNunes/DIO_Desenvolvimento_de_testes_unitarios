package one.digitalinnovation.spaceXfoodstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpaceXfoodAlreadyRegisteredException extends Exception{

    public SpaceXfoodAlreadyRegisteredException(String foodName) {
        super(String.format("Food with name %s already registered in the SpaceX system.", foodName));
    }
}
