package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;

    public Item(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
