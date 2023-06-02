package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RequestServiceTest {

    @Autowired
    private final UserService userService;
    @Autowired
    private final RequestService requestService;

    private final ItemRequestDto request = ItemRequestDto
            .builder()
            .id(1L)
            .description("a smaller vacuum cleaner to vacuum a small vacuum cleaner")
            .requestorId(1L)
            .build();

    private final UserDto user = UserDto
            .builder()
            .id(1L)
            .name("Monica")
            .email("geller@mail.ru")
            .build();

    @BeforeEach
    void setRequest() {
        userService.create(user);
    }

    @Test
    @DisplayName("Тест создать запрос")
    void addRequestTest() {
        ItemRequestDto newRequest = requestService.addRequest(request, user.getId());
        assertEquals(request.getId(), newRequest.getId());
        assertThrows(ObjectNotFoundException.class,
                () -> requestService.addRequest(request, 99L));
    }
}
