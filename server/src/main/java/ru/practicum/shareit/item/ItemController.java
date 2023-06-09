package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto add(@RequestHeader(name = USER_ID_HEADER) Long ownerId,
            @RequestBody ItemDto item) {
        return itemService.addItem(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(@RequestHeader(name = USER_ID_HEADER) Long ownerId,
                        @RequestBody ItemDto item,
                        @PathVariable Long itemId) {
        return itemService.editItem(item, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader(name = USER_ID_HEADER) Long userId,
            @PathVariable Long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getOwnerItems(@RequestHeader(name = USER_ID_HEADER) Long ownerId,
                                             @RequestParam(name = "from", defaultValue = "0") int from,
                                             @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.getOwnerItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text") String text,
                                      @RequestParam(name = "from", defaultValue = "0") int from,
                                      @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(name = USER_ID_HEADER) Long userId,
                            @RequestBody CommentDto comment,
                            @PathVariable Long itemId) {
        return itemService.postComment(userId, comment, itemId);
    }
}
