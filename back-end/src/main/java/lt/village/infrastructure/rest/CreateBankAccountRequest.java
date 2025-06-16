package lt.village.infrastructure.rest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lt.village.infrastructure.rest.dto.BankDto;
import lt.village.infrastructure.rest.dto.CitizenDto;

import java.math.BigDecimal;

@Data
public class CreateBankAccountRequest {

    @NotNull
    private BankDto bank;
    @NotNull
    private CitizenDto citizen;
    @NotNull
    private BigDecimal initialBalance;
}
