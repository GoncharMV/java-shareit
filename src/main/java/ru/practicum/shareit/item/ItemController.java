package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItemDto item) {
        return itemService.addItem(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                        @RequestBody ItemDto item,
                        @PathVariable Long itemId) {
        return itemService.editItem(item, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return itemService.getOwnerItems(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text") String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                            @Valid @RequestBody CommentDto comment,
                            @PathVariable Long itemId) {
        return itemService.postComment(userId, comment, itemId);
    }
}
