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
     *  = 86400000 milliseconds. It promise that DailyJob will work ones a day. It must replace the 5_000 at sleepDailyJob(5_000) method. */
    private final long MILLISECONDS_IN_DAY = Duration.ofDays(1).toMillis();
    private List<Customer> customerList = new ArrayList<>();
    private List<Coupon> couponList = new ArrayList<>();
    private AdminFacade adminFacade = new AdminFacade();

    @Override
    public void run() {
        while (!quit) {
            System.out.println("DailyJob is started running in the background");

            doJob();

            sleepDailyJob(5_000);
        }
    }

    /**
     * I have change the MILLISECONDS_IN_DAY param to 5_000 milliseconds in order to check that couponExpirationDailyJob can stop. */
    private void sleepDailyJob(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
            System.out.println("I'm running in the background after sleep"); // only for check
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread doesn't succeed to sleep");
        }
    }

    /** This method changes status to DISABLE (as delete) of all expired coupons and
     * deletes customer purchases of those expired coupons.
     * */
    private synchronized void doJob() {
        try {
            couponList = couponsDAO.getAllCoupons();
            for (Coupon coupon : couponList) {
                if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
                    try {
                        couponsDAO.deleteCouponAsChangeStatus(coupon.getId());   //status to DISABLE
                        List<Integer> customerIdOfExpiredCoupons = couponsDAO.getCustomersIdFromCustomersVsCoupons(coupon.getId());
                        for (Integer customerIdOfExpiredCoupon : customerIdOfExpiredCoupons) {
                            customersDAO.deleteCustomerPurchase(customerIdOfExpiredCoupon); // deletes customer purchases
                        }
                    } catch (InternalSystemException e) {
                        throw new RuntimeException("DB error from CouponExpirationDailyJob.", e);
                    }
                }
            }

        } catch (SQLException e) {
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
