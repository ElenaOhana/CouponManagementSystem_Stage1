package businesslogic.facade;

import DB.DAO.*;

public abstract class ClientFacade {

    protected static CompaniesDAO companiesDAO = CompaniesDBDAO.getInstance();
    protected static CustomersDAO customersDAO = CustomersDBDAO.getInstance();
    protected static CouponsDAO couponsDAO = CouponsDBDAO.getInstance();
}
