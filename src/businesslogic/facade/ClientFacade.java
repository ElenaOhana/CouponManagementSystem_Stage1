package businesslogic.facade;

import DB.DAO.*;

public abstract class ClientFacade {

    protected static CompaniesDAO companiesDAO = new CompaniesDBDAO();
    protected static CustomersDAO customersDAO = new CustomersDBDAO();
    protected static CouponsDAO couponsDAO = new CouponsDBDAO();
}
