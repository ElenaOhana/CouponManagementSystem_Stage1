package tests;

import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import java_beans_entities.Coupon;
import login.ClientType;
import login.LoginManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test2 {

    public static void testAll() {
        LocalDateTime startDate = LocalDateTime.parse("2021-14-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-14-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(1111, 1, "Nofesh", "Spa in Galil", startDate, endDate, 30, 300,"image");
        CompanyFacade companyFacade;
        try {
            companyFacade = (CompanyFacade) LoginManager.login("companyA@gmail.com", "1111", ClientType.COMPANY);
            companyFacade.updateCoupon(coupon);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        } catch (NullPointerException e){}

    }
}

