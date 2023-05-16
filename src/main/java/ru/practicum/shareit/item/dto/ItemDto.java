package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotNull(message = "Необходимо название предмета")
    @NotEmpty(message = "Необходимо название предмета")
    private String name;
    @NotNull(message = "Необходимо описание предмета")
    private String description;
    @NotNull
    private Boolean available;
    private Long ownerId;
    private Long requestId;
    private BookingForItem lastBooking;
    private BookingForItem nextBooking;
    private List<CommentDto> comments;

    public void setLastBooking(Booking booking) {
        this.lastBooking = BookingMapper.toBookingForItemDto(booking);
    }

    public void setNextBooking(Booking booking) {
        this.nextBooking = BookingMapper.toBookingForItemDto(booking);
    }

    public void setComments(List<Comment> comments) {
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment c : comments) {
            commentsDto.add(ItemMapper.toCommentDto(c));
        }
        this.comments = commentsDto;
    }

}
