package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> errorHandler(IllegalArgumentException e) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", String.format("Unknown state: UNSUPPORTED_STATUS"));
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

}
