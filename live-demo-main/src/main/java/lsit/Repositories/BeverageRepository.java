package lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import lsit.Models.Beverage;

@Repository
public class BeverageRepository {
    static HashMap<UUID, Beverage> beverages = new HashMap<>();

    public void add(Beverage beverage){
        beverage.setId(UUID.randomUUID());
        beverages.put(beverage.getId(), beverage);
    }

    public Beverage get(UUID id){
        return beverages.get(id);
    }

    public void remove(UUID id){//done
        beverages.remove(id);
    }

    public void update(Beverage updatedBeverage){
        Beverage oldBeverage = beverages.get(updatedBeverage.getId());
        oldBeverage.setName(updatedBeverage.getName());
        oldBeverage.setBuyPrice(updatedBeverage.getBuyPrice());
        oldBeverage.setSellPrice(updatedBeverage.getSellPrice());
        oldBeverage.setVolume(updatedBeverage.getVolume());
    }

    public List<Beverage> list(){
        return new ArrayList<>(beverages.values());
    }
}
