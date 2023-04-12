package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItemDto item) {
        return itemService.add();
    }

    @PatchMapping
    public ItemDto edit(@Valid @RequestBody ItemDto item) {
        return itemService.edit();
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Long itemId) {
        return itemService.get(itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(Long ownerId) {
        return itemService.getOwnerItems(ownerId);
    }

/*    @GetMapping
    public void search() {

    }*/
}
