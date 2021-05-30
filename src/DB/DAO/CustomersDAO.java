package DB.DAO;

import exceptions.InternalSystemException;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomersDAO {

     boolean isCustomerExists(String email, String password) throws SQLException, InternalSystemException;

     void addCustomer(Customer customer) throws SQLException, InternalSystemException;

     Customer addCustomerWithReturnCustomer(Customer customer) throws SQLException, InternalSystemException;

     void updateCustomer(Customer customer) throws SQLException, InternalSystemException;

     /**
      * This method changes Customer status to INACTIVE. */
     void deleteCustomerAsChangeStatus(int customerID) throws SQLException, InternalSystemException;

     List<Customer> getAllCustomers() throws SQLException;

     Customer getOneCustomer(int customerID) throws SQLException, InternalSystemException;

     int loginCustomer(String email) throws SQLException;

     /**
      * This is the only method that really deletes a table row.  (from customers_vs_coupons table.) */
     void deleteCustomerPurchase(Integer customerId) throws SQLException;

     Customer getCustomerByEmail(String email) throws SQLException, InternalSystemException;
}
