import DB.ConnectionPool;
import daily_job.CouponExpirationDailyJob;

public class CouponSystemManager { // is Singleton
    private static CouponSystemManager instance;
    ConnectionPool connectionPool;
    Thread dailyJob;

    private CouponSystemManager() {
        initThread();
        connectionPool = ConnectionPool.getInstance();
    }

    private void initThread() {
        dailyJob = new Thread(new CouponExpirationDailyJob());
        dailyJob.start();
        dailyJob.setDaemon(true);
    }

    public static CouponSystemManager getInstance() {
        if (instance == null) {
            instance = new CouponSystemManager();
        }
        return instance;
    }





    /* Job- ה. הפסקת ה
.)ConnectionPool- של ה closeAllConnections קריאה לפונקציה ( Connections- ו. סגירת כל*/
}
