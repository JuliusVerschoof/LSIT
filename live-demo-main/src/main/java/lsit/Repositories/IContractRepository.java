package lsit.Repositories;

import lsit.Models.Contract;
import java.util.List;
import java.util.UUID;

public interface IContractRepository {
    void add(Contract contract);
    Contract get(UUID id);
    void remove(UUID id);
    void update(Contract updatedContract);
    List<Contract> list();
    boolean check(Contract contract);
}