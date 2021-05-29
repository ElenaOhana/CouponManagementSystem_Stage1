package tests;

import DB.DAO.CouponsDAO;
import DB.DAO.CouponsDBDAO;
import DB.DAO.CustomersDBDAO;
import DB.DBPseudoDataManager;
import businesslogic.facade.AdminFacade;
import businesslogic.facade.CompanyFacade;
import businesslogic.facade.CustomerFacade;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import exceptions.TestException;
import java_beans_entities.*;
import login.ClientType;
import login.LoginManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public void testAll() {

        DBPseudoDataManager.dropCreateTables();
        try {
            DBPseudoDataManager.createCategories(new Category("Shopping"));
            DBPseudoDataManager.createCategories(new Category("Sport"));
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
        Company newFarm = new Company("New Farm", "new_farm@company2.com", "new222");

        System.out.println("-------------------------------------Administrator Facade Test-----------------------------------------");
        System.out.println(tab + "LOGIN_ADMIN METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade = (AdminFacade) LoginManager.login("adin@adin.com", "admin", ClientType.ADMINISTRATOR); /* Should provide "Wrong admin credentials" error. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade = (AdminFacade) LoginManager.login("admin@admin.com", "admin", ClientType.COMPANY); /* Should provide "Wrong admin credentials" error because wrong ClientType. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade = (AdminFacade) LoginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR); /* Should get adminFacade successfully, because it's right credentials and correct ClientType*/
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
            adminFacade.addCompany(new Company("Zara women", "zara_women@zawomen.com", "ZARA123")); /* Should create a new newFarm successfully */
            System.out.println(tab + "Create a new newFarm successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Zara women", "zara_women@zawomen.com", "ZARA123")); /* Should provide "Company already exist error, because we trying to create Company with the same name and email."*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Zara women", "figure@figureNet.com", "fig123")); /* Should provide "Duplicate entry 'Zara women' for key 'Name_UNIQUE', because we trying to create new Company with the name that exists already."*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCompany(new Company("Z-women", "zara_women@zawomen.com", "ZARA123")); /* Should provide "Company already exist error", because we trying to create new Company with the email that exists already. "*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();

        System.out.println(tab + "UPDATE_COMPANY METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(3, "New Farm", "new_farm@newFarm.com", "2222")); /* Should provide "Getting newFarm failed, no ID obtained" error, because we trying update a non exists newFarm. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade.addCompany(newFarm); /* Should add a new newFarm (New Farm). */
            adminFacade.updateCompany(new Company(3, "New Farm", "new_farm@newFarm.com", "2222")); /* Should update an existing newFarm by id. */
            System.out.println(tab + "Updating a newFarm successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(3, "Pharmacy", "new_farm@newFarm.com", "2222")); /* Should provide an "Trying update a newFarm name error.", because we trying update a newFarm name. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCompany(new Company(1, "New Farm", "new_farm@newFarm.com", "2222")); /* Should provide an error, because we trying update a newFarm id. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();


        System.out.println(tab + "ADD_CUSTOMER METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade.addCustomer(new Customer("Maria", "Gohovich", "maria_go@gmail.com", "1111")); /* Should create a new customer successfully */
            System.out.println(tab + "Create a new customer successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.addCustomer(new Customer("Maria", "Gohovich", "maria_go@gmail.com", "1111")); /* Should provide an error: "Wrong credentials." error, because we trying to create Customer with the same email."*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            Customer customer = null;
            adminFacade.addCustomer(customer); /* Should provide an error: "Customer is not found error"*/
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();


        /* Login as a customer in order to do a customer purchase before we want to delete them */
        System.out.println("-----Login as a customer in order to do a customer purchase-------------");
        createCouponInDatabase(3);
        createCouponInDatabase(3);
        CustomerFacade customerMariaFacade = null;
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            customerMariaFacade = (CustomerFacade) LoginManager.login("maria_go@gmail.com", "1111", ClientType.CUSTOMER); /* Should get adminFacade successfully, because it's right credentials */
            System.out.println(tab + "Right customer credentials");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        System.out.println(tab + "------------PROPER CASE------------");
        if (customerMariaFacade != null) {
            createCustomerPurchaseInDatabase(1, customerMariaFacade);
            createCustomerPurchaseInDatabase(2, customerMariaFacade);

            List<Coupon> mariaCoupons = null;
            try {
                mariaCoupons = customerMariaFacade.getCustomerCoupons();
            } catch (CouponSystemException e) {
                e.printStackTrace();
            }
            for (Coupon coupon : mariaCoupons) {
                System.out.println(tab + "Customer Maria purchased a coupon with couponId: " + coupon.getId());
                System.out.println(coupon);
            }
            System.out.println(tab + "-----------------------------------");
        }

        System.out.println();
        System.out.println();
        System.out.println(tab + "DELETE_COMPANY METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.deleteCompanyAsChangeStatus(newFarm.getId()); /* Should provide an error, because we trying to pass Company without id */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            /*1) Should change status to INACTIVE of the existing newFarm (New Farm, id=3) as well as
             *  2) should change status to DISABLE of Company coupons and
             *  3) to delete the customer purchase in customers_vs_coupons table */
            adminFacade.deleteCompanyAsChangeStatus(new Company(3, "New Farm", "new_farm@newFarm.com", "2222").getId());
            System.out.println(tab + "Delete as change newFarm status and it's coupons, and delete customer purchase successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println(tab + "DELETE_CUSTOMER METHOD TESTING:");
        /* Create new coupon and his purchase in DB before we want to delete the Customer and his purchase */
        createCouponInDatabase(1);
        CouponsDBDAO couponsDBDAO = CouponsDBDAO.getInstance();
        if (customerMariaFacade != null) {
            createCustomerPurchaseInDatabase(3, customerMariaFacade);
            System.out.println(tab + "Customer purchased a coupon with couponId 3");
            System.out.println(tab + "-----------------------------------");

            try {
                System.out.println(tab + "------------PROPER CASE------------");
                /*1) Should change status to INACTIVE of theCustomer as well as
                2) delete the customer purchase in customers_vs_coupons table */
                adminFacade.deleteCustomerAsChangeStatus(adminFacade.getOneCustomer(1).getId());
                System.out.println(tab + "Delete as change customer status and delete customer purchase successfully");
                System.out.println(tab + "-----------------------------------");
            } catch (CouponSystemException e) {
                e.printStackTrace();
            }
            System.out.println("------Check INACTIVE(deleted) Customer can do purchase?-------");
            System.out.println(tab + "---------------ERROR---------------");/*Should provide an error because the Maria-client status is INACTIVE already and she can not make a purchase*/
            createCouponInDatabase(1);
            try {
                customerMariaFacade.purchaseCoupon(couponsDBDAO.getOneCoupon(4)); /* (error) purchase a new ABLE Coupon(Id=4) with amount 1. */
            } catch (CouponSystemException | SQLException e) {
                System.out.println(tab + e.getMessage());
                System.out.println(tab + "-----------------------------------");
            }
        }

        System.out.println();
        System.out.println(tab + "UPDATE_CUSTOMER METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            adminFacade.updateCustomer(new Customer(2, "Maria", "Gohovich", "maria_go@gmail.com", "1111")); /* Should provide "Update Customer failed, no rows affected." error, because we trying update an id customer. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            adminFacade.updateCustomer(new Customer(1, "Maria", "Gorovich", "maria_go@gmail.com", "1111"));/* Should update an existing customer by id. */
            System.out.println(tab + "Updating a customer successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println(tab + "GET_ALL_CUSTOMERS METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            List<Customer> customerList = adminFacade.getAllCustomers();
            System.out.println(tab + "Getting all customers successfully");
            System.out.println(tab + customerList);
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println(tab + "GET_ALL_COMPANIES METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            List<Company> companiesList = adminFacade.getAllCompanies();
            System.out.println(tab + "Getting all companies successfully");
            System.out.println(tab + companiesList);
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println(tab + "GET_ONE_CUSTOMER METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            Customer customer = adminFacade.getOneCustomer(1);
            System.out.println(tab + "Getting one customer successfully");
            System.out.println(tab + customer);
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println(tab + "GET_ONE_COMPANY METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            Company company = adminFacade.getOneCompany(1);
            System.out.println(tab + "Getting one company successfully");
            System.out.println(tab + company);
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("-------------------------------------Company Facade Test-----------------------------------------");
        CompanyFacade companyZaraFacade = null;
        Coupon coupon2 = null;

        System.out.println(tab + "LOGIN_COMPANY METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");/* Should provide "Wrong credentials" error, because we try to login company with wrong email. */
            companyZaraFacade = (CompanyFacade) LoginManager.login("za_women@zawomen.com", "ZARA123", ClientType.COMPANY);
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");/* Should provide "Wrong credentials" error, because we try to login company with wrong password. */
            companyZaraFacade = (CompanyFacade) LoginManager.login("zara_women@zawomen.com", "123", ClientType.COMPANY);
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage() + e.getCause());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            companyZaraFacade = (CompanyFacade) LoginManager.login("zara_women@zawomen.com", "ZARA123", ClientType.COMPANY);
            System.out.println(tab + "Right company credentials");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println(tab + "ADD_COUPON METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            coupon2 = createFamilyHofeshCoupon(1, 5);
            if (companyZaraFacade != null) {
                companyZaraFacade.addCoupon(coupon2);
                System.out.println(coupon2);
            }
            System.out.println(tab + "Add coupon successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            Coupon coupon = createFamilyHofeshCoupon(1, 6); /* Create a coupon of Zara-Company that has the same title */
            if (companyZaraFacade != null) {
                companyZaraFacade.addCoupon(coupon); /*Should provide an "The coupon already exists." error because we adding a new coupon with the same title to the company*/
                System.out.println(coupon);
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            Coupon coupon = createNofeshPacketCoupon(3, 1); /* Create coupon of Company that didn't login */
            if (companyZaraFacade != null) {
                companyZaraFacade.addCoupon(coupon); /*Should provide an "The action is illegal" error because we adding a coupon that doesn't belong to Zara Company */
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            if (companyZaraFacade != null) {
                try {
                    companyZaraFacade.addCoupon(couponsDBDAO.getOneCoupon(4)); /*Should provide an "The coupon already exists." error because we adding an existing coupon of Zara Company */
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();
        System.out.println("------Create a new Company(KalCar, CompanyId=4) and his Login in order to check: Adding a new coupon with the same title to another company------");
        CompanyFacade kalCarCompany = null;
        try {
            adminFacade.addCompany(new Company("KalCar", "car_car@kalcar.co.il", "car5555"));
            kalCarCompany = (CompanyFacade) LoginManager.login("car_car@kalcar.co.il", "car5555", ClientType.COMPANY);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(tab + "------------PROPER CASE------------");
            coupon2 = createFamilyHofeshCoupon(4, 6);
            if (kalCarCompany != null) {
                kalCarCompany.addCoupon(coupon2); /*Adding a new coupon with the same title to another company*/
            }
            System.out.println(tab + "Add coupon successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println(tab + "UPDATE_COUPON METHOD TESTING:");
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            coupon2 = createFamilyHofeshCoupon(1, 3); /* The login Company Zara(id=1) updated it's coupon */
            if (companyZaraFacade != null) {
                companyZaraFacade.updateCoupon(coupon2);
            }
            System.out.println(tab + "Update coupon successfully");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            Coupon coupon = createNofeshPacketCoupon(3, 1);  /* Updating coupon of Company that didn't login is illegal */
            if (companyZaraFacade != null) {
                companyZaraFacade.updateCoupon(coupon); /*Should provide an error because we update a coupon that doesn't belong to Zara Company*/
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "---------------ERROR---------------");
            Coupon coupon = createNofeshPacketCoupon(3, 4);  /* Update companyId of coupon (of another Company) is illegal */
            if (companyZaraFacade != null) {
                companyZaraFacade.updateCoupon(coupon); /* Should provide an error because we trying to update a companyId of coupon */
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        System.out.println();
        System.out.println(tab + "DELETE_COUPON METHOD TESTING:");
        try {
            System.out.println(tab + "---------------ERROR---------------");
            coupon2 = createNofeshPacketCoupon(1, 7);
            if (companyZaraFacade != null) {
                companyZaraFacade.addCoupon(coupon2);
                companyZaraFacade.deleteCoupon(coupon2.getId()); /* Should provide an error because there are no customer purchases for this coupon(id = 7) yet, but it change the coupon status to DISABLE */
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        /* Create Ron Customer in DB and his login in order to do a customer purchase before we want to delete a customer purchase */
        System.out.println("-----Login as a Ron customer(id=3) in order to do a customer purchase for coupon(id=3)-------------");
        CustomerFacade customerRonFacade = null;
        Customer ron;
        try {
            ron = createCustomerInDatabase(new Customer("Ron", "Goldberg", "ron_gold12@gmail.com", "3456"));
            try {
                customerRonFacade = (CustomerFacade) LoginManager.login("ron_gold12@gmail.com", "3456", ClientType.CUSTOMER); /* Should get customerFacade successfully, because it's right credentials */
                if (customerRonFacade != null) {
                    createCustomerPurchaseInDatabase(3, customerRonFacade);
                    List<Coupon> ronCoupons = customerRonFacade.getCustomerCoupons();
                    System.out.println(ronCoupons);
                }
                System.out.println(tab + "Log in as Ron customer(id=3) and his purchase was successful");
            } catch (CouponSystemException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(tab + "------------PROPER CASE------------");
        if (companyZaraFacade != null) {
            try {
                companyZaraFacade.deleteCoupon(3);
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Delete coupon successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();

        System.out.println("------Check if it possible to purchase a DISABLE(deleted) Coupon?-------");
        try {
            try {
                coupon2 = couponsDBDAO.getOneCoupon(7); /*the coupon was deleted than it has DISABLE status */
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(tab + "---------------ERROR---------------");
            if (customerRonFacade != null) {
                customerRonFacade.purchaseCoupon(coupon2); /* Should provide an error because we trying to purchase a deleted coupon (with DISABLE status) */
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();

        System.out.println(tab + "GET_COMPANY_COUPONS METHOD TESTING:");
        System.out.println(tab + "------------PROPER CASE------------");
        if (companyZaraFacade != null) {
            try {
                List<Coupon> companyCoupons = companyZaraFacade.getCompanyCoupons();
                for (Coupon companyCoupon : companyCoupons) {
                    System.out.println(companyCoupon);
                }
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get company coupons successfully");
        System.out.println(tab + "-----------------------------------");

        CompanyFacade companyNewFarmFacade;
        try {
            System.out.println(tab + "---------------ERROR---------------");
            companyNewFarmFacade = (CompanyFacade) LoginManager.login("new_farm@newFarm.com", "2222", ClientType.COMPANY);
            if (companyNewFarmFacade != null) {
                companyNewFarmFacade.getCompanyCoupons(); /* Should provide an error because the NewFarm company can't get Coupons because NewFarm has INACTIVE status (it's deleted)*/
            }
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();

        System.out.println(tab + "GET_COMPANY_COUPONS_BY_CATEGORY METHOD TESTING:");
        Category category = new Category(3, "Spa and wellness");
        System.out.println(tab + "------------PROPER CASE------------");
        if (companyZaraFacade != null) {
            try {
                List<Coupon> couponsByCategory = companyZaraFacade.getCompanyCoupons(category);
                for (Coupon coupon : couponsByCategory) {
                    System.out.println(coupon);
                }
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get company coupons by category successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();


        System.out.println(tab + "GET_COUPONS_UP_TO_MAX_COMPANY_PRICE METHOD TESTING:");
        double maxPrice = 500;
        System.out.println(tab + "------------PROPER CASE------------");
        if (companyZaraFacade != null) {
            try {
                List<Coupon> couponsUPTOMaxCompanyPrice = companyZaraFacade.getCompanyCoupons(maxPrice);
                for (Coupon coupon : couponsUPTOMaxCompanyPrice) {
                    System.out.println(coupon);
                }
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get coupons up to max company price successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();

        System.out.println(tab + "GET_COMPANY_DETAILS METHOD TESTING:");
        System.out.println(tab + "------------PROPER CASE------------");
        if (companyZaraFacade != null) {
            try {
                Company zaraCompany = companyZaraFacade.getCompanyDetails(); /* I don't bring a whole object (without conmany coupons)*/
                System.out.println(tab + zaraCompany);
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get company details successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////");


        System.out.println("-------------------------------------Customer Facade Test-----------------------------------------");
        System.out.println(tab + "CUSTOMER_LOGIN METHOD TESTING:");
        System.out.println(tab + "---------------ERROR---------------");
        try {
            customerMariaFacade = (CustomerFacade) LoginManager.login("maria_go@gmail.com", "1111", ClientType.CUSTOMER);/*Will provide the "Customer does not exists/Status is INACTIVE" error because we trying to login with deleted Customer */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        try {
            System.out.println(tab + "---------------ERROR---------------");
            customerRonFacade = (CustomerFacade) LoginManager.login("ron_gold", "3456", ClientType.CUSTOMER); /* Should provide "Wrong credentials" error. */
        } catch (CouponSystemException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        try {
            System.out.println(tab + "------------PROPER CASE------------");
            customerRonFacade = (CustomerFacade) LoginManager.login("ron_gold12@gmail.com", "3456", ClientType.CUSTOMER); /* Should get customerFacade successfully, because it's right credentials */
            System.out.println(tab + "Right admin credentials");
            System.out.println(tab + "-----------------------------------");
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println(tab + "PURCHASE_COUPON METHOD TESTING:");
        System.out.println(tab + "---------------ERROR---------------");
        try {
            if (customerRonFacade != null) {
                customerRonFacade.purchaseCoupon(couponsDBDAO.getOneCoupon(3));/* Error because amount is 0 of coupon(id=3) */
            }
        } catch (CouponSystemException | SQLException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        System.out.println(tab + "---------------ERROR---------------");
        try {
            if (customerRonFacade != null) {
                customerRonFacade.purchaseCoupon(couponsDBDAO.getOneCoupon(7));/* Error because purchase a DISABLE coupon */
            }
        } catch (CouponSystemException | SQLException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        System.out.println(tab + "---------------ERROR---------------");
        try {
            if (customerRonFacade != null) {
                createExpiredCouponInDatabase(8, 4);
                customerRonFacade.purchaseCoupon(couponsDBDAO.getOneCoupon(8));/* Error because purchase an expired coupon */
            }
        } catch (CouponSystemException | SQLException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }

        System.out.println(tab + "---------------ERROR---------------");
        try {
            if (customerRonFacade != null) {
                Coupon couponForPurchaseTwice = couponsDBDAO.getOneCoupon(5);
                couponForPurchaseTwice.setAmount(20);
                couponsDBDAO.updateCoupon(couponForPurchaseTwice);
                customerRonFacade.purchaseCoupon(couponForPurchaseTwice);
                customerRonFacade.purchaseCoupon(couponForPurchaseTwice);/* Error because purchase coupon twice */
            }
        } catch (CouponSystemException | SQLException e) {
            System.out.println(tab + e.getMessage());
            System.out.println(tab + "-----------------------------------");
        }
        System.out.println();

        System.out.println(tab + "GET_CUSTOMER_COUPONS METHOD TESTING:");
        System.out.println(tab + "------------PROPER CASE------------");
        if (customerRonFacade != null) {
            try {
                List<Coupon> customerCoupons = customerRonFacade.getCustomerCoupons();
                for (Coupon customerCoupon : customerCoupons) {
                    System.out.println(customerCoupon);
                }
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get customer coupons successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();

        System.out.println(tab + "GET_CUSTOMER_COUPONS_BY_CATEGORY METHOD TESTING:");
        System.out.println(tab + "------------PROPER CASE------------");
        if (customerRonFacade != null) {
            try {
                createCustomerPurchaseInDatabase(4, customerRonFacade); /* create additional Customer purchase for testing getCustomerCoupons(category) method*/
                List<Coupon> couponsByCategory = customerRonFacade.getCustomerCoupons(category);/* CategoryId=3 */
                for (Coupon coupon : couponsByCategory) {
                    System.out.println(coupon);
                }
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get customer coupons by category successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();


        System.out.println(tab + "GET_COUPONS_UP_TO_MAX_CUSTOMER_PRICE METHOD TESTING:");
        System.out.println(tab + "------------PROPER CASE------------");
        if (customerRonFacade != null) {
            try {
                List<Coupon> couponsUPTOMaxCustomerPrice = customerRonFacade.getCustomerCoupons(maxPrice);/* maxPrice = 500 */
                for (Coupon coupon : couponsUPTOMaxCustomerPrice) {
                    System.out.println(coupon);
                }
            } catch (CouponSystemException couponSystemException) {
                couponSystemException.printStackTrace();
            }
        }
        System.out.println(tab + "Get customer up to max company price successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();

        System.out.println(tab + "GET_CUSTOMER_DETAILS METHOD TESTING:");
        System.out.println(tab + "------------PROPER CASE------------");
        if (customerRonFacade != null) {
            try {
                ron = customerRonFacade.getCustomerDetails();
                ArrayList<Coupon> ronCoupons = (ArrayList<Coupon>) couponsDBDAO.getCustomerCouponsByCustomerId(ron.getId()); /* In order to print a whole object */
                ron.setCoupons(ronCoupons);
                System.out.println(ron);
            } catch (SQLException | CouponSystemException e) {
                e.printStackTrace();
            }
        }
        System.out.println(tab + "Get customer details successfully");
        System.out.println(tab + "-----------------------------------");
        System.out.println();


    }

    /* This method creates coupons in DB without login of any Company in order to test Customer Facade and DailyJobThread => addCoupon(coupon) NOT via companyFacade */
    private static void createExpiredCouponInDatabase(int couponId, int companyId) {
        LocalDateTime startDate = LocalDateTime.parse("2021-02-30 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-04-30 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        CouponsDBDAO couponsDBDAO = CouponsDBDAO.getInstance();
        Coupon coupon = new Coupon(couponId, companyId, 2, "Honda", "Honda dream day", startDate, endDate, 15, 150000, "image");
        try {
            if (couponsDBDAO != null) {
                couponsDBDAO.addCoupon(coupon); /* straight through couponsDBDAO */
            }
        } catch (SQLException | InternalSystemException e) {
            e.printStackTrace();
        }
    }


    private static Coupon getCouponFromDatabase(int couponId) {
        CouponsDAO couponsDBDAO = CouponsDBDAO.getInstance();
        Coupon coupon = null;
        try {
            coupon = couponsDBDAO.getOneCoupon(couponId);
        } catch (SQLException | InternalSystemException e) {
            e.printStackTrace();
        } finally {
            if (coupon == null) {
                System.out.println("get Coupon From Database ERROR");
            }
        }
        return coupon;
    }

    private static Customer createCustomerInDatabase(Customer customer) {
        CustomersDBDAO customersDBDAO = CustomersDBDAO.getInstance();
        Customer customer1 = null;
        try {
            if (customer != null) {
                customer1 = customersDBDAO.addCustomerWithReturnCustomer(customer);
            }
        } catch (SQLException | InternalSystemException e) {
            e.printStackTrace();
        }
        return customer1;
    }

    private static void createCustomerPurchaseInDatabase(int couponId, CustomerFacade customerFacade) {
        CouponsDBDAO couponsDBDAO = CouponsDBDAO.getInstance();
        try {
            customerFacade.purchaseCoupon(couponsDBDAO.getOneCoupon(couponId));
        } catch (SQLException | CouponSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates coupons in DB without login of any Company in order to test Admin Facade only => addCoupon(coupon) NOT via companyFacade */
    private static void createCouponInDatabase(int companyId) {
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        CouponsDBDAO couponsDBDAO = CouponsDBDAO.getInstance();
        Coupon coupon = new Coupon(companyId, 2, "Yoga for everyone", "Body wellness day", startDate, endDate, 1, 200, "image");
        try {
            if (couponsDBDAO != null) {
                couponsDBDAO.addCoupon(coupon); /* straight through couponsDBDAO */
            }
        } catch (SQLException | InternalSystemException e) {
            e.printStackTrace();
        }
    }

    private static Coupon createFamilyHofeshCoupon(int companyId, int couponId) {
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(couponId, companyId, 3, "Family hofesh", "Family spa rest with babes", startDate, endDate, 1, 500, "image");
        return coupon;
    }

    private static Coupon createNofeshPacketCoupon(int companyId, int couponId) {
        LocalDateTime startDate = LocalDateTime.parse("2021-12-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(couponId, companyId, 3, "Nofesh packet", "Spa in Galil", startDate, endDate, 1, 300, "image");
        return coupon;
    }
}


