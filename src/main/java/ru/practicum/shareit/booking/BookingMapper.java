package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public final class BookingMapper {

    private BookingMapper() {
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User user, BookingStatus status) {
        return Booking.builder()
                .bookingId(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(status)
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getBookingId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus().toString())
                .item(toBookingItemDto(booking.getItem()))
                .booker(toUserShortDto(booking.getBooker()))
                .build();
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking b : bookings) {
            bookingDtos.add(toBookingDto(b));
        }
        return bookingDtos;
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

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getUserId());
    }
}
