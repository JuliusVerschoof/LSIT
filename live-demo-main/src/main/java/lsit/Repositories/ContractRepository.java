package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Contract;

@Repository
public class ContractRepository {
    static HashMap<UUID, Contract> contracts = new HashMap<>();

    public void add(Contract contract){
        contract.setId(UUID.randomUUID());
        contracts.put(contract.getId(), contract);
    }

    public Contract get(UUID id){
        return contracts.get(id);
    }

    public void remove(UUID id){//done
        contracts.remove(id);
    }

    public void update(Contract updatedContract){
        Contract oldContract = contracts.get(updatedContract.getId());
        oldContract.setClientName(updatedContract.getClientName());
        oldContract.setStartDate(updatedContract.getStartDate());
        oldContract.setEndDate(updatedContract.getEndDate());
        oldContract.setDayOfWeek(updatedContract.getDayOfWeek());
        oldContract.setProducts(updatedContract.getProducts());
    }

    public List<Contract> list(){
        return new ArrayList<>(contracts.values());
    }
}
