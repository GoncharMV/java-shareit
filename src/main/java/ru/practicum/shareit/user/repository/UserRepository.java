package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    User getUser(Long id);

    Collection<User> getAll();

    User create(User user);

    User edit(User user, Long userId);

    void delete(Long id);
}
