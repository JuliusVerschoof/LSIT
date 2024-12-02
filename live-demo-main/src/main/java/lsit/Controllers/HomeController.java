package lsit.Controllers;

import java.util.List;

import lsit.Repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private BeverageController beverageController;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ContractController contractController;

    @GetMapping("/")
    public ResponseEntity get(){
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/user")
    public String getUser(OAuth2AuthenticationToken authentication){
        // var groups = (List<String>)authentication.getPrincipal().getAttribute("groups");
        // return groups.get(0);

        var userAttributes = authentication.getPrincipal().getAttributes();

        // StringBuilder sb = new StringBuilder();
        // for(var entry : userAttributes.entrySet()){
        //     var s = entry.getKey() + ": " + entry.getValue();
        //     sb.append("\n").append(s);
        // }

        return "<pre> \n" +
            userAttributes.entrySet().parallelStream().collect(
                StringBuilder::new,
                (s, e) -> s.append(e.getKey()).append(": ").append(e.getValue()),
                (a, b) -> a.append("\n").append(b)
            ) +
            "</pre>";
    }

}
