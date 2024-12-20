package lsit.Repositories;

import lsit.Models.Contract;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class InMemoryContractRepository implements IContractRepository {
    private final HashMap<UUID, Contract> contracts = new HashMap<>();

    @Override
    public void add(Contract contract) {
        contract.setId(UUID.randomUUID());
        contracts.put(contract.getId(), contract);
    }

    @Override
    public Contract get(UUID id) {
        return contracts.get(id);
    }

    @Override
    public void remove(UUID id) {
        contracts.remove(id);
    }

    @Override
    public void update(Contract updatedContract) {
        Contract oldContract = contracts.get(updatedContract.getId());
        if (oldContract != null) {
            oldContract.setClientName(updatedContract.getClientName());
            oldContract.setDayOfWeek(updatedContract.getDayOfWeek());
            oldContract.setStartDate(updatedContract.getStartDate());
            oldContract.setEndDate(updatedContract.getEndDate());
        }
    }

    @Override
    public List<Contract> list() {
        return new ArrayList<>(contracts.values());
    }

    @Override
    public boolean check(Contract contract) {
        String startDateString = contract.getStartDate();
        String endDateString = contract.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);
        LocalDate currentDate = LocalDate.now();

        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }
}
