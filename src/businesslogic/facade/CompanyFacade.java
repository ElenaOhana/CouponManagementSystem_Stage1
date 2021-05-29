package businesslogic.facade;

import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import exceptions.NotFoundException;
import java_beans_entities.Category;
import java_beans_entities.ClientStatus;
import java_beans_entities.Company;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyFacade extends ClientFacade {
    private int companyId;

    public CompanyFacade(int companyId) {
        this.companyId = companyId;
    }

    public static boolean login(String email, String password) throws CouponSystemException {
        boolean loginTrue;
        try {
            loginTrue = companiesDAO.isCompanyExists(email, password);
            if (loginTrue) {
                return true;
            } else {
                throw new CouponSystemException("DB error. Wrong credentials.");
            }
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error", e);
        }
    }
/**
* The method checks by email and password param if company exists, and by ClientStatus param if company was deleted or not.
* Returns the company Id if company exists and has ACTIVE client status, otherwise throws CouponSystemException. */
    public static int loginCompanyReturnId(String email, String password) throws CouponSystemException {
        int companyId = 0;
        try {
            if (companiesDAO.isCompanyExists(email, password)) {
                Company company = companiesDAO.getCompanyByEmail(email);
                ClientStatus companyStatus = company.getClientStatus();
                if (companyStatus != ClientStatus.INACTIVE) { /* The Company that was deleted(has the INACTIVE status) can not login */
                    try {
                        companyId = companiesDAO.loginCompany(email);
                    } catch (SQLException e) {
                        throw new CouponSystemException("DB error.", e);
                    }
                } else {
                    throw new CouponSystemException("Company does not exists/Status is INACTIVE");
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
        boolean containsTitle = false;
        int counter = 0;
        try {
            if (coupon != null) {
                if (coupon.getCompanyId() == companyId) {
                    List<Coupon> couponList = couponsDAO.getCompanyCouponsByCompanyId(companyId);
                    for (Coupon coupon1 : couponList) {
                        String receiveTitle = coupon.getTitle();
                        String fromDBTitle = coupon1.getTitle();
                        if (receiveTitle.equals(fromDBTitle)) {
                            counter++;
                        }
                    }
                    if (counter > 0) {
                        containsTitle = true;
                    }
                    if (!couponList.contains(coupon) && !containsTitle) {
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
                if (coupon.getCompanyId() == companyId) {
                    couponsDAO.updateCoupon(coupon);
                } else {
                    throw new CouponSystemException("The action is illegal.");
                }
            }
        } catch (InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        } catch (SQLException e) {
            throw new CouponSystemException("DB error", e);
        }
    }

    public void deleteCoupon(int couponId) throws CouponSystemException {
        List<Integer> customerIdList;
        try {
            Coupon coupon = couponsDAO.getOneCoupon(couponId);
            if (coupon.getCompanyId() == companyId) {
                couponsDAO.deleteCouponAsChangeStatus(couponId); /* Change status to DISABLE (as delete) of coupon */
                customerIdList = couponsDAO.getCustomersIdFromCustomersVsCoupons(coupon.getId());
                if (customerIdList != null) {
                    if (customerIdList.isEmpty()) {
                        throw new NotFoundException("There are no customer purchases for coupon " + couponId);
                    }
                    for (Integer customerId : customerIdList) {
                        customersDAO.deleteCustomerPurchase(customerId); /* Delete customer purchase */
                    }
                } else {
                    throw new NotFoundException("Customers are not found");
                }
            } else {
                throw new CouponSystemException("The action is illegal.");
            }
        } catch (SQLException | InternalSystemException e) {
            throw new CouponSystemException("DB error.", e);
        }
    }

    public List<Coupon> getCompanyCoupons() throws CouponSystemException {
        List<Coupon> couponList;
        List<Coupon> couponListByCompany = new ArrayList<>();
        try {
            couponList = couponsDAO.getAllCoupons();
            for (Coupon coupon : couponList) {
                if (coupon.getCompanyId() == companyId) {
                    couponListByCompany.add(coupon);
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
        return couponListByCompany;
    }

    public List<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {
        List<Coupon> couponList;
        List<Coupon> couponListByCompany = new ArrayList<>();
        try {
            couponList = couponsDAO.getCouponListByCategory(category);
            for (Coupon coupon : couponList) {
                if (coupon.getCompanyId() == companyId) {
                    couponListByCompany.add(coupon);
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
        return couponListByCompany;
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {
        List<Coupon> couponListUpToPrice = new ArrayList<>();
        try {
            List<Coupon> couponList =  couponsDAO.getCouponListLessThanMaxPrice(maxPrice);
            for (Coupon coupon : couponList) {
                if (coupon.getCompanyId() == companyId) {
                    couponListUpToPrice.add(coupon);
                }
            }
        } catch (SQLException e) {
            throw new CouponSystemException("DB error");
        }
        return couponListUpToPrice;
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
