package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemControllerTest {

    private final String header = "X-Sharer-User-Id";

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final UserDto owner = UserDto.builder()
            .id(1L)
            .name("Dr.Bashir")
            .email("bashirBond@mail.ru")
            .build();

    private final ItemDto item = ItemDto.builder()
            .id(1L)
            .name("Tricorder")
            .description("Can perform environmental scans, data recording, and data analysis")
            .available(true)
            .build();

    ItemDto item2 = ItemDto.builder()
            .id(2L)
            .name("Triname")
            .description("desc2")
            .build();

    @Test
    @DisplayName("Тест получения item по ID")
    void getItemTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(item);

        mvc.perform(get("/items/1")
                .header(header, owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())));
    }

    @Test
    @DisplayName("Тест получения всех items пользователя")
    void getOwnerItemsTest() throws Exception {
                when(itemService.getOwnerItems(anyLong()))
                .thenReturn(List.of(item, item2));

        mvc.perform(get("/items")
                        .header(header, owner.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item.getName())))
                .andExpect(jsonPath("$.[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$.[1].id", is(item2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(item2.getName())))
                .andExpect(jsonPath("$.[1].description", is(item2.getDescription())));
    }

    @Test
    @DisplayName("Тест поиска вещей")
    void searchTest() throws Exception {
        when(itemService.search(anyString()))
                .thenReturn(List.of(item, item2));

        mvc.perform(get("/items/search")
                        .header(header, owner.getId())
                        .param("text", "Tri"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item.getName())))
                .andExpect(jsonPath("$.[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$.[1].id", is(item2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(item2.getName())))
                .andExpect(jsonPath("$.[1].description", is(item2.getDescription())));
    }

    @Test
    @DisplayName("Тест добавление комментария")
    void postCommentTest() throws Exception {
        CommentDto comment = CommentDto
                .builder()
                .text("Nice one")
                .build();
        when(itemService.postComment(anyLong(), any(), anyLong()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, owner.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())));
    }
}
