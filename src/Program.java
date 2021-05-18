import daily_job.CouponExpirationDailyJob;
import tests.Test;

public class Program {

    public static void main(String[] args) {
        CouponSystemManager couponSystemManager = CouponSystemManager.getInstance();
        Test test = new Test();
        test.testAll(); /* testAll() method is synchronized */
        CouponExpirationDailyJob couponExpirationDailyJob = couponSystemManager.startJob();

        synchronized (couponExpirationDailyJob) {
            try {
                couponExpirationDailyJob.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            couponSystemManager.couponExpirationDailyJob.stop();
            couponSystemManager.closeAllConnections();
        }
    }
}
