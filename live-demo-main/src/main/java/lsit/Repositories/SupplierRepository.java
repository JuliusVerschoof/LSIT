package lsit.Repositories;

import lsit.Models.Supplier;
import lsit.Models.Supplier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    public void remove(UUID id){
        suppliers.remove(id);
    }

    public void update(Supplier updatedSupplier){
        Supplier oldSupplier = suppliers.get(updatedSupplier.getId());
        oldSupplier.setName(updatedSupplier.getName());
    }

    public List<Supplier> list(){
        return new ArrayList<>(suppliers.values());
    }
}
