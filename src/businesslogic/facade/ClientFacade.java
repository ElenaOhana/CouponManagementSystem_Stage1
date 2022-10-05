package businesslogic.facade;

import DB.DAO.*;
import exceptions.CouponSystemException;

public abstract class ClientFacade {

    protected static CompaniesDAO companiesDAO = CompaniesDBDAO.getInstance();
    protected static CustomersDAO customersDAO = CustomersDBDAO.getInstance();
    protected static CouponsDAO couponsDAO = CouponsDBDAO.getInstance();
}



    //static boolean login(String email, String password) throws CouponSystemException//???




