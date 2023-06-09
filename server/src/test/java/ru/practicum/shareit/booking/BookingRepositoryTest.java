package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;

    private User booker;

    private Item item;

    private Booking bookingFuture;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .userId(1L)
                .name("Robert")
                .email("bert.r@mail.ru")
                .build();

        booker = User.builder()
                .userId(2L)
                .name("Karina")
                .email("karkar@mail.ru")
                .build();

        item = Item.builder()
                .itemId(1L)
                .name("Clock")
                .description("To show the time")
                .owner(owner)
                .available(true)
                .build();

        bookingFuture = Booking.builder()
                .bookingId(1L)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(500))
                .build();
    }

    @Test
    @DisplayName("Тест найти будущие бронирования")
    void testFindByBookerFuture() {
        userRepository.save(owner);
        itemRepository.save(item);
        userRepository.save(booker);
        bookingRepository.save(bookingFuture);

        List<Booking> bookings = bookingRepository.findByBookerFuture(booker.getUserId(),
                LocalDateTime.now(),
                Pageable.ofSize(10));

        assertEquals(1, bookings.size());
        assertEquals(bookingFuture.getBookingId(), bookings.get(0).getBookingId());
    }

    @Test
    @DisplayName("Тест получения current бронирований владельца")
    void testFindByOwnerCurrent() {
        userRepository.save(owner);
        itemRepository.save(item);
        userRepository.save(booker);
        bookingRepository.save(bookingFuture);

        List<Booking> bookings = bookingRepository.findByItemOwnerCurrent(owner.getUserId(),
                LocalDateTime.now(),
                Pageable.ofSize(10));
        assertEquals(0, bookings.size());
    }

    @Test
    @DisplayName("Тест получения бронирований владельца опреледенного статуса")
    void testFindByOwnerStatus() {
        userRepository.save(owner);
        itemRepository.save(item);
        userRepository.save(booker);
        bookingRepository.save(bookingFuture);

        List<Booking> bookings = bookingRepository.findByItemOwnerAndStatus(owner.getUserId(),
                BookingStatus.WAITING,
                Pageable.ofSize(10));
        assertEquals(0, bookings.size());
    }

}
