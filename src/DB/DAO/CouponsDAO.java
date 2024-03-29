package DB.DAO;

import exceptions.InternalSystemException;
import java_beans_entities.Category;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.util.List;

public interface CouponsDAO {

    void addCoupon(Coupon coupon) throws SQLException, InternalSystemException;

    void updateCoupon(Coupon coupon) throws SQLException, InternalSystemException;

    /**
     * This method changes coupon status to DISABLE. */
    void deleteCouponAsChangeStatus(int couponID) throws SQLException, InternalSystemException;

    List<Coupon> getAllCoupons() throws SQLException;

    Coupon getOneCoupon(int couponID) throws SQLException, InternalSystemException;

    void addCouponPurchase(int customerID, int couponId) throws SQLException, InternalSystemException;

    List<Coupon> getCouponListByCategory(Category category) throws SQLException;

    List<Coupon> getCouponListLessThanMaxPrice(double maxPrice) throws SQLException;

    List<Coupon> getCustomerCouponsByCustomerId(int customerId) throws SQLException;

    List<Coupon> getCompanyCouponsByCompanyId(int companyId) throws SQLException;

    List<Integer> getCustomersIdFromCustomersVsCoupons(int couponId) throws SQLException;
}
