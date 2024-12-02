package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Supplier;

@Repository
public class SupplierRepository {

    static HashMap<UUID, Supplier> suppliers = new HashMap<>();

    public void add(Supplier supplier){
        supplier.setId(UUID.randomUUID());
        suppliers.put(supplier.getId(), supplier);
    }

    public Supplier get(UUID id){
        return suppliers.get(id);
    }

    public void remove(UUID id){//done
        suppliers.remove(id);
    }

    public void update(Supplier updatedSupplier){
        Supplier oldSupplier = suppliers.get(updatedSupplier.getId());
        oldSupplier.setName(updatedSupplier.getName());
        oldSupplier.setAssortment(updatedSupplier.getAssortment());
    }

    public List<Supplier> list(){
        return new ArrayList<>(suppliers.values());
    }
    
}