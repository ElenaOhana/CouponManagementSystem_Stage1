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
            throw new CouponSystemException("Wrong admin credentials");
        }
        return loginOk;
    }

    public void addCompany(Company company) throws CouponSystemException {
        boolean isCompanyExists;
        try {
            isCompanyExists = companiesDAO.isCompanyExists(company.getEmail(), company.getPassword());
            if (!isCompanyExists) {
                companiesDAO.addCompany(company);
            } else {
                throw new InternalSystemException("Company already exist error.");
            }
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

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

    public void deleteCompanyAsChangeStatus(int companyId) throws CouponSystemException {
        List<Integer> customerIdList;
        try {
            List<Coupon> couponList = couponsDAO.getCompanyCouponsByCompanyId(companyId);
            for (Coupon coupon : couponList) {
                couponsDAO.deleteCouponAsChangeStatus(coupon.getId()); /* Delete(as change status of) all coupons that Company created */
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

    public List<Company> getAllCompanies() throws CouponSystemException {
        List<Company> companiesList;
        try {
            companiesList = companiesDAO.getAllCompanies(); //I'm return a whole object of Company
            for (Company company : companiesList) {
                ArrayList<Coupon> couponsOfCompany = (ArrayList<Coupon>) couponsDAO.getCompanyCouponsByCompanyId(company.getId());
                company.setCoupons(couponsOfCompany);
            }
        } catch (SQLException e) {
                throw new CouponSystemException("DB error.", e);
        }
        return companiesList;
    }

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

    public void addCustomer(Customer customer) throws CouponSystemException {
        try {
            if (customer != null) {
                customersDAO.addCustomerWithReturnCustomer(customer);
            } else throw new NotFoundException("Customer is not found") ;
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
            customersDAO.deleteCustomerPurchase(customerID);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public List<Customer> getAllCustomers() throws CouponSystemException {
        List<Customer> customerList;
        try {
            customerList = customersDAO.getAllCustomers();
            for (Customer customer : customerList) { //I'm return a whole object of Customer
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
