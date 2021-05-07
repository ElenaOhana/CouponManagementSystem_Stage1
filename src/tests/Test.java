package tests;

import DB.DBPseudoDataManager;
import businesslogic.facade.AdminFacade;
import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import exceptions.TestException;
import java_beans_entities.Coupon;
import login.ClientType;
import login.LoginManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void testAll() {
        DBPseudoDataManager.dropCreateTables();

        try {
            facadeTesting();
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    private static void facadeTesting() throws TestException {
        System.out.println("-------------------------------------Administrator Facade Test-----------------------------------------");
        try {
            AdminFacade adminFacade1 = (AdminFacade) LoginManager.login("adin@adin.com", "admin", ClientType.ADMINISTRATOR);// Should provide "Wrong admin credentials" error.
            AdminFacade adminFacade = (AdminFacade) LoginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR); // Getting company details successfully
        } catch (CouponSystemException e) {
            System.out.println("Wrong admin credentials"); // sout moshno! must System.out.println("Wrong admin credentials");
        }


        System.out.println("-------------------------------------Company Facade Test-----------------------------------------");
        CompanyFacade companyFacade;
        try {
            companyFacade = (CompanyFacade) LoginManager.login("companyA@gmail.com", "1111", ClientType.COMPANY);
            Coupon coupon = addCouponWithWrongCompanyId();
            if (companyFacade != null) {
                companyFacade.addCoupon(coupon);
            }

            Coupon coupon2 = addCouponWithRightCompanyId();
            if (companyFacade != null) {
                companyFacade.addCoupon(coupon2);
            }

            if (companyFacade != null) {
                companyFacade.updateCoupon(coupon2);
            }
        } catch ( CouponSystemException e) {
            throw new TestException("Company Id is 3 instead of 1.", e);
        }

        //System.out.println("-------------------------------------Customer Facade Test-----------------------------------------");
    }

    private static Coupon addCouponWithRightCompanyId() throws TestException{
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(1, 1, 3, "Family hofesh", "Family spa rest with babes", startDate, endDate, 30, 300, "image");
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


