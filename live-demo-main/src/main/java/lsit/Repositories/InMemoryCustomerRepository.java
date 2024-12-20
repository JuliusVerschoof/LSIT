package lsit.Repositories;

import lsit.Models.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class InMemoryCustomerRepository implements ICustomerRepository {
    private final HashMap<UUID, Customer> customers = new HashMap<>();

    @Override
    public void add(Customer customer) {
        customer.setId(UUID.randomUUID());
        customers.put(customer.getId(), customer);
    }

    @Override
    public Customer get(UUID id) {
        return customers.get(id);
    }

    @Override
    public void remove(UUID id) {
        customers.remove(id);
    }

    @Override
    public void update(Customer updatedCustomer) {
        Customer oldCustomer = customers.get(updatedCustomer.getId());
        if (oldCustomer != null) {
            oldCustomer.setName(updatedCustomer.getName());
        }
    }

    @Override
    public List<Customer> list() {
        return new ArrayList<>(customers.values());
    }
}
