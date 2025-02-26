package lt.village.infrastructure.repository;

import lt.village.infrastructure.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
