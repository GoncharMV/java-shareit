package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDto booking) {
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam boolean approved
                                   ) {
        return bookingService.updateStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookersBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookersBooking(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnersBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getOwnersBooking(userId, state);
    }

}
