package DB;

import exceptions.InternalSystemException;
import java_beans_entities.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* In DBPseudoDataManager I am creating a pseudo data
* */
public class DBPseudoDataManager {
    static final String queryTempDropTableCompanies = "DROP TABLE IF EXISTS `coupon_management_system`.`companies`";
    static final String queryTempDropTableCategories = "DROP TABLE IF EXISTS `coupon_management_system`.`categories`";
    static final String queryTempDropTableCustomers = "DROP TABLE IF EXISTS `coupon_management_system`.`customers`";
    static final String queryTempDropTableCoupons = "DROP TABLE IF EXISTS `coupon_management_system`.`coupons`";
    static final String queryTempDropTableCustomersVSCoupons = "DROP TABLE IF EXISTS `coupon_management_system`.`customers_vs_coupons`";
    static final String queryTempCreateCategories = "INSERT INTO `coupon_management_system`.`categories` (`FirstName`) VALUES (?);";

    static final String queryTempCreateTableCompanies = "CREATE TABLE IF NOT EXISTS`coupon_management_system`.`companies` (\n" +
            "\t`ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "\t`Name` VARCHAR(45) NULL,\n" +
            "\t`Email` VARCHAR(45) NULL,\n" +
            "\t`Password` VARCHAR(45) NULL,\n" +
            "\t`Status` VARCHAR(45) NULL DEFAULT 'ACTIVE'," +
            "\t PRIMARY KEY (`ID`),\n" +
            "\t UNIQUE INDEX `Email_UNIQUE` (`Email` ASC),\n" +
            "\t UNIQUE INDEX `Name_UNIQUE` (`Name` ASC));";

    static final String queryTempCreateTableCategories = "CREATE TABLE IF NOT EXISTS`coupon_management_system`.`categories`(\n" +
            "\t`Id` INT NOT NULL AUTO_INCREMENT,\n" +
            "\t`FirstName` VARCHAR(45) NOT NULL,\n" +
            "\t PRIMARY KEY (`Id`)\n" +
            ");";

    static final String queryTempCreateTableCustomers = "CREATE TABLE IF NOT EXISTS`coupon_management_system`.`customers` (\n" +
            "  `Id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `FirstName` VARCHAR(45) NULL,\n" +
            "  `LastName` VARCHAR(45) NULL,\n" +
            "  `Email` VARCHAR(45) NULL,\n" +
            "  `Password` VARCHAR(45) NULL,\n" +
            "  `Status` VARCHAR(45) NULL DEFAULT 'ACTIVE'," +
            "  PRIMARY KEY (`Id`),\n" +
            "  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC));";

    static final String queryTempCreateTableCoupons = "CREATE TABLE IF NOT EXISTS`coupon_management_system`.`coupons` (\n" +
            "  `Id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `Title` VARCHAR(45) NOT NULL,\n" +
            "  `Description` VARCHAR(45) NOT NULL,\n" +
            "  `StartDate` DATETIME NOT NULL,\n" +
            "  `EndDate` DATETIME NOT NULL,\n" +
            "  `Amount` INT NULL DEFAULT '0',\n" +
            "  `Price` DOUBLE NOT NULL,\n" +
            "  `IMAGE` VARCHAR(45) NULL,\n" +
            "  `CompanyId` INT NOT NULL,\n" +
            "  `CategoryId` INT NOT NULL,\n" +
            "  `Status` VARCHAR(45) NULL DEFAULT 'ABLE',\n" +
            "  PRIMARY KEY (`Id`),\n" +
            "  INDEX `CompanyId` (`CompanyId` ASC),\n" +
            "  INDEX `CategoryId` (`CategoryId` ASC),\n" +
            "  CONSTRAINT `CompanyId_fk`\n" +
            "    FOREIGN KEY (`CompanyId`)\n" +
            "    REFERENCES `coupon_management_system`.`companies` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `CategoryId_fk`\n" +
            "    FOREIGN KEY (`CategoryId`)\n" +
            "    REFERENCES `coupon_management_system`.`categories` (`Id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";

    static final String queryTempCreateTableCustomersVSCoupons = "CREATE TABLE IF NOT EXISTS`coupon_management_system`.`customers_vs_coupons` (\n" +
            "  `CustomerId` INT NOT NULL,\n" +
            "  `CouponId` INT NOT NULL,\n" +
            "  PRIMARY KEY (`CustomerId`, `CouponId`),\n" +
            "  INDEX `CouponId_idx` (`CouponId` ASC),\n" +
            "  INDEX `CustomerId_idx` (`CustomerId` ASC),\n" +
            "  CONSTRAINT `CouponId`\n" +
            "    FOREIGN KEY (`CouponId`)\n" +
            "    REFERENCES `coupon_management_system`.`coupons` (`Id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `CustomerId`\n" +
            "    FOREIGN KEY (`CustomerId`)\n" +
            "    REFERENCES `coupon_management_system`.`customers` (`Id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";

    public static void dropCreateTables() {
        dropTables();
        createTables();
    }

    private static void toExecuteQuery(String query) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    private static void createTables() {
        toExecuteQuery(queryTempCreateTableCategories);
        toExecuteQuery(queryTempCreateTableCompanies);
        toExecuteQuery(queryTempCreateTableCoupons);
        toExecuteQuery(queryTempCreateTableCustomers);
        toExecuteQuery(queryTempCreateTableCustomersVSCoupons);
    }

    private static void dropTables() {
        toExecuteQuery(queryTempDropTableCustomersVSCoupons);
        toExecuteQuery(queryTempDropTableCustomers);
        toExecuteQuery(queryTempDropTableCoupons);
        toExecuteQuery(queryTempDropTableCategories);
        toExecuteQuery(queryTempDropTableCompanies);
    }

    public static void createCategories(Category category) throws SQLException, InternalSystemException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryTempCreateCategories)) {
            preparedStatement.setString(1,category.getName());
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new InternalSystemException("Creating Category failed, no rows affected.");
            }
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }
}
