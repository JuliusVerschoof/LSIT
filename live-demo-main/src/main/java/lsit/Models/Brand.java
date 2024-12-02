package lsit.Models;

import java.util.UUID;

public class Brand {
    private UUID id;
    private String name;

    public Brand(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
