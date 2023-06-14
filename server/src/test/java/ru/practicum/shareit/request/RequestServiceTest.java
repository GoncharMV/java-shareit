package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.CustomServerErrorException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Collection;

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

    private final ItemRequestDto request = ItemRequestDto.builder()
            .id(1L)
            .description("a smaller vacuum cleaner to vacuum a small vacuum cleaner")
            .requestorId(1L)
            .build();

    private final UserDto requestor = UserDto.builder()
            .id(1L)
            .name("Monica")
            .email("geller@mail.ru")
            .build();

    private final UserDto user2 = UserDto.builder()
            .id(2L)
            .name("Joey")
            .email("doesntsharefood@mail.ru")
            .build();

    private final ItemRequestDto request2 = ItemRequestDto.builder()
            .id(2L)
            .description("more food")
            .requestorId(2L)
            .build();

    @BeforeEach
    void setRequest() {
        userService.create(requestor);
        userService.create(user2);
    }

    @Test
    @DisplayName("Тест создать запрос")
    void testAddRequest() {
        ItemRequestDto newRequest = requestService.addRequest(request, requestor.getId());
        assertEquals(request.getId(), newRequest.getId(),
                "ID expected to be " + request.getId() + ", but was " + newRequest.getId());
        assertThrows(ObjectNotFoundException.class,
                () -> requestService.addRequest(request, 99L));
    }

    @Test
    @DisplayName("Тест получения всех запросов пользователя")
    void testGetUsersRequests() {
        requestService.addRequest(request, requestor.getId());
        Collection<ItemRequestDto> requests = requestService.getUserRequests(requestor.getId());

        assertEquals(request.getId(), requests.iterator().next().getId(),
                "ID expected to be " + request.getId() + ", but was " + requests.iterator().next().getId());
        assertEquals(request.getDescription(), requests.iterator().next().getDescription(),
                "Description expected to be " + request.getDescription()
                        + " but was " + requests.iterator().next().getDescription());
        assertThrows(ObjectNotFoundException.class,
                () -> requestService.getUserRequests(99L));
    }

    @Test
    @DisplayName("Тест получения запроса по ID")
    void testGetRequestById() {
        ItemRequestDto addedRequest = requestService.addRequest(request, requestor.getId());
        ItemRequestDto newRequest = requestService.getRequestById(request.getId(), requestor.getId());
        assertEquals(addedRequest.getId(), newRequest.getId(),
                "ID expected to be " + request.getId() + ", but was " + newRequest.getId());
        assertEquals(addedRequest.getDescription(), newRequest.getDescription(),
                "Description expected to be " + request.getDescription()
                        + " but was " + newRequest.getDescription());
        assertEquals(addedRequest.getCreated(), newRequest.getCreated());
    }

    @Test
    @DisplayName("Тест попытки получения запроса по ID")
    void testGetRequestByIdWrong() {
        requestService.addRequest(request, requestor.getId());
        assertThrows(CustomServerErrorException.class,
                () -> requestService.getRequestById(request.getId(), 99L));
        assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequestById(99L, requestor.getId()));

    }


    @Test
    @DisplayName("Тест получения всех запросов")
    void testGetAllRequests() {
        requestService.addRequest(request, requestor.getId());
        requestService.addRequest(request2, user2.getId());
        Collection<ItemRequestDto> requests = requestService
                .getAllRequests(0, 20, requestor.getId());

        assertEquals(1, requests.size(),
                "Requests size expected to be 1, but was " + requests.size());
        assertEquals(user2.getId(), requests.iterator().next().getRequestorId());
    }

    @Test
    @DisplayName("Тест попытка получения запросов от несуществующего пользователя")
    void testGetAllRequestsWrongUser() {
        assertThrows(CustomServerErrorException.class,
                () -> requestService.getAllRequests(0, 20, 99L));
    }
}
