package businesslogic.facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Company;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/*
    :Clients הגישה למערכת מתחלקת לשלושה סוגי
    ניהול רשימת החברות ורשימת הלקוחות. – Administrator .1
    ניהול רשימת קופונים המשויכים לחברה. – Company .2
    רכישת קופונים. – Customer .3
     */
public class AdminFacade extends ClientFacade {

    private final String email = "admin@admin.com";
    private final String password = "admin";

    private Set<Company> companySet;
    private Set<Customer> customerSet;

    public AdminFacade() {
    }

    public boolean add(Company company) {
        companySet.add(company);
        return true;//todo  maybe? Exception
    }
    public boolean add(Customer customer) {
        return customerSet.add(customer);// todo maybe? Exception
    }
    /////////////////

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

    public void deleteCompany(int companyId) throws CouponSystemException {
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

   /* public List<Company> getAllCompanies() {

    }*/
}
