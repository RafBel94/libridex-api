package com.rafbel94.libridex_api.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafbel94.libridex_api.entity.User;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Serializable> {
    User findById(Integer id);
    User findByEmail(String email);
}
