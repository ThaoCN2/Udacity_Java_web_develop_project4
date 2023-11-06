package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp () {
        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        // create user
        User user = new User();

        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setCart(new Cart());

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("other")).thenReturn(null);
    }

    @Test
    public void createUserSuccess() {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("passwordHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setConfirmPassword("password");
        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("passwordHashed", user.getPassword());
    }

    @Test
    public void createUserFail() {
        // pass invalid
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("abc");
        request.setConfirmPassword("abc");
        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        // confirm Password invalid
        request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setConfirmPassword("Password");
        response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUsername() {
        final ResponseEntity<User> response = userController.findByUserName("username");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("username", user.getUsername());
    }

    @Test
    public void findUserByUsernameNotFound() {
        final ResponseEntity<User> response = userController.findByUserName("other");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserById() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(1, user.getId());;
    }

    @Test
    public void findUserByIdNotFound() {
        final ResponseEntity<User> response = userController.findById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
