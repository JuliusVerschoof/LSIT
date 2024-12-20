package lsit.Repositories;

import lsit.Models.Supplier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class InMemorySupplierRepository implements ISupplierRepository {
    private final HashMap<UUID, Supplier> suppliers = new HashMap<>();

    @Override
    public void add(Supplier supplier) {
        supplier.setId(UUID.randomUUID());
        suppliers.put(supplier.getId(), supplier);
    }

    @Override
    public Supplier get(UUID id) {
        return suppliers.get(id);
    }

    @Override
    public void remove(UUID id) {
        suppliers.remove(id);
    }

    @Override
    public void update(Supplier updatedSupplier) {
        Supplier oldSupplier = suppliers.get(updatedSupplier.getId());
        if (oldSupplier != null) {
            oldSupplier.setName(updatedSupplier.getName());
        }
    }

    @Override
    public List<Supplier> list() {
        return new ArrayList<>(suppliers.values());
    }
}
