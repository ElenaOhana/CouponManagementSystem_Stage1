package DB.DAO;

import java_beans_entities.Customer;

import java.util.List;

public interface CustomersDAO {

     boolean isCustomerExists(String email, String password);

     void addCustomer(Customer customer);

     void updateCustomer(Customer customer);

     void deleteCustomer(int customerID);

     List<Customer> getAllCustomers();

     Customer getOneCustomer(int customerID);
}
