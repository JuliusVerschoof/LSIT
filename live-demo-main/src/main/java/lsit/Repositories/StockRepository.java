package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Beverage;

@Repository
public class StockRepository {
    static ArrayList<HashMap<UUID, Beverage>> BeveragesStock = new ArrayList<>();

    public void add(Beverage b, int type){//done
        BeveragesStock.ensureCapacity(type); 
        BeveragesStock.get(type).put(b.uuid, b);
    }
/* 
    public Array get(UUID id){
        return pets.get(id);
    }
*/
    public void remove(int brand){// remove entire brand?
        BeveragesStock.remove(brand);
    }

    public void update(HashMap<UUID, Beverage> b,int brand){//idk about this
        HashMap<UUID, Beverage> x = b;
        BeveragesStock.set(brand,x);
    }
    /* 
    public List<Pet> list(){
        ArrayList<Bever>new ArrayList<>(pets.values())
        return new ArrayList<>(pets.values());
    }*/
}
