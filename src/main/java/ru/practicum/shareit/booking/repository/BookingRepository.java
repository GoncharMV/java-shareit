package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository  extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerUserIdOrderByStartDesc(Long userId, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.booker.userId = ?1 and ?2 between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findByBookerCurrent(Long bookerId, LocalDateTime time, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.booker.userId = ?1 and ?2 > b.end " +
            "order by b.start desc")
    List<Booking> findByBookerPast(Long bookerId, LocalDateTime time, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.booker.userId = ?1 and ?2 < b.start " +
            "order by b.start desc")
    List<Booking> findByBookerFuture(Long bookerId, LocalDateTime time, Pageable pageable);

    @Query(value = "select b from Booking b " +
            "where b.booker.userId = ?1 and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findByBookerAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByItemOwnerUserIdOrderByStartDesc(long ownerId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.start < ?2 " +
            "and b.end > ?2 " +
            "and b.item.owner.userId = ?1 " +
            "order by b.start")
    List<Booking> findByItemOwnerCurrent(long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.end < ?2 " +
            "and b.item.owner.userId = ?1 " +
            "order by b.start desc")
    List<Booking> findByItemOwnerPast(long userId, LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.start > ?2 " +
            "and b.item.owner.userId = ?1 " +
            "order by b.start desc")
    List<Booking> findByItemOwnerFuture(long userId, LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.status = ?2 " +
            "and b.item.owner.userId = ?1 " +
            "order by b.status desc")
    List<Booking> findByItemOwnerAndStatus(long userId, BookingStatus status, Pageable pageable);

    Booking getFirstByItemItemIdAndStatusAndStartIsBeforeOrderByStartDesc(
            Long itemId,
            BookingStatus status,
            LocalDateTime currentTime);

    Booking getFirstByItemItemIdAndStatusAndStartIsAfterOrderByStartAsc(
            Long itemId,
            BookingStatus status,
            LocalDateTime currentTime);

    List<Booking> getAllByBookerUserIdAndItem_ItemIdAndStatusAndEndIsBefore(
            Long bookerId,
            Long itemId,
            BookingStatus bookingStatus,
            LocalDateTime currentTime);
}
