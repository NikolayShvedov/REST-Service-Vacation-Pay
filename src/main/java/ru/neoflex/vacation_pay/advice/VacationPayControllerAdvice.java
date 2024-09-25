package ru.neoflex.vacation_pay.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.neoflex.vacation_pay.model.ErrorResponse;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class VacationPayControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(BindException ex) {
        var errorMessage = "Validation and Binding error";
        var errors = new HashMap<String, Object>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        if (ex.getCause() instanceof DateTimeParseException || ex.getMessage().contains("java.time.LocalDate")) {
            errors.put("startVacationDate", "Invalid date format. Expected format: yyyy-MM-dd");
        }

        log.error(errorMessage + ": ", ex);
        var errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(errorMessage)
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null));
    }
}