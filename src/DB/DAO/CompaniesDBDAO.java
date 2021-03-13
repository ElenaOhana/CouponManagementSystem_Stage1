package DB.DAO;

import DB.ConnectionPool;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.ClientStatus;
import java_beans_entities.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompaniesDBDAO implements CompaniesDAO {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean isCompanyExists(String email, String password) throws SQLException {
        final String queryTempGetEmailAndPassword = "SELECT `Email`, `Password` FROM `Companies` WHERE `Email` = ? AND `Password` = ?";
        Connection connection = connectionPool.getConnection();
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetEmailAndPassword)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.executeQuery();
            try (ResultSet resultSet = preparedStatement.getResultSet()) { //TODO the check
                boolean emailsEquals = resultSet.getString("Email").equalsIgnoreCase(email);
                boolean passwordsEquals = resultSet.getString("Password").equalsIgnoreCase(password);
                if (resultSet.next()) {
                    if (emailsEquals && passwordsEquals) {
                        result = true;
                    }
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return result;
    }

    @Override
    public void addCompany(Company company) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCompany = "INSERT INTO `Companies` (`name`, `email`, `Password`) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCompany)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            //preparedStatement.setString(4, company.getClientStatus().toString());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Creating Company failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void updateCompany(Company company) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempUpdateCompany = "UPDATE `Companies` SET `name` = ?, `email` = ?, `password` = ?, `Status` = ? WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempUpdateCompany)) {
            preparedStatement.setInt(5, company.getId());
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.setString(4, company.getClientStatus().toString()); // TODO maybe to change - i don't need it
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Update Company failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void deleteCompany(int companyID) throws SQLException, InternalSystemException { // I do not delete object, I add status inactive instead // TODO  to add status inactive to ClientStatus after deleting
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCompanyStatus = "UPDATE `Companies` SET `Status` = `inactive` WHERE `id` = ?"; //TODO TO CHECK `inactive`
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCompanyStatus)) {
            preparedStatement.setInt(1, companyID);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Delete Company failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public List<Company> getAllCompanies() throws SQLException {
        List<Company> companies = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        final String queryTempGetAllCompanies = "SELECT * FROM `Companies`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetAllCompanies)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String status = resultSet.getString("Status");
                    ClientStatus clientStatus = ClientStatus.valueOf(status);
                    Company company = new Company(id, name, email, password, clientStatus);
                    companies.add(company);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return companies;
    }

    @Override
    public Company getOneCompany(int companyID) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        Company company = null;
        String queryTempGetCompanyByID = "SELECT * FROM `Companies` WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCompanyByID)) {
            preparedStatement.setInt(1, companyID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String status = resultSet.getString("Status");
                    ClientStatus clientStatus = ClientStatus.valueOf(status);
                    company = new Company(id, name, email, password, clientStatus);
                } else {
                    throw new InternalSystemException("Creating company failed, no ID obtained.");
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return company;
    }
}
