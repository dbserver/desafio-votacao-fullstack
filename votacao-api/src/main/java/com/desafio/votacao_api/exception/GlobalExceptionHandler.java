package com.desafio.votacao_api.exception;

import com.desafio.votacao_api.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST endpoints.
 *
 * Centralizes exception handling across all controllers and provides
 * standardized error responses to API clients.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles all not-found domain exceptions.
     *
     * @param exception the caught exception
     * @param request the current web request
     * @return ResponseEntity with status 404 (Not Found)
     */
    @ExceptionHandler({
            PautaNotFoundException.class,
            SessaoVotacaoNotFoundException.class,
            VotoNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
            RuntimeException exception,
            WebRequest request) {
        LOGGER.warn("Resource not found: {}", exception.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                exception.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles CPF validation exceptions from external eligibility check.
     *
     * @param exception the caught exception
     * @return ResponseEntity with status 404 (Not Found)
     */
    @ExceptionHandler({CpfNotFoundException.class, UnableToVoteException.class})
    public ResponseEntity<Map<String, Object>> handleCpfValidationExceptions(RuntimeException exception) {
        LOGGER.warn("CPF validation failed: {}", exception.getMessage());

        Map<String, Object> error = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", exception.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidCpfException.
     *
     * @param exception the caught exception
     * @param request the current web request
     * @return ResponseEntity with status 400 (Bad Request)
     */
    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCpfException(
            InvalidCpfException exception,
            WebRequest request) {
        LOGGER.warn("Invalid CPF input: {}", exception.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid CPF",
                exception.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ErrorOnCreatePautaException.
     *
     * @param exception the caught exception
     * @param request the current web request
     * @return ResponseEntity with status 400 (Bad Request)
     */
    @ExceptionHandler(ErrorOnCreatePautaException.class)
    public ResponseEntity<ErrorResponseDTO> handleErrorOnCreatePautaException(
            ErrorOnCreatePautaException exception,
            WebRequest request) {
        LOGGER.warn("Business error while creating agenda: {}", exception.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error Creating Agenda",
                exception.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors from request body validation.
     *
     * @param exception the caught exception
     * @param request the current web request
     * @return ResponseEntity with status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            WebRequest request) {
        String errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        LOGGER.warn("Validation error: {}", errors);
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles SessionClosedException.
     *
     * @param exception the caught exception
     * @param request the current web request
     * @return ResponseEntity with status 409 (Conflict)
     */
    @ExceptionHandler(SessionClosedException.class)
    public ResponseEntity<ErrorResponseDTO> handleSessionClosedException(
            SessionClosedException exception,
            WebRequest request) {
        LOGGER.warn("Voting session closed: {}", exception.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Voting Session Closed",
                exception.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles invalid URL/path/query parameter types (e.g., malformed UUID).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            WebRequest request) {
        String message = "Invalid value for parameter '" + exception.getName() + "'.";
        LOGGER.warn("Type mismatch for parameter '{}': {}", exception.getName(), exception.getValue());

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request Parameter",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing query parameters and returns which parameter is missing.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            WebRequest request) {
        String message = "Required parameter '" + exception.getParameterName() + "' is missing.";
        LOGGER.warn("Missing request parameter '{}'.", exception.getParameterName());

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Missing Request Parameter",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles malformed JSON payloads and invalid enum values.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            WebRequest request) {
        LOGGER.warn("Malformed request body: {}", exception.getMessage());

        String message = "Request body is invalid or has malformed JSON.";
        Throwable cause = exception.getMostSpecificCause();
        String causeMessage = cause != null ? cause.getMessage() : "";

        if (causeMessage.contains("Missing required creator property")) {
            String fieldName = extractQuotedValue(causeMessage);
            message = fieldName.isBlank()
                    ? "Request body is missing required fields."
                    : "Required field '" + fieldName + "' is missing.";
        } else if (causeMessage.contains("Cannot deserialize value")) {
            String fieldName = extractFieldFromReferenceChain(causeMessage);
            message = fieldName.isBlank()
                    ? "One or more fields contain invalid values."
                    : "Invalid value for field '" + fieldName + "'.";
        } else if (causeMessage.contains("Required request body is missing")) {
            message = "Request body is required.";
        }

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Malformed Request Body",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String extractQuotedValue(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        Matcher matcher = Pattern.compile("'([^']+)'").matcher(value);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extractFieldFromReferenceChain(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        Matcher matcher = Pattern.compile("\\\"([^\\\"]+)\\\"\\]\\)").matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        }

        Matcher fallback = Pattern.compile("\\\"([^\\\"]+)\\\"").matcher(value);
        return fallback.find() ? fallback.group(1) : "";
    }

    /**
     * Handles bean validation errors outside of @RequestBody validation.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(
            ConstraintViolationException exception,
            WebRequest request) {
        String message = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        LOGGER.warn("Constraint violation: {}", message);

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles database constraint violations (e.g., duplicated unique keys).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            WebRequest request) {
        LOGGER.warn("Data integrity violation: {}", exception.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Data Conflict",
                "Operation violates a data integrity rule.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles domain validation errors.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException exception,
            WebRequest request) {
        LOGGER.warn("Invalid argument: {}", exception.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Input",
                exception.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions.
     *
     * @param exception the caught exception
     * @param request the current web request
     * @return ResponseEntity with status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception exception,
            WebRequest request) {
        LOGGER.error("Unexpected server error", exception);

        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please contact support if the problem persists.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




