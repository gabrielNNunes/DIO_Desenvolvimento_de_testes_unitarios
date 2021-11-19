package one.digitalinnovation.spaceXfoodstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceXfoodType {



    TUBE("Food in tube"),
    DEHYDRATED("Dehydrated food"),
    THERMOSTABILIZED("Thermostabilized food"),
    FRESH_or_NATURAL("Fresh food or in its natural form"),
    LIQUID("Liquid food"),
    CANNED("canned food"),
    SEED("Seed for future planting");


    private final String description;
}
