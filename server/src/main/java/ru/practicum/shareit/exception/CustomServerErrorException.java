package ru.practicum.shareit.exception;

public class CustomServerErrorException extends RuntimeException {

    public CustomServerErrorException(String message) {
        super(message);
    }
}
