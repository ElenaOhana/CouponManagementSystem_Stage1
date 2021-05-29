package businesslogic.facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import exceptions.NotFoundException;
import java_beans_entities.Company;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.util.*;

public class AdminFacade extends ClientFacade {

    public AdminFacade() {
    }

    /**
     * The method checks by email and password param if the admin insert right credentials.
     * Returns boolean if true, otherwise throws CouponSystemException.
     */
    public static boolean login(String email, String password) throws CouponSystemException {
        boolean loginOk;
        boolean emailOk = email.equalsIgnoreCase("admin@admin.com");
        boolean passwordOk = password.equalsIgnoreCase("admin");
        if (emailOk && passwordOk) {
            loginOk = true;
        } else {
            throw new CouponSystemException("Wrong admin credentials");
        }
        return loginOk;
    }

    /**
     * The method receives the company and checks by company email and password if the company exists in Database, if company doesn't exists - the method adds company,
     * if the company is null - NotFoundException is thrown,
     * otherwise throws CouponSystemException.
     */
    public void addCompany(Company company) throws CouponSystemException {
        boolean isCompanyExists;
        try {
            if (company != null) {
                isCompanyExists = companiesDAO.isCompanyExists(company.getEmail(), company.getPassword());
                if (!isCompanyExists) {
                    companiesDAO.addCompany(company);
                } else {
                    throw new InternalSystemException("Company already exist error.");
                }
            }  else throw new NotFoundException("Company is not found");
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    /**
     * The method receives the company and gets by its id the company from DB in order to check if the client trying to update the company name - in this case, CouponSystemException is thrown,
     * otherwise, the company is updated in DB.
     */
    public void updateCompany(Company company) throws CouponSystemException {
        if (company != null) {
            try {
                Company company1 = companiesDAO.getOneCompany(company.getId());
                /* Without this check the Database will not allow to update the Company name too, but I wanted the client to receive any feedback on wrong action. */
                if (!company1.getName().equalsIgnoreCase(company.getName())) {
                    throw new CouponSystemException("Trying update a company name error.");
                }
                companiesDAO.updateCompany(company);
            } catch (SQLException | InternalSystemException e) {
                throw new CouponSystemException("DB error.", e);
            }
        }
    }

    /**
     * By receiving the company Id the method changes status to DISABLE of all coupons that the Company created,
     * deletes customer purchase from customers_vs_coupons table,
     * and changes status to INACTIVE of the company.
     */
    public void deleteCompanyAsChangeStatus(int companyId) throws CouponSystemException {
        List<Integer> customerIdList;
        try {
            List<Coupon> couponList = couponsDAO.getCompanyCouponsByCompanyId(companyId);
            for (Coupon coupon : couponList) {
                couponsDAO.deleteCouponAsChangeStatus(coupon.getId()); /*Delete(as change status) of all coupons that Company created*/
                customerIdList = couponsDAO.getCustomersIdFromCustomersVsCoupons(coupon.getId());
                if (customerIdList != null) {
                    for (Integer customerId : customerIdList) {
                        customersDAO.deleteCustomerPurchase(customerId); /* Delete customer purchase */
                    }
                } else {
                    throw new NotFoundException("customerIdList not found " + customerIdList);
                }
            }
            companiesDAO.deleteCompanyAsChangeStatus(companyId); /* Delete(as change status of) company */

        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    /**
     * The method gets all companies(includes their coupons) from DB and returns them.
     * The CouponSystemException is thrown when the SQLException is thrown in the SQL query (in CouponsDAO or CompaniesDAO class).
     */
    public List<Company> getAllCompanies() throws CouponSystemException {
        List<Company> companiesList;
        try {
            companiesList = companiesDAO.getAllCompanies(); /* I'm return a whole object of Company */
            for (Company company : companiesList) {
                ArrayList<Coupon> couponsOfCompany = (ArrayList<Coupon>) couponsDAO.getCompanyCouponsByCompanyId(company.getId());
                company.setCoupons(couponsOfCompany);
            }
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return companiesList;
    }

    /**
     * By receiving the company Id the method gets one company(includes its coupons) from DB and returns it.
     * The CouponSystemException is thrown when the SQLException or InternalSystemException is thrown in the SQL query (in CouponsDAO or CompaniesDAO class).
     */
    public Company getOneCompany(int companyID) throws CouponSystemException {
        Company company;
        try {
            company = companiesDAO.getOneCompany(companyID);
            company.setCoupons((ArrayList<Coupon>) couponsDAO.getCompanyCouponsByCompanyId(companyID));
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return company;
    }

    /**
     * The method receives the customer and checks by customer email and password if the customer exists in Database, if customer doesn't exists - the method adds customer,
     * if the customer is null - NotFoundException is thrown,
     * otherwise throws CouponSystemException.
     */
    public void addCustomer(Customer customer) throws CouponSystemException {
        boolean isCustomerExists;
        try {
            if (customer != null) {
                isCustomerExists = customersDAO.isCustomerExists(customer.getEmail(), customer.getEmail());
                if (!isCustomerExists) {
                    customersDAO.addCustomer(customer);
                }
            } else throw new NotFoundException("Customer is not found");
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    /**
     * The method receives the customer, checks if the client trying to update the customer id - in this cas, InternalSystemException is thrown from CustomersDAO class,
     * otherwise, the customer is updated in DB.
     */
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
            customersDAO.deleteCustomerPurchase(customerID);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public List<Customer> getAllCustomers() throws CouponSystemException {
        List<Customer> customerList;
        try {
            customerList = customersDAO.getAllCustomers();
            for (Customer customer : customerList) {  /* I'm return a whole object of Customer */
                ArrayList<Coupon> customerCoupons = (ArrayList<Coupon>) couponsDAO.getCustomerCouponsByCustomerId(customer.getId());
                customer.setCoupons(customerCoupons);
            }
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customerList;
    }

    public Customer getOneCustomer(int customerID) throws CouponSystemException {
        Customer customer;
        try {
            customer = customersDAO.getOneCustomer(customerID);
            customer.setCoupons((ArrayList<Coupon>) couponsDAO.getCustomerCouponsByCustomerId(customerID));
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customer;
    }
}
