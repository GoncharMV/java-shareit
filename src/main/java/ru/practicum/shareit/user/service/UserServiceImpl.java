package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAll() {
        Collection<User> users = userRepository.getAll();
        Collection<UserDto> dtoUsers = new ArrayList<>();
        for (User u : users) {
            dtoUsers.add(UserMapper.toUserDto(u));
        }
        return dtoUsers;
    }

    @Override
    public UserDto get(Long userId) {
        return UserMapper.toUserDto(userRepository.getUser(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userRepository.create(user);
        return UserMapper.toUserDto(userRepository.getUser(user.getId()));
    }

    @Override
    public UserDto edit(UserDto userDto, Long userId) {
        userRepository.edit(UserMapper.toUser(userDto), userId);
        return get(userId);
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }
}
