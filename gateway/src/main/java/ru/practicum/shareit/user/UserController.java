package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    public final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto requestDto) {
        return userClient.createUser(requestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> editUser(@PathVariable Long userId,
                                           @RequestBody UserRequestDto requestDto) {
        return userClient.editUser(userId, requestDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        return userClient.deleteUser(userId);
    }

}
