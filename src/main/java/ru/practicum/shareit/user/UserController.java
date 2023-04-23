package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info("Создан пользователь: " + user);
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto edit(@RequestBody UserDto user, @PathVariable Long userId) {
        log.info("Обновлен пользователь: " + user);
        return userService.edit(user, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Удален пользователь: " + userId);
        userService.delete(userId);
    }
}
