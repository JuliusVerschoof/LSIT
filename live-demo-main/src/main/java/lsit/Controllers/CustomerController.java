package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import lsit.Models.Customer;
import lsit.Repositories.CustomerRepository; //assuming we get a customerRepository

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> list() {
        return customerRepository.list();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable("id") UUID id) {
        return customerRepository.get(id);
    }

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        customerRepository.add(customer);
        return customer;
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable("id") UUID id, @RequestBody Customer customer) {
        customer.id = id;
        customerRepository.update(customer);
        return customer;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        customerRepository.remove(id);
    }
}