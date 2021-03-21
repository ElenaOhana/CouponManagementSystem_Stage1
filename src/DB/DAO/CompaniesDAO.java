package DB.DAO;

import exceptions.InternalSystemException;
import java_beans_entities.Company;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.util.List;

public interface CompaniesDAO {

     boolean isCompanyExists(String email, String password) throws SQLException, InternalSystemException;

     void addCompany(Company company) throws SQLException, InternalSystemException;

     void updateCompany(Company company) throws SQLException, InternalSystemException;

     void deleteCompany(int companyID) throws SQLException, InternalSystemException;

     List<Company> getAllCompanies() throws SQLException;

     Company getOneCompany(int companyID) throws SQLException, InternalSystemException;

     Company getCompanyByName(String name) throws SQLException, InternalSystemException;

     boolean isCompanyNameExists(String name) throws SQLException;

     boolean isCompanyEmailExists(String email) throws SQLException;

     int loginCompany(String email, String password) throws SQLException;

}
