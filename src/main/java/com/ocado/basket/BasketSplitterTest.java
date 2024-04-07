package com.ocado.basket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class BasketSplitterTest {
    private BasketSplitter basketSplitter;
    private final String testConfigFilePath = "/home/mikolajmichalczyk/IdeaProjects/basket/Zadanie/config.json"; // Update this path

    @BeforeEach
    void setUp() {
        basketSplitter = new BasketSplitter(testConfigFilePath);
    }

    // tests if the config file is loaded correctly
    @Test
    void testCreateItemDeliveryMap() {
        Map<String, List<String>> itemDeliveryMap = basketSplitter.getItemDeliveryMap();
        assertNotNull(itemDeliveryMap);
        assertTrue(itemDeliveryMap.containsKey("Bread - Flat Bread"));
        assertTrue(itemDeliveryMap.get("Bread - Flat Bread").contains("In-store pick-up"));
    }

    // tests our split method on one test case
    @Test
    void testSplit() {
        List<String> items = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht", "Fond - Chocolate"); // Example items
        // Create expected delivery map with mutable ArrayLists
        Map<String, List<String>> expectedDeliveryMap = Map.of(
                "Courier", new ArrayList<>(Arrays.asList(
                        "Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White",
                        "Flower - Daisies", "Cookies - Englishbay Wht"
                )),
                "Mailbox delivery", new ArrayList<>(Arrays.asList("Fond - Chocolate"))
        );

        // Invoke the split method
        Map<String, List<String>> splitResult = basketSplitter.split(items);

        // Assertions
        assertNotNull(splitResult);
        assertEquals(expectedDeliveryMap, splitResult);
    }

}

