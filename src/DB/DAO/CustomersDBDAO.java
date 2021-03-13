package DB.DAO;

import DB.ConnectionPool;
import exceptions.InternalSystemException;
import java_beans_entities.ClientStatus;
import java_beans_entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersDBDAO implements CustomersDAO {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean isCustomerExists(String email, String password) throws SQLException {
        final String queryTempGetEmailAndPassword = "SELECT `email`, `password` FROM `customers` WHERE `email` = ? AND `password` = ?";
        Connection connection = connectionPool.getConnection();
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetEmailAndPassword)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean emailsEquals = resultSet.getString("email").equalsIgnoreCase(email);
                boolean passwordsEquals = resultSet.getString("password").equalsIgnoreCase(password);
                if (resultSet.next()) {
                    if (emailsEquals && passwordsEquals) {
                        result = true;
                    }
                } // TODO like at HM
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return result;
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCustomer = "INSERT INTO `customers` (`FirstName`, `LastName`, `email`, `password`) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCustomer)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Creating Customer failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempUpdateCustomer = "UPDATE `customers` SET `FirstName` = ?, `LastName` = ?, `email` = ?, `password` = ?  WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempUpdateCustomer)) {
            preparedStatement.setInt(5, customer.getId());
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Update Customer failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void deleteCustomer(int customerID) throws SQLException, InternalSystemException { // I do not delete object, I add status inactive instead
        // TODO  to add status inactive to ClientStatus after deleting
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCustomerStatus = "UPDATE `customers` SET `Status` = `inactive` WHERE `id` = ?"; //TODO TO CHECK `inactive`
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCustomerStatus)) {
            preparedStatement.setInt(1, customerID);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Delete Customer failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        final String queryTempGetAllCustomers = "SELECT * FROM `customers`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetAllCustomers)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String status = resultSet.getString("Status");
                    ClientStatus customerStatus = ClientStatus.valueOf(status);
                    Customer customer = new Customer(id, firstName, lastName, email, password, customerStatus);
                    customerList.add(customer);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return customerList;
    }

    @Override
    public Customer getOneCustomer(int customerID) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        Customer customer = null;
        final String queryTempGetCustomerByID = "SELECT * FROM `customers` WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCustomerByID)) {
            preparedStatement.setInt(1, customerID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String status = resultSet.getString("Status");
                    ClientStatus customerStatus = ClientStatus.valueOf(status);
                    customer = new Customer(id, firstName, lastName, email, password, customerStatus);
                } else {
                    throw new InternalSystemException("Creating customer failed, no ID obtained.");
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return customer;
    }
}
