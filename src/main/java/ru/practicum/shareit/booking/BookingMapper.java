package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public final class BookingMapper {

    private BookingMapper() {
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User user, BookingStatus status) {
        return  new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                status
        );
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking b : bookings) {
            bookingDtos.add(toBookingDto(b));
        }
        return bookingDtos;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getBookingId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus().toString(),
                toBookingItemDto(booking.getItem()),
                toBookerDto(booking.getBooker())
                );
    }

    public static BookerDto toBookerDto(User user) {
        return new BookerDto(user.getUserId());
    }

    public static BookingItemDto toBookingItemDto(Item item) {
        return new BookingItemDto(item.getItemId(), item.getName());
    }

    public static BookingForItem toBookingForItemDto(Booking booking) {
        BookingForItem bookingDto = new BookingForItem();
            if (booking != null) {
                bookingDto.setId(booking.getBookingId());
                bookingDto.setBookerId(booking.getBooker().getUserId());
            } else {
                bookingDto = null;
            }
            return bookingDto;

    }
}
