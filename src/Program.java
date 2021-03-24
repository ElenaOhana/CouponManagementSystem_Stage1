import tests.Test2;

public class Program {
    public static void main(String[] args) {
        CouponSystemManager couponSystemManager = CouponSystemManager.getInstance();

        Test2.testAll();



        couponSystemManager.dailyJob.stop();//But Daemon Thread ends by himself after "program" finish
        couponSystemManager.connectionPool.closeAllConnections();
    }
}
