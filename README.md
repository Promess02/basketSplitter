### Basket Splitter

This utility class is designed to split a list of items into groups based on delivery occurrences. It leverages item delivery mappings from a configuration file to determine delivery methods for each item.

#### Usage

To use the `BasketSplitter` class, follow these steps:

1. **Initialization**:
   ```java
   BasketSplitter splitter = new BasketSplitter("/path/to/config.json");
   ```

   Replace `/path/to/config.json` with the absolute path to your configuration file containing item delivery mappings.

2. **Splitting Items**:
   ```java
   List<String> items = List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht", "Fond - Chocolate");
   Map<String, List<String>> deliveryGroups = splitter.split(items);
   ```

   This will split the provided list of items into delivery groups based on the predefined delivery mappings.

#### Methods

- **Constructor**:
  ```java
  public BasketSplitter(String absolutePathToConfigFile)
  ```

  Initializes the `BasketSplitter` with the specified path to the configuration file.

- **split**:
  ```java
  public Map<String, List<String>> split(List<String> items)
  ```

  Splits the given list of items into groups based on delivery occurrences.

- **getItemDeliveryMap**:
  ```java
  public Map<String, List<String>> getItemDeliveryMap()
  ```

  Retrieves the map of items to their associated delivery methods.

#### Configuration File

The configuration file (`config.json`) should contain a JSON object with item delivery mappings in the following format:

```json
{
  "Cocoa Butter": ["Courier", "Parcel locker"],
  "Tart - Raisin And Pecan": ["Courier", "Parcel locker", "Same day delivery"],
  "Table Cloth 54x72 White": ["Courier", "Same day delivery"],
  "Flower - Daisies": ["Parcel locker", "Next day shipping"],
  "Cookies - Englishbay Wht": ["Courier", "Pick-up point"],
  "Fond - Chocolate": ["Mailbox delivery"]
}
```

Each item is mapped to a list of delivery methods.

#### Dependencies

- [Jackson](https://github.com/FasterXML/jackson) is used to parse JSON input for item delivery mappings.
- [JUnit](https://github.com/junit-team/junit4) is used for unit testing

#### Notes

Ensure that the configuration file path provided to the `BasketSplitter` constructor is correct and accessible.

For further details and examples, refer to the provided code and Javadoc comments within the `BasketSplitter` class.