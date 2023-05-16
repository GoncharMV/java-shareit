package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingOutputDto addBooking(BookingInputDto bookingInputDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Пользователь не найден");
        });

        Item item = itemRepository.findById(bookingInputDto.getItemId()).orElseThrow(() -> {
            throw new ObjectNotFoundException("Предмет не найден");
        });

        if (!item.getAvailable()) {
            throw new BookingException("Предмет недоступен для бронирования");
        }
        if (bookingInputDto.getStart().equals(bookingInputDto.getEnd())) {
            throw new BookingException("Конец бронирования не должен равняться началу бронирования");
        }

        if (bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())) {
            throw new BookingException("Конец бронирования не может быть раньше начала бронирования");
        }

        if (userId.equals(item.getOwner().getUserId())) {
            throw new ObjectNotFoundException("Невозможно забронировать свою вещь");
        }

        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingInputDto,
                                                    item,
                                                    user,
                                                    BookingStatus.WAITING
                                                    )
        );
        return BookingMapper.toOutputDto(booking);
    }

    @Override
    @Transactional
    public BookingOutputDto updateStatus(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new BookingException("Бронирование не найдено");
        });
        Item item = booking.getItem();
        if (!item.getOwner().getUserId().equals(ownerId)) {
            throw new ObjectNotFoundException("Нет доступа для изменения статуса бронирования");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingException("Статус бронирования окончателен");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toOutputDto(booking);
    }

    @Override
    public BookingOutputDto getBooking(Long bookingId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
                    throw new ObjectNotFoundException("Бронирование не найдено");
                });

        Item item = booking.getItem();
        if (!Objects.equals(booking.getBooker().getUserId(), userId) && !Objects.equals(item.getOwner().getUserId(), userId)) {
            throw new ObjectNotFoundException("Нет доступа для просмотра бронирования");
        }

        return BookingMapper.toOutputDto(booking);
    }

    @Override
    public List<BookingOutputDto> getBookersBooking(Long bookerId, String state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                return BookingMapper.toBookingDtoList(bookingRepository.findAllByBookerUserIdOrderByStartDesc(bookerId));
            case "CURRENT":
                bookings = bookingRepository.findByBookerCurrent(bookerId, LocalDateTime.now());
                return BookingMapper.toBookingDtoList(bookings);
            case "PAST":
                bookings = bookingRepository.findByBookerPast(bookerId, LocalDateTime.now());
                return BookingMapper.toBookingDtoList(bookings);
            case "FUTURE":
                bookings = bookingRepository.findByBookerFuture(bookerId, LocalDateTime.now());
                return BookingMapper.toBookingDtoList(bookings);
            case "WAITING":
                bookings = bookingRepository.findByBookerAndStatus(bookerId, BookingStatus.WAITING);
                return BookingMapper.toBookingDtoList(bookings);
            case "REJECTED":
                bookings = bookingRepository.findByBookerAndStatus(bookerId, BookingStatus.REJECTED);
                return BookingMapper.toBookingDtoList(bookings);
            default: throw new BookingException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingOutputDto> getOwnersBooking(Long ownerId, String state) {

        userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                return BookingMapper.toBookingDtoList(bookingRepository.findByItemOwnerUserIdOrderByStartDesc(ownerId));
            case "CURRENT":
                bookings = bookingRepository.findByItemOwnerCurrent(ownerId, LocalDateTime.now());
                return BookingMapper.toBookingDtoList(bookings);
            case "PAST":
                bookings = bookingRepository.findByItemOwnerPast(ownerId, LocalDateTime.now());
                return BookingMapper.toBookingDtoList(bookings);
            case "FUTURE":
                bookings = bookingRepository.findByItemOwnerFuture(ownerId, LocalDateTime.now());
                return BookingMapper.toBookingDtoList(bookings);
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerAndStatus(ownerId, BookingStatus.WAITING);
                return BookingMapper.toBookingDtoList(bookings);
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerAndStatus(ownerId, BookingStatus.REJECTED);
                return BookingMapper.toBookingDtoList(bookings);
            default: throw new BookingException("Unknown state: " + state);
        }
    }
}
