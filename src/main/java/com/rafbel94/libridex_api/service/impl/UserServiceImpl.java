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

    /**
     * Loads the user by their email.
     *
     * @param email the email of the user to be loaded
     * @return the UserDetails of the loaded user
     * @throws UsernameNotFoundException if the user is not found
     */
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

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to be found
     * @return the user if found, or null if not found
     */
    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to be found
     * @return the user if found, or null if not found
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registers a new user.
     *
     * @param user the user to be registered
     * @return the registered user
     */
    @Override
    public User registerUser(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Updates the user's token.
     *
     * @param token the new token
     * @param user  the user whose token is to be updated
     */
    @Override
    public void updateUserToken(String token, User user) {
        user.setToken(token);
        userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Finds all users.
     *
     * @return the list of all users
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Validates the login data that could not be validated through jakarta validations
     *
     * @param userLoginDTO the user login data transfer object containing the login details to be validated
     * @return the list of validation errors, or an empty list if validation is successful
     */
    @Override
    public List<String> validateLogin(UserLoginDTO userLoginDTO) {
        List<String> errors = new ArrayList<>();
        User user = userRepository.findByEmail(userLoginDTO.getEmail());
        if (user == null) {
            errors.add("There's no account with that email");
        } else if (!new BCryptPasswordEncoder().matches(userLoginDTO.getPassword(), user.getPassword())) {
            errors.add("Wrong credentials");
        }
        return errors;
    }

    /**
     * Validates the registration data that could not be validated through jakarta validations
     *
     * @param user the user registration data transfer object containing the registration details to be validated
     * @return the list of validation errors, or an empty list if validation is successful
     */
    @Override
    public List<String> validateRegister(UserRegisterDTO user) {
        List<String> errors = new ArrayList<>();
        if (userRepository.findByEmail(user.getEmail()) != null)
            errors.add("The email is already being used");
        else if (!user.getPassword().matches(user.getRepeatPassword()))
            errors.add("The passwords doesn't match");
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
