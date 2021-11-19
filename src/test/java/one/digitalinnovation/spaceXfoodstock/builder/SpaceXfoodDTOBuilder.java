package one.digitalinnovation.spaceXfoodstock.builder;

import lombok.Builder;
import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.enums.SpaceXfoodType;

@Builder
public class SpaceXfoodDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Precooked meat";

    @Builder.Default
    private String brand = "Spam";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private SpaceXfoodType type = SpaceXfoodType.CANNED;

    public SpaceXfoodDTO toFoodDTO() {
        return new SpaceXfoodDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }
}
