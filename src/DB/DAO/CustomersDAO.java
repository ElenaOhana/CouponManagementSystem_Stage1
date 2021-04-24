package DB.DAO;

import exceptions.InternalSystemException;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomersDAO {

     boolean isCustomerExists(String email, String password) throws SQLException, InternalSystemException;

     void addCustomer(Customer customer) throws SQLException, InternalSystemException;

     void updateCustomer(Customer customer) throws SQLException, InternalSystemException;

     void deleteCustomerAsChangeStatus(int customerID) throws SQLException, InternalSystemException;

     List<Customer> getAllCustomers() throws SQLException;

     Customer getOneCustomer(int customerID) throws SQLException, InternalSystemException;

    int loginCustomer(String email, String password) throws SQLException;
}
