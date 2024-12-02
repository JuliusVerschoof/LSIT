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
public class ContractRepository {
    static HashMap<UUID, Contract> contracts = new HashMap<>();

    public void add(Contract contract){
        contract.setId(UUID.randomUUID());
        contracts.put(contract.getId(), contract);
    }

    public Contract get(UUID id){
        return contracts.get(id);
    }

    public void remove(UUID id){
        contracts.remove(id);
    }

    public void update(Contract updatedContract){
        if (updatedContract != null) {
            try {
                Contract oldContract = contracts.get(updatedContract.getId());
                oldContract.setClientName(updatedContract.getClientName());
                oldContract.setDayOfWeek(updatedContract.getDayOfWeek());
                oldContract.setEndDate(updatedContract.getEndDate());
                oldContract.setStartDate(updatedContract.getStartDate());
            }catch (Exception e){
                System.err.println("Error updating contract: " + e.getMessage());
            }
        }else{
            System.err.println("Contract is null");
        }

    }

    public List<Contract> list(){
        return new ArrayList<>(contracts.values());
    }

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
