package tests;

import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import exceptions.TestException;
import java_beans_entities.Coupon;
import login.ClientType;
import login.LoginManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test2 {

    public static void testAll() {
        try {
            adminFacadeTest();
        } catch (TestException e) {
            e.printStackTrace();
        }

    }

    private static void adminFacadeTest() throws TestException {
        System.out.println("-------------------------------------Admin Facade Test-----------------------------------------");
        CompanyFacade companyFacade;
        try {
            companyFacade = (CompanyFacade) LoginManager.login("companyA@gmail.com", "1111", ClientType.COMPANY);
            /*Coupon coupon = addCouponWithWrongCompanyId();
            companyFacade.addCoupon(coupon);*/

            Coupon coupon2 = addCouponWithRightCompanyId();
            companyFacade.addCoupon(coupon2);

            companyFacade.updateCoupon(coupon2);
        } catch ( CouponSystemException e) {
            throw new TestException("Company Id is 3 instead of 1.", e);
        }

    }

    private static Coupon addCouponWithRightCompanyId() throws TestException{
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(1, 1, 3, "Family hofesh", "Spa with babes", startDate, endDate, 30, 300, "image");
        return coupon;
    }

    private static Coupon addCouponWithWrongCompanyId() throws TestException{
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(1, 3, 3, "Nofesh", "Spa in Galil", startDate, endDate, 30, 300, "image");
        return coupon;
    }
        //LocalDateTime endDate2 = LocalDateTime.now();
}


