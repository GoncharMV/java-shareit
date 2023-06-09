package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto edit(UserDto userDto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));

         if (userDto.getName() != null) {
             user.setName(userDto.getName());
         }
         if (userDto.getEmail() != null) {
             user.setEmail(userDto.getEmail());
         }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto get(Long userId) {
       User user =  userRepository.findById(userId).orElseThrow(()
               -> new ObjectNotFoundException("Пользователь не найден"));

       return UserMapper.toUserDto(user);
    }
}
