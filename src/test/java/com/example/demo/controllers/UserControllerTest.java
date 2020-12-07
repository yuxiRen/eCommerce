package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {


    private UserController userController;

    private User user;
    private Optional<User> userOptional;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
        user = new User();
        user.setId(1L);
        user.setUsername("Test");
        user.setPassword("test");
        userOptional = Optional.of(user);
    }

    @Test
    public void createUserHappyPath() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");
        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());

        User newUser = userResponseEntity.getBody();
        assertNotNull(newUser);
        assertEquals(0, newUser.getId());
        assertEquals("test", newUser.getUsername());
        assertEquals("thisIsHashed", newUser.getPassword());
    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(userOptional);
        ResponseEntity<User> userResponseEntity = userController.findById(1L);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User returnUser = userResponseEntity.getBody();
        assertNotNull(returnUser);
        assertEquals(1L, returnUser.getId());
        assertEquals("Test", returnUser.getUsername());
        assertEquals("test", returnUser.getPassword());
    }

    @Test
    void findByUserName() {
        when(userRepository.findByUsername("Test")).thenReturn(user);
        ResponseEntity<User> userResponseEntity = userController.findByUserName("Test");
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User returnUser = userResponseEntity.getBody();
        assertNotNull(returnUser);
        assertEquals(1L, returnUser.getId());
        assertEquals("Test", returnUser.getUsername());
        assertEquals("test", returnUser.getPassword());
    }

}