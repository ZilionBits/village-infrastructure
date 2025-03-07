package lt.village.infrastructure.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lt.village.infrastructure.model.Citizen;
import lt.village.infrastructure.repository.CitizenRepository;
import lt.village.infrastructure.rest.dto.CitizenDto;
import org.springframework.stereotype.Service;

@Transactional
@AllArgsConstructor
@Service
public class CitizenService {

    private CitizenRepository citizenRepository;

    public String addCitizen(CitizenDto citizenDto) {
        if (isCitizenExist(citizenDto)) {
            throw new EntityExistsException(String.format("Citizen %s %s already exists",
                    citizenDto.getFirstName(), citizenDto.getLastName()));

        } else {
            var citizen = new Citizen();
            citizen.setFirstName(citizenDto.getFirstName());
            citizen.setLastName(citizenDto.getLastName());
            citizen.setEmail(citizenDto.getEmail());
            citizen.setPhone(citizenDto.getPhone());
            citizen.setCashBalance(citizenDto.getCashBalance());
            citizenRepository.save(citizen);
            return String.format("Successfully added citizen: %s %s",
                    citizenDto.getFirstName(), citizenDto.getLastName());
        }
    }

    protected boolean isCitizenExist(CitizenDto citizenDto) {
        return citizenRepository.findByFirstName(citizenDto.getFirstName()) != null
                && citizenRepository.findByLastName(citizenDto.getLastName()) != null;
    }

}
