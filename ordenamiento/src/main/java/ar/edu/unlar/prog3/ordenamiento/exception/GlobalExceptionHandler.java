package ar.edu.unlar.prog3.ordenamiento.exception;

import ar.edu.unlar.prog3.ordenamiento.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSortCriteriaException.class)
    public ResponseEntity<ErrorResponse> manejarCriterioInvalido(InvalidSortCriteriaException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                exception.getCriterioRecibido(),
                exception.getCriteriosAceptados()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
