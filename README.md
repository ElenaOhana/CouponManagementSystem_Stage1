# CouponManagementSystem_Stage1
For running the project should be created the <coupon_management_system> schema in MySQL Database.

There is in ConnectionPool class:

String host = "localhost:3306";
String dbName = "coupon_management_system";

username = "root";
password = "";

The project contains the script in DBPseudoDataManager class that creates the DB tables from the scratch each run.
Tables data fills by testAll() method in Test class.

the public static void main(String[] args) method in Program class.

In my project I don't delete coupons/companies/customers, instead of it I change their enum Status.
The only void deleteCustomerPurchase(Integer customerId) method that really deletes a table row.
It deletes the row from customers_vs_coupons table.

