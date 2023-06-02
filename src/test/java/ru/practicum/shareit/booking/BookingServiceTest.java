package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingServiceTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BookingServiceImpl bookingService;

    private final UserDto owner = UserDto
            .builder()
            .id(1L)
            .name("donald")
            .email("duck@mail.ru")
            .build();

    private final UserDto booker = UserDto
            .builder()
            .id(2L)
            .name("Monterey Jack")
            .email("ilovecheese@mail.ru")
            .build();

    private final ItemDto item = ItemDto.builder()
            .id(1L)
            .name("Cheese")
            .description("I'm not real hungry")
            .available(true)
            .build();

    private final ItemDto itemNotAvailable = ItemDto.builder()
            .id(2L)
            .name("Mouse")
            .description("You are what ya eat, ya know.")
            .available(false)
            .build();

    private LocalDateTime testTime;

    private BookingDto bookingDto;

    @BeforeEach
    void setBooking() {
        testTime = LocalDateTime.now();
        bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(item.getId())
                .start(testTime.plusSeconds(1))
                .end(testTime.plusSeconds(10))
                .build();
        userService.create(owner);
        userService.create(booker);
        itemService.addItem(item, owner.getId());
        itemService.addItem(itemNotAvailable, owner.getId());
    }

    @Test
    @DisplayName("Тест добавить бронирование")
    void addBookingTest() {
        BookingDto booking = bookingService.addBooking(bookingDto, booker.getId());

        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItemId(), booking.getItem().getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    @DisplayName("Тест бронирование несуществующей вещи")
    void addBookingWrongItemTest() {
        bookingDto.setItemId(99L);
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.addBooking(bookingDto, booker.getId()));
    }

    @Test
    @DisplayName("Тест попытка бронирования своей вещи; несуществующий пользователь")
    void addBookingWrongUserTest() {
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.addBooking(bookingDto, owner.getId()));
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.addBooking(bookingDto, 99L));
    }

    @Test
    @DisplayName("Тест попытка создания бронирования с невалидной датой начала/конца")
    void addBookingWrongDataTest() {
        bookingDto.setStart(testTime.plusSeconds(10));
        assertThrows(BookingException.class,
                () -> bookingService.addBooking(bookingDto, booker.getId()));
        bookingDto.setEnd(testTime.minusMinutes(10L));
        assertThrows(BookingException.class,
                () -> bookingService.addBooking(bookingDto, booker.getId()));
    }

    @Test
    @DisplayName("Тест поменять статус бронирования")
    void updateStatusTest() {
        BookingDto booking = bookingService.addBooking(bookingDto, booker.getId());
        BookingDto bookingApproved = bookingService.updateStatus(booking.getId(), owner.getId(), true);
        assertEquals(BookingStatus.APPROVED, bookingApproved.getStatus());
    }

    @Test
    @DisplayName("Тест попытка изменения статуса несуществующего бронирования")
    void updateStatusWrongBookingTest() {
        bookingService.addBooking(bookingDto, booker.getId());
        assertThrows(BookingException.class,
                () -> bookingService.updateStatus(99L, owner.getId(), true));
    }

    @Test
    @DisplayName("Тест попытка изменения статуса не владельцом")
    void updateStatusWrongUserTest() {
        bookingService.addBooking(bookingDto, booker.getId());
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.updateStatus(bookingDto.getId(), booker.getId(), true));
    }

    @Test
    @DisplayName("Тест получение бронирования по ID")
    void getBookingTest() {
        BookingDto booking = bookingService.addBooking(bookingDto, booker.getId());
        BookingDto bookingGetByBooker = bookingService.getBooking(booking.getId(), booker.getId());
        BookingDto bookingGetByOwner = bookingService.getBooking(booking.getId(), owner.getId());

        assertEquals(bookingGetByOwner.getId(), bookingGetByBooker.getId());
        assertEquals(bookingGetByOwner.getItem().getName(), bookingGetByBooker.getItem().getName());
        assertEquals(bookingDto.getId(), bookingGetByBooker.getId());
    }

    @Test
    @DisplayName("Тест попытка получения бронирования по ID невалидный пользователь")
    void getBookingByIdWrongUser() {
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBooking(bookingDto.getId(), 99L));
        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBooking(99L, owner.getId()));

        UserDto someUser = UserDto
                .builder()
                .id(3L)
                .name("someUser")
                .email("someUser@mail.ru")
                .build();

        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBooking(bookingDto.getId(), someUser.getId()));
    }
}
