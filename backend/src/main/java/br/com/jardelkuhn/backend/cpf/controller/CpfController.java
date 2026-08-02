package br.com.jardelkuhn.backend.cpf.controller;

import br.com.jardelkuhn.backend.cpf.businessrule.CpfValidator;
import br.com.jardelkuhn.backend.cpf.dto.CpfRequestDTO;
import br.com.jardelkuhn.backend.cpf.dto.CpfResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/cpf")
@CrossOrigin(origins = "*")
public class CpfController implements ICpfController {
    @Override
    public ResponseEntity<CpfResponseDTO> validate(CpfRequestDTO cpfRequestDTO) {
        if (!CpfValidator.isCpf(cpfRequestDTO.cpf())) {
            throw new EntityNotFoundException();
        }

        boolean canVote = new Random().nextBoolean();

        if (canVote) {
            return ResponseEntity.ok(new CpfResponseDTO(CpfResponseDTO.Status.ABLE_TO_VOTE));
        } else {
            return ResponseEntity.ok(new CpfResponseDTO(CpfResponseDTO.Status.UNABLE_TO_VOTE));
        }
    }
}
