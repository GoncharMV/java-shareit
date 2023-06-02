package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private final String header = "X-Sharer-User-Id";

    private final ItemRequestDto requestDto = ItemRequestDto
            .builder()
            .id(1L)
            .description("a smaller vacuum cleaner to vacuum a small vacuum cleaner")
            .requestorId(1L)
            .build();

    private final UserDto requestor = UserDto
            .builder()
            .id(1L)
            .name("Monica")
            .email("geller@mail.ru")
            .build();

    private final ItemRequest request = RequestMapper.toItemRequest(requestDto, UserMapper.toUser(requestor));

    @Test
    void addRequestTest() throws Exception {
        when(requestService.addRequest(any(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, requestor.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getRequestId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(request.getRequestor().getUserId()), Long.class));
    }

    @Test
    void getAllByRequestorTest() throws Exception {
        ItemRequestDto itemRequest1 = ItemRequestDto.builder()
                .requestorId(UserMapper.toUser(requestor).getUserId())
                .description("1")
                .id(1L).build();
        ItemRequestDto itemRequest2 = ItemRequestDto.builder()
                .requestorId(UserMapper.toUser(requestor).getUserId())
                .description("2")
                .id(2L).build();
        when(requestService.getUserRequests(anyLong()))
                .thenReturn(List.of(itemRequest1, itemRequest2));

        mvc.perform(get("/requests")
                        .header(header, requestor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",
                        is(itemRequest1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description",
                        is(itemRequest1.getDescription())))
                .andExpect(jsonPath("$.[0].requestorId",
                        is(itemRequest1.getRequestorId()), Long.class))
                .andExpect(jsonPath("$.[1].id",
                        is(itemRequest2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].description",
                        is(itemRequest2.getDescription())))
                .andExpect(jsonPath("$.[1].requestorId",
                        is(itemRequest2.getRequestorId()), Long.class));

    }

    @Test
    void getByRequestIdTest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header(header, requestor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getRequestId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(request.getRequestor().getUserId()), Long.class));
    }
}