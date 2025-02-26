package lt.village.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @PrePersist
    private void generateAccountNumber() {
        StringBuilder accountNumberFormatted = new StringBuilder(
                UUID.randomUUID().toString().replace("-", "")
                .substring(0,16).toUpperCase());
        this.accountNumber = accountNumberFormatted
                .insert(4,' ').insert(9,' ').insert(14,' ')
                .toString();
    }
}
