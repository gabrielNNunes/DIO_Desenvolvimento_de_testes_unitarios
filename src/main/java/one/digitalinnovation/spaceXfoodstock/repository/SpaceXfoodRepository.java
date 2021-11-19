package one.digitalinnovation.spaceXfoodstock.repository;

import one.digitalinnovation.spaceXfoodstock.entity.SpaceXfood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpaceXfoodRepository extends JpaRepository<SpaceXfood, Long> {

    Optional<SpaceXfood> findByName(String name);
}
