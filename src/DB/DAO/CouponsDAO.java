package DB.DAO;

import exceptions.InternalSystemException;
import java_beans_entities.Category;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.util.List;

public interface CouponsDAO {

    void addCoupon(Coupon coupon) throws SQLException, InternalSystemException;

    void updateCoupon(Coupon coupon) throws SQLException, InternalSystemException;

    void deleteCoupon(int couponID) throws SQLException, InternalSystemException;

    List<Coupon> getAllCoupons() throws SQLException;

    Coupon getOneCoupon(int couponID) throws SQLException, InternalSystemException;

    void addCouponPurchase(int customerID, int couponId) throws SQLException, InternalSystemException;

    void deleteCouponPurchase(int customerID, int couponId) throws SQLException, InternalSystemException;

    List<Coupon> getCouponListByCategory(Category category) throws SQLException;

    List<Coupon> getCouponListByMaxPrice(double maxPrice) throws SQLException;
}
