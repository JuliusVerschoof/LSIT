package lsit.Models;

import java.util.UUID;

public class Brand {
    private String name;
    private  UUID id;

    public Brand(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
