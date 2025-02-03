package com.rafbel94.libridex_api.service;

import java.util.List;

import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.model.UserLoginDTO;
import com.rafbel94.libridex_api.model.UserRegisterDTO;

public interface UserService {
    User findById(Integer id);
    User findByEmail(String email);
    User registerUser(User user);
    void updateUserToken(String token, User user);
    void deleteUserById(Integer id);
    List<User> findAllUsers();
    List<String> validateUser(UserRegisterDTO user);
    List<String> validateLogin(UserLoginDTO user);
    UserRegisterDTO toRegisterDTO(User user);
    User toEntity(UserRegisterDTO userRegisterDTO);
}
