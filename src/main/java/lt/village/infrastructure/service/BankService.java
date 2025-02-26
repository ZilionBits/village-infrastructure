package lt.village.infrastructure.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lt.village.infrastructure.model.BankAccount;
import lt.village.infrastructure.repository.BankAccountRepository;
import lt.village.infrastructure.repository.BankRepository;
import lt.village.infrastructure.repository.CitizenRepository;
import lt.village.infrastructure.rest.dto.BankDto;
import lt.village.infrastructure.rest.dto.CitizenDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@Service
public class BankService {

    private BankRepository bankRepository;
    private CitizenRepository citizenRepository;
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public String createBankAccount(BankDto bank, CitizenDto citizen, BigDecimal initialBalance) {

        var selectedBank = bankRepository.findById(bank.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Bank with ID: %s not found", bank.getId())));
        var client = citizenRepository.findById(citizen.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Citizen with ID: %s not found", citizen.getId())));

        if (client.getCashBalance().compareTo(initialBalance) < 0) {
            return String.format("Citizen %s %s has insufficient funds (%.2f) to deposit initial balance (%.2f)",
                    client.getFirstName(), client.getLastName(), client.getCashBalance(), initialBalance);
        } else {
            client.setCashBalance(client.getCashBalance().subtract(initialBalance));
        }

        var newBankAccount = new BankAccount();
        newBankAccount.setBalance(initialBalance);
        newBankAccount.setCitizen(client);
        newBankAccount.setBank(selectedBank);
        bankAccountRepository.save(newBankAccount);

        return String.format("Bank Account with account number: (%s) and initial balance: %.2f created",
                newBankAccount.getAccountNumber(), initialBalance);
    }

}
