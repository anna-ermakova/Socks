package pro.sky.socks.controllers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.webjars.NotFoundException;
import pro.sky.socks.exception.EmptyStockException;
import pro.sky.socks.exception.FileProcessingException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error)->{
            String fielName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fielName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    public String handleFileNotFoundException(FileNotFoundException exception) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleotFoundExceptions(NotFoundException exception) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileProcessingException.class)
    public String handleFileProcessingException(FileProcessingException exception) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyStockException.class)
    public ResponseEntity<String> handleInvalidException(EmptyStockException emptyStockException) {
        return ResponseEntity.badRequest().body(emptyStockException.getMessage());
    }
}
