package businesslogic.facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Company;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    :Clients הגישה למערכת מתחלקת לשלושה סוגי
    ניהול רשימת החברות ורשימת הלקוחות. – Administrator .1
    ניהול רשימת קופונים המשויכים לחברה. – Company .2
    רכישת קופונים. – Customer .3
     */
public class AdminFacade extends ClientFacade {
    private Set<Company> companySet = new HashSet<>();
    private Set<Customer> customerSet = new HashSet<>();

    public AdminFacade() {
    }

    public boolean add(Company company) {
        companySet.add(company);
        return true;//todo  maybe? Exception
    }

    public boolean add(Customer customer) {
        return customerSet.add(customer);// todo maybe? Exception
    }

    public static boolean login(String email, String password) throws CouponSystemException { // Hard-Coded- יש לבדוק אותם כ
        boolean loginOk;
        boolean emailOk = email.equalsIgnoreCase("admin@admin.com");
        boolean passwordOk = password.equalsIgnoreCase("admin");
        if (emailOk && passwordOk) {
            loginOk = true;
        } else {
            throw new CouponSystemException("Wrong credentials");
        }
        return loginOk;
    }

    public void addCompany(Company company) throws CouponSystemException {
        boolean isCompanyNameExists;
        boolean isCompanyEmailExists;
        try {
            isCompanyNameExists = companiesDAO.isCompanyNameExists(company.getName());
            isCompanyEmailExists = companiesDAO.isCompanyEmailExists(company.getEmail());
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.", e);
        }
        try {
            if (!(isCompanyNameExists && isCompanyEmailExists)) { // To prevent the SQLException about the explicitly defined name and email as unique in DB.
                companiesDAO.addCompany(company);
            } else {
                throw new CouponSystemException("DB error.");
            }
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

    public void deleteCompanyAsChangeStatus(int companyId) throws CouponSystemException {
        try {
            List<Coupon> couponList = couponsDAO.getCompanyCouponsByCompanyId(companyId);//Delete all coupons that Company created
            for (Coupon coupon : couponList) {
                couponsDAO.deleteCouponAsChangeStatus(coupon.getId());
            }
            companiesDAO.deleteCompanyAsChangeStatus(companyId);
            //couponsDAO.deleteCouponPurchase(customersId); // יש למחוק בנוסף גם את היסטוריית רכישת הקופונים של החברה ע"י לקוחות

        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public List<Company> getAllCompanies() throws CouponSystemException {
        List<Company> companiesList;
        try {
            companiesList = companiesDAO.getAllCompanies(); //I'm return a whole object
        } catch (SQLException e) {
                throw new CouponSystemException("DB error.", e);
        }
        return companiesList;
    }

    public Company getOneCompany(int companyID) throws CouponSystemException {
        Company company;
        try {
            company = companiesDAO.getOneCompany(companyID);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return company;
    }

    public void addCustomer(Customer customer) throws CouponSystemException {
        try {
            customersDAO.addCustomer(customer);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public void updateCustomer(Customer customer) throws CouponSystemException {
        try {
            customersDAO.updateCustomer(customer);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public void deleteCustomerAsChangeStatus(int customerID) throws CouponSystemException {
        try {
            customersDAO.deleteCustomerAsChangeStatus(customerID);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public List<Customer> getAllCustomers() throws CouponSystemException {
        List<Customer> customerList;
        try {
            customerList = customersDAO.getAllCustomers();
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customerList;
    }

    public Customer getOneCustomer(int customerID) throws CouponSystemException {
        Customer customer;
        try {
            customer = customersDAO.getOneCustomer(customerID);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customer;
    }
}
