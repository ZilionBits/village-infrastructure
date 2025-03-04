package lt.village.infrastructure.rest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lt.village.infrastructure.rest.dto.CitizenDto;

import java.math.BigDecimal;

@Data
public class SendBalanceBetweenBankAccountRequest {

    @NotNull
    private CitizenDto payer;
    @NotNull
    private String bankAccountPayer;
    @DecimalMin(value = "0.01", message = "Can't send less than 0.01")
    private BigDecimal amount;
    @NotNull
    private CitizenDto receiver;
    @NotNull
    private String bankAccountReceiver;
}
