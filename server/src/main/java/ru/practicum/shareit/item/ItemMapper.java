package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public final class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getUserId())
                .requestId(item.getRequestId())
                .build();
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return Item.builder()
                .itemId(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .requestId(itemDto.getRequestId())
                .build();
    }

    public static List<ItemDto> toItemDtoList(List<Item> itemList) {
        List<ItemDto> items = new ArrayList<>();
        for (Item i : itemList) {
            items.add(toItemDto(i));
        }
        return items;
    }

    public static Comment toComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .author(user)
                .item(item)
                .createDate(commentDto.getCreated())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreateDate())
                .build();
    }

}
