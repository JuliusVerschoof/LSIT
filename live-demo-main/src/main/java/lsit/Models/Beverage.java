package lsit.Models;

import java.util.UUID;

public class Beverage {
    private UUID id;
    private String name;
    private double sellPrice; //Price for customer
    private double buyPrice; //Price from Supplier
    private int volume; //in ml

    public Beverage(UUID id, String name, double sellPrice, double buyPrice, int volume) {
        this.id = id;
        this.name = name;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.volume = volume;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSellPrice() {
        return sellPrice;
    }
    public double getBuyPrice() {
        return buyPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
