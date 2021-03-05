package DB.DAO;

import DB.ConnectionPool;
import java_beans_entities.ClientStatus;
import java_beans_entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersDBDAO implements CustomersDAO{
    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean isCustomerExists(String email, String password) {
        final String queryTempGetEmailAndPassword = "SELECT `email`, `password` FROM `customers` WHERE `email` = ? AND `password` = ?";
        Connection connection = connectionPool.getConnection();
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetEmailAndPassword)){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean emailsEquals = resultSet.getString("email").equalsIgnoreCase(email);
                boolean passwordsEquals = resultSet.getString("password").equalsIgnoreCase(password);
                if (resultSet.next()) {
                    if (emailsEquals && passwordsEquals) {
                        result = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
        return result;
    }

    @Override
    public void addCustomer(Customer customer) {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCustomer = "INSERT INTO `customers` (`FirstName`, `LastName`, `email`, `password`) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCustomer)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Creating Customer failed, no rows affected."); //TODO Exception - Creating
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void updateCustomer(Customer customer) {
        Connection connection = connectionPool.getConnection();
        final String queryTempUpdateCustomer = "UPDATE `customers` SET `FirstName` = ?, `LastName` = ?, `email` = ?, `password` = ?  WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempUpdateCustomer)){
            preparedStatement.setInt(5, customer.getId());
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Update Customer failed, no rows affected."); //TODO Exception - Update
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void deleteCustomer(int customerID) { // I do not delete object, I add status inactive instead
        // TODO  to add status inactive to ClientStatus after deleting
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCustomerStatus = "UPDATE `customers` SET `CompanyStatus` = `inactive` WHERE `id` = ?"; //TODO TO CHECK `inactive`
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCustomerStatus)) {
            preparedStatement.setInt(1, customerID);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Delete Customer failed, no rows affected."); //TODO Exception - Delete
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        final String queryTempGetAllCustomers = "SELECT * FROM `customers`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetAllCustomers)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email =resultSet.getString("email");
                    String password =resultSet.getString("password");
                    String status =resultSet.getString("Status");
                    ClientStatus clientStatus = ClientStatus.valueOf(status);
                    Customer customer = new Customer(id, firstName, lastName, email, password, clientStatus);
                    customerList.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
        return customerList;
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        return null;
    }
}
