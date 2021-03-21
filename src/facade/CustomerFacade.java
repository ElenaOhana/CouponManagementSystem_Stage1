package facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Category;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerFacade extends ClientFacade {
    private int customerId;

    public CustomerFacade(int customerId) {
        this.customerId = customerId;
    }

    public static boolean login(String email, String password) throws CouponSystemException {
        boolean loginTrue;
        try {
            loginTrue = customersDAO.isCustomerExists(email, password);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return loginTrue;
    }

    public static int loginCustomerReturnId(String email, String password) throws CouponSystemException {
        int customerId = 0;
        try {
            if (customersDAO.isCustomerExists(email, password)) {
                try {
                    customerId = customersDAO.loginCustomer(email, password);
                } catch (SQLException e) {
                    throw new CouponSystemException("DB error.");
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("An error has occurred.", e);
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customerId;
    }

    public void purchaseCoupon(Coupon coupon) throws CouponSystemException { // Fixme - here transaction? access to Connection from here?
        int couponExpiryDate = coupon.getEndDate().compareTo(LocalDateTime.now());
        List<Coupon> couponListByCustomerId;
        try {
            couponListByCustomerId = couponsDAO.getCustomerCouponsByCustomerId(customerId);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.");
        }
        if (!couponListByCustomerId.contains(coupon)) {
            if (coupon.getAmount() > 0) {
                if (couponExpiryDate > 0) {
                    try {
                        couponsDAO.addCouponPurchase(customerId, coupon.getId());
                    } catch (SQLException e) {
                        throw new CouponSystemException("DB error.");
                    } catch (InternalSystemException e) {
                        throw new CouponSystemException("DB error.", e);
                    }
                    try {
                        couponsDAO.deleteCoupon(coupon.getId()); // TODO ה. להוריד את הכמות במלאי של הקופון ב 1
                    } catch (SQLException e) {
                        throw new CouponSystemException("DB error.");
                    } catch (InternalSystemException e) {
                        throw new CouponSystemException("DB error.", e);
                    }
                }
            }
        }
    }

    public List<Coupon> getCustomerCoupons() throws CouponSystemException {
        List<Coupon> couponList;
        try {
            //if (login())// Fixme צריך לבדור login?
            couponList = couponsDAO.getCustomerCouponsByCustomerId(customerId);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.");
        }
        return couponList;
    }

    public List<Coupon> getCustomerCoupons(Category category) throws CouponSystemException {
        List<Coupon> couponListByCustomer;
        List<Coupon> couponListByCategory;
        try {
            //couponListByCustomer = couponsDAO.getCustomerCouponsByCustomerId(customerId); // Fixme איך לבדוק login - transaction or filter on Category?
            couponListByCategory = couponsDAO.getCouponListByCategory(category);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.");
        }
        return couponListByCategory;
    }

    public List<Coupon> getCustomerCoupons(double maxPrice) throws CouponSystemException {
        List<Coupon> couponListByMaxPrice;
        try {
            couponListByMaxPrice = couponsDAO.getCouponListLessThanMaxPrice(maxPrice);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.");
        }
        return couponListByMaxPrice;
    }

    public Customer getCustomerDetails() throws CouponSystemException {
        Customer customer;
        try {
            customer = customersDAO.getOneCustomer(customerId);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.");
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customer;
    }
}
