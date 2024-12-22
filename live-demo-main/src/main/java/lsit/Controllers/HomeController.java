package lsit.Controllers;

import java.util.List;

import lsit.Repositories.IBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private BeverageController beverageController;

    @Autowired
    private IBrandRepository brandRepository;

    @Autowired
    private ContractController contractController;

    @GetMapping("/")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser(OAuth2AuthenticationToken authentication) {
        var groups = (List<String>) authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        if (groups == null || groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: No valid roles assigned."); // 403 Forbidden
        }

        var userAttributes = authentication.getPrincipal().getAttributes();
        StringBuilder userInfo = new StringBuilder("<pre>\n");
        userAttributes.forEach((key, value) -> userInfo.append(key).append(": ").append(value).append("\n"));
        userInfo.append("</pre>");

        return ResponseEntity.ok(userInfo.toString());
    }
}
