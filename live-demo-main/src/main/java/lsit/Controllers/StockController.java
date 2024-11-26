package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import lsit.Models.Beverage;
import lsit.Repositories.StockRepository;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockRepository stockRepository;

    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @PostMapping("/add")
    public void add(@RequestBody Beverage beverage, @RequestParam int type) {
        stockRepository.add(beverage, type);
    }

    @DeleteMapping("/{brand}")
    public void remove(@PathVariable("brand") int brand) {
        stockRepository.remove(brand);
    }

    @PutMapping("/{brand}")
    public void update(@PathVariable("brand") int brand, @RequestBody HashMap<UUID, Beverage> updatedStock) {
        stockRepository.update(updatedStock, brand);
    }
}