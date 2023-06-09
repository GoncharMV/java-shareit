package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private final Item item = Item.builder()
            .itemId(1L)
                .name("Clock")
                .description("To show the time")
                .available(true)
                .build();

    @Test
    @DisplayName("Проверка поиска")
    void testItemSearch() {
        itemRepository.save(item);

        Collection<Item> items = itemRepository.search("clock", Pageable.ofSize(10));
        assertEquals(1, items.size());
    }
}
