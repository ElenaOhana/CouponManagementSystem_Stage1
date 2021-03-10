package DB.DAO;

import DB.ConnectionPool;
import java_beans_entities.ClientStatus;
import java_beans_entities.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompaniesDBDAO implements  CompaniesDAO{
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean isCompanyExists(String email, String password) {
        final String queryTempGetEmailAndPassword = "SELECT `Email`, `Password` FROM `Companies` WHERE `Email` = ? AND `Password` = ?";
        Connection connection = connectionPool.getConnection();
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetEmailAndPassword)){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.executeQuery();
            try(ResultSet resultSet = preparedStatement.getResultSet()) { //TODO the check
                boolean emailsEquals = resultSet.getString("Email").equalsIgnoreCase(email);
                boolean passwordsEquals = resultSet.getString("Password").equalsIgnoreCase(password);
                if (resultSet.next()) {
                    if (emailsEquals && passwordsEquals) {
                        result = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } //TODO if need the finally block here?? If YES - in ALL methods to change
        finally {//YES
            connectionPool.restoreConnection(connection);
        }
        return result;
    }

    @Override
    public void addCompany(Company company) {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCompany = "INSERT INTO `Companies` (`name`, `email`, `Password`) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCompany)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            //preparedStatement.setString(4, company.getClientStatus().toString());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Creating Company failed, no rows affected."); //TODO Exception - Create
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void updateCompany(Company company) {
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
                throw new SQLException("Update Company failed, no rows affected."); //TODO Exception - Update
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void deleteCompany(int companyID) { // I do not delete object, I add status inactive instead // TODO  to add status inactive to ClientStatus after deleting
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCompanyStatus = "UPDATE `Companies` SET `Status` = `inactive` WHERE `id` = ?"; //TODO TO CHECK `inactive`
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCompanyStatus)) {
            preparedStatement.setInt(1, companyID);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Delete Company failed, no rows affected."); //TODO Exception - Delete
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public List<Company> getAllCompanies() {
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
                    return companies;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
        return null;
    }

    @Override
    public Company getOneCompany(int companyID) {
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
                    throw new SQLException("Creating company failed, no ID obtained."); //FIXME  Exception get
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
        return company;
    }
}
