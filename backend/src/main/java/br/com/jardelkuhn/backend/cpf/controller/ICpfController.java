package br.com.jardelkuhn.backend.cpf.controller;

import br.com.jardelkuhn.backend.cpf.dto.CpfRequestDTO;
import br.com.jardelkuhn.backend.cpf.dto.CpfResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(
        name = "Validar CPF",
        description = "Endpoints responsáveis pela validação de CPF para verificar se um associado está apto a votar"
)
public interface ICpfController {

    @Operation(
            summary = "Valida se um CPF está apto para votação",
            description = """
                    Recebe um CPF e verifica sua validade através de um serviço externo simulado.
                    
                    - Caso o CPF seja inválido, a API retorna HTTP 404 (Not Found).
                    - Caso o CPF seja válido, retorna HTTP 200 com a informação se o associado está apto ou não para votar.
                    
                    Possíveis retornos:
                    - ABLE_TO_VOTE: CPF válido e associado apto a votar.
                    - UNABLE_TO_VOTE: CPF válido, porém associado não apto a votar.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "CPF válido e status de votação retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CpfResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "ABLE_TO_VOTE"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF inválido ou não encontrado no serviço de validação",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "message": "CPF inválido"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "CPF informado possui formato inválido"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CpfResponseDTO> validate(@Valid @RequestBody CpfRequestDTO cpfRequestDTO);
}
