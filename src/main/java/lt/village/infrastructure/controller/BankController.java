package lt.village.infrastructure.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lt.village.infrastructure.rest.CreateBankAccountRequest;
import lt.village.infrastructure.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/bank")
public class BankController {

    private static final String MESSAGE_STRING = "message";
    private BankService bankService;

    @PostMapping
    public ResponseEntity<Map<String,String>> createBankAccount(@RequestBody CreateBankAccountRequest createBankAccountRequest) {
        try {
            var response = bankService
                    .createBankAccount(createBankAccountRequest.getBank(), createBankAccountRequest.getCitizen(), createBankAccountRequest.getInitialBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE_STRING, response));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MESSAGE_STRING, ex.getMessage()));
        }
    }

}
