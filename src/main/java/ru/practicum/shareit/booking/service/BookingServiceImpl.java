package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableUtil;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
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
    public BookingDto addBooking(BookingDto bookingDto, Long userId) {

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> {
            throw new ObjectNotFoundException("Предмет не найден");
        });

        if (!item.getAvailable()) {
            throw new BookingException("Предмет недоступен для бронирования");
        }
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingException("Конец бронирования не должен равняться началу бронирования");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingException("Конец бронирования не может быть раньше начала бронирования");
        }

        if (userId.equals(item.getOwner().getUserId())) {
            throw new ObjectNotFoundException("Невозможно забронировать свою вещь");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Пользователь не найден");
        });
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto,
                                                    item,
                                                    user,
                                                    BookingStatus.WAITING
                                                    )
        );
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long bookingId, Long ownerId, boolean approved) {
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

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
                    throw new ObjectNotFoundException("Бронирование не найдено");
                });

        Item item = booking.getItem();
        if (!Objects.equals(booking.getBooker().getUserId(), userId)
                && !Objects.equals(item.getOwner().getUserId(), userId)) {
            throw new ObjectNotFoundException("Нет доступа для просмотра бронирования");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookersBooking(Long bookerId, BookingState state, int from, int size) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

        List<Booking> bookings;

        Pageable pageable = PageableUtil.pageManager(from, size, "start");

        switch (state) {
            case ALL:
                return BookingMapper.toBookingDtoList(bookingRepository
                        .findAllByBookerUserIdOrderByStartDesc(bookerId, pageable));
            case CURRENT:
                bookings = bookingRepository.findByBookerCurrent(bookerId, LocalDateTime.now(), pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case PAST:
                bookings = bookingRepository.findByBookerPast(bookerId, LocalDateTime.now(), pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case FUTURE:
                bookings = bookingRepository.findByBookerFuture(bookerId, LocalDateTime.now(), pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatus(bookerId, BookingStatus.WAITING, pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatus(bookerId, BookingStatus.REJECTED, pageable);
                return BookingMapper.toBookingDtoList(bookings);
            default: throw new BookingException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDto> getOwnersBooking(Long ownerId, BookingState state, int from, int size) {

        userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

        List<Booking> bookings;

        Pageable pageable = PageableUtil.pageManager(from, size, "start");

        switch (state) {
            case ALL:
                return BookingMapper.toBookingDtoList(bookingRepository
                        .findByItemOwnerUserIdOrderByStartDesc(ownerId, pageable));
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerCurrent(ownerId, LocalDateTime.now(), pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case PAST:
                bookings = bookingRepository.findByItemOwnerPast(ownerId, LocalDateTime.now(), pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerFuture(ownerId, LocalDateTime.now(), pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case WAITING:
                bookings = bookingRepository.findByItemOwnerAndStatus(ownerId, BookingStatus.WAITING, pageable);
                return BookingMapper.toBookingDtoList(bookings);
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerAndStatus(ownerId, BookingStatus.REJECTED, pageable);
                return BookingMapper.toBookingDtoList(bookings);
            default: throw new BookingException("Unknown state: " + state);
        }
    }
}
