package tests;

import DB.DAO.CouponsDBDAO;
import DB.DAO.CustomersDBDAO;
import DB.DBPseudoDataManager;
import businesslogic.facade.AdminFacade;
import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import exceptions.TestException;
import java_beans_entities.*;
import login.ClientType;
import login.LoginManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void testAll() {

        DBPseudoDataManager.dropCreateTables();
        try {
            DBPseudoDataManager.createCategories(new Category("Shopping"));
            DBPseudoDataManager.createCategories(new Category("Pharmacy"));
            DBPseudoDataManager.createCategories(new Category("Spa and wellness"));
            DBPseudoDataManager.createCategories(new Category("Traveling"));
        } catch (SQLException | InternalSystemException e) {
            e.printStackTrace();
        }

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
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade = (AdminFacade) LoginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR); /* Should get adminFacade successfully, because it's right credentials */
            System.out.println(tab + "Right admin credentials");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println(tab + "ADD_COMPANY METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            assert adminFacade != null;
            adminFacade.addCompany(new Company("Zara women", "zara_women@company.com", "ZARA123")); /* Should create a new company successfully */
            System.out.println(tab + "Create a new company successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Zara women", "zara_women@company.com", "ZARA123")); /* Should provide "Company already exist error."*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Zara women", "zara@company.com", "ZARA123")); /* Should provide "Duplicate entry 'Zara women' for key 'Name_UNIQUE', because we trying to create Company with the same name."*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();

        System.out.println(tab + "UPDATE METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(3, "New Farm","new_farm@company.com", "2222")); /* Should provide "Getting company failed, no ID obtained" error, because we trying update a non exists company. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade.addCompany(company); /* Should add a new company (New Farm). */
            adminFacade.updateCompany(new Company(3, "New Farm","new_farm@company.com", "2222")); /* Should update an existing company by id. */
            System.out.println(tab + "Updating a company successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(3, "Pharmacy","new_farm@company.com", "2222")); /* Should provide an "Trying update a company name error.", because we trying update a company name. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(2, "New Farm","new_farm@company.com", "2222")); /* Should provide an error, because we trying update a company id. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();

        System.out.println(tab + "DELETE METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.deleteCompanyAsChangeStatus(company.getId()); /* Should provide an error, because we trying to pass Company without id */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }

        createCouponInDatabase();
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade.deleteCompanyAsChangeStatus(new Company(3, "New Farm","new_farm@company.com", "2222").getId()); /* Should change status (==delete) an existing company (New Farm). */
            System.out.println(tab + "Delete/change status company successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        createCustomerInDatabase();
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade.deleteCompanyAsChangeStatus(new Company(3, "New Farm","new_farm@company.com", "2222").getId()); /* Should change status (==delete) an existing company (New Farm). */
            System.out.println(tab + "Delete/change status customer successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }






       /* System.out.println("-------------------------------------Company Facade Test-----------------------------------------");
        CompanyFacade companyFacade;
        try {
            companyFacade = (CompanyFacade) LoginManager.login("zara_women@company.com", "ZARA123", ClientType.COMPANY);

            Coupon coupon2 = addCouponWithRightCompanyId();
            if (companyFacade != null) {
                companyFacade.addCoupon(coupon2);
            } else {
                System.out.println("companyFacade is null because wrong credentials");
            }

            Coupon coupon = addCouponWithWrongCompanyId();
            if (companyFacade != null) {
                companyFacade.addCoupon(coupon);
            } else {
                System.out.println("companyFacade is null because wrong credentials");
            }

            if (companyFacade != null) {
                companyFacade.updateCoupon(coupon2);
            }
        } catch ( CouponSystemException e) {
            throw new TestException("Company Id is 3 instead of 1.", e);
        }*/

        //System.out.println("-------------------------------------Customer Facade Test-----------------------------------------");
    }

    private static void createCustomerInDatabase() {
        CustomersDBDAO customersDBDAO = CustomersDBDAO.getInstance();
        try {
            customersDBDAO.addCustomer(new Customer("Ron", "Goldberg", "ron_gold12", "3456"));
        } catch (SQLException |InternalSystemException e) {
            e.printStackTrace();
        }
    }

    private static void createCouponInDatabase() {
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        CouponsDBDAO couponsDBDAO = CouponsDBDAO.getInstance();
        try {
            if (couponsDBDAO != null) {
                couponsDBDAO.addCoupon(new Coupon( 3, 2, "Body lotion", "Body wellness", startDate, endDate, 30, 300, "image"));
            }
        } catch (SQLException | InternalSystemException e) {
            e.printStackTrace();
        }
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
}


