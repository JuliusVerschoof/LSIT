package lsit.Repositories;

import lsit.Models.Customer;
import lsit.Models.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerRepository {
    static HashMap<UUID, Customer> customers = new HashMap<>();

    public void add(Customer customer) {
        customer.setId(UUID.randomUUID());
        customers.put(customer.getId(), customer);
    }

    public Customer get(UUID id) {
        return customers.get(id);
    }

    public void remove(UUID id) {
        customers.remove(id);
    }

    public void update(Customer updatedCustomer) {
        Customer oldCustomer = customers.get(updatedCustomer.getId());
        oldCustomer.setName(updatedCustomer.getName());
    }

    public List<Customer> list() {
        return new ArrayList<>(customers.values());
    }
}
