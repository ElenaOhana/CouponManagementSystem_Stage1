import tests.Test;

public class Program {
    public static void main(String[] args) {
        CouponSystemManager couponSystemManager = CouponSystemManager.getInstance();

        Test.testAll();

        couponSystemManager.dailyJob.stop();//But Daemon Thread ends by himself after "program" finish
        couponSystemManager.closeAllConnections();
    }
}
