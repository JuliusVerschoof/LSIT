package lsit.Models;

import java.util.List;
import java.util.UUID;

public class Supplier {
    private  UUID id;
    private  String name;
    private  List<Beverage> assortment;

    public Supplier(UUID id, String name, List<Beverage> assortment) {
        this.id = id;
        this.name = name;
        this.assortment = assortment;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Beverage> getAssortment() {
        return assortment;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssortment(List<Beverage> assortment) {
        this.assortment = assortment;
    }
}
