package lsit.Repositories;

import lsit.Models.Beverage;
import lsit.Models.Brand;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Repository
public class BrandRepository {
    static HashMap<UUID, Brand> brands = new HashMap<>();

    public void add(Brand brand){
        brand.setId(UUID.randomUUID());
        brands.put(brand.getId(), brand);
    }

    public Brand getByID(UUID id){
        return brands.get(id);
    }

    public void remove(UUID id){
        brands.remove(id);
    }

    public void update(Brand updatedBrand){
        Brand oldBrand = brands.get(updatedBrand.getId());
        oldBrand.setName(updatedBrand.getName());
    }

    public List<Brand> list(){
        return new ArrayList<>(brands.values());
    }
}


