package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @BeforeEach
    void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    void getItems() {
        when(itemRepository.findAll()).thenReturn(createTestItemList());
        assertNotNull(itemController.getItems());
        assertEquals(createTestItemList().size(), itemController.getItems().getBody().size());
    }

    @Test
    void getItemById() {
        Optional<Item> testOptionalItem = Optional.of(createTestItemList().get(0));
        when(itemRepository.findById(1L)).thenReturn(testOptionalItem);
        assertNotNull(itemController.getItemById(1L));
        assertEquals("Apple", itemController.getItemById(1L).getBody().getName());
    }

    @Test
    void getItemsByName() {
        List<Item> list = new ArrayList<>();
        list.add(createTestItemList().get(1));
        System.out.println(createTestItemList().get(1).getName());
        when(itemRepository.findByName("Banana")).thenReturn(list);
        assertNotNull(itemController.getItemsByName("Banana").getBody().get(0));
        assertEquals("Organic Sunshine Banana", itemController.getItemsByName("Banana").getBody().get(0).getDescription());
        assertEquals(list.size(), itemController.getItemsByName("Banana").getBody().size());

        assertEquals(404, itemController.getItemsByName("BadBanana").getStatusCodeValue());
    }

    private List<Item> createTestItemList() {
        Item item1 = new Item();
        item1.setName("Apple");
        item1.setId(1L);
        item1.setDescription("California Red Apple");
        item1.setPrice(new BigDecimal(1.99));

        Item item2 = new Item();
        item2.setName("Banana");
        item2.setId(2L);
        item2.setDescription("Organic Sunshine Banana");
        item2.setPrice(new BigDecimal(1.88));

        Item item3 = new Item();
        item3.setName("Cherry");
        item3.setId(3L);
        item3.setDescription("WoodStock Cherries");
        item3.setPrice(new BigDecimal(6.99));

        List<Item> testItems = new ArrayList<>();
        testItems.add(item1);
        testItems.add(item2);
        testItems.add(item3);
        return testItems;
    }
}
