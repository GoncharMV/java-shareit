package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private BookingStatus status;
    private BookingItemDto item;
    private UserShortDto booker;
}
