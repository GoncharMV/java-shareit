package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.dto.UserShortDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    private Long id;
    @FutureOrPresent(message = "Дата начала не должна быть в прошлом")
    @NotNull(message = "Необходимо указать дату начала бронирования")
    private LocalDateTime start;
    @Future(message = "Окончание бронирования не может быть в прошлом")
    @NotNull(message = "Необходимо указать дату окончания бронирования")
    private LocalDateTime end;
    @NotNull(message = "Необходим id объекта для брони")
    private Long itemId;
    private BookingStatus status;
    private BookingItemDto item;
    private UserShortDto booker;
}
