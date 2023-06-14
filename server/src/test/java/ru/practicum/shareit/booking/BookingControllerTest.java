package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingControllerTest {

    private final String header = "X-Sharer-User-Id";

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Pants")
            .description("Those pants you wanted altered are ready to be picked up.")
            .available(true)
            .build();

    private final UserDto booker = UserDto
            .builder()
            .id(1L)
            .name("Elim")
            .email("notaspy@mail.ru")
            .build();

    private LocalDateTime testTime;

    private BookingDto bookingDto;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setData() {
        testTime = LocalDateTime.now();
        bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(itemDto.getId())
                .start(testTime.plusSeconds(1))
                .end(testTime.plusSeconds(12))
                .build();
    }

    @Test
    @DisplayName("Тест добавления бронирования")
    void testAddBooking() throws Exception {
        User user = UserMapper.toUser(booker);
        Item item = ItemMapper.toItem(this.itemDto, user);
        Booking booking = BookingMapper.toBooking(bookingDto, item, user, BookingStatus.WAITING);
        when(bookingService.addBooking(any(), anyLong()))
                .thenReturn(BookingMapper.toBookingDto(booking));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, 3L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    @DisplayName("Тест изменения статуса бронирования")
    void testUpdateStatus() throws Exception {
        User user = UserMapper.toUser(booker);
        Item item = ItemMapper.toItem(this.itemDto, user);
        Booking booking = BookingMapper.toBooking(bookingDto, item, user, BookingStatus.WAITING);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(BookingMapper.toBookingDto(booking));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, 3L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    @DisplayName("Тест получения информации по бронирования")
    void testGetBooking() throws Exception {
        User user = UserMapper.toUser(booker);
        Item item = ItemMapper.toItem(this.itemDto, user);
        Booking booking = BookingMapper.toBooking(bookingDto, item, user, BookingStatus.WAITING);
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(BookingMapper.toBookingDto(booking));

        mvc.perform(get("/bookings/1")
                        .header(header, 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    @DisplayName("Тест получения бронирований заказчиком")
    void testGetBookersBooking() throws Exception {
        when(bookingService.getBookersBooking(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header(header, booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus())));
    }

}
