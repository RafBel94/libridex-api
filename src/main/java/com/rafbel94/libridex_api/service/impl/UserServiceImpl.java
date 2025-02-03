package com.rafbel94.libridex_api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.model.UserLoginDTO;
import com.rafbel94.libridex_api.model.UserRegisterDTO;
import com.rafbel94.libridex_api.repository.UserRepository;
import com.rafbel94.libridex_api.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    @Qualifier("userRepository")
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        UserBuilder builder = null;

        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(email);
            builder.password(user.getPassword());
            builder.authorities(new SimpleGrantedAuthority(user.getRole()));
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        return builder.build();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void updateUserToken(String token, User user) {
        user.setToken(token);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Validates user and returns a List<String> containing found errors
    @Override
    public List<String> validateUser(UserRegisterDTO user) {
        List<String> errors = new ArrayList<>();
        if (!user.getPassword().matches(user.getRepeatPassword()))
            errors.add("The passwords doesn't match");
        if (user.getPassword().isBlank() || user.getRepeatPassword().isBlank())
            errors.add("The password and repeat password fields are mandatory");
        if (userRepository.findByEmail(user.getEmail()) != null)
            errors.add("The email is already being used");
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            errors.add("The email format is invalid");
        if (user.getEmail().isBlank())
            errors.add("The email is mandatory");
        return errors;
    }

    @Override
    public List<String> validateLogin(UserLoginDTO userLoginDTO) {
        List<String> errors = new ArrayList<>();
        if (userLoginDTO.getEmail().isBlank())
            errors.add("The email is mandatory");
        if (userLoginDTO.getPassword().isBlank())
            errors.add("The password is mandatory");

        if (!errors.isEmpty())
            return errors;

        if (findByEmail(userLoginDTO.getEmail()) == null){
            errors.add("Theres no users with that email");
            return errors;
        } else if (!new BCryptPasswordEncoder().matches(userLoginDTO.getPassword(), findByEmail(userLoginDTO.getEmail()).getPassword()))
            errors.add("Wrong credentials");
        
        return errors;
    }

    // MODEL MAPPERS
    @Override
    public UserRegisterDTO toRegisterDTO(User user) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserRegisterDTO.class);
    }

    @Override
    public User toEntity(UserRegisterDTO userRegisterDTO) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userRegisterDTO, User.class);
    }

}
