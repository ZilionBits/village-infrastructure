package lt.village.infrastructure.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CitizenDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @NotEmpty
    private BigDecimal cashBalance;

}
