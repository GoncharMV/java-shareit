package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingOutputDto addBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                       @Valid @RequestBody BookingInputDto booking) {
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto updateStatus(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId,
                                        @RequestParam boolean approved
                                   ) {
        return bookingService.updateStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                      @PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutputDto> getBookersBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookersBooking(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getOwnersBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getOwnersBooking(userId, state);
    }

}
