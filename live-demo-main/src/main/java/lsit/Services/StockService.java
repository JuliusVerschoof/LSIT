package lsit.Services;

import java.util.*;

import lsit.Models.Brand;
import lsit.Models.Beverage;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private static final HashMap<UUID,HashMap<UUID,Integer>> fullStock = new HashMap<>();

    // Add a Beverage to the stock
    public void add(Brand brand, Beverage beverage, Optional<Integer> initialStock){
       
        //Check if the brand exist in the stock
        if (fullStock.containsKey(brand.getId())){
            if(fullStock.get(brand.getId()).containsKey(beverage.getId())){
                System.err.println("BeveragesStock already exists");
            }else{
                fullStock.get(brand.getId()).put(beverage.getId(), initialStock.orElse(0));
            }
        }else{
            HashMap<UUID,Integer> newStock = new HashMap<>();
            newStock.put(beverage.getId(),initialStock.orElse(0));
            fullStock.put(brand.getId(),newStock);
        }
    }

    // Chance Stock -> Make it negative if you want to decrease stock
    public void changeStock(Brand brand, Beverage beverage, int amount){
        if (fullStock.containsKey(brand.getId())){
            HashMap<UUID,Integer> brandStock = fullStock.get(brand.getId());
            if(brandStock.containsKey(beverage.getId())){
                brandStock.put(beverage.getId(),brandStock.get(beverage.getId())+amount);
            }else{
                brandStock.put(beverage.getId(),amount);
            }
        }else{
            System.err.println("BeveragesStock does not exist");
        }
    }

    // Remove Brand
    public void removeBrand(Brand brand){
        fullStock.remove(brand.getId());
    }

    // Remove beverage from brand x
    public void removeBeverage(Brand brand, Beverage beverage){
        fullStock.get(brand.getId()).remove(beverage.getId());
    }

    //  Get all stock
    public static HashMap<UUID, HashMap<UUID, Integer>> getFullStock() {
        return fullStock;
    }
}
