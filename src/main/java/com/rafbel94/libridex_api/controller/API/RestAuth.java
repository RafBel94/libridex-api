package com.rafbel94.libridex_api.controller.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.model.UserLoginDTO;
import com.rafbel94.libridex_api.model.UserRegisterDTO;
import com.rafbel94.libridex_api.service.TokenService;
import com.rafbel94.libridex_api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class RestAuth {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("tokenService")
    private TokenService tokenService;

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @param userLoginDTO model used to validate login paramethers
     * @apiNote userLoginDTO attributes are validated through jakarta validations
     * @return a ResponseEntity containing a map with the user's email, role, and a
     *         JWT token valid for 24 hours if authentication is successful,
     *         or an error message and HTTP status 401 (Unauthorized) if
     *         authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        List<String> errors = userService.validateLogin(userLoginDTO);

        if (!errors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        User user = userService.findByEmail(userLoginDTO.getEmail());

        String token = tokenService.getJWTToken(user.getEmail());
        userService.updateUserToken(token, user);

        Map<String, Object> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new user with the provided user details.
     *
     * @param userRegisterDTO model used to validate register paramethers
     * @apiNote userRegisterDTO attributes are validated through jakarta validations
     * @return the registered user object
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        List<String> errors = userService.validateRegister(userRegisterDTO);
        if (!errors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        userService.registerUser(userService.toEntity(userRegisterDTO));

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered succesfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
