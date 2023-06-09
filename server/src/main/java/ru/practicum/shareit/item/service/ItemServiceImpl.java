package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableUtil;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Пользователь не найден");
        });
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, user)));
    }

    @Override
    @Transactional
    public ItemDto editItem(ItemDto itemDto, Long ownerId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Предмет не найден"));
        if (!ownerId.equals(item.getOwner().getUserId())) {
            throw new ValidationException("Право редактирования есть только у владельца");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Предмет не найден"));
        ItemDto itemDto = ItemMapper.toItemDto(item);

        itemDto.setComments(commentRepository.findAll());
        if (item.getOwner().getUserId().equals(userId)) {
            itemDto.setNextBooking(getNextBooking(itemId));
            itemDto.setLastBooking(getLastBooking(itemId));
        }
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getOwnerItems(Long ownerId, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, "itemId");

        List<Item> items = itemRepository.findAllByOwnerUserIdOrderByItemId(ownerId, pageable);
        Collection<ItemDto> itemsDto = new ArrayList<>();


        for (Item i : items) {
            ItemDto itemDto = ItemMapper.toItemDto(i);
            itemDto.setLastBooking(getLastBooking(i.getItemId()));
            itemDto.setNextBooking(getNextBooking(i.getItemId()));

            itemsDto.add(itemDto);

        }

        return itemsDto;
    }

    @Override
    public Collection<ItemDto> search(String text, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, "itemId");
        List<Item> items =  itemRepository.search(text, pageable);

        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemMapper.toItemDto(i));
        }
        return itemsDto;
    }

    @Override
    public CommentDto postComment(Long userId, CommentDto commentDto, Long itemId) {
        commentDto.setCreated(LocalDateTime.now());
        List<Booking> bookings = bookingRepository.getAllByBookerUserIdAndItem_ItemIdAndStatusAndEndIsBefore(
                userId,
                itemId,
                BookingStatus.APPROVED,
                commentDto.getCreated());
        if (bookings.isEmpty()) {
            throw new BookingException("Комментарий может оставлять пользователь, который бронировал вещь");
        }

        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("Предмет не найден"));

        Comment comment = commentRepository.save(ItemMapper.toComment(commentDto, user, item));
        return ItemMapper.toCommentDto(comment);
    }

    public Booking getLastBooking(Long itemId) {
        return bookingRepository.getFirstByItemItemIdAndStatusAndStartIsBeforeOrderByStartDesc(
                itemId,
                BookingStatus.APPROVED,
                LocalDateTime.now()
        );
    }

    public Booking getNextBooking(Long itemId) {
        return bookingRepository.getFirstByItemItemIdAndStatusAndStartIsAfterOrderByStartAsc(
                itemId,
                BookingStatus.APPROVED,
                LocalDateTime.now()
        );
    }
}