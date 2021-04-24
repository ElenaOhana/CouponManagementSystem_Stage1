package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* In DBManager I am creating a pseudo data
* */
public class DBManager {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    static final String queryTempDropTableCompanies = "DROP TABLE IF EXISTS `coupon_management_system`.`companies`";
    static final String queryTempDropTableCategories = "DROP TABLE IF EXISTS `coupon_management_system`.`categories`";
    static final String queryTempDropTableCustomers = "DROP TABLE IF EXISTS `coupon_management_system`.`customers`";
    static final String queryTempDropTableCoupons = "DROP TABLE IF EXISTS `coupon_management_system`.`coupons`";
    static final String queryTempDropTableCustomersVSCoupons = "DROP TABLE IF EXISTS `coupon_management_system`.`customers_vs_coupons`";

    static final String queryTempCreateTableCompanies = "CREATE TABLE `coupon_management_system`.`companies` (\n" +
            "\t`ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "\t`Name` VARCHAR(45) NULL,\n" +
            "\t`Email` VARCHAR(45) NULL,\n" +
            "\t`Password` VARCHAR(45) NULL,\n" +
            "\t`Status` VARCHAR(45) NULL DEFAULT 'ACTIVE'," +
            "\t PRIMARY KEY (`ID`),\n" +
            "\t UNIQUE INDEX `Email_UNIQUE` (`Email` ASC),\n" +
            "\t UNIQUE INDEX `Name_UNIQUE` (`Name` ASC));";

    static final String queryTempCreateTableCategories = "CREATE TABLE `coupon_management_system`.`categories`(\n" +
            "\t`Id` INT NOT NULL AUTO_INCREMENT,\n" +
            "\t`FirstName` VARCHAR(45) NOT NULL,\n" +
            "\t PRIMARY KEY (`Id`)\n" +
            ");";

    static final String queryTempCreateTableCustomers = "CREATE TABLE `coupon_management_system`.`customers` (\n" +
            "  `Id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `FirstName` VARCHAR(45) NULL,\n" +
            "  `LastName` VARCHAR(45) NULL,\n" +
            "  `Email` VARCHAR(45) NULL,\n" +
            "  `Password` VARCHAR(45) NULL,\n" +
            "  `Status` VARCHAR(45) NULL DEFAULT 'ACTIVE'," +
            "  PRIMARY KEY (`Id`),\n" +
            "  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC));";

    static final String queryTempCreateTableCoupons = "CREATE TABLE `coupon_management_system`.`coupons` (\n" +
            "  `Id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `Title` VARCHAR(45) NULL,\n" +
            "  `Description` VARCHAR(45) NULL,\n" +
            "  `StartDate` DATETIME NULL,\n" +
            "  `EndDate` DATETIME NULL,\n" +
            "  `Amount` INT NULL,\n" +
            "  `Price` DOUBLE NULL,\n" +
            "  `IMAGE` VARCHAR(45) NULL,\n" +
            "  `CompanyId` INT NOT NULL,\n" +
            "  `CategoryId` INT NOT NULL,\n" +
            "  PRIMARY KEY (`Id`));";

    static final String queryTempCreateTableCustomersVSCoupons = "DROP TABLE `coupon_management_system`.`customers_vs_coupons`";



    public static void dropCreateTables() {
        dropTables();
        createTables();
    }

    private static void createTables() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempCreateTableCompanies)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

        connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempCreateTableCategories)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

      /*  connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempCreateTableCoupons)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }*/

        connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempCreateTableCustomers)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

       /* connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempCreateTableCustomersVSCoupons)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }*/
    }

    private static void dropTables() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempDropTableCustomersVSCoupons)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

        connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempDropTableCustomers)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

        connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempDropTableCoupons)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

        connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempDropTableCategories)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }

        connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempDropTableCompanies)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }
}
