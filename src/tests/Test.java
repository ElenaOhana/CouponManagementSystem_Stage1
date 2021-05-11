package tests;

import DB.DBPseudoDataManager;
import businesslogic.facade.AdminFacade;
import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import exceptions.TestException;
import java_beans_entities.Company;
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
        String tab = "      ";
        AdminFacade adminFacade = null;
        Company company = new Company("New Farm", "new_farm@company2.com", "new222");

        System.out.println("-------------------------------------Administrator Facade Test-----------------------------------------");
        System.out.println(tab + "LOGIN METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade = (AdminFacade) LoginManager.login("adin@adin.com", "admin", ClientType.ADMINISTRATOR); /* Should provide "Wrong admin credentials" error. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
        }
        try {
            adminFacade = (AdminFacade) LoginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR); /* Should get adminFacade successfully, because it's right credentials */
            System.out.println(tab + "Right admin credentials");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println(tab + "ADD_COMPANY METHOD TESTING:");
        try {
            assert adminFacade != null;
            adminFacade.addCompany(new Company("Zara women", "zara_women@company.com", "ZARA123")); /* Should create a new company successfully */
            System.out.println(tab + "Create a new company successfully");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Zara women", "zara_women@company.com", "ZARA123")); /* Should provide "Company already exist error."*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Zara women", "zara@company.com", "ZARA123")); /* Should provide "Duplicate entry 'Zara women' for key 'Name_UNIQUE'"*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
        }
        System.out.println();
        System.out.println(tab + "UPDATE METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(3, "New Farm","new_farm@company.com", "2222")); /* Should provide "Getting company failed, no rows affected" error, because we trying update a non exists company. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
        }

        try {
            adminFacade.addCompany(company); /* Should add a new company (New Farm). */
            adminFacade.updateCompany(new Company(3, "New Farm","new_farm@company.com", "2222")); /* Should update an existing company by id. */
            System.out.println(tab + "Updating a company successfully");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(3, "Pharmacy","new_farm@company.com", "2222")); /* Should provide an error, because we trying update a company name. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
        }

        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(2, "New Farm","new_farm@company.com", "2222")); /* Should provide an error, because we trying update a company id. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
        }


            // adminFacade.deleteCompanyAsChangeStatus(company.getId());/* Should delete an existing company (New Farm). */










       /* System.out.println("-------------------------------------Company Facade Test-----------------------------------------");
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
        }*/

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


