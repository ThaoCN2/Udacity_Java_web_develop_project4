package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock((UserRepository.class));

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        // Create item
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(BigDecimal.valueOf(3.5));
        item.setDescription("Test description");

        List<Item> items = new ArrayList<>();
        items.add(item);

        // create user
        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");

        // create cart
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(3.5));

        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findByUsername("other")).thenReturn(null);
    }

    @Test
    public void orderSuccess() {
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
    }

    @Test
    public void orderFailByUserNotFound() {
        ResponseEntity<UserOrder> response = orderController.submit("other");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOderByUserSuccess() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();
        assertNotNull(userOrders);
    }

    @Test
    public void getOderFailByUserNotFound() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("other");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
