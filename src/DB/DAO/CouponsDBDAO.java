package DB.DAO;

import DB.ConnectionPool;
import java_beans_entities.Coupon;
import java_beans_entities.CouponStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CouponsDBDAO implements CouponsDAO {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public void addCoupon(Coupon coupon) {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCoupon = "INSERT INTO `Coupons` (`title`, `Description`, `StartDate`, `EndDate`, `Amount`, `Price`, `IMAGE`, `CompanyId`, `CategoryId`) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCoupon)) {
            preparedStatement.setString(1, coupon.getTitle());
            preparedStatement.setString(2, coupon.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(coupon.getStartDate())); // TODO if IT IS RIGHT WAY: the DATETIME receive the Timestamp?
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
            e.printStackTrace();//FIXME
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void updateCoupon(Coupon coupon) {
        Connection connection = connectionPool.getConnection();
        final String queryTempUpdateCoupon = "UPDATE `coupons` SET `title`=?, `Description`=?, `StartDate`=?, `EndDate`=?, `Amount`=?, `Price`=?, `IMAGE`=?, `CompanyId`=?, `CategoryId`=?, WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempUpdateCoupon)) {
            preparedStatement.setInt(10, coupon.getId());
            preparedStatement.setString(1, coupon.getTitle());
            preparedStatement.setString(2, coupon.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(coupon.getStartDate()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(5, coupon.getAmount());
            preparedStatement.setDouble(6, coupon.getPrice());
            preparedStatement.setString(7, coupon.getImage());
            preparedStatement.setInt(8, coupon.getCompanyId());
            preparedStatement.setInt(9, coupon.getCategoryID());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Update Customer failed, no rows affected."); //TODO Exception - Update
            }
        } catch (SQLException e) {
            e.printStackTrace();//FIXME
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public void deleteCoupon(int couponID) {
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCouponStatus = "UPDATE `coupons` SET `Status` = `unable` WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCouponStatus)) {
            preparedStatement.setInt(1, couponID);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new SQLException("Delete Customer failed, no rows affected."); //FIXME Exception - Delete
            }
        } catch (SQLException e) {
            e.printStackTrace(); //FIXME
        }
        connectionPool.restoreConnection(connection);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> couponList = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        final String queryTempGetAllCoupons = "SELECT * FROM `coupons`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetAllCoupons)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    LocalDateTime startDate = resultSet.getTimestamp("startDate").toLocalDateTime();
                    LocalDateTime endDate = resultSet.getTimestamp("endDate").toLocalDateTime();
                    int amount = resultSet.getInt("amount");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    int companyId = resultSet.getInt("companyId");
                    int categoryId = resultSet.getInt("categoryId");
                    String couponStatusStr = resultSet.getString("status");
                    CouponStatus couponStatus = CouponStatus.valueOf(couponStatusStr);
                    Coupon coupon = new Coupon(id, companyId, categoryId, title, description,startDate,endDate,amount,price,image,couponStatus);
                    couponList.add(coupon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();//FIXME
        }
        connectionPool.restoreConnection(connection);
        return couponList;
    }

    @Override
    public Coupon getOneCoupon(int couponID) {
        Connection connection = connectionPool.getConnection();
        Coupon coupon = null;
        final String queryTempGetCouponByID = "SELECT * FROM `coupons` WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCouponByID)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    LocalDateTime startDate = resultSet.getTimestamp("startDate").toLocalDateTime();
                    LocalDateTime endDate = resultSet.getTimestamp("endDate").toLocalDateTime();
                    int amount = resultSet.getInt("amount");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    int companyId = resultSet.getInt("companyId");
                    int categoryId = resultSet.getInt("categoryId");
                    String couponStatusStr = resultSet.getString("status");
                    CouponStatus couponStatus = CouponStatus.valueOf(couponStatusStr);
                    coupon = new Coupon(id, companyId, categoryId, title, description, startDate, endDate, amount, price, image, couponStatus);
                } else {
                    throw new SQLException("Creating coupon failed, no ID obtained."); //FIXME  Exception get
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); //FIXME
        }
        connectionPool.restoreConnection(connection);
        return coupon;
    }

        @Override
    public void addCouponPurchase(int customerID, int couponId) {
        Connection connection = connectionPool.getConnection();
        final String queryTempAddCouponPurchase = "INSERT INTO `customers_vs_coupons` (`CustomerId`, `CouponId`) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempAddCouponPurchase)) {

        } catch (SQLException e) {
            e.printStackTrace(); //FIXME
        }

            connectionPool.restoreConnection(connection);
    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponId) {
        Connection connection = connectionPool.getConnection();

        connectionPool.restoreConnection(connection);
    }
}
