package businesslogic.facade;

import com.sun.xml.internal.bind.v2.TODO;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Category;
import java_beans_entities.ClientStatus;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            throw new CouponSystemException("DB error", e);
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return loginTrue;
    }

    public static int loginCustomerReturnId(String email, String password) throws CouponSystemException {
        int customerId = 0;
        try {
            if (customersDAO.isCustomerExists(email, password)) {
                Customer customer = customersDAO.getCustomerByEmail(email);
                ClientStatus customerStatus = customer.getClientStatus();
                if (customerStatus != ClientStatus.INACTIVE) { /* The Customer that was deleted(has the INACTIVE status) can not login */
                    try {
                        customerId = customersDAO.loginCustomer(email);
                    } catch (SQLException e) {
                        throw new CouponSystemException("DB error.", e);
                    }
                }else {
                    throw new CouponSystemException("Customer does not exists/Status is INACTIVE");
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("An error has occurred.", e);
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
        return customerId;
    }

    public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
        int couponExpiryDate = coupon.getEndDate().compareTo(LocalDateTime.now());
        List<Coupon> couponListByCustomerId;
        Customer customer;
        try {
            customer = customersDAO.getOneCustomer(customerId);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error.", e);
        }
        ClientStatus customerStatus = customer.getClientStatus();
        if (customerStatus != ClientStatus.INACTIVE) {
            try {
                couponListByCustomerId = couponsDAO.getCustomerCouponsByCustomerId(customerId);
            } catch (SQLException e) {
                throw new CouponSystemException("DB error.", e);
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
                            int newAmount = coupon.getAmount();
                            newAmount--;
                            coupon.setAmount(newAmount);
                            couponsDAO.updateCoupon(coupon);
                        } catch (SQLException e) {
                            throw new CouponSystemException("DB error.");
                        } catch (InternalSystemException e) {
                            throw new CouponSystemException("DB error.", e);
                        }
                    } else {
                        throw new CouponSystemException("The coupon date is expired.");
                    }
                } else {
                    throw new CouponSystemException("Not enough coupons to purchase.");
                }
            } else {
                throw new CouponSystemException("The customer with Id: " + customerId + " has already the coupon with Id" + coupon.getId());
            }
        } else {
            throw new CouponSystemException("Customer does not exists/Status is INACTIVE");
        }
    }

    public List<Coupon> getCustomerCoupons() throws CouponSystemException {
        List<Coupon> couponList;
        try {
            //login  не нужно проверять где нет келет
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
