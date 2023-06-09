package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingJsonTest {
    @Autowired
    JacksonTester<BookingDto> json;

    private final LocalDateTime testStart = LocalDateTime.of(2023, 3,
            6, 17, 10, 1);
    private final LocalDateTime testEnd = LocalDateTime.of(2023, 3,
            7, 17, 10, 1);

    @Test
    void testBookingJson() throws Exception {
        BookingDto bookingDto = BookingDto
                .builder()
                .id(1L)
                .start(testStart)
                .end(testEnd)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(testStart.toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(testEnd.toString());
    }

}
