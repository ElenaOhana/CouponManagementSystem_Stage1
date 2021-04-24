package daily_job;

import DB.DAO.CouponsDAO;
import DB.DAO.CouponsDBDAO;
import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import exceptions.InternalSystemException;
import java_beans_entities.Coupon;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CouponExpirationDailyJob implements Runnable {

    private CouponsDAO couponsDAO = new CouponsDBDAO();
    private boolean quit = false;

    public CouponExpirationDailyJob() {
    }

    @Override
    public void run() {
        while (!quit) {
            System.out.println("I'm running in the background"); // TODO
            long localTimePlusFive = LocalTime.now().plusSeconds(0).toNanoOfDay(); // to show that the expired coupons are deleted
            /*try {
                Thread.sleep(localTimePlusFive);
                System.out.println("I'm running in the background after");
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread doesn't succeed to sleep");
            }*/
            try {
                for (Coupon coupon : couponsDAO.getAllCoupons()) {
                    if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
                        couponsDAO.deleteCouponAsChangeStatus(coupon.getId());
                        //couponsDAO.deleteCouponPurchase();// TODO how do i get to customer id here?? TO CHECK CASCADE ))) => DOESN'T NEED couponsDAO.deleteCouponPurchase();
                        System.out.println("Expired coupons are deleted");
                    }
                }
            } catch (SQLException | InternalSystemException e) {
                e.getCause();
            }
        }
    }

    public void stop() {
        quit = true;
    }
}
