package ru.practicum.shareit.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectNotFound(final ObjectNotFoundException e) {
        return Map.of("error", e.getMessage());
    }
}
