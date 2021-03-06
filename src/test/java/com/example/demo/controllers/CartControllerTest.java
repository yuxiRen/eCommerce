package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);
    private Item item;
    private Optional<Item> itemOptional;
    @BeforeEach
    void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(new BigDecimal("1.99"));
        item.setDescription("California Red Apple");
        itemOptional = Optional.of(item);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Test");
        user.setPassword("test");
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(new BigDecimal(10));
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }
}
