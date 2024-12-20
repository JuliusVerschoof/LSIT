package lsit.Repositories;

import lsit.Models.Brand;
import java.util.List;
import java.util.UUID;

public interface IBrandRepository {
    void add(Brand brand);
    Brand get(UUID id);
    void remove(UUID id);
    void update(Brand updatedBrand);
    List<Brand> list();
}