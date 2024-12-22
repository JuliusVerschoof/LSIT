package lsit.Controllers;

import java.util.*;

import lsit.Models.Brand;
import lsit.Models.Beverage;
import lsit.Services.StockService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Add stock (Restricted to Operations and Supply groups)
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Beverage beverage, @RequestBody Brand brand,
                                      @RequestParam Optional<Integer> initialStock, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/operations") &&
            !groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Insufficient permissions.");
        }
        stockService.add(brand, beverage, initialStock);
        return ResponseEntity.status(HttpStatus.CREATED).body("Stock added successfully.");
    }

    // Remove a brand (Restricted to Supply group)
    @DeleteMapping("/{brand}")
    public ResponseEntity<String> removeBrand(@PathVariable("brand") Brand brand, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Insufficient permissions.");
        }
        stockService.removeBrand(brand);
        return ResponseEntity.ok("Brand removed successfully.");
    }

    // Remove a beverage from a brand (Restricted to Supply group)
    @DeleteMapping("/{brand}/{beverage}")
    public ResponseEntity<String> removeBeverage(@PathVariable("brand") Brand brand, @PathVariable("beverage") Beverage beverage,
                                                 OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Insufficient permissions.");
        }
        stockService.removeBeverage(brand, beverage);
        return ResponseEntity.ok("Beverage removed successfully.");
    }

    // Update stock (Restricted to Operations and Supply groups)
    @PutMapping("/{brand}/{beverage}/{amount}")
    public ResponseEntity<String> updateStock(@PathVariable("brand") Brand brand, @PathVariable("beverage") Beverage beverage,
                                              @PathVariable Integer amount, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/operations") &&
            !groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Insufficient permissions.");
        }
        stockService.changeStock(brand, beverage, amount);
        return ResponseEntity.ok("Stock updated successfully.");
    }
}
