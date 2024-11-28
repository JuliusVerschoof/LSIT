package lsit.Controllers;

import java.util.*;

import lsit.Models.Brand;
import org.springframework.web.bind.annotation.*;

import lsit.Models.Beverage;
import lsit.Services.StockService;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/add")
    public void add(@RequestBody Beverage beverage, @RequestBody Brand brand, 
                    @RequestParam Optional<Integer> initialStock) {
        stockService.add(brand,beverage, initialStock);
    }

    @DeleteMapping("/{brand}")
    public void remove(@PathVariable("brand") Brand brand) {
        stockService.removeBrand(brand);
    }

    @DeleteMapping("/{brand}{beverage}")
    public void remove(@PathVariable("brand") Brand brand, @PathVariable("beverage") Beverage beverage) {
        stockService.removeBeverage(brand,beverage);
    }

    @PutMapping("/{brand}{beverage}{amount}")
    public void update(@PathVariable("brand") Brand brand, @PathVariable("beverage") Beverage beverage,
                       @PathVariable Integer amount) {
        // Assumption is that amount is positive for increase, negative for decrease
        stockService.changeStock(brand,beverage,amount);
    }



}