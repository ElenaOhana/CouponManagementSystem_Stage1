import DB.ConnectionPool;
import daily_job.CouponExpirationDailyJob;

public class CouponSystemManager { // is Singleton
    private static CouponSystemManager instance;
    private ConnectionPool connectionPool;
    private Thread dailyJob;

    public CouponExpirationDailyJob couponExpirationDailyJob;

    public static CouponSystemManager getInstance() {
        if (instance == null) {
            instance = new CouponSystemManager();
        }
        return instance;
    }

    private CouponSystemManager() {
        connectionPool = ConnectionPool.getInstance();
   }

    public void closeAllConnections() {
        connectionPool.closeAllConnections();
    }

    public CouponExpirationDailyJob startJob() {
        couponExpirationDailyJob = new CouponExpirationDailyJob();
        dailyJob = new Thread(couponExpirationDailyJob);
        dailyJob.start();
        return couponExpirationDailyJob;
    }
}
