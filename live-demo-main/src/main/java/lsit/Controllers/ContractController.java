package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import lsit.Models.Contract;
import lsit.Repositories.ContractRepository; // Assuming we get contractrepository 

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractRepository contractRepository;

    public ContractController(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @GetMapping
    public List<Contract> list() {
        return contractRepository.list();
    }

    @GetMapping("/{id}")
    public Contract get(@PathVariable("id") UUID id) {
        return contractRepository.get(id);
    }

    @PostMapping
    public Contract add(@RequestBody Contract contract) {
        contractRepository.add(contract);
        return contract;
    }

    @PutMapping("/{id}")
    public Contract update(@PathVariable("id") UUID id, @RequestBody Contract contract) {
        contract.setId(id);
        contractRepository.update(contract);
        return contract;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        contractRepository.remove(id);
    }
}