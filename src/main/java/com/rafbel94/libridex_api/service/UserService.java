package com.rafbel94.libridex_api.service;

import java.util.List;

import com.rafbel94.libridex_api.entity.User;

public interface UserService {
    User findById(Integer id);
    User findByEmail(String email);
    User addUser(User user);
    void deleteUserById(Integer id);
    List<User> findAllUsers();
}
