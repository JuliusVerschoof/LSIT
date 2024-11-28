package lsit.Models;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// Assumption
public class Contract {
    private  UUID id;
    private  String clientName;
    private  String startDate;
    private  String endDate;
    private  String dayOfWeek;
    private  HashMap<Beverage,Integer> products;

    public Contract(UUID id, String clientName, String startDate, String endDate, String dayOfWeek) {
        this.id = id;
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayOfWeek = dayOfWeek;
        this.products = new HashMap<>();
    }

    public UUID getId() {
        return id;
    }
    public String getClientName() {
        return clientName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public HashMap<Beverage,Integer> getProducts() {
        return products;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProducts(HashMap<Beverage, Integer> products) {
        this.products = products;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}


//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//public class ParseAndFormatExample {
//    public static void main(String[] args) {
//        // Parsing a date string
//        String dateString = "2023-11-25";
//        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
//        System.out.println("Parsed Date: " + date);
//
//        // Formatting a date to string
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
//        String formattedDate = date.format(formatter);
//        System.out.println("Formatted Date: " + formattedDate);
//    }
//}

