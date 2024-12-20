package lsit.Repositories;

import lsit.Models.Brand;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class InMemoryBrandRepository implements IBrandRepository {
    private final HashMap<UUID, Brand> brands = new HashMap<>();

    @Override
    public void add(Brand brand) {
        brand.setId(UUID.randomUUID());
        brands.put(brand.getId(), brand);
    }

    @Override
    public Brand get(UUID id) {
        return brands.get(id);
    }

    @Override
    public void remove(UUID id) {
        brands.remove(id);
    }

    @Override
    public void update(Brand updatedBrand) {
        Brand oldBrand = brands.get(updatedBrand.getId());
        if (oldBrand != null) {
            oldBrand.setName(updatedBrand.getName());
        }
    }

    @Override
    public List<Brand> list() {
        return new ArrayList<>(brands.values());
    }
}