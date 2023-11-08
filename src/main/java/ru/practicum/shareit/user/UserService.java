package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, int id);

    UserDto getUserById(int id);

    void deleteUser(int id);

    List<UserDto> getAllUsers();

}
