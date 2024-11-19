package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Beverage;

@Repository
public class BeverageRepository {
    static HashMap<UUID, Beverage> beverages = new HashMap<>();


    public void add(Beverage b){//done
        b.id = UUID.randomUUID();
        beverages.add(b.id, b);
        StockRepository.add(b,b.brand);
    }

    public Beverage get(UUID id){//done
        return beverages.get(id);
    }

    public void remove(UUID id){//done
        beverages.remove(id);
        StockRepository.remove(id);
    }

    public void update(Beverage b){ //done
        Beverage x = beverages.get(b.id);
        x.name = b.brand;
        //and any other features in the model 
    }

    public List<Beverage> list(){//done
        return new ArrayList<>(beverages.values());
    }
}
