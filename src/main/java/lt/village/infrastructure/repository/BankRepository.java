package lt.village.infrastructure.repository;

import lt.village.infrastructure.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
