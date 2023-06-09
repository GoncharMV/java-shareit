package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    @NotBlank(message = "Необходим текст комментария")
    @NotEmpty(message = "Необходим текст комментария")
    private String text;
    private String authorName;
    private LocalDateTime created;
}
