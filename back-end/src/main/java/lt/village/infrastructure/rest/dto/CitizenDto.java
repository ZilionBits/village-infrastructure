package lt.village.infrastructure.rest.dto;


import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CitizenDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @DecimalMin(value = "0", message = "Cash balance can not be negative.")
    private BigDecimal cashBalance;

}
