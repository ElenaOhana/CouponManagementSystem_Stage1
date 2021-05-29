package daily_job;

import DB.DAO.CouponsDAO;
import DB.DAO.CouponsDBDAO;
import DB.DAO.CustomersDAO;
import DB.DAO.CustomersDBDAO;
import businesslogic.facade.AdminFacade;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import exceptions.NotFoundException;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CouponExpirationDailyJob implements Runnable {

    private CouponsDAO couponsDAO = CouponsDBDAO.getInstance();
    private CustomersDAO customersDAO = CustomersDBDAO.getInstance();
    private boolean quit = false;
    /**
     *  = 86400000 milliseconds. It promise that DailyJob will work ones a day */
    private final long MILLISECONDS_IN_DAY = Duration.ofDays(1).toMillis();
    private List<Customer> customerList = new ArrayList<>();
    private List<Coupon> couponList = new ArrayList<>();
    private AdminFacade adminFacade = new AdminFacade();

    @Override
    public void run() {
        while (!quit) {
            System.out.println("DailyJob is started running in the background");

            doJob();

            try {
                Thread.sleep(5_000); // In order to check the couponExpirationDailyJob.stop() (in Program class) is work, you can change the MILLISECONDS_IN_DAY to 5_000.
                System.out.println("I'm running in the background after sleep"); // only for check
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread doesn't succeed to sleep");
            }
        }
    }

    private synchronized void doJob() {
        try {
            couponList = couponsDAO.getAllCoupons();
            for (Coupon coupon : couponList) {
                int couponId = coupon.getId();
                if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
                    try {
                        couponsDAO.deleteCouponAsChangeStatus(couponId);  /* Change status to DISABLE (as delete) of coupon */
                    } catch (InternalSystemException e) {
                        e.printStackTrace();
                    }
                }
            }
      ///////////TODO from here
            try {
                customerList = adminFacade.getAllCustomers();
            } catch (CouponSystemException e) {
                e.printStackTrace();
            }
            System.out.println(customerList); //TODO
        } catch (SQLException e) {
            throw new RuntimeException("DB error from CouponExpirationDailyJob.", e);
        }
        try {
            for (Customer customer : customerList) {
                couponList = customer.getCoupons();
                int customerId = customer.getId();
                System.out.println("**********couponList from Thread " + couponList); // TODO
                if (couponList == null) {
                    throw new NotFoundException("There are not coupons purchase for customer with id: " + customer.getId()+ ", customer name:" + customer.getFirstName());
                } else {
                    for (Coupon coupon : couponList) {
                        int couponId = coupon.getId();
                        if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
                            customersDAO.deleteCustomerPurchase(customerId); /* Delete customer purchase from customers_vs_coupons */
                            System.out.println("Expired coupons and their purchases are deleted");
                        } else {
                            System.out.println("There are not expired coupons for customer with id: " + customer.getId()+ ", customer name: " + customer.getFirstName());
                        }
                    }
                }

            }
        } catch (SQLException | NotFoundException e) {
            throw new RuntimeException("DB error from CouponExpirationDailyJob.", e);
        }
        finally {
            this.notifyAll();
        }
    }

    public void stop() {
        quit = true;
    }
}
