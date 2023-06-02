package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceTest {

    @Autowired
    private final UserService userService;
    @Autowired
    private final ItemService itemService;

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

    private final ItemDto itemUpdateAvailable = ItemDto.builder()
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
        ItemDto newItem = itemService.editItem(itemUpdateAvailable, owner.getId(), initItem.getId());

        assertEquals(initItem.getId(), newItem.getId());
        assertEquals(false, newItem.getAvailable());
    }

    @Test
    @DisplayName("Тест попытка редактирования не владельцом")
    void editItemWrongUserTest() {
        assertThrows(ValidationException.class,
                () -> itemService.editItem(itemUpdateAvailable, wrongUser.getId(), initItem.getId()));
    }

}
