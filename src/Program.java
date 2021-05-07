import tests.Test;

public class Program {
    public static void main(String[] args) {
        CouponSystemManager couponSystemManager = CouponSystemManager.getInstance();

        Test.testAll();

        couponSystemManager.couponExpirationDailyJob.stop();
        couponSystemManager.closeAllConnections();
    }
}
