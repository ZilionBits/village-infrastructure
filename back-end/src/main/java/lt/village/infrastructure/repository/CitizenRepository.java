package lt.village.infrastructure.repository;

import lt.village.infrastructure.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Citizen findByFirstName(String firstName);
    Citizen findByLastName(String lastName);
}
