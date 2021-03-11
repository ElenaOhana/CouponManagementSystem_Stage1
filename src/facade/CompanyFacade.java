package facade;

import java_beans_entities.Category;
import java_beans_entities.Company;
import java_beans_entities.Coupon;

import java.util.List;

public class CompanyFacade extends ClientFacade{
    private int companyId;

    public CompanyFacade(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean login(String mail, String password) {
        return companiesDAO.isCompanyExists(mail, password);
    }

    public void addCoupon(Coupon coupon) {
        couponsDAO.addCoupon(coupon);
    }

    public void updateCoupon(Coupon coupon) {
        couponsDAO.updateCoupon(coupon);
    }

    public void deleteCoupon(int couponId) {
        couponsDAO.deleteCoupon(couponId);
    }

    public List<Coupon> getCompanyCoupons() {
        List<Coupon> couponList;
        couponList = couponsDAO.getAllCoupons();
        return couponList;
    }

    public List<Coupon> getCompanyCoupons(Category category) {
        return couponsDAO.getCouponListByCategory(category);
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) {
        return couponsDAO.getCouponListByMaxPrice(maxPrice);
    }

    public Company getCompanyDetails(int companyId) { // TODO without parameter how will we know what Company to return?
        return companiesDAO.getOneCompany(companyId);
    }
}
