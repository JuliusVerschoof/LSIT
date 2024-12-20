package lsit.Repositories;

import lsit.Models.Customer;
import java.util.List;
import java.util.UUID;

public interface ICustomerRepository {
    void add(Customer customer);
    Customer get(UUID id);
    void remove(UUID id);
    void update(Customer updatedCustomer);
    List<Customer> list();
}