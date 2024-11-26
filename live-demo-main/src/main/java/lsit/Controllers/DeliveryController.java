package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import lsit.Models.Delivery;
import lsit.Repositories.DeliveryRepository; // assmuning we get a Delivery Repository

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @GetMapping
    public List<Delivery> list() {
        return deliveryRepository.list();
    }

    @GetMapping("/{id}")
    public Delivery get(@PathVariable("id") UUID id) {
        return deliveryRepository.get(id);
    }

    @PostMapping("/schedule")
    public Delivery schedule(@RequestBody Delivery delivery) {
        deliveryRepository.add(delivery);
        return delivery;
    }

    @PutMapping("/{id}/status")
    public Delivery updateStatus(@PathVariable("id") UUID id, @RequestBody Delivery delivery) {
        delivery.id = id;
        deliveryRepository.update(delivery);
        return delivery;
    }
}