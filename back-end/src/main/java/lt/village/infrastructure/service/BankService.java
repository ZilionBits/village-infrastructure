package lt.village.infrastructure.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lt.village.infrastructure.model.Bank;
import lt.village.infrastructure.model.BankAccount;
import lt.village.infrastructure.model.Citizen;
import lt.village.infrastructure.repository.BankAccountRepository;
import lt.village.infrastructure.repository.BankRepository;
import lt.village.infrastructure.repository.CitizenRepository;
import lt.village.infrastructure.rest.SendBalanceBetweenBankAccountRequest;
import lt.village.infrastructure.rest.dto.BankDto;
import lt.village.infrastructure.rest.dto.CitizenDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Transactional
@AllArgsConstructor
@Service
public class BankService {

    private BankRepository bankRepository;
    private CitizenRepository citizenRepository;
    private BankAccountRepository bankAccountRepository;

    public String createBankAccount(BankDto bank, CitizenDto citizen, BigDecimal initialBalance) {

        var selectedBank = verifyBank(bank);
        var client = verifyCitizen(citizen);

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

    public String closeBankAccount(CitizenDto citizen, String accountNumber) throws IllegalAccessException {
        var client = verifyCitizen(citizen);
        var selectedBankAccount = verifyAccountNumber(accountNumber);

        if ( isAccountNumberBelongToCitizen(client, selectedBankAccount) ) {
            transferFromBankAccountToCitizen(client, selectedBankAccount);
            bankAccountRepository.delete(selectedBankAccount);
        }

        return String.format("Bank Account ( %s ) for holder %s %s successfully closed.",
                accountNumber, client.getFirstName(), client.getLastName());
    }

    public String sendBalanceFromBankAccountToBankAccount(SendBalanceBetweenBankAccountRequest transferRequest) throws IllegalAccessException {
        var clientPayer = verifyCitizen(transferRequest.getPayer());
        var clientReceiver = verifyCitizen(transferRequest.getReceiver());
        var selectedBankAccountPayer = verifyAccountNumber(transferRequest.getBankAccountPayer());
        var selectedBankAccountReceiver = verifyAccountNumber(transferRequest.getBankAccountReceiver());

        if ( isAccountNumberBelongToCitizen(clientPayer, selectedBankAccountPayer) &&
                isAccountNumberBelongToCitizen(clientReceiver, selectedBankAccountReceiver) &&
                checkIfBalanceEnoughToTransfer(selectedBankAccountPayer, transferRequest.getAmount()) ) {

            selectedBankAccountPayer.setBalance(selectedBankAccountPayer.getBalance().subtract(transferRequest.getAmount()));
            selectedBankAccountReceiver.setBalance(selectedBankAccountReceiver.getBalance().add(transferRequest.getAmount()));

        }

        return String.format("Payment: ( %.2f ) successfully proceeded.", transferRequest.getAmount());

    }

    protected Citizen verifyCitizen(CitizenDto citizen) {
        return citizenRepository.findById(citizen.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Citizen with ID: %s not found", citizen.getId())));
    }

    protected Bank verifyBank(BankDto bank) {
        return bankRepository.findById(bank.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Bank with ID: %s not found", bank.getId())));
    }

    protected BankAccount verifyAccountNumber(String accountNumber) {
        return bankAccountRepository.findAll().stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Bank account with number: (%s) not exist.", accountNumber)));
    }

    protected boolean isAccountNumberBelongToCitizen(Citizen citizen, BankAccount account) throws IllegalAccessException {
        if( citizen.getBankAccounts().stream()
                .anyMatch(acc -> acc.getAccountNumber().equals(account.getAccountNumber())) ) {
            return true;
        } else {
            throw new IllegalAccessException(String.format("Fraud detected, given Account Number: ( %s ) not belong to Citizen: ( %s %s )",
                    account.getAccountNumber(), citizen.getFirstName(), citizen.getLastName()));
        }
    }

    protected boolean checkIfBalanceEnoughToTransfer(BankAccount payerAccount, BigDecimal transferAmount) {
        if ( transferAmount.compareTo(payerAccount.getBalance()) <= 0 ) {
            return true;
        } else {
            throw new IllegalArgumentException(String.format("Citizen %s %s has insufficient funds ( %.2f ) to send ( %.2f )",
                    payerAccount.getCitizen().getFirstName(), payerAccount.getCitizen().getLastName(), payerAccount.getBalance(), transferAmount));
        }
    }

    protected void transferFromBankAccountToCitizen(Citizen client, BankAccount closedAccount) {
        client.setCashBalance(client.getCashBalance().add(closedAccount.getBalance()));
        citizenRepository.save(client);
    }


}
