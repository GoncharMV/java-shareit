package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(BookingDto bookingDto, Long userId);

    BookingDto updateStatus(Long bookingId, Long ownerId, boolean approved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getBookersBooking(Long bookerId, String state);

    List<BookingDto> getOwnersBooking(Long ownerId, String state);

}
