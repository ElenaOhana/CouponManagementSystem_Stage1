package daily_job;

import DB.DAO.CouponsDAO;
import DB.DAO.CouponsDBDAO;
import DB.DAO.CustomersDAO;
import DB.DAO.CustomersDBDAO;
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
    private final long MILLISECONDS_IN_DAY = Duration.ofDays(1).toMillis(); /* = 86400000 milliseconds. It promise that DailyJob will work ones a day */
    private List<Customer> customerList = new ArrayList<>();
    private List<Coupon> couponList = new ArrayList<>();

    @Override
    public void run() {
        while (!quit) {
            System.out.println("DailyJob is started running in the background");
            /* I have put a Thread to sleep here in order to let the main thread to add expired coupons first. */
            /*try {
                System.out.println("DailyJob goes to sleep for 10 seconds");
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread doesn't succeed to sleep");
            }*/

            doJob();

            try {
                System.out.println("I'm running in the background after sleep"); // only for check
                Thread.sleep(5_000); // In order to check the couponExpirationDailyJob.stop() (in Program class) is work, you can change the MILLISECONDS_IN_DAY to 5_000.
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread doesn't succeed to sleep");
            }
        }
    }

    private synchronized void doJob() {
        try {
            customerList = customersDAO.getAllCustomers();
            System.out.println(customerList);
        } catch (SQLException e) {
            throw new RuntimeException("DB error from CouponExpirationDailyJob.", e);
        }
        try {
            for (Customer customer : customerList) {
                couponList = customer.getCoupons();
                int customerId = customer.getId();
                System.out.println(couponList);
                if (couponList == null) {
                    throw new NotFoundException("There are not coupons purchase for customer: " + customer);
                    //System.out.println("There are not coupons purchase for customer:  " + customer);
                } else {
                    for (Coupon coupon : couponList) {
                        int couponId = coupon.getId();
                        if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
                            couponsDAO.deleteCouponAsChangeStatus(couponId);
                            couponsDAO.deleteCouponPurchase(customerId, couponId);// TODO -TO CHECK CASCADE ))) => DOESN'T NEED couponsDAO.deleteCouponPurchase();
                            System.out.println("Expired coupons and their purchases are deleted");
                        } else {
                            System.out.println("There are not expired coupons for customer " + customer);
                        }
                    }
                }

            }
//           /* while (couponList == null){
//                wait();
//            }*/
//
        } catch (SQLException | InternalSystemException | NotFoundException e) {
            throw new RuntimeException("DB error from CouponExpirationDailyJob.", e);
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        finally {
            this.notifyAll();
        }
    }

    public void stop() {
        quit = true;
    }
}
