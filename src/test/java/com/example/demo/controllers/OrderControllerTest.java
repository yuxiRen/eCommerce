package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @BeforeEach
    void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    void submit() {
        when(userRepository.findByUsername("Test")).thenReturn(createTestUser());
        User returnedUser = userRepository.findByUsername("Test");
        UserOrder testUserOrder = UserOrder.createFromCart(returnedUser.getCart());
        orderRepository.save(testUserOrder);

        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("Test");

        assertNotNull(userOrderResponseEntity);
        assertEquals(200, userOrderResponseEntity.getStatusCodeValue());
        assertEquals(1, userOrderResponseEntity.getBody().getItems().size());
        assertEquals("Apple", userOrderResponseEntity.getBody().getItems().get(0).getName());
        assertEquals(404, orderController.submit("testNullUsername").getStatusCodeValue());
        assertEquals(HttpStatus.NOT_FOUND, orderController.submit("testNullUsername").getStatusCode());
    }

    @Test
    void getOrdersForUser() {

        List<UserOrder> returnedUSerOrder = new ArrayList<>();
        returnedUSerOrder.add(UserOrder.createFromCart(createTestUser().getCart()));

        List<UserOrder> actualUserOrders = new ArrayList<>();
        actualUserOrders.add(UserOrder.createFromCart(createTestUser().getCart()));

        when(userRepository.findByUsername("Test")).thenReturn(createTestUser());
        User returnedUser = userRepository.findByUsername("Test");
        when(orderRepository.findByUser(returnedUser)).thenReturn(returnedUSerOrder);

        ResponseEntity<List<UserOrder>> userOrders = orderController.getOrdersForUser("Test");


        assertNotNull(userOrders);
        assertEquals(200, userOrders.getStatusCodeValue());
        assertEquals(actualUserOrders.size(), userOrders.getBody().size());
        assertEquals(actualUserOrders.get(0).getTotal(), userOrders.getBody().get(0).getTotal());

        assertEquals(404, orderController.getOrdersForUser("testNullUsername").getStatusCodeValue());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Test");
        user.setPassword("test");
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> testItems = new ArrayList<>();
        Item testItem = new Item();
        testItem.setDescription("California Red Apple");
        testItem.setId(1L);
        testItem.setName("Apple");
        testItems.add(testItem);
        cart.setItems(testItems);
        cart.setTotal(new BigDecimal(10));
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }
}
