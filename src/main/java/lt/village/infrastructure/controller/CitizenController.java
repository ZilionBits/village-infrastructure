package lt.village.infrastructure.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lt.village.infrastructure.rest.dto.CitizenDto;
import lt.village.infrastructure.service.CitizenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/citizen")
public class CitizenController {

    private static final String MESSAGE_STRING = "message";
    private final CitizenService citizenService;

    @PostMapping
    public ResponseEntity<Map<String, String>> addCitizen(@RequestBody @Valid CitizenDto citizenDto) {
        try {
            var response = citizenService.addCitizen(citizenDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(MESSAGE_STRING, response));
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(MESSAGE_STRING, ex.getMessage()));
        }
    }
}
