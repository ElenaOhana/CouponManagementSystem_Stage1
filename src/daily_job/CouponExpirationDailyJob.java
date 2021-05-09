package daily_job;

import DB.DAO.CouponsDAO;
import DB.DAO.CouponsDBDAO;
import DB.DAO.CustomersDAO;
import DB.DAO.CustomersDBDAO;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Coupon;
import java_beans_entities.Customer;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CouponExpirationDailyJob implements Runnable {

    private CouponsDAO couponsDAO = CouponsDBDAO.getInstance();
    private CustomersDAO customersDAO = CustomersDBDAO.getInstance();
    private boolean quit = false;
    private final long MILLISECONDS_IN_DAY = Duration.ofDays(1).toMillis(); // = 86400000 milliseconds. It promise that DailyJob will work ones a day

    public CouponExpirationDailyJob() {
    }

    @Override
    public void run() {
        while (!quit) {
            System.out.println("DailyJob is started running in the background");
            //System.out.println(MILLISECONDS_IN_DAY);
            try {
                for (Customer customer : customersDAO.getAllCustomers()) {
                    List<Coupon> couponList = customer.getCoupons();
                    int customerId = customer.getId();
                    for (Coupon coupon : couponList) {
                        int couponId = coupon.getId();
                        if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
                            couponsDAO.deleteCouponAsChangeStatus(couponId);
                            couponsDAO.deleteCouponPurchase(customerId, couponId);// TODO -TO CHECK CASCADE ))) => DOESN'T NEED couponsDAO.deleteCouponPurchase();
                            System.out.println("Expired coupons and their purchases are deleted");
                        }
                    }
                }
            } catch (SQLException | InternalSystemException e) {
                 throw new RuntimeException("DB error from CouponExpirationDailyJob.", e);
            }
            try {
                Thread.sleep(MILLISECONDS_IN_DAY);
                System.out.println("I'm running in the background after"); // TODO
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread doesn't succeed to sleep");
            }
        }
    }

    public void stop() {
        quit = true;
    }
}
