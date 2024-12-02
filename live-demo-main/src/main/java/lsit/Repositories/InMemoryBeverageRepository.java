package lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import lsit.Models.Beverage;

@Repository
public class InMemoryBeverageRepository implements BeverageRepositoryInterface {
    private final HashMap<UUID, Beverage> beverages = new HashMap<>();

    @Override
    public void add(Beverage beverage) {
        beverage.setId(UUID.randomUUID());
        beverages.put(beverage.getId(), beverage);
    }

    @Override
    public Beverage get(UUID id) {
        return beverages.get(id);
    }

    @Override
    public void remove(UUID id) {
        beverages.remove(id);
    }

    @Override
    public void update(Beverage updatedBeverage) {
        Beverage oldBeverage = beverages.get(updatedBeverage.getId());
        if (oldBeverage != null) {
            oldBeverage.setName(updatedBeverage.getName());
            oldBeverage.setBuyPrice(updatedBeverage.getBuyPrice());
            oldBeverage.setSellPrice(updatedBeverage.getSellPrice());
            oldBeverage.setVolume(updatedBeverage.getVolume());
        }
    }

    @Override
    public List<Beverage> list() {
        return new ArrayList<>(beverages.values());
    }
}