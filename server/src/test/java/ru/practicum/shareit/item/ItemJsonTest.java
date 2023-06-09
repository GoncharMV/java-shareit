package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemJsonTest {
        @Autowired
        JacksonTester<ItemDto> json;

        @Test
        void testItemJson() throws Exception {
            ItemDto itemDto = ItemDto.builder()
                    .id(1L)
                    .name("screwdriver")
                    .available(true)
                    .description("u can shot u can fix")
                    .build();

            JsonContent<ItemDto> result = json.write(itemDto);

            assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
            assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("screwdriver");
            assertThat(result).extractingJsonPathStringValue("$.description")
                    .isEqualTo("u can shot u can fix");
            assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        }
}
