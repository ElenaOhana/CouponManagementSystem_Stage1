/*package tests;

import businesslogic.facade.CompanyFacade;
import exceptions.CouponSystemException;
import java_beans_entities.Coupon;
import login.ClientType;
import login.LoginManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public Class<?> getEntityFromListEntities(List list) {
        for (Object o : list) {
            if (o instanceof Coupon) {
                return o.getClass();
            }
        }

    }

    public static List initJavaBeansEntities() { // TODO return entities ArrayList??
        LocalDateTime startDate = LocalDateTime.parse("2021-14-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse("2021-14-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Coupon coupon = new Coupon(1111, 1, "Nofesh", "Spa in Galil", startDate, endDate, 30, 300,"image");
        List listEntities = new ArrayList();
        listEntities.add(coupon);

        return listEntities;
    }

    public static void testCompanyFacade(Coupon coupon) {
        CompanyFacade companyFacade;
        try {
            companyFacade = (CompanyFacade) LoginManager.login("companyA@gmail.com", "1111", ClientType.COMPANY);
            companyFacade.updateCoupon(coupon);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){}
       *//* } try {
            if (companyFacade == null) {*//*

        //companyFacade.updateCoupon();
    }

    public static void testAll() {
        initJavaBeansEntities();
        //testCompanyFacade(coupon);


    }
}*/
        /*
וקריאה לכל פונקציית AdminFacade קבלת אובייקט ,Administrator- כ LoginManager- ב. התחברות ע"י ה
לוגיקה עסקית שלו.
וקריאה לכל פונקציית CompanyFacade קבלת אובייקט ,Company- כ LoginManager- ג. התחברות ע"י ה
לוגיקה עסקית שלו.
וקריאה לכל פונקציית CustomerFacade קבלת אובייקט ,Customer- כ LoginManager- ד. התחברות ע"י ה
לוגיקה עסקית שלו.
 */
