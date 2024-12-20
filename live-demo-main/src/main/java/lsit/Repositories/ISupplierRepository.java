package lsit.Repositories;

import lsit.Models.Supplier;
import java.util.List;
import java.util.UUID;

public interface ISupplierRepository {
    void add(Supplier supplier);
    Supplier get(UUID id);
    void remove(UUID id);
    void update(Supplier updatedSupplier);
    List<Supplier> list();
}
