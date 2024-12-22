package lsit.Controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import lsit.Models.Customer;
import lsit.Repositories.ICustomerRepository;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerRepository customerRepository;

    public CustomerController(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> list(OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("customer", "operations", "supply", "storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        return ResponseEntity.ok(customerRepository.list()); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("customer", "operations", "supply", "storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Customer customer = customerRepository.get(id);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        return ResponseEntity.ok(customer); // 200 OK
    }

    @PostMapping
    public ResponseEntity<Customer> add(@RequestBody Customer customer, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("operations"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        customerRepository.add(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable UUID id, @RequestBody Customer customer, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("operations"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Customer existingCustomer = customerRepository.get(id);
        if (existingCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        customer.setId(id);
        customerRepository.update(customer);
        return ResponseEntity.ok(customer); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Customer customer = customerRepository.get(id);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        customerRepository.remove(id);
        return ResponseEntity.noContent().build(); // 204 No Content
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
