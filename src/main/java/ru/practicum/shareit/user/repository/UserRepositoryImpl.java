package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.ObjectNotFoundException;
import ru.practicum.shareit.validation.ValidateException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userStorage = new HashMap<>();
    private long generateId = 1;

    @Override
    public User getUser(Long id) {
        return userStorage.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.values();
    }

    @Override
    public User create(User user) {
        String newEmail = user.getEmail();
        if (isEmailExists(newEmail)) {
            throw new ValidateException("Пользователь с таким e-mail уже существует");
        }
        user.setId(generateId++);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User edit(User newUser, Long userId) {
        if (userStorage.containsKey(userId)) {
            if (newUser.getName() != null) {
                userStorage.get(userId).setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                if (isEmailExists(newUser.getEmail()) && !userStorage.get(userId).getEmail().equals(newUser.getEmail())) {
                    throw new ValidateException("Пользователь с таким e-mail уже существует");
                }
                userStorage.get(userId).setEmail(newUser.getEmail());
            }
        } else {
            throw new ObjectNotFoundException("Пользователя несуществует");
        }
        return getUser(userId);
    }

    @Override
    public void delete(Long id) {
        userStorage.remove(id);
    }

    private Long generateId() {
        return (long) userStorage.size() + 1;
    }

    private boolean isEmailExists(String newEmail) {
        return getAll().stream().map(User::getEmail).anyMatch(email -> email.equals(newEmail));
    }
}
