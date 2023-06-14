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
class UserServiceTest {

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
            .email("updated@mail.ru")
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
    void testCreateUser() {
        userService.create(userDto3);
        List<UserDto> users = userService.getAll();

        assertEquals(3, users.size(),
                "Users size expected to be 2, but was " + users.size());
        assertEquals(userDto3.getName(), users.get(2).getName(),
                "Name expected to be " + userDto.getName() + ", but was " + users.get(2).getName());
    }

    @Test
    @DisplayName("Тест редактирования пользователя")
    void testEditUser() {
        userService.edit(userUpdate, 1L);

        assertEquals(userUpdate.getName(), userService.get(1L).getName(),
                "Name expected to be " + userUpdate.getName());
        assertEquals(userUpdate.getEmail(), userService.get(1L).getEmail(),
                "Email expected to be " + userUpdate.getEmail());

        assertThrows(ObjectNotFoundException.class,
                () -> userService.edit(userUpdate, 99L));
    }

    @Test
    @DisplayName("Тест удаления пользователя")
    void testDeleteUser() {
        userService.delete(1L);
        List<UserDto> users = userService.getAll();

        assertEquals(1, users.size(),
                "Users size expected to be 1, but was " + users.size());
        assertEquals(2L, users.get(0).getId(),
                "User ID expected to be 2L, but was " + users.get(0).getId());
    }

    @Test
    @DisplayName("Тест получения пользователя по ID")
    void testGetUser() {
        UserDto user = userService.get(1L);

        assertEquals(userDto.getName(), user.getName(),
                "Name expected to be " + userDto.getName() + ", but was " + user.getName());
        assertEquals(userDto.getEmail(), user.getEmail(),
                "Email expected to be " + userDto.getEmail() + ", but was " + user.getEmail());
        assertThrows(ObjectNotFoundException.class,
                () -> userService.get(99L));
    }

    @Test
    @DisplayName("Тест получения списка всех пользователей")
    void testGetUsers() {
        List<UserDto> users = userService.getAll();

        assertEquals(2, users.size(),
                "Users size expected to be 2, but was " + users.size());
    }

}
