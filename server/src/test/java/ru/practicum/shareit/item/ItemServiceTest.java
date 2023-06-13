package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemServiceTest {

    @Autowired
    private final UserService userService;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final BookingService bookingService;

    private final UserDto owner = UserDto.builder()
            .id(1L)
            .name("Dr.Bashir")
            .email("bashirBond@mail.ru")
            .build();

    private final UserDto wrongUser = UserDto.builder()
            .id(2L)
            .name("Captain Chair")
            .email("makeItSo@mail.ru")
            .build();

    private final ItemDto item1 = ItemDto.builder()
            .id(2L)
            .name("Tea")
            .description("Earl Grey. Hot")
            .available(true)
            .build();

    private final ItemDto item = ItemDto.builder()
            .id(1L)
            .name("Tricorder")
            .description("Can perform environmental scans, data recording, and data analysis")
            .available(true)
            .build();

    private ItemDto initItem;

    private final ItemDto itemUpdate = ItemDto.builder()
            .name("new Name")
            .description("even cooler description")
            .available(false)
            .build();

    @BeforeEach
    void setUsers() {
        userService.create(owner);
        userService.create(wrongUser);
        initItem = itemService.addItem(item1, owner.getId());
    }

    @Test
    @DisplayName("Тест добавления item")
    void addItemTest() {
        ItemDto newItem = itemService.addItem(item, owner.getId());

        assertEquals(item.getName(), newItem.getName());
        assertEquals(item.getId(), newItem.getId());
        assertEquals(item.getDescription(), newItem.getDescription());
        assertEquals(true, newItem.getAvailable());

    }

    @Test
    void addItemWrongUserTest() {
        assertThrows(ObjectNotFoundException.class,
                () -> itemService.addItem(item, 99L));
    }

    @Test
    @DisplayName("Тест редактирования item")
    void editItemTest() {
        ItemDto newItem = itemService.editItem(itemUpdate, owner.getId(), initItem.getId());

        assertEquals(initItem.getId(), newItem.getId());
        assertEquals(itemUpdate.getName(), newItem.getName());
        assertEquals(itemUpdate.getDescription(), newItem.getDescription());
        assertEquals(false, newItem.getAvailable());
    }

    @Test
    @DisplayName("Тест попытка редактирования не владельцом")
    void editItemWrongUserTest() {
        assertThrows(ValidationException.class,
                () -> itemService.editItem(itemUpdate, wrongUser.getId(), initItem.getId()));
    }

    @Test
    @DisplayName("Тест получение item по ID")
    void getItemTest() {
        ItemDto itemAdded = itemService.addItem(item, owner.getId());
        ItemDto itemGet = itemService.getItem(itemAdded.getId(), owner.getId());
        assertEquals(itemAdded.getId(), itemGet.getId());
        assertEquals(itemAdded.getName(), itemGet.getName());
    }

    @Test
    @DisplayName("Тест попытка получения несуществующего объекта")
    void getItemWrongIdTest() {
        assertThrows(ObjectNotFoundException.class,
                () -> itemService.getItem(99L, owner.getId()));
    }

    @Test
    @DisplayName("Тест получения всех предметов пользователя")
    void getOwnerItemsTest() {
        itemService.addItem(item, owner.getId());
        itemService.addItem(item1, owner.getId());

        Collection<ItemDto> items = itemService.getOwnerItems(owner.getId(), 0, 10);

        assertEquals(2, items.size());
    }

    @Test
    @DisplayName("Тест поиска предметов")
    void searchTest() {
        itemService.addItem(item, owner.getId());
        itemService.addItem(item1, owner.getId());

        Collection<ItemDto> items = itemService.search(item.getName(), 0, 10);
        assertEquals(item.getName(), items.iterator().next().getName());
        assertEquals(1, items.size());
    }

    @Test
    void searchBlankTextTest() {
        itemService.addItem(item, owner.getId());
        itemService.addItem(item1, owner.getId());

        Collection<ItemDto> items = itemService.search("", 0, 10);
        assertEquals(0, items.size());
    }

    @Test
    @DisplayName("Тест попытка оставить комментарий без бронирования ")
    void postCommentWrongUserTest() {
        ItemDto itemDto = itemService.addItem(item, owner.getId());

        CommentDto commentForItem = CommentDto.builder()
                .text("Оч крутой трайкойдер")
                .build();

        assertThrows(BookingException.class,
                () -> itemService.postComment(wrongUser.getId(), commentForItem, itemDto.getId()));
    }

    @Test
    @DisplayName("Тест пост комментария")
    void postCommentTest() {
        LocalDateTime testTime = LocalDateTime.now();
        ItemDto itemDto = itemService.addItem(item, owner.getId());

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(itemDto.getId())
                .start(testTime.minusMinutes(10))
                .end(testTime.minusMinutes(1))
                .build();

        BookingDto booking = bookingService.addBooking(bookingDto, wrongUser.getId());
        bookingService.updateStatus(booking.getId(), owner.getId(), true);


        CommentDto commentForItem = CommentDto.builder()
                .text("Оч крутой трайкойдер")
                .build();
        CommentDto comment = itemService.postComment(booking.getBooker().getId(), commentForItem, itemDto.getId());

        assertEquals(commentForItem.getText(), comment.getText());

    }


}
