package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import java.util.List;

public interface BookingService {

    BookingOutputDto addBooking(BookingInputDto bookingInputDto, Long userId);

    BookingOutputDto updateStatus(Long bookingId, Long ownerId, boolean approved);

    BookingOutputDto getBooking(Long bookingId, Long userId);

    List<BookingOutputDto> getBookersBooking(Long bookerId, String state);

    List<BookingOutputDto> getOwnersBooking(Long ownerId, String state);

}
