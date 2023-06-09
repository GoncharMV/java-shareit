package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%') ) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND i.available = true " +
            "ORDER BY i.itemId")
    List<Item> search(String text, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByOwnerUserIdOrderByItemId(Long ownerId, Pageable pageable);
}
