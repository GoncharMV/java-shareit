package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;
    @Transient
    private List<Item> items;

}
