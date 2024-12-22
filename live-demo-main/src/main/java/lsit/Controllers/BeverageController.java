package lsit.Controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import lsit.Models.Beverage;
import lsit.Repositories.IBeverageRepository;

@RestController
@RequestMapping("/beverages")
public class BeverageController {

    private final IBeverageRepository beverageRepository;

    public BeverageController(IBeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    @GetMapping
    public ResponseEntity<List<Beverage>> list(OAuth2AuthenticationToken authentication) {
        // Check if user has the appropriate role for this operation
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("customer", "operations", "supply", "storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        return ResponseEntity.ok(beverageRepository.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beverage> get(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("customer", "operations", "supply", "storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Beverage beverage = beverageRepository.get(id);
        if (beverage == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        return ResponseEntity.ok(beverage);
    }

    @PostMapping
    public ResponseEntity<Beverage> add(@RequestBody Beverage b, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("operations", "supply"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        beverageRepository.add(b);
        return ResponseEntity.status(HttpStatus.CREATED).body(b);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beverage> update(@PathVariable UUID id, @RequestBody Beverage b, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("operations", "supply"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        if (beverageRepository.get(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        b.setId(id);
        beverageRepository.update(b);
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("supply"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        if (beverageRepository.get(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        beverageRepository.remove(id);
        return ResponseEntity.noContent().build();
    }

    // Utility method to check if the user has the required role
    private boolean hasAccess(List<String> groups, List<String> requiredRoles) {
        for (String role : requiredRoles) {
            if (groups.stream().anyMatch(group -> group.endsWith(role))) {
                return true;
            }
        }
        return false;
    }
}
