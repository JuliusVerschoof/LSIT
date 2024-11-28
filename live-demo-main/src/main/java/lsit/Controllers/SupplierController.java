package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import lsit.Models.Supplier;
import lsit.Repositories.SupplierRepository; // assuming we will have a supplier repository

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public List<Supplier> list() {
        return supplierRepository.list();
    }

    @GetMapping("/{id}")
    public Supplier get(@PathVariable("id") UUID id) {
        return supplierRepository.get(id);
    }

    @PostMapping
    public Supplier add(@RequestBody Supplier supplier) {
        supplierRepository.add(supplier);
        return supplier;
    }

    @PutMapping("/{id}")
    public Supplier update(@PathVariable("id") UUID id, @RequestBody Supplier supplier) {
        supplier.setId(id);
        supplierRepository.update(supplier);
        return supplier;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        supplierRepository.remove(id);
    }
}