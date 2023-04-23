package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Booking {
    private LocalDate date;
    private boolean isConfirmed;
}
