package lsit.Controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import lsit.Models.Contract;
import lsit.Repositories.IContractRepository;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final IContractRepository contractRepository;

    public ContractController(IContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @GetMapping
    public ResponseEntity<List<Contract>> list(OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("customer", "operations", "supply", "storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        return ResponseEntity.ok(contractRepository.list()); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> get(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("customer", "operations", "supply", "storage"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Contract contract = contractRepository.get(id);
        if (contract == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        return ResponseEntity.ok(contract);
    }

    @PostMapping
    public ResponseEntity<Contract> add(@RequestBody Contract contract, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("operations", "supply"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        if (contractRepository.check(contract)) {
            contractRepository.add(contract);
            return ResponseEntity.status(HttpStatus.CREATED).body(contract); // 201 Created
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> update(@PathVariable UUID id, @RequestBody Contract contract, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("operations", "supply"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        if (contractRepository.get(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        if (contractRepository.check(contract)) {
            contract.setId(id);
            contractRepository.update(contract);
            return ResponseEntity.ok(contract); // 200 OK
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (!hasAccess(groups, List.of("supply"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        Contract contract = contractRepository.get(id);
        if (contract == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        contractRepository.remove(id);
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
