package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorUserId(Long userId);

    @Query(value = "select r from ItemRequest as r " +
            "where r.requestor.userId not in ?1")
    List<ItemRequest> findAllExceptOwners(Long userId, Pageable pageable);
}
