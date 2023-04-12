package ru.practicum.shareit.item.model;


import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Item {
    @Generated
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private boolean available;
    private Long owner;
    private ItemRequest request;

}
