package ru.neoflex.vacation_pay.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.neoflex.vacation_pay.model.ErrorResponse;

import java.security.InvalidParameterException;
import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class VacationPayControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof DateTimeParseException || ex.getMessage().contains("java.time.LocalDate")) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(BAD_REQUEST.value(), "Invalid date format"));
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(BAD_REQUEST.value(), "Malformed JSON request"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}
