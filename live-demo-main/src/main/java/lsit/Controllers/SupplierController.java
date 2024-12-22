package lsit.Controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import lsit.Models.Supplier;
import lsit.Repositories.ISupplierRepository;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final ISupplierRepository supplierRepository;

    public SupplierController(ISupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // List all suppliers (Restricted to Supply and Operations groups)
    @GetMapping
    public ResponseEntity<List<Supplier>> list(OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply") &&
            !groups.contains("lsit-ken3239/roles/grouplsitbeer/operations")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        return ResponseEntity.ok(supplierRepository.list()); // 200 OK
    }

    // Get a specific supplier by ID (Restricted to Supply and Operations groups)
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> get(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply") &&
            !groups.contains("lsit-ken3239/roles/grouplsitbeer/operations")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Supplier supplier = supplierRepository.get(id);
        if (supplier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        return ResponseEntity.ok(supplier); // 200 OK
    }

    // Add a new supplier (Restricted to Supply group)
    @PostMapping
    public ResponseEntity<Supplier> add(@RequestBody Supplier supplier, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 403 Forbidden
        }
        supplierRepository.add(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplier); // 201 Created
    }

    // Update a supplier (Restricted to Supply group)
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable UUID id, @RequestBody Supplier supplier, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 403 Forbidden
        }
        Supplier existingSupplier = supplierRepository.get(id);
        if (existingSupplier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        supplier.setId(id);
        supplierRepository.update(supplier);
        return ResponseEntity.ok(supplier); // 200 OK
    }

    // Delete a supplier (Restricted to Supply group)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!groups.contains("lsit-ken3239/roles/grouplsitbeer/supply")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Supplier supplier = supplierRepository.get(id);
        if (supplier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        supplierRepository.remove(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
