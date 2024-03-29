package DB.DAO;

import DB.ConnectionPool;
import exceptions.InternalSystemException;
import java_beans_entities.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CouponsDBDAO is Singleton*/
public class CouponsDBDAO implements CouponsDAO {

    private ConnectionPool connectionPool;

    private static CouponsDBDAO instance;

    private CouponsDBDAO() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static CouponsDBDAO getInstance() {
        if (instance == null) {
            instance = new CouponsDBDAO();
        }
        return instance;
    }

    @Override
    public void addCoupon(Coupon coupon) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempInsertCoupon = "INSERT INTO `Coupons` (`title`, `Description`, `StartDate`, `EndDate`, `Amount`, `Price`, `IMAGE`, `CompanyId`, `CategoryId`) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempInsertCoupon)) {
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
                throw new InternalSystemException("Creating Coupon failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void updateCoupon(Coupon coupon) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempUpdateCoupon = "UPDATE `coupons` SET `title`=?, `Description`=?, `StartDate`=?, `EndDate`=?, `Amount`=?, `Price`=?, `IMAGE`=?, `CategoryId`=? WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempUpdateCoupon)) {
            preparedStatement.setInt(9, coupon.getId());
            preparedStatement.setString(1, coupon.getTitle());
            preparedStatement.setString(2, coupon.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(coupon.getStartDate()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(5, coupon.getAmount());
            preparedStatement.setDouble(6, coupon.getPrice());
            preparedStatement.setString(7, coupon.getImage());
            preparedStatement.setInt(8, coupon.getCategoryID());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Update Customer failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void deleteCouponAsChangeStatus(int couponID) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempChangeCouponStatusByCouponId = "UPDATE `coupons` SET `Status` = ? WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempChangeCouponStatusByCouponId)) {
            preparedStatement.setString(1, "DISABLE");
            preparedStatement.setInt(2, couponID);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Delete Coupon failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public List<Coupon> getAllCoupons() throws SQLException {
        List<Coupon> couponList = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        final String queryTempGetAllCoupons = "SELECT * FROM `coupons`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetAllCoupons)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
                    Coupon coupon = new Coupon(id, companyId, categoryId, title, description, startDate, endDate, amount, price, image, couponStatus);
                    couponList.add(coupon);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return couponList;
    }

    @Override
    public List<Coupon> getCouponListByCategory(Category category) throws SQLException {
        Connection connection = connectionPool.getConnection();
        Coupon coupon;
        List<Coupon> couponList = new ArrayList<>();
        final String queryTempGetCouponByCategory = "SELECT * FROM `coupons` WHERE `CategoryId` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCouponByCategory)) {
            preparedStatement.setInt(1, category.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
                    //int categoryId = resultSet.getInt("categoryId");
                    String couponStatusStr = resultSet.getString("status");
                    CouponStatus couponStatus = CouponStatus.valueOf(couponStatusStr);
                    coupon = new Coupon(id, companyId, category.getId(), title, description, startDate, endDate, amount, price, image, couponStatus);
                    couponList.add(coupon);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return couponList;
    }

    @Override
    public List<Coupon> getCouponListLessThanMaxPrice(double maxPrice) throws SQLException {
        final String queryTempSelectCouponsLessOrEqualsThanMaxPrice = "SELECT * FROM `coupons` WHERE `price` < ?";
        Connection connection = connectionPool.getConnection();
        Coupon coupon;
        List<Coupon> couponList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempSelectCouponsLessOrEqualsThanMaxPrice)) {
            preparedStatement.setDouble(1, maxPrice);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
                    coupon = new Coupon(id, companyId, categoryId, title, description, startDate, endDate, amount, price, image, couponStatus);
                    couponList.add(coupon);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return couponList;
    }

    @Override
    public Coupon getOneCoupon(int couponID) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        Coupon coupon;
        final String queryTempGetCouponByID = "SELECT * FROM `coupons` WHERE `id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCouponByID)) {
            preparedStatement.setInt(1, couponID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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
                    coupon = new Coupon(couponID, companyId, categoryId, title, description, startDate, endDate, amount, price, image, couponStatus);
                } else {
                    throw new InternalSystemException("Creating coupon failed, no ID obtained.");
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return coupon;
    }

    @Override
    public void addCouponPurchase(int customerID, int couponId) throws SQLException, InternalSystemException {
        Connection connection = connectionPool.getConnection();
        final String queryTempAddCouponPurchase = "INSERT INTO `customers_vs_coupons` (`CustomerId`, `CouponId`) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempAddCouponPurchase)) {
            preparedStatement.setInt(1, customerID);
            preparedStatement.setInt(2, couponId);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Adding CouponPurchase failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public List<Coupon> getCustomerCouponsByCustomerId(int customerId) throws SQLException {
        Connection connection = connectionPool.getConnection();
        final String queryTempGetCouponListByCustomerId = "SELECT * FROM coupons as c join customers_vs_coupons as cvc on cvc.CustomerId = ? WHERE cvc.couponId = c.id";
        Coupon coupon;
        List<Coupon> couponList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCouponListByCustomerId)) {
            preparedStatement.setInt(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
                    coupon = new Coupon(id, companyId, categoryId, title, description, startDate, endDate, amount, price, image, couponStatus);
                    couponList.add(coupon);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return couponList;
    }

    @Override
    public List<Coupon> getCompanyCouponsByCompanyId(int companyID) throws SQLException{
        Connection connection = connectionPool.getConnection();
        Coupon coupon;
        List<Coupon> couponList = new ArrayList<>();
        String queryTempGetCompanyCoupons = "SELECT * FROM coupons where companyId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCompanyCoupons)) {
            preparedStatement.setInt(1, companyID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        int categoryId = resultSet.getInt("categoryId");
                        String title = resultSet.getString("title");
                        String description = resultSet.getString("description");
                        LocalDateTime startDate = resultSet.getTimestamp("startDate").toLocalDateTime();
                        LocalDateTime endDate = resultSet.getTimestamp("endDate").toLocalDateTime();
                        int amount = resultSet.getInt("amount");
                        double price = resultSet.getDouble("price");
                        String status = resultSet.getString("coupons.status");
                        String image = resultSet.getString("image");
                        CouponStatus couponStatus = CouponStatus.valueOf(status);
                        coupon = new Coupon(id, companyID,categoryId,title,description,startDate, endDate, amount, price,image, couponStatus);
                        couponList.add(coupon);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return couponList;
    }

    @Override
    public List<Integer> getCustomersIdFromCustomersVsCoupons(int couponId) throws SQLException {
        List<Integer> customerIdList = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        String queryTempGetCustomersCoupons = "SELECT customerId FROM customers_vs_coupons where couponId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempGetCustomersCoupons)) {
            preparedStatement.setInt(1, couponId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("customerId");
                    customerIdList.add(id);
                }
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return customerIdList;
    }
}
