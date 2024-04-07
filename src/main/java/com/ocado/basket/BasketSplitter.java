package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class for splitting items into groups based on delivery occurrences.
 */
public class BasketSplitter {
    private String absolutePathToConfigFile;
    // Map of item deliveries (item) -> List(delivery1, delivery2)
    private Map<String, List<String>> itemDeliveryMap;
    private List<String> items;

    /**
     * Constructs a BasketSplitter with the specified path to the configuration file.
     *
     * @param absolutePathToConfigFile The absolute path to the configuration file containing item deliveries.
     */
    public BasketSplitter(String absolutePathToConfigFile) {
        this.absolutePathToConfigFile = absolutePathToConfigFile;
        itemDeliveryMap = createItemDeliveryMap(absolutePathToConfigFile);
    }

    /**
     * Splits the given list of items into groups based on delivery occurrences.
     *
     * @param items The list of items to split.
     * @return A map of delivery methods to lists of items for each delivery.
     */
    public Map<String, List<String>> split(List<String> items) {
        // Create a copy of the items list to work with
        List<String> remainingItems = new ArrayList<>(items);

        Map<String, List<String>> output = new HashMap<>();
        Map<String, List<String>> deliveryItemMap = mapItemsToDeliveries(remainingItems);
        Map<String, Integer> itemDeliveryOccurrences = countOccurrences(mapDeliveriesToItems(remainingItems));
        List<String> deliveriesWithOccurrencesSorted = convertToSortedList(itemDeliveryOccurrences);

        int deliveryIndex = 0;
        while (!remainingItems.isEmpty() && deliveryIndex < deliveriesWithOccurrencesSorted.size()) {
            // Get the current delivery method
            String delivery = deliveriesWithOccurrencesSorted.get(deliveryIndex);

            // Get the list of items with the current delivery method
            List<String> itemsWithCurrentDelivery = new ArrayList<>(deliveryItemMap.get(delivery));

            // Put the delivery method in the output map with its corresponding items
            output.put(delivery, itemsWithCurrentDelivery);

            // Remove the processed items from the remaining items list
            remainingItems.removeAll(itemsWithCurrentDelivery);

            // Update the deliveryItemMap, itemDeliveryOccurrences, and deliveriesWithOccurrencesSorted based on remaining items
            deliveryItemMap = mapItemsToDeliveries(remainingItems);
            itemDeliveryOccurrences = countOccurrences(mapDeliveriesToItems(remainingItems));
            deliveriesWithOccurrencesSorted = convertToSortedList(itemDeliveryOccurrences);

            deliveryIndex++;
        }

        return output;
    }

    // Helper methods...

    /**
     * Reads the item deliveries from a configuration file.
     *
     * @param absolutePathToConfigFile The absolute path to the configuration file.
     * @return A map of item names to lists of delivery methods.
     */
    private Map<String, List<String>> createItemDeliveryMap(String absolutePathToConfigFile) {
        Map<String, List<String>> output = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput;
        try {
            // Read input from file
            jsonInput = new String(Files.readAllBytes(Paths.get(absolutePathToConfigFile)));
            // Read value from JSON input to the map object using Jackson dependency
            output = mapper.readValue(jsonInput, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Computes the occurrences of delivery methods for each item.
     *
     * @param items The map of items to their associated delivery methods.
     * @return A map of delivery methods to their occurrences.
     */
    private Map<String, Integer> countOccurrences(Map<String, List<String>> items) {
        Map<String, Integer> output = new HashMap<>();
        for (List<String> item : items.values()) {
            for (String delivery : item) {
                output.put(delivery, output.getOrDefault(delivery, 0) + 1);
            }
        }
        return output;
    }

    /**
     * Maps items to their associated delivery methods.
     *
     * @param items The list of items to map.
     * @return A map of items to their associated delivery methods.
     */
    private Map<String, List<String>> mapDeliveriesToItems(List<String> items) {
        Map<String, List<String>> output = new HashMap<>();
        for (String item : items) {
            List<String> itemDeliveryList = itemDeliveryMap.get(item);
            output.put(item, itemDeliveryList);
        }
        return output;
    }

    /**
     * Maps delivery methods to the items associated with each method.
     *
     * @param items The list of items to map.
     * @return A map of delivery methods to the items associated with each method.
     */
    private Map<String, List<String>> mapItemsToDeliveries(List<String> items) {
        Map<String, List<String>> output = new HashMap<>();
        for (String item : items) {
            List<String> deliveryMethods = itemDeliveryMap.get(item);
            for (String deliveryMethod : deliveryMethods) {
                output.computeIfAbsent(deliveryMethod, k -> new ArrayList<>()).add(item);
            }
        }
        return output;
    }

    /**
     * Converts a map of delivery occurrences to a sorted list of delivery methods based on occurrence counts.
     *
     * @param itemDeliveryOccurrences The map of delivery methods to their occurrences.
     * @return A sorted list of delivery methods.
     */
    private List<String> convertToSortedList(Map<String, Integer> itemDeliveryOccurrences) {
        return itemDeliveryOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Retrieves the item delivery map.
     *
     * @return The map of items to their associated delivery methods.
     */
    public Map<String, List<String>> getItemDeliveryMap() {
        return itemDeliveryMap;
    }
}
