package facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Category;
import java_beans_entities.Company;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.util.List;

public class CompanyFacade extends ClientFacade{
    private int companyId;

    public CompanyFacade(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean login(String mail, String password) throws CouponSystemException {
        boolean loginTrue;
        try {
            loginTrue =  companiesDAO.isCompanyExists(mail, password);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
        return loginTrue;
    }

    public void addCoupon(Coupon coupon) throws CouponSystemException {
        try {
            couponsDAO.addCoupon(coupon);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error", e);
        }
    }

    public void updateCoupon(Coupon coupon) throws CouponSystemException {
        try {
            couponsDAO.updateCoupon(coupon);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error", e);
        }
    }

    public void deleteCoupon(int couponId) throws CouponSystemException {
        try {
            couponsDAO.deleteCoupon(couponId);
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error", e);
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
            return couponsDAO.getCouponListByCategory(category);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {
        try {
            return couponsDAO.getCouponListByMaxPrice(maxPrice);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
    }

    public Company getCompanyDetails(int companyId) throws CouponSystemException { // TODO without parameter how will we know what Company to return?
        try {
            return companiesDAO.getOneCompany(companyId);
        } catch (InternalSystemException | SQLException e) {
            throw new CouponSystemException("DB error", e); //and write to log file
        }
    }
}
