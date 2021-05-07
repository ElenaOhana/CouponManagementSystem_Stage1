import DB.ConnectionPool;
import daily_job.CouponExpirationDailyJob;

public class CouponSystemManager { // is Singleton
    private static CouponSystemManager instance;
    ConnectionPool connectionPool;
    Thread dailyJob;
    CouponExpirationDailyJob couponExpirationDailyJob = new CouponExpirationDailyJob();

    public static CouponSystemManager getInstance() {
        if (instance == null) {
            instance = new CouponSystemManager();
        }
        return instance;
    }

    private CouponSystemManager() {
        initThread();
        connectionPool = ConnectionPool.getInstance();
    }

    private void initThread() {
        dailyJob = new Thread(couponExpirationDailyJob);
        dailyJob.start();
    }

    public void closeAllConnections() {
        connectionPool.closeAllConnections();
    }
}
