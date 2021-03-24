package daily_job;

import DB.DAO.CouponsDAO;
import DB.DAO.CouponsDBDAO;

public class CouponExpirationDailyJob implements Runnable {

    private CouponsDAO couponsDAO = new CouponsDBDAO();
    private boolean quit = false;

    public CouponExpirationDailyJob() {
    }

    @Override
    public void run() {
        while (!quit) {

            System.out.println("I'm running in the background"); // TODO

        }
    }

    public void stop() {
        quit = true;
    }
}
