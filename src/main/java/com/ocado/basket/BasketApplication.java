package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketApplication {
    public static void main(String[] args) {
        //creating a basket splitter
        BasketSplitter basketSplitter = new BasketSplitter("/home/mikolajmichalczyk/IdeaProjects/basket/Zadanie/config.json");
        // splitting items
        var split1 = basketSplitter.split(loadItems("/home/mikolajmichalczyk/IdeaProjects/basket/Zadanie/basket-1.json"));
        var split2 = basketSplitter.split(loadItems("/home/mikolajmichalczyk/IdeaProjects/basket/Zadanie/basket-2.json"));
        System.out.println(split1);
        System.out.println(split2);
    }
    /**

     */
    private static List<String> loadItems(String path){
        List<String> output = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput;
        try{
            //read input from file
            jsonInput = new String(Files.readAllBytes(Paths.get(path)));
            // read value from json input to the map object with jackson dependency
            output = mapper.readValue(jsonInput, new TypeReference<>() {});
        }catch (IOException e){
            e.printStackTrace();
        }
        return output;
    }

}
