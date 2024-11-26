package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lsit.Models.Beverage;
import lsit.Repositories.BeverageRepository;

@RestController
public class BeverageController {

    private final BeverageRepository beverageRepository;

    public BeverageController(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    @GetMapping("/beverages")
    public List<Beverage> list() {
        return beverageRepository.list();
    }

    @GetMapping("/beverages/{id}")
    public Beverage get(@PathVariable("id") UUID id) {
        return beverageRepository.get(id);
    }

    @PostMapping("/beverages")
    public Beverage add(@RequestBody Beverage b) {
        beverageRepository.add(b);
        return b;
    }

    @PutMapping("/beverages/{id}")
    public Beverage update(@PathVariable("id") UUID id, @RequestBody Beverage b) {
        b.id = id;
        beverageRepository.update(b);
        return b;
    }

    @DeleteMapping("/beverages/{id}")
    public void delete(@PathVariable("id") UUID id) {
        beverageRepository.remove(id);
    }
}