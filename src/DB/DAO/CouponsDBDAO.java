package DB.DAO;

import DB.ConnectionPool;
import java_beans_entities.Coupon;

import java.sql.*;
import java.util.List;

public class CouponsDBDAO implements CouponsDAO {
    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public void addCoupon(Coupon coupon) {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCoupon = "INSERT INTO `Coupons` (`title`, `Description`, `StartDate`, `EndDate`, `Amount`, `Price`, `IMAGE`, `CompanyId`, `CategoryId`) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCoupon)) {
            preparedStatement.setString(1, coupon.getTitle());
            preparedStatement.setString(2, coupon.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(coupon.getStartDate())); // TODO check if the Date
            preparedStatement.setTimestamp(4, Timestamp.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(5, coupon.getAmount());
            preparedStatement.setDouble(6, coupon.getPrice());
            preparedStatement.setString(7, coupon.getImage());
            preparedStatement.setInt(8, coupon.getCompanyId());
            preparedStatement.setInt(9, coupon.getCategoryID());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Creating Coupon failed, no rows affected."); //TODO Exception - Creating
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void updateCoupon(Coupon coupon) {
        Connection connection = connectionPool.getConnection();

    }

    @Override
    public void deleteCoupon(int couponID) {
        Connection connection = connectionPool.getConnection();

    }

    @Override
    public List<Coupon> getAllCoupons() {
        Connection connection = connectionPool.getConnection();
        return null;
    }

    @Override
    public Coupon getOneCoupon(int couponID) {
        Connection connection = connectionPool.getConnection();
        return null;
    }

    @Override
    public void addCouponPurchase(int customerID, int couponId) {
        Connection connection = connectionPool.getConnection();

    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponId) {
        Connection connection = connectionPool.getConnection();

    }
}
