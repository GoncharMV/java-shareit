package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("one")
            .email("one@mail.ru")
            .build();

    private final UserDto userDto2 = UserDto.builder()
            .id(2L)
            .name("second")
            .email("second@mail.ru")
            .build();

    private final UserDto userDto3 = UserDto.builder()
            .id(3L)
            .name("third")
            .email("third@mail.ru")
            .build();

    private final UserDto userUpdate = UserDto.builder()
            .name("updated")
            .build();

    @Autowired
    private final UserService userService;

    @BeforeEach
    void setUsers() {
        userService.create(userDto);
        userService.create(userDto2);
    }

    @Test
    @DisplayName("Тест создания пользователя")
    void createTest() {
        userService.create(userDto3);
        List<UserDto> users = userService.getAll();

        assertEquals(3, users.size());
        assertEquals("third", users.get(2).getName());
    }

    @Test
    @DisplayName("Тест редактирования пользователя")
    void editTest() {
        userService.edit(userUpdate, 1L);

        assertEquals("updated", userService.get(1L).getName());
        assertEquals("one@mail.ru", userService.get(1L).getEmail());

        assertThrows(ObjectNotFoundException.class,
                () -> userService.edit(userUpdate, 99L));
    }

    @Test
    @DisplayName("Тест удаления пользователя")
    void deleteTest() {
        userService.delete(1L);
        List<UserDto> users = userService.getAll();

        assertEquals(1, users.size());
        assertEquals(2L, users.get(0).getId());
    }

    @Test
    @DisplayName("Тест получения пользователя по ID")
    void getUserTest() {
        UserDto user = userService.get(1L);

        assertEquals("one", user.getName());
        assertEquals("one@mail.ru", user.getEmail());
    }

    @Test
    @DisplayName("Тест получения списка всех пользователей")
    void getAllTest() {
        List<UserDto> users = userService.getAll();

        assertEquals(2, users.size());
    }

}
