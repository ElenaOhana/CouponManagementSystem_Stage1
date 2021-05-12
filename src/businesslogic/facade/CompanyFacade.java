package businesslogic.facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Category;
import java_beans_entities.Company;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.util.List;


/*
    :Clients הגישה למערכת מתחלקת לשלושה סוגי
    ניהול רשימת החברות ורשימת הלקוחות. – Administrator .1
    ניהול רשימת קופונים המשויכים לחברה. – Company .2
    רכישת קופונים. – Customer .3
     */
public class CompanyFacade extends ClientFacade {
    private int companyId;

    public CompanyFacade(int companyId) {
        this.companyId = companyId;
    }

    public static boolean login(String email, String password) throws CouponSystemException {
        boolean loginTrue;
        try {
            loginTrue = companiesDAO.isCompanyExists(email, password);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error", e);//TODO Company doesn't exist
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return loginTrue;
    }

    public static int loginCompanyReturnId(String email, String password) throws CouponSystemException {
        int companyId = 0;
        try {
            if (companiesDAO.isCompanyExists(email, password)) {
                try {
                    companyId = companiesDAO.loginCompany(email);
                } catch (SQLException e) {
                    throw new CouponSystemException("DB error.", e);
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("An error has occurred.", e);
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return companyId;
    }

    public void addCoupon(Coupon coupon) throws CouponSystemException {
        try {
            if (coupon != null) {
                if (coupon.getCompanyId() == companyId) { // TODO to check in all places where we (have to check Login) if the coupons.companyId == companyId
                    List<Coupon> couponList = couponsDAO.getCompanyCouponsByCompanyId(companyId);
                    if (!couponList.contains(coupon)) {
                        couponsDAO.addCoupon(coupon);
                    } else {
                        throw new CouponSystemException("An error has occurred. The coupon already exists.");
                    }
                } else {
                    throw new CouponSystemException("The action is illegal");
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("DB error", e);
        }
    }

    //Documentation: I do 2 catch blocks: with cause for InternalSystemException that thrown from internal check in query method, and without cause for SQLException that throws preparedStatement.
    public void updateCoupon(Coupon coupon) throws CouponSystemException {
        try {
            if (coupon != null) {
                couponsDAO.updateCoupon(coupon);
            }
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error", e);
        }
    }

    //Fixme
    public void deleteCoupon(int couponId) throws CouponSystemException {
        try {
            couponsDAO.deleteCouponAsChangeStatus(couponId);
            // todo from tirg
            deleteCoupon(couponId);
            //couponsDAO.deleteCouponPurchase(); // customerID ??? Holds customersList?
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public List<Coupon> getCompanyCoupons() throws CouponSystemException {
        List<Coupon> couponList;
        try {
            couponList = couponsDAO.getAllCoupons();
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
        return couponList;
    }

    public List<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {
        try {
            return couponsDAO.getCouponListByCategory(category); // TODO to ask if it OK to return in try block
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {
        try {
            return couponsDAO.getCouponListLessThanMaxPrice(maxPrice);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
    }

    public Company getCompanyDetails() throws CouponSystemException { // We know what CompanyId because it that connected
        Company company;
        try {
            company = companiesDAO.getOneCompany(companyId);
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
        return company;
    }
}
