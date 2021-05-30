package DB.DAO;

import DB.ConnectionPool;
import exceptions.InternalSystemException;
import java_beans_entities.ClientStatus;
import java_beans_entities.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomersDBDAO is Singleton*/
public class CustomersDBDAO implements CustomersDAO {

    private static CustomersDBDAO instance;

    private ConnectionPool connectionPool;

    private CustomersDBDAO() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static CustomersDBDAO getInstance() {
        if (instance == null) {
            instance = new CustomersDBDAO();
        }
        return instance;
    }

    @Override
    public boolean isCustomerExists(String email, String password) throws SQLException, InternalSystemException {
        final String queryTempGetEmailAndPassword = "SELECT `email`, `password` FROM `customers` WHERE `email` = ?";
        Connection connection = connectionPool.getConnection();
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetEmailAndPassword)) {
            preparedStatement.setString(1, email);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                if (resultSet.getString("Password").equalsIgnoreCase(password)) {
                    result = true;
                } else if (!resultSet.getString("Password").equalsIgnoreCase(password)) {
                    throw new InternalSystemException(" Wrong credentials.");
                }
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
    public Customer addCustomerWithReturnCustomer(Customer customer) throws SQLException, InternalSystemException {
        int id;
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCustomer = "INSERT INTO `customers` (`FirstName`, `LastName`, `email`, `password`) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCustomer, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Creating customer failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else throw new SQLException("Creating customer failed, no ID obtained.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return new Customer(id, customer.getFirstName(), customer.getLastName(),customer.getEmail(),customer.getPassword());
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
    public void deleteCustomerAsChangeStatus(int customerID) throws SQLException, InternalSystemException { /* I do not delete object, I add status inactive instead */
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCustomerStatus = "UPDATE `customers` SET `Status` = ? WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCustomerStatus)) {
            preparedStatement.setString(1, "INACTIVE");
            preparedStatement.setInt(2, customerID);
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
        Customer customer;
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

    @Override
    public int loginCustomer(String email) throws SQLException {
        final String queryTempGetIdByEmailAndPassword = "SELECT `id` FROM `customers` WHERE `Email` = ?";
        Connection connection = connectionPool.getConnection();
        int id = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetIdByEmailAndPassword)) {
            preparedStatement.setString(1, email);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return id;
    }

    @Override
    public void deleteCustomerPurchase(Integer customerId) throws SQLException {
        final String queryTempDeleteCustomerPurchase = "DELETE FROM `customers_vs_coupons` WHERE customerId = ?";
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempDeleteCustomerPurchase)) {
            preparedStatement.setInt(1, customerId);
            int countRow  = preparedStatement.executeUpdate();
            if (countRow == 0) {
                throw new SQLException("Error delete customer purchase by customerId " + customerId);
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        Customer customer;
        final String queryTempGetCustomerByEmail = "SELECT * FROM `customers` WHERE `email` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCustomerByEmail)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
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
