package DB.DAO;

import java_beans_entities.Company;

import java.util.List;

public interface CompaniesDAO {

     boolean isCompanyExists(String email, String password);

     void addCompany(Company company);

     void updateCompany(Company company);

     void deleteCompany(int companyID);

     List<Company> getAllCompanies();

     Company getOneCompany(int companyID);
}
