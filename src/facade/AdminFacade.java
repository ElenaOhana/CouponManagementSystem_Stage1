package facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Company;

import java.sql.SQLException;
import java.util.List;

public class AdminFacade extends ClientFacade {
    private final String email = "admin@admin.com";
    private final String password = "admin";

    @Override
    public boolean login(String email, String password) throws CouponSystemException { // Hard-Coded- יש לבדוק אותם כ
        boolean loginOk = false;
        boolean emailOk = email.equalsIgnoreCase("admin@admin.com");
        boolean passwordOk = password.equalsIgnoreCase("admin");
        if (emailOk && passwordOk) {
            loginOk = true;
        }
        return loginOk;
    }

    public void addCompany(Company company) throws CouponSystemException {
        try {
            companiesDAO.addCompany(company);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public void updateCompany(Company company) throws CouponSystemException {
        try {
            companiesDAO.updateCompany(company);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public void deleteCompany(int companyId) throws CouponSystemException {
        try {
            companiesDAO.deleteCompany(companyId);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

   /* public List<Company> getAllCompanies() {

    }*/
}
